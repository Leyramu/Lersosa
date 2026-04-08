use crate::{
    app_state::AppState,
    dto::chat::{
        ChatAgentLoopRequest, ChatAgentLoopResponse, ChatContinuationRequest, ChatMessage,
        ChatRequest, ChatResponse, ChatRole, ChatStreamChunk, ChatStreamDone,
        ChatTokenizerMetadata, ChatToolCall,
    },
    error::ServiceError,
    tokenization::chat_tokenizer::{ChatTokenizer, SpecialToken, validate_messages, validate_tools},
};
use axum::response::sse::{Event, Sse};
use jsonschema::validator_for;
use serde_json::Value;
use std::convert::Infallible;
use tokio_stream::{Stream, StreamExt as _, iter};

pub(crate) fn parse_tool_call(reply: &str) -> Option<ChatToolCall> {
    let patterns = [
        ("<tool_call>", "</tool_call>"),
        ("<|tool_call|>", "</|tool_call|>"),
    ];

    for (start, end) in patterns {
        let Some(start_pos) = reply.find(start) else {
            continue;
        };
        let body_start = start_pos + start.len();
        let Some(end_rel_pos) = reply[body_start..].find(end) else {
            continue;
        };
        let raw = reply[body_start..(body_start + end_rel_pos)].trim().to_string();
        if raw.is_empty() {
            continue;
        }

        let value = serde_json::from_str::<Value>(&raw).ok()?;
        let name = value.get("name")?.as_str()?.trim().to_string();
        if name.is_empty() {
            return None;
        }
        let arguments = value
            .get("arguments")
            .cloned()
            .unwrap_or(Value::Object(Default::default()));
        let arguments_json = serde_json::to_string(&arguments).ok()?;

        return Some(ChatToolCall {
            name,
            arguments_json,
            raw,
        });
    }

    None
}

fn validate_tool_call_schema(request: &ChatRequest, tool_call: &ChatToolCall) -> Result<(), ServiceError> {
    let tool = request
        .tools
        .iter()
        .find(|tool| tool.name == tool_call.name)
        .ok_or_else(|| {
            ServiceError::Validation(format!(
                "tool_call '{}' is not present in request.tools",
                tool_call.name
            ))
        })?;

    let schema_json = tool.parameters_json.as_ref().ok_or_else(|| {
        ServiceError::Validation(format!(
            "tool '{}' is missing parameters_json for strict schema validation",
            tool_call.name
        ))
    })?;

    let schema_value: Value = serde_json::from_str(schema_json).map_err(|err| {
        ServiceError::Validation(format!(
            "invalid parameters_json schema for tool '{}': {err}",
            tool_call.name
        ))
    })?;

    let args_value: Value = serde_json::from_str(&tool_call.arguments_json).map_err(|err| {
        ServiceError::Validation(format!(
            "tool_call '{}' arguments_json is not valid JSON: {err}",
            tool_call.name
        ))
    })?;

    let validator = validator_for(&schema_value).map_err(|err| {
        ServiceError::Validation(format!(
            "failed to compile JSON schema for tool '{}': {err}",
            tool_call.name
        ))
    })?;

    let details = validator
        .iter_errors(&args_value)
        .take(3)
        .map(|error| error.to_string())
        .collect::<Vec<_>>();

    if !details.is_empty() {
        return Err(ServiceError::Validation(format!(
            "tool_call '{}' arguments do not match JSON schema: {}",
            tool_call.name,
            details.join("; ")
        )));
    }

    Ok(())
}

