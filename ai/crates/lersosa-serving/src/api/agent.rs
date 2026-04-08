use std::{
    collections::HashMap,
    sync::{Arc, OnceLock},
    time::{SystemTime, UNIX_EPOCH},
};

use lersosa_core::training::vision_autotune::run_fire_vision_autotune;
use lersosa_crawler::{ImageCrawlRequest, crawl_image_dataset, search_image_datasets};
use tokio::sync::RwLock;

use crate::{
    dto::agent::{
        AgentChatRequest, AgentChatResponse, AgentRole, AgentTaskSnapshot, AgentTaskStage,
        AgentTaskStatus, AgentToolExecution,
    },
    error::ServiceError,
};

static TASKS: OnceLock<Arc<RwLock<HashMap<String, AgentTaskSnapshot>>>> = OnceLock::new();

pub async fn agent_chat(payload: AgentChatRequest) -> Result<AgentChatResponse, ServiceError> {
    if payload.async_mode.unwrap_or(false) {
        let snapshot = submit_agent_task(payload).await?;
        return Ok(queued_response(snapshot));
    }

    run_pipeline(payload, None).await
}

pub async fn submit_agent_task(payload: AgentChatRequest) -> Result<AgentTaskSnapshot, ServiceError> {
    validate_payload(&payload)?;

    let task_id = payload.run_id.clone().unwrap_or_else(default_run_id);
    let snapshot = AgentTaskSnapshot {
        task_id: task_id.clone(),
        status: AgentTaskStatus::Queued,
        stage: None,
        result: None,
        error: None,
    };

    {
        let tasks = task_store();
        let mut guard = tasks.write().await;
        guard.insert(task_id.clone(), snapshot.clone());
    }

    tokio::spawn(async move {
        set_task_status(&task_id, AgentTaskStatus::Running, None, None).await;
        set_task_stage(&task_id, AgentTaskStage::Searching).await;
        match run_pipeline(payload, Some(&task_id)).await {
            Ok(result) => {
                set_task_status(&task_id, AgentTaskStatus::Completed, Some(result), None).await;
            }
            Err(err) => {
                set_task_status(
                    &task_id,
                    AgentTaskStatus::Failed,
                    None,
                    Some(err.to_string()),
                )
                .await;
            }
        }
    });

    Ok(snapshot)
}

pub async fn get_agent_task(task_id: &str) -> Option<AgentTaskSnapshot> {
    let tasks = task_store();
    let guard = tasks.read().await;
    guard.get(task_id).cloned()
}

