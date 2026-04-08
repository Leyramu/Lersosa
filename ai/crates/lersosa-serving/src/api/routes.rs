use axum::{Json, Router, extract::{Path, State}, routing::{get, post}};
use axum::response::sse::Sse;
use lersosa_core::runtime::BackendKind;

use crate::{
    api::{agent, chat},
    app_state::AppState,
    dto::agent::{AgentChatRequest, AgentChatResponse, AgentTaskSnapshot},
    dto::chat::{
        ChatAgentLoopRequest, ChatAgentLoopResponse, ChatContinuationRequest, ChatRequest,
        ChatResponse,
    },
    dto::infer::{
        GenerateBatchRequest, GenerateBatchResponse, GenerateRequest, GenerateResponse,
        HealthResponse, InferBatchRequest, InferBatchResponse, InferRequest, InferResponse,
        ReloadRequest, ReloadResponse,
    },
    error::ServiceError,
    service_config::ServiceRuntimeConfig,
};

pub fn router(state: AppState) -> Router {
    Router::new()
        .route("/healthz", get(healthz))
        .route("/readyz", get(readyz))
        .route("/v1/infer", post(infer))
        .route("/v1/infer/batch", post(infer_batch))
        .route("/v1/generate", post(generate))
        .route("/v1/generate/batch", post(generate_batch))
        .route("/v1/chat", post(chat_turn))
        .route("/v1/agent/chat", post(agent_chat_turn))
        .route("/v1/agent/tasks", post(agent_task_submit))
        .route("/v1/agent/jobs/{task_id}", get(agent_task_get))
        .route("/v1/chat/continue", post(chat_turn_continue))
        .route("/v1/chat/agent-loop", post(chat_agent_loop_turn))
        .route("/v1/chat/stream", post(chat_turn_stream))
        .route("/v1/reload", post(reload))
        .with_state(state)
}

async fn healthz(State(state): State<AppState>) -> Json<HealthResponse> {
    Json(HealthResponse {
        status: "ok",
        backend: format!("{:?}", state.backend()),
    })
}

async fn readyz(State(state): State<AppState>) -> Json<HealthResponse> {
    Json(HealthResponse {
        status: "ready",
        backend: format!("{:?}", state.backend()),
    })
}

async fn infer(
    State(state): State<AppState>,
    Json(payload): Json<InferRequest>,
) -> Result<Json<InferResponse>, ServiceError> {
    let output = state.infer(payload.tokens)?;

    Ok(Json(InferResponse {
        next_token_id: output.next_token_id,
        logits_shape: output.logits_shape,
        backend: format!("{:?}", state.backend()),
    }))
}

async fn infer_batch(
    State(state): State<AppState>,
    Json(payload): Json<InferBatchRequest>,
) -> Result<Json<InferBatchResponse>, ServiceError> {
    let items: Vec<Vec<i64>> = payload.items.into_iter().map(|item| item.tokens).collect();
    let outputs = state.infer_batch(items)?;

    let results = outputs
        .into_iter()
        .map(|output| InferResponse {
            next_token_id: output.next_token_id,
            logits_shape: output.logits_shape,
            backend: format!("{:?}", state.backend()),
        })
        .collect();

    Ok(Json(InferBatchResponse { results }))
}

async fn generate(
    State(state): State<AppState>,
    Json(payload): Json<GenerateRequest>,
) -> Result<Json<GenerateResponse>, ServiceError> {
    let output = state.generate(payload.prompt_tokens, payload.max_new_tokens, payload.stop_token_id)?;

    Ok(Json(GenerateResponse {
        prompt_tokens: output.prompt_tokens,
        generated_tokens: output.generated_tokens,
        logits_shape: output.logits_shape,
        next_token_id: output.next_token_id,
        stop_reason: output.stop_reason.to_string(),
        backend: format!("{:?}", state.backend()),
    }))
}

