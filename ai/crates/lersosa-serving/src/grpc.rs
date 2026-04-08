use std::net::SocketAddr;
use std::pin::Pin;

use lersosa_core::runtime::BackendKind;
use tonic::{Request, Response, Status, transport::Server};
use tokio_stream::{Stream, StreamExt as _, iter};

use crate::{
    api::chat,
    app_state::AppState,
    dto::chat::{ChatMessage, ChatRequest as HttpChatRequest, ChatRole, ChatToolChoice, ChatToolDefinition},
    tokenization::chat_tokenizer::{ChatTokenizer, validate_messages, validate_tools},
};

pub mod proto {
    tonic::include_proto!("lersosa.inference.v1");
}

#[derive(Clone)]
pub struct InferenceGrpcService {
    state: AppState,
}

impl InferenceGrpcService {
    pub fn new(state: AppState) -> Self {
        Self { state }
    }

    fn map_chat_role(role: i32) -> Result<ChatRole, Status> {
        match proto::ChatRole::try_from(role).unwrap_or(proto::ChatRole::Unspecified) {
            proto::ChatRole::System => Ok(ChatRole::System),
            proto::ChatRole::User => Ok(ChatRole::User),
            proto::ChatRole::Assistant => Ok(ChatRole::Assistant),
            proto::ChatRole::Unspecified => {
                Err(Status::invalid_argument("chat role must be system/user/assistant"))
            }
        }
    }

    fn map_chat_request(payload: proto::ChatRequest) -> Result<HttpChatRequest, Status> {
        let messages = payload
            .messages
            .into_iter()
            .map(|message| {
                Ok(ChatMessage {
                    role: Self::map_chat_role(message.role)?,
                    content: message.content,
                })
            })
            .collect::<Result<Vec<_>, Status>>()?;

        let tools = payload
            .tools
            .into_iter()
            .map(|tool| ChatToolDefinition {
                name: tool.name,
                description: if tool.description.trim().is_empty() {
                    None
                } else {
                    Some(tool.description)
                },
                parameters_json: if tool.parameters_json.trim().is_empty() {
                    None
                } else {
                    Some(tool.parameters_json)
                },
            })
            .collect();

        let tool_choice = payload.tool_choice.and_then(|choice| {
            use proto::chat_tool_choice::Choice;
            match choice.choice {
                Some(Choice::Auto(_)) => Some(ChatToolChoice::Auto),
                Some(Choice::None(_)) => Some(ChatToolChoice::None),
                Some(Choice::Required(_)) => Some(ChatToolChoice::Required),
                Some(Choice::Named(name)) => Some(ChatToolChoice::Named(name)),
                None => None,
            }
        });

        Ok(HttpChatRequest {
            system: if payload.system.trim().is_empty() {
                None
            } else {
                Some(payload.system)
            },
            messages,
            tools,
            tool_choice,
            max_new_tokens: payload.max_new_tokens as usize,
            stop_token_id: if payload.has_stop_token_id {
                Some(payload.stop_token_id)
            } else {
                None
            },
        })
    }
}

#[tonic::async_trait]
impl proto::inference_service_server::InferenceService for InferenceGrpcService {
    type ChatStreamStream = Pin<Box<dyn Stream<Item = Result<proto::ChatStreamResponse, Status>> + Send + 'static>>;

    async fn health(
        &self,
        _request: Request<proto::HealthRequest>,
    ) -> Result<Response<proto::HealthResponse>, Status> {
        Ok(Response::new(proto::HealthResponse {
            status: "ok".to_string(),
            backend: format!("{:?}", self.state.backend()),
        }))
    }

    async fn infer(
        &self,
        request: Request<proto::InferRequest>,
    ) -> Result<Response<proto::InferResponse>, Status> {
        let output = self
            .state
            .infer(request.into_inner().tokens)
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        Ok(Response::new(proto::InferResponse {
            next_token_id: output.next_token_id,
            logits_shape: output.logits_shape.iter().map(|v| *v as u64).collect(),
            backend: format!("{:?}", self.state.backend()),
        }))
    }

    async fn infer_batch(
        &self,
        request: Request<proto::InferBatchRequest>,
    ) -> Result<Response<proto::InferBatchResponse>, Status> {
        let items: Vec<Vec<i64>> = request.into_inner().items.into_iter().map(|x| x.tokens).collect();
        let outputs = self
            .state
            .infer_batch(items)
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        let results = outputs
            .into_iter()
            .map(|output| proto::InferResponse {
                next_token_id: output.next_token_id,
                logits_shape: output.logits_shape.iter().map(|v| *v as u64).collect(),
                backend: format!("{:?}", self.state.backend()),
            })
            .collect();

        Ok(Response::new(proto::InferBatchResponse { results }))
    }