pub async fn chat(
    state: AppState,
    request: ChatRequest,
) -> Result<ChatResponse, ServiceError> {
    let tokenizer = ChatTokenizer::default();
    validate_messages(request.system.as_deref(), &request.messages)?;
    validate_tools(&request.tools, request.tool_choice.as_ref())?;

    let prompt = tokenizer.render_prompt(
        request.system.as_deref(),
        &request.messages,
        &request.tools,
        request.tool_choice.as_ref(),
    )?;
    let prompt_tokens = tokenizer.encode_chat_prompt(
        request.system.as_deref(),
        &request.messages,
        &request.tools,
        request.tool_choice.as_ref(),
    )?;

    if prompt_tokens.len() > state.limits().max_tokens_per_request {
        return Err(ServiceError::Validation(format!(
            "prompt length {} exceeds max_tokens_per_request {}",
            prompt_tokens.len(),
            state.limits().max_tokens_per_request
        )));
    }

    let output = state.generate(
        prompt_tokens.clone(),
        request.max_new_tokens,
        request.stop_token_id.or(Some(tokenizer.default_stop_token_id())),
    )?;

    let reply = tokenizer.decode_bytes(&output.generated_tokens);
    let tool_call = parse_tool_call(&reply);
    if let Some(tool_call) = &tool_call {
        validate_tool_call_schema(&request, tool_call)?;
    }

    Ok(ChatResponse {
        prompt,
        reply,
        tool_call,
        prompt_tokens: output.prompt_tokens,
        generated_tokens: output.generated_tokens,
        stop_reason: output.stop_reason.to_string(),
        backend: format!("{:?}", state.backend()),
        tokenizer: ChatTokenizerMetadata {
            pad_token_id: SpecialToken::Pad.id(),
            bos_token_id: SpecialToken::Bos.id(),
            eos_token_id: SpecialToken::Eos.id(),
            unk_token_id: SpecialToken::Unk.id(),
            system_token_id: SpecialToken::System.id(),
            user_token_id: SpecialToken::User.id(),
            assistant_token_id: SpecialToken::Assistant.id(),
            tool_token_id: SpecialToken::Tool.id(),
            tool_choice_token_id: SpecialToken::ToolChoice.id(),
            end_of_tools_token_id: SpecialToken::EndOfTools.id(),
            end_of_turn_token_id: SpecialToken::EndOfTurn.id(),
            byte_offset: tokenizer.byte_offset(),
        },
    })
}

pub fn build_continuation_request(payload: ChatContinuationRequest) -> Result<ChatRequest, ServiceError> {
    if payload.tool_call.name.trim().is_empty() {
        return Err(ServiceError::Validation(
            "tool_call.name must not be empty".to_string(),
        ));
    }
    if payload.tool_output.trim().is_empty() {
        return Err(ServiceError::Validation(
            "tool_output must not be empty".to_string(),
        ));
    }

    let mut next = payload.prior_request;
    validate_tool_call_schema(&next, &payload.tool_call)?;
    // Tool metadata is only needed for strict validation before continuation generation.
    // Drop it afterward to keep prompts compact on tiny-context toy artifacts.
    next.tools.clear();
    next.tool_choice = None;

    // Keep continuation compact so small-context toy artifacts can still run.
    next.messages.push(ChatMessage {
        role: ChatRole::Assistant,
        content: payload.tool_call.name.clone(),
    });
    next.messages.push(ChatMessage {
        role: ChatRole::User,
        content: payload.tool_output.trim().to_string(),
    });

    if let Some(max_new_tokens) = payload.max_new_tokens {
        next.max_new_tokens = max_new_tokens;
    }
    if payload.stop_token_id.is_some() {
        next.stop_token_id = payload.stop_token_id;
    }

    Ok(next)
}

pub async fn chat_continue(
    state: AppState,
    payload: ChatContinuationRequest,
) -> Result<ChatResponse, ServiceError> {
    let request = build_continuation_request(payload)?;
    chat(state, request).await
}

