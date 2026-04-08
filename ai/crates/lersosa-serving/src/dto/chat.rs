use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, PartialEq, Eq, Serialize, Deserialize)]
#[serde(rename_all = "lowercase")]
pub enum ChatRole {
    System,
    User,
    Assistant,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ChatMessage {
    pub role: ChatRole,
    pub content: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ChatToolDefinition {
    pub name: String,
    pub description: Option<String>,
    pub parameters_json: Option<String>,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(tag = "type", content = "name", rename_all = "snake_case")]
pub enum ChatToolChoice {
    Auto,
    None,
    Required,
    Named(String),
}

#[derive(Debug, Clone, Deserialize)]
pub struct ChatRequest {
    pub system: Option<String>,
    pub messages: Vec<ChatMessage>,
    #[serde(default)]
    pub tools: Vec<ChatToolDefinition>,
    pub tool_choice: Option<ChatToolChoice>,
    #[serde(default = "default_max_new_tokens")]
    pub max_new_tokens: usize,
    pub stop_token_id: Option<i64>,
}

#[derive(Debug, Deserialize)]
pub struct ChatContinuationRequest {
    pub prior_request: ChatRequest,
    pub tool_call: ChatToolCall,
    pub tool_output: String,
    pub max_new_tokens: Option<usize>,
    pub stop_token_id: Option<i64>,
}

#[derive(Debug, Deserialize)]
pub struct ChatAgentLoopRequest {
    pub prior_request: ChatRequest,
    pub tool_output: String,
    pub tool_call: Option<ChatToolCall>,
    pub max_new_tokens: Option<usize>,
    pub stop_token_id: Option<i64>,
}

#[derive(Debug, Serialize)]
pub struct ChatAgentLoopResponse {
    pub initial_response: Option<ChatResponse>,
    pub used_tool_call: ChatToolCall,
    pub final_response: ChatResponse,
}

fn default_max_new_tokens() -> usize {
    64
}

#[derive(Debug, Serialize)]
pub struct ChatResponse {
    pub prompt: String,
    pub reply: String,
    pub tool_call: Option<ChatToolCall>,
    pub prompt_tokens: Vec<i64>,
    pub generated_tokens: Vec<i64>,
    pub stop_reason: String,
    pub backend: String,
    pub tokenizer: ChatTokenizerMetadata,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ChatToolCall {
    pub name: String,
    pub arguments_json: String,
    pub raw: String,
}

#[derive(Debug, Clone, Serialize)]
pub struct ChatStreamChunk {
    pub event: &'static str,
    pub delta: String,
    pub done: bool,
    pub token_id: Option<i64>,
}

#[derive(Debug, Clone, Serialize)]
pub struct ChatStreamDone {
    pub event: &'static str,
    pub done: bool,
    pub stop_reason: String,
    pub backend: String,
    pub tool_call: Option<ChatToolCall>,
}

#[derive(Debug, Serialize)]
pub struct ChatTokenizerMetadata {
    pub pad_token_id: i64,
    pub bos_token_id: i64,
    pub eos_token_id: i64,
    pub unk_token_id: i64,
    pub system_token_id: i64,
    pub user_token_id: i64,
    pub assistant_token_id: i64,
    pub tool_token_id: i64,
    pub tool_choice_token_id: i64,
    pub end_of_tools_token_id: i64,
    pub end_of_turn_token_id: i64,
    pub byte_offset: i64,
}