    async fn generate(
        &self,
        request: Request<proto::GenerateRequest>,
    ) -> Result<Response<proto::GenerateResponse>, Status> {
        let payload = request.into_inner();
        let stop_token_id = if payload.has_stop_token_id {
            Some(payload.stop_token_id)
        } else {
            None
        };
        let output = self
            .state
            .generate(payload.prompt_tokens, payload.max_new_tokens as usize, stop_token_id)
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        Ok(Response::new(proto::GenerateResponse {
            prompt_tokens: output.prompt_tokens,
            generated_tokens: output.generated_tokens,
            logits_shape: output.logits_shape.iter().map(|v| *v as u64).collect(),
            next_token_id: output.next_token_id,
            stop_reason: output.stop_reason.to_string(),
            backend: format!("{:?}", self.state.backend()),
        }))
    }

    async fn generate_batch(
        &self,
        request: Request<proto::GenerateBatchRequest>,
    ) -> Result<Response<proto::GenerateBatchResponse>, Status> {
        let results = request
            .into_inner()
            .items
            .into_iter()
            .map(|item| {
                let stop_token_id = if item.has_stop_token_id {
                    Some(item.stop_token_id)
                } else {
                    None
                };
                self.state
                    .generate(item.prompt_tokens, item.max_new_tokens as usize, stop_token_id)
                    .map(|output| proto::GenerateResponse {
                        prompt_tokens: output.prompt_tokens,
                        generated_tokens: output.generated_tokens,
                        logits_shape: output.logits_shape.iter().map(|v| *v as u64).collect(),
                        next_token_id: output.next_token_id,
                        stop_reason: output.stop_reason.to_string(),
                        backend: format!("{:?}", self.state.backend()),
                    })
                    .map_err(|err| Status::invalid_argument(err.to_string()))
            })
            .collect::<Result<Vec<_>, _>>()?;

        Ok(Response::new(proto::GenerateBatchResponse { results }))
    }

    async fn chat(
        &self,
        request: Request<proto::ChatRequest>,
    ) -> Result<Response<proto::ChatResponse>, Status> {
        let payload = Self::map_chat_request(request.into_inner())?;
        let output = chat::chat(self.state.clone(), payload)
            .await
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        Ok(Response::new(proto::ChatResponse {
            prompt: output.prompt,
            reply: output.reply,
            tool_call: output.tool_call.map(|tool_call| proto::ToolCall {
                name: tool_call.name,
                arguments_json: tool_call.arguments_json,
                raw: tool_call.raw,
            }),
            prompt_tokens: output.prompt_tokens,
            generated_tokens: output.generated_tokens,
            stop_reason: output.stop_reason,
            backend: output.backend,
            tokenizer: Some(proto::ChatTokenizerMetadata {
                pad_token_id: output.tokenizer.pad_token_id,
                bos_token_id: output.tokenizer.bos_token_id,
                eos_token_id: output.tokenizer.eos_token_id,
                unk_token_id: output.tokenizer.unk_token_id,
                system_token_id: output.tokenizer.system_token_id,
                user_token_id: output.tokenizer.user_token_id,
                assistant_token_id: output.tokenizer.assistant_token_id,
                tool_token_id: output.tokenizer.tool_token_id,
                tool_choice_token_id: output.tokenizer.tool_choice_token_id,
                end_of_tools_token_id: output.tokenizer.end_of_tools_token_id,
                end_of_turn_token_id: output.tokenizer.end_of_turn_token_id,
                byte_offset: output.tokenizer.byte_offset,
            }),
        }))
    }