async fn run_pipeline(
    payload: AgentChatRequest,
    task_id: Option<&str>,
) -> Result<AgentChatResponse, ServiceError> {
    validate_payload(&payload)?;

    let inferred_goal = infer_goal(&payload);
    let planned_tools = vec![
        "search_dataset".to_string(),
        "crawl_images".to_string(),
        "train_and_tune".to_string(),
        "select_best_model".to_string(),
    ];

    if !payload.execute {
        return Ok(AgentChatResponse {
            inferred_goal: inferred_goal.clone(),
            agent_reply: format!(
                "Goal understood: {inferred_goal}. Set execute=true to run dataset search, image crawling, automatic tuning, and best-model saving."
            ),
            planned_tools,
            tool_executions: Vec::new(),
            run_artifact_dir: None,
            best_model_path: None,
            best_hyper_params: None,
            leaderboard_path: None,
            task_id: None,
            task_status: None,
            task_stage: None,
        });
    }

    let run_id = payload.run_id.unwrap_or_else(default_run_id);
    let run_artifact_dir = format!("artifacts/runs/{run_id}");
    let dataset_dir = format!("{run_artifact_dir}/dataset");
    let training_dir = format!("{run_artifact_dir}/training");

    let max_source_pages = payload.max_source_pages.unwrap_or(5).max(1);
    let max_images = payload.max_images.unwrap_or(200).max(1);

    let mut executions = Vec::new();

    if let Some(task_id) = task_id {
        set_task_stage(task_id, AgentTaskStage::Searching).await;
    }

    let mut used_fallback_sources = false;
    let search_hits = match search_image_datasets(&inferred_goal, max_source_pages).await {
        Ok(hits) if !hits.is_empty() => hits,
        _ => {
            used_fallback_sources = true;
            offline_dataset_sources(&inferred_goal)
                .into_iter()
                .take(max_source_pages)
                .map(|url| lersosa_crawler::DatasetSearchHit {
                    title: "offline_fallback".to_string(),
                    url,
                })
                .collect()
        }
    };

    if search_hits.is_empty() {
        return Err(ServiceError::Internal(
            "dataset search failed and no fallback sources available".to_string(),
        ));
    }

    let search_args = serde_json::json!({
        "query": inferred_goal,
        "limit": max_source_pages
    });
    executions.push(AgentToolExecution {
        name: "search_dataset".to_string(),
        arguments_json: serialize_json(&search_args)?,
        output_summary: format!(
            "found {} candidate dataset pages{}, fallback_used={}",
            search_hits.len(),
            summarize_urls(&search_hits),
            used_fallback_sources
        ),
    });

    if let Some(task_id) = task_id {
        set_task_stage(task_id, AgentTaskStage::Crawling).await;
    }

    let source_urls = search_hits
        .iter()
        .map(|hit| hit.url.clone())
        .collect::<Vec<_>>();

    let crawl_request = ImageCrawlRequest {
        query: inferred_goal.clone(),
        source_urls: Some(source_urls),
        output_dir: dataset_dir.clone(),
        max_source_pages,
        max_images,
        user_agent: "LersosaAgentCrawler/1.0".to_string(),
        timeout_secs: 20,
        blocked_domains: vec![
            "facebook.com".to_string(),
            "instagram.com".to_string(),
            "pinterest.com".to_string(),
        ],
        allowed_license_keywords: vec![
            "creativecommons".to_string(),
            "public domain".to_string(),
            "cc-by".to_string(),
            "mit license".to_string(),
        ],
        allow_unknown_license: true,
    };
    let crawl_summary = crawl_image_dataset(&crawl_request)
        .await
        .map_err(|err| ServiceError::Internal(format!("image crawl failed: {err}")))?;

    executions.push(AgentToolExecution {
        name: "crawl_images".to_string(),
        arguments_json: serialize_json(&crawl_request)?,
        output_summary: format!(
            "downloaded_images={}, deduped_images={}, failed_images={}, skipped_by_domain={}, skipped_by_license={}, manifest={}",
            crawl_summary.downloaded_images,
            crawl_summary.deduped_images,
            crawl_summary.failed_images,
            crawl_summary.skipped_by_domain,
            crawl_summary.skipped_by_license,
            crawl_summary.dataset_manifest
        ),
    });

    if let Some(task_id) = task_id {
        set_task_stage(task_id, AgentTaskStage::Training).await;
    }

    let tune_result = run_fire_vision_autotune(&dataset_dir, &training_dir, 42)
        .map_err(ServiceError::Internal)?;

    executions.push(AgentToolExecution {
        name: "train_and_tune".to_string(),
        arguments_json: serde_json::json!({
            "dataset_dir": dataset_dir,
            "artifact_dir": training_dir,
            "seed": 42
        })
        .to_string(),
        output_summary: format!(
            "completed {} trials, best score {:.4}, valid_loss {:.4}",
            tune_result.trial_count,
            tune_result.best_trial.score,
            tune_result.best_trial.valid_loss
        ),
    });

    executions.push(AgentToolExecution {
        name: "select_best_model".to_string(),
        arguments_json: serde_json::json!({
            "leaderboard_path": tune_result.leaderboard_path,
            "best_model_path": tune_result.best_model_path
        })
        .to_string(),
        output_summary: format!(
            "best trial=#{}, hidden_layers={}, hidden_width={}, technique={}",
            tune_result.best_trial.trial_id,
            tune_result.best_trial.hyper_params.hidden_layers,
            tune_result.best_trial.hyper_params.hidden_width,
            tune_result.best_trial.hyper_params.technique
        ),
    });

    if let Some(task_id) = task_id {
        set_task_stage(task_id, AgentTaskStage::Selecting).await;
    }

    Ok(AgentChatResponse {
        inferred_goal: inferred_goal.clone(),
        agent_reply: format!(
            "Pipeline complete: images crawled, hyperparameters tuned, and best model saved at {}.",
            tune_result.best_model_path
        ),
        planned_tools,
        tool_executions: executions,
        run_artifact_dir: Some(run_artifact_dir),
        best_model_path: Some(tune_result.best_model_path),
        best_hyper_params: Some(tune_result.best_trial.hyper_params),
        leaderboard_path: Some(tune_result.leaderboard_path),
        task_id: task_id.map(ToString::to_string),
        task_status: task_id.map(|_| AgentTaskStatus::Running),
        task_stage: task_id.map(|_| AgentTaskStage::Selecting),
    })
}

