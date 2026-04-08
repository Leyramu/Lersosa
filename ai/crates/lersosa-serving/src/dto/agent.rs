use lersosa_core::training::vision_autotune::VisionHyperParams;
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "lowercase")]
pub enum AgentRole {
    System,
    User,
    Assistant,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct AgentMessage {
    pub role: AgentRole,
    pub content: String,
}

#[derive(Debug, Clone, Deserialize)]
pub struct AgentChatRequest {
    pub messages: Vec<AgentMessage>,
    #[serde(default)]
    pub execute: bool,
    pub run_id: Option<String>,
    pub max_source_pages: Option<usize>,
    pub max_images: Option<usize>,
    pub async_mode: Option<bool>,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "snake_case")]
pub enum AgentTaskStage {
    Searching,
    Crawling,
    Training,
    Selecting,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
#[serde(rename_all = "snake_case")]
pub enum AgentTaskStatus {
    Queued,
    Running,
    Completed,
    Failed,
}

#[derive(Debug, Clone, Serialize)]
pub struct AgentTaskSnapshot {
    pub task_id: String,
    pub status: AgentTaskStatus,
    pub stage: Option<AgentTaskStage>,
    pub result: Option<AgentChatResponse>,
    pub error: Option<String>,
}

#[derive(Debug, Clone, Serialize)]
pub struct AgentToolExecution {
    pub name: String,
    pub arguments_json: String,
    pub output_summary: String,
}

#[derive(Debug, Clone, Serialize)]
pub struct AgentChatResponse {
    pub inferred_goal: String,
    pub agent_reply: String,
    pub planned_tools: Vec<String>,
    pub tool_executions: Vec<AgentToolExecution>,
    pub run_artifact_dir: Option<String>,
    pub best_model_path: Option<String>,
    pub best_hyper_params: Option<VisionHyperParams>,
    pub leaderboard_path: Option<String>,
    pub task_id: Option<String>,
    pub task_status: Option<AgentTaskStatus>,
    pub task_stage: Option<AgentTaskStage>,
}