    async fn chat_stream(
        &self,
        request: Request<proto::ChatRequest>,
    ) -> Result<Response<Self::ChatStreamStream>, Status> {
        let payload = Self::map_chat_request(request.into_inner())?;
        let tokenizer = ChatTokenizer::default();

        validate_messages(payload.system.as_deref(), &payload.messages)
            .map_err(|err| Status::invalid_argument(err.to_string()))?;
        validate_tools(&payload.tools, payload.tool_choice.as_ref())
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        let prompt_tokens = tokenizer
            .encode_chat_prompt(
                payload.system.as_deref(),
                &payload.messages,
                &payload.tools,
                payload.tool_choice.as_ref(),
            )
            .map_err(|err| Status::invalid_argument(err.to_string()))?;

        if prompt_tokens.len() > self.state.limits().max_tokens_per_request {
            return Err(Status::invalid_argument(format!(
                "prompt length {} exceeds max_tokens_per_request {}",
                prompt_tokens.len(),
                self.state.limits().max_tokens_per_request
            )));
        }

        let stop_token_id = payload
            .stop_token_id
            .unwrap_or_else(|| tokenizer.default_stop_token_id());

        let mut context_tokens = prompt_tokens;
        let mut pending_bytes = Vec::new();
        let mut events: Vec<Result<proto::ChatStreamResponse, Status>> = Vec::new();
        let mut final_reason = "max_new_tokens".to_string();
        let mut full_reply = String::new();

        for _ in 0..payload.max_new_tokens {
            let output = self
                .state
                .generate(context_tokens.clone(), 1, Some(stop_token_id))
                .map_err(|err| Status::invalid_argument(err.to_string()))?;

            let next_token = match output.generated_tokens.first().copied() {
                Some(token) => token,
                None => break,
            };
            context_tokens.push(next_token);
            final_reason = output.stop_reason.to_string();

            if let Some(delta) = tokenizer.decode_stream_token(next_token, &mut pending_bytes) {
                if !delta.is_empty() {
                    full_reply.push_str(&delta);
                    events.push(Ok(proto::ChatStreamResponse {
                        event: "delta".to_string(),
                        delta,
                        stop_reason: String::new(),
                        done: false,
                        backend: format!("{:?}", self.state.backend()),
                        token_id: next_token,
                        has_token_id: true,
                        tool_call: None,
                    }));
                }
            }

            if next_token == stop_token_id || output.stop_reason.to_string() != "max_new_tokens" {
                break;
            }
        }

        if !pending_bytes.is_empty() {
            let tail = String::from_utf8_lossy(&pending_bytes).to_string();
            if !tail.is_empty() {
                full_reply.push_str(&tail);
                events.push(Ok(proto::ChatStreamResponse {
                    event: "delta".to_string(),
                    delta: tail,
                    stop_reason: String::new(),
                    done: false,
                    backend: format!("{:?}", self.state.backend()),
                    token_id: 0,
                    has_token_id: false,
                    tool_call: None,
                }));
            }
        }

        let done_tool_call = chat::parse_tool_call(&full_reply).map(|tool_call| proto::ToolCall {
            name: tool_call.name,
            arguments_json: tool_call.arguments_json,
            raw: tool_call.raw,
        });

        events.push(Ok(proto::ChatStreamResponse {
            event: "done".to_string(),
            delta: String::new(),
            stop_reason: final_reason,
            done: true,
            backend: format!("{:?}", self.state.backend()),
            token_id: 0,
            has_token_id: false,
            tool_call: done_tool_call,
        }));

        Ok(Response::new(Box::pin(iter(events).map(|event| event)) as Self::ChatStreamStream))
    }

    async fn reload(
        &self,
        request: Request<proto::ReloadRequest>,
    ) -> Result<Response<proto::ReloadResponse>, Status> {
        let payload = request.into_inner();

        let mut next = self
            .state
            .config_snapshot()
            .map_err(|err| Status::internal(err.to_string()))?;

        if !payload.backend.is_empty() {
            next.backend = BackendKind::parse(&payload.backend).ok_or_else(|| {
                Status::invalid_argument(format!(
                    "Invalid backend '{}'. Use: auto, ndarray, tch, wgpu, vulkan, cuda",
                    payload.backend
                ))
            })?;
        }
        if payload.max_batch_size > 0 {
            next.limits.max_batch_size = payload.max_batch_size as usize;
        }
        if payload.max_tokens_per_request > 0 {
            next.limits.max_tokens_per_request = payload.max_tokens_per_request as usize;
        }
        if payload.max_payload_bytes > 0 {
            next.limits.max_payload_bytes = payload.max_payload_bytes as usize;
        }
        if !payload.artifact_dir.is_empty() {
            next.model.artifact_dir = payload.artifact_dir;
        }

        self.state
            .reload(next)
            .map_err(|err| Status::internal(err.to_string()))?;

        let snapshot = self
            .state
            .config_snapshot()
            .map_err(|err| Status::internal(err.to_string()))?;

        Ok(Response::new(proto::ReloadResponse {
            status: "reloaded".to_string(),
            backend: format!("{:?}", self.state.backend()),
            artifact_dir: snapshot.model.artifact_dir,
        }))
    }
}

pub async fn serve(state: AppState, addr: SocketAddr) -> Result<(), tonic::transport::Error> {
    let service = InferenceGrpcService::new(state);

    Server::builder()
        .add_service(proto::inference_service_server::InferenceServiceServer::new(service))
        .serve(addr)
        .await
}