async fn generate_batch(
    State(state): State<AppState>,
    Json(payload): Json<GenerateBatchRequest>,
) -> Result<Json<GenerateBatchResponse>, ServiceError> {
    let results = payload
        .items
        .into_iter()
        .map(|item| {
            let output = state.generate(item.prompt_tokens, item.max_new_tokens, item.stop_token_id)?;
            Ok(GenerateResponse {
                prompt_tokens: output.prompt_tokens,
                generated_tokens: output.generated_tokens,
                logits_shape: output.logits_shape,
                next_token_id: output.next_token_id,
                stop_reason: output.stop_reason.to_string(),
                backend: format!("{:?}", state.backend()),
            })
        })
        .collect::<Result<Vec<_>, ServiceError>>()?;

    Ok(Json(GenerateBatchResponse { results }))
}

async fn chat_turn(
    State(state): State<AppState>,
    Json(payload): Json<ChatRequest>,
) -> Result<Json<ChatResponse>, ServiceError> {
    let response = chat::chat(state, payload).await?;
    Ok(Json(response))
}

async fn agent_chat_turn(
    Json(payload): Json<AgentChatRequest>,
) -> Result<Json<AgentChatResponse>, ServiceError> {
    let response = agent::agent_chat(payload).await?;
    Ok(Json(response))
}

async fn agent_task_submit(
    Json(payload): Json<AgentChatRequest>,
) -> Result<Json<AgentTaskSnapshot>, ServiceError> {
    let response = agent::submit_agent_task(payload).await?;
    Ok(Json(response))
}

async fn agent_task_get(
    Path(task_id): Path<String>,
) -> Result<Json<AgentTaskSnapshot>, ServiceError> {
    let snapshot = agent::get_agent_task(&task_id)
        .await
        .ok_or_else(|| ServiceError::Validation(format!("task '{task_id}' not found")))?;
    Ok(Json(snapshot))
}

async fn chat_turn_continue(
    State(state): State<AppState>,
    Json(payload): Json<ChatContinuationRequest>,
) -> Result<Json<ChatResponse>, ServiceError> {
    let response = chat::chat_continue(state, payload).await?;
    Ok(Json(response))
}

async fn chat_agent_loop_turn(
    State(state): State<AppState>,
    Json(payload): Json<ChatAgentLoopRequest>,
) -> Result<Json<ChatAgentLoopResponse>, ServiceError> {
    let response = chat::chat_agent_loop(state, payload).await?;
    Ok(Json(response))
}

async fn chat_turn_stream(
    State(state): State<AppState>,
    Json(payload): Json<ChatRequest>,
) -> Result<Sse<impl tokio_stream::Stream<Item = Result<axum::response::sse::Event, std::convert::Infallible>>>, ServiceError> {
    chat::chat_stream(state, payload).await
}

async fn reload(
    State(state): State<AppState>,
    Json(payload): Json<ReloadRequest>,
) -> Result<Json<ReloadResponse>, ServiceError> {
    let mut next: ServiceRuntimeConfig = state.config_snapshot()?;

    if let Some(backend) = payload.backend {
        next.backend = BackendKind::parse(&backend).ok_or_else(|| {
            ServiceError::Validation(format!(
                "Invalid backend '{backend}'. Use: auto, ndarray, tch, wgpu, vulkan, cuda"
            ))
        })?;
    }
    if let Some(max_batch_size) = payload.max_batch_size {
        next.limits.max_batch_size = max_batch_size;
    }
    if let Some(max_tokens_per_request) = payload.max_tokens_per_request {
        next.limits.max_tokens_per_request = max_tokens_per_request;
    }
    if let Some(max_payload_bytes) = payload.max_payload_bytes {
        next.limits.max_payload_bytes = max_payload_bytes;
    }
    if let Some(artifact_dir) = payload.artifact_dir {
        next.model.artifact_dir = artifact_dir;
    }

    state.reload(next)?;

    let snapshot = state.config_snapshot()?;

    Ok(Json(ReloadResponse {
        status: "reloaded",
        backend: format!("{:?}", state.backend()),
        artifact_dir: snapshot.model.artifact_dir,
    }))
}