pub async fn chat_agent_loop(
    state: AppState,
    payload: ChatAgentLoopRequest,
) -> Result<ChatAgentLoopResponse, ServiceError> {
    if payload.tool_output.trim().is_empty() {
        return Err(ServiceError::Validation(
            "tool_output must not be empty".to_string(),
        ));
    }

    let prior_request = payload.prior_request;
    let mut initial_response = None;

    let used_tool_call = if let Some(tool_call) = payload.tool_call.clone() {
        validate_tool_call_schema(&prior_request, &tool_call)?;
        tool_call
    } else {
        let first = chat(state.clone(), prior_request.clone()).await?;
        let inferred = first.tool_call.clone().ok_or_else(|| {
            ServiceError::Validation(
                "agent loop expected a tool_call but initial response did not contain one"
                    .to_string(),
            )
        })?;
        initial_response = Some(first);
        inferred
    };

    let continuation_request = ChatContinuationRequest {
        prior_request,
        tool_call: used_tool_call.clone(),
        tool_output: payload.tool_output,
        max_new_tokens: payload.max_new_tokens,
        stop_token_id: payload.stop_token_id,
    };

    let final_response = chat_continue(state, continuation_request).await?;

    Ok(ChatAgentLoopResponse {
        initial_response,
        used_tool_call,
        final_response,
    })
}

