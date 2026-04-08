use serde::{Deserialize, Serialize};

#[derive(Debug, Deserialize)]
pub struct InferRequest {
    pub tokens: Vec<i64>,
}

#[derive(Debug, Deserialize)]
pub struct InferBatchRequest {
    pub items: Vec<InferRequest>,
}

#[derive(Debug, Serialize)]
pub struct InferResponse {
    pub next_token_id: i64,
    pub logits_shape: [usize; 3],
    pub backend: String,
}

#[derive(Debug, Deserialize)]
pub struct GenerateRequest {
    pub prompt_tokens: Vec<i64>,
    #[serde(default = "default_max_new_tokens")]
    pub max_new_tokens: usize,
    pub stop_token_id: Option<i64>,
}

fn default_max_new_tokens() -> usize {
    32
}

#[derive(Debug, Serialize)]
pub struct GenerateResponse {
    pub prompt_tokens: Vec<i64>,
    pub generated_tokens: Vec<i64>,
    pub logits_shape: [usize; 3],
    pub next_token_id: i64,
    pub stop_reason: String,
    pub backend: String,
}

#[derive(Debug, Serialize)]
pub struct InferBatchResponse {
    pub results: Vec<InferResponse>,
}

#[derive(Debug, Deserialize)]
pub struct GenerateBatchRequest {
    pub items: Vec<GenerateRequest>,
}

#[derive(Debug, Serialize)]
pub struct GenerateBatchResponse {
    pub results: Vec<GenerateResponse>,
}

#[derive(Debug, Deserialize)]
pub struct ReloadRequest {
    pub backend: Option<String>,
    pub artifact_dir: Option<String>,
    pub max_batch_size: Option<usize>,
    pub max_tokens_per_request: Option<usize>,
    pub max_payload_bytes: Option<usize>,
}

#[derive(Debug, Serialize)]
pub struct ReloadResponse {
    pub status: &'static str,
    pub backend: String,
    pub artifact_dir: String,
}

#[derive(Debug, Serialize)]
pub struct HealthResponse {
    pub status: &'static str,
    pub backend: String,
}