fn validate_payload(payload: &AgentChatRequest) -> Result<(), ServiceError> {
    if payload.messages.is_empty() {
        return Err(ServiceError::Validation(
            "messages must not be empty".to_string(),
        ));
    }
    Ok(())
}

fn infer_goal(payload: &AgentChatRequest) -> String {
    let fallback = "fire detection dataset".to_string();
    payload
        .messages
        .iter()
        .rev()
        .find(|m| matches!(m.role, AgentRole::User))
        .map(|m| m.content.trim().to_string())
        .filter(|text| !text.is_empty())
        .unwrap_or(fallback)
}

fn default_run_id() -> String {
    let now = SystemTime::now()
        .duration_since(UNIX_EPOCH)
        .map(|d| d.as_secs())
        .unwrap_or(0);
    format!("run-{now}")
}

fn serialize_json<T: serde::Serialize>(value: &T) -> Result<String, ServiceError> {
    serde_json::to_string(value)
        .map_err(|err| ServiceError::Internal(format!("failed to encode tool arguments: {err}")))
}

fn summarize_urls(hits: &[lersosa_crawler::DatasetSearchHit]) -> String {
    if hits.is_empty() {
        return String::new();
    }
    let preview = hits
        .iter()
        .take(3)
        .map(|hit| hit.url.clone())
        .collect::<Vec<_>>()
        .join(", ");
    format!(", top results: {preview}")
}

fn queued_response(snapshot: AgentTaskSnapshot) -> AgentChatResponse {
    AgentChatResponse {
        inferred_goal: "queued".to_string(),
        agent_reply: format!("Task '{}' queued. Poll /v1/agent/jobs/{{task_id}} for status.", snapshot.task_id),
        planned_tools: vec!["search_dataset".to_string(), "crawl_images".to_string(), "train_and_tune".to_string(), "select_best_model".to_string()],
        tool_executions: Vec::new(),
        run_artifact_dir: None,
        best_model_path: None,
        best_hyper_params: None,
        leaderboard_path: None,
        task_id: Some(snapshot.task_id),
        task_status: Some(snapshot.status),
        task_stage: snapshot.stage,
    }
}

fn offline_dataset_sources(goal: &str) -> Vec<String> {
    let mut sources = vec![
        "https://commons.wikimedia.org/wiki/Category:Fires".to_string(),
        "https://commons.wikimedia.org/wiki/Category:Firefighting".to_string(),
        "https://openverse.org/".to_string(),
        "https://www.kaggle.com/datasets".to_string(),
    ];

    let lower = goal.to_ascii_lowercase();
    if lower.contains("smoke") {
        sources.insert(
            0,
            "https://commons.wikimedia.org/wiki/Category:Smoke".to_string(),
        );
    }
    sources
}

fn task_store() -> Arc<RwLock<HashMap<String, AgentTaskSnapshot>>> {
    TASKS
        .get_or_init(|| Arc::new(RwLock::new(HashMap::new())))
        .clone()
}

async fn set_task_status(
    task_id: &str,
    status: AgentTaskStatus,
    result: Option<AgentChatResponse>,
    error: Option<String>,
) {
    let tasks = task_store();
    let mut guard = tasks.write().await;
    let stage = guard.get(task_id).and_then(|snapshot| snapshot.stage.clone());
    guard.insert(
        task_id.to_string(),
        AgentTaskSnapshot {
            task_id: task_id.to_string(),
            status,
            stage,
            result,
            error,
        },
    );
}

async fn set_task_stage(task_id: &str, stage: AgentTaskStage) {
    let tasks = task_store();
    let mut guard = tasks.write().await;

    if let Some(snapshot) = guard.get_mut(task_id) {
        snapshot.stage = Some(stage);
        return;
    }

    guard.insert(
        task_id.to_string(),
        AgentTaskSnapshot {
            task_id: task_id.to_string(),
            status: AgentTaskStatus::Running,
            stage: Some(stage),
            result: None,
            error: None,
        },
    );
}