pub async fn chat_stream(
    state: AppState,
    request: ChatRequest,
) -> Result<Sse<impl Stream<Item = Result<Event, Infallible>>>, ServiceError> {
    let tokenizer = ChatTokenizer::default();
    validate_messages(request.system.as_deref(), &request.messages)?;
    validate_tools(&request.tools, request.tool_choice.as_ref())?;

    let prompt_tokens = tokenizer.encode_chat_prompt(
        request.system.as_deref(),
        &request.messages,
        &request.tools,
        request.tool_choice.as_ref(),
    )?;

    if prompt_tokens.len() > state.limits().max_tokens_per_request {
        return Err(ServiceError::Validation(format!(
            "prompt length {} exceeds max_tokens_per_request {}",
            prompt_tokens.len(),
            state.limits().max_tokens_per_request
        )));
    }

    let stop_token_id = request
        .stop_token_id
        .unwrap_or_else(|| tokenizer.default_stop_token_id());
    let mut context_tokens = prompt_tokens;
    let mut pending_bytes = Vec::new();
    let mut events: Vec<Result<Event, Infallible>> = Vec::new();
    let mut final_reason = "max_new_tokens".to_string();
    let backend = format!("{:?}", state.backend());
    let mut full_reply = String::new();

    for _ in 0..request.max_new_tokens {
        let output = state.generate(context_tokens.clone(), 1, Some(stop_token_id))?;
        let next_token = match output.generated_tokens.first().copied() {
            Some(token) => token,
            None => break,
        };

        context_tokens.push(next_token);
        final_reason = output.stop_reason.to_string();

        if let Some(delta) = tokenizer.decode_stream_token(next_token, &mut pending_bytes) {
            if !delta.is_empty() {
                full_reply.push_str(&delta);
                let payload = ChatStreamChunk {
                    event: "delta",
                    delta,
                    done: false,
                    token_id: Some(next_token),
                };
                let payload = serde_json::to_string(&payload)
                    .map_err(|err| ServiceError::Internal(format!("failed to encode stream chunk: {err}")))?;
                events.push(Ok(Event::default().event("delta").data(payload)));
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
            let payload = ChatStreamChunk {
                event: "delta",
                delta: tail,
                done: false,
                token_id: None,
            };
            let payload = serde_json::to_string(&payload)
                .map_err(|err| ServiceError::Internal(format!("failed to encode stream chunk: {err}")))?;
            events.push(Ok(Event::default().event("delta").data(payload)));
        }
    }

    let done = ChatStreamDone {
        event: "done",
        done: true,
        stop_reason: final_reason,
        backend,
        tool_call: parse_tool_call(&full_reply),
    };
    let done = serde_json::to_string(&done)
        .map_err(|err| ServiceError::Internal(format!("failed to encode stream done event: {err}")))?;
    events.push(Ok(Event::default().event("done").data(done)));

    Ok(Sse::new(iter(events).map(|event| event)))
}

#[cfg(test)]
mod tests {
    use super::*;
    use crate::dto::chat::{ChatMessage, ChatRole};

    fn msg(role: ChatRole, content: &str) -> ChatMessage {
        ChatMessage {
            role,
            content: content.to_string(),
        }
    }

    #[test]
    fn multi_turn_prompt_assembly_works() {
        let tokenizer = ChatTokenizer::default();
        let messages = vec![
            msg(ChatRole::User, "hello"),
            msg(ChatRole::Assistant, "hi"),
            msg(ChatRole::User, "what is transformer"),
        ];

        let prompt = tokenizer
            .render_prompt(Some("you are concise"), &messages, &[], None)
            .expect("prompt should render");
        let tokens = tokenizer
            .encode_chat_prompt(Some("you are concise"), &messages, &[], None)
            .expect("prompt should encode");

        assert!(!tokens.is_empty());
        assert_eq!(tokens[0], SpecialToken::Bos.id());
        assert!(prompt.contains("<|assistant|>"));
    }

    #[test]
    fn rejects_invalid_multi_turn_order() {
        let bad_messages = vec![msg(ChatRole::User, "u1"), msg(ChatRole::User, "u2")];
        let error = validate_messages(None, &bad_messages).expect_err("must reject bad order");
        assert!(error.to_string().contains("alternate"));
    }

    #[test]
    fn parses_tool_call_block_from_reply() {
        let reply = r#"normal text <tool_call>{"name":"lookup_weather","arguments":{"city":"beijing"}}</tool_call>"#;
        let parsed = parse_tool_call(reply).expect("tool call should be parsed");
        assert_eq!(parsed.name, "lookup_weather");
        assert!(parsed.arguments_json.contains("beijing"));
    }

    #[test]
    fn builds_continuation_turns_with_tool_result() {
        let payload = ChatContinuationRequest {
            prior_request: ChatRequest {
                system: None,
                messages: vec![msg(ChatRole::User, "北京天气如何？")],
                tools: vec![crate::dto::chat::ChatToolDefinition {
                    name: "lookup_weather".to_string(),
                    description: None,
                    parameters_json: Some(
                        "{\"type\":\"object\",\"properties\":{\"city\":{\"type\":\"string\"}},\"required\":[\"city\"]}"
                            .to_string(),
                    ),
                }],
                tool_choice: None,
                max_new_tokens: 16,
                stop_token_id: None,
            },
            tool_call: ChatToolCall {
                name: "lookup_weather".to_string(),
                arguments_json: "{\"city\":\"beijing\"}".to_string(),
                raw: "{\"name\":\"lookup_weather\",\"arguments\":{\"city\":\"beijing\"}}".to_string(),
            },
            tool_output: "晴，23C".to_string(),
            max_new_tokens: Some(8),
            stop_token_id: None,
        };

        let request = build_continuation_request(payload).expect("continuation request should build");
        assert_eq!(request.messages.len(), 3);
        assert!(request.messages[1].content.contains("lookup_weather"));
        assert_eq!(request.messages[2].content, "晴，23C");
        assert_eq!(request.max_new_tokens, 8);
    }

    #[test]
    fn rejects_tool_call_when_schema_mismatch() {
        let request = ChatRequest {
            system: None,
            messages: vec![msg(ChatRole::User, "x")],
            tools: vec![crate::dto::chat::ChatToolDefinition {
                name: "lookup_weather".to_string(),
                description: None,
                parameters_json: Some(
                    "{\"type\":\"object\",\"properties\":{\"city\":{\"type\":\"string\"}},\"required\":[\"city\"]}"
                        .to_string(),
                ),
            }],
            tool_choice: None,
            max_new_tokens: 8,
            stop_token_id: None,
        };

        let tool_call = ChatToolCall {
            name: "lookup_weather".to_string(),
            arguments_json: "{\"zipcode\":10001}".to_string(),
            raw: String::new(),
        };

        let err = validate_tool_call_schema(&request, &tool_call)
            .expect_err("schema mismatch should fail");
        assert!(err.to_string().contains("do not match JSON schema"));
    }
}

