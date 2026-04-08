use std::sync::{Arc, RwLock};

use lersosa_core::runtime::BackendKind;

use crate::{
    error::ServiceError,
    model_manager::ModelManager,
    service_config::{InferenceLimits, ServiceRuntimeConfig},
};

#[derive(Clone)]
pub struct AppState {
    pub manager: Arc<RwLock<ModelManager>>,
    pub config: Arc<RwLock<ServiceRuntimeConfig>>,
}

impl AppState {
    pub fn new(config: ServiceRuntimeConfig) -> Self {
        let backend = config.backend;
        let artifact_dir = config.model.artifact_dir.clone();
        Self {
            manager: Arc::new(RwLock::new(ModelManager::new(backend, artifact_dir))),
            config: Arc::new(RwLock::new(config)),
        }
    }

    pub fn backend(&self) -> BackendKind {
        self.manager
            .read()
            .unwrap_or_else(|err| panic!("Model manager poisoned: {err}"))
            .backend()
    }

    pub fn limits(&self) -> InferenceLimits {
        self.config
            .read()
            .unwrap_or_else(|err| panic!("Config lock poisoned: {err}"))
            .limits
            .clone()
    }

    pub fn reload(&self, next: ServiceRuntimeConfig) -> Result<(), ServiceError> {
        next
            .validate()
            .map_err(|err| ServiceError::Validation(format!("invalid runtime config: {err}")))?;

        {
            let mut manager = self
                .manager
                .write()
                .map_err(|err| ServiceError::Internal(format!("Model manager lock poisoned: {err}")))?;
            manager.reload(next.backend, next.model.artifact_dir.clone());
        }

        {
            let mut config = self
                .config
                .write()
                .map_err(|err| ServiceError::Internal(format!("Config lock poisoned: {err}")))?;
            *config = next;
        }

        Ok(())
    }

    pub fn config_snapshot(&self) -> Result<ServiceRuntimeConfig, ServiceError> {
        self.config
            .read()
            .map(|cfg| cfg.clone())
            .map_err(|err| ServiceError::Internal(format!("Config lock poisoned: {err}")) )
    }

    pub fn infer(&self, tokens: Vec<i64>) -> Result<lersosa_core::inference::transformer_lm::TransformerLmInferenceOutput, ServiceError> {
        self.validate_tokens(&tokens)?;
        let manager = self
            .manager
            .read()
            .map_err(|err| ServiceError::Internal(format!("Model manager lock poisoned: {err}")))?;
        manager.infer(tokens)
    }

    pub fn generate(
        &self,
        prompt_tokens: Vec<i64>,
        max_new_tokens: usize,
        stop_token_id: Option<i64>,
    ) -> Result<lersosa_core::inference::transformer_lm::TransformerLmGenerationOutput, ServiceError> {
        self.validate_prompt(&prompt_tokens, max_new_tokens)?;
        let manager = self
            .manager
            .read()
            .map_err(|err| ServiceError::Internal(format!("Model manager lock poisoned: {err}")))?;
        manager.generate(prompt_tokens, max_new_tokens, stop_token_id)
    }

    pub fn infer_batch(
        &self,
        items: Vec<Vec<i64>>,
    ) -> Result<Vec<lersosa_core::inference::transformer_lm::TransformerLmInferenceOutput>, ServiceError> {
        self.validate_batch(&items)?;
        let manager = self
            .manager
            .read()
            .map_err(|err| ServiceError::Internal(format!("Model manager lock poisoned: {err}")))?;

        let mut results = Vec::with_capacity(items.len());
        for tokens in items {
            results.push(manager.infer(tokens)?);
        }
        Ok(results)
    }

    fn validate_tokens(&self, tokens: &[i64]) -> Result<(), ServiceError> {
        let limits = self.limits();

        if tokens.is_empty() {
            return Err(ServiceError::Validation("tokens must not be empty".to_string()));
        }
        if tokens.len() > limits.max_tokens_per_request {
            return Err(ServiceError::Validation(format!(
                "tokens length {} exceeds max_tokens_per_request {}",
                tokens.len(), limits.max_tokens_per_request
            )));
        }

        Ok(())
    }

    fn validate_prompt(&self, prompt_tokens: &[i64], max_new_tokens: usize) -> Result<(), ServiceError> {
        let limits = self.limits();

        if prompt_tokens.is_empty() {
            return Err(ServiceError::Validation("prompt_tokens must not be empty".to_string()));
        }
        if prompt_tokens.len() > limits.max_tokens_per_request {
            return Err(ServiceError::Validation(format!(
                "prompt length {} exceeds max_tokens_per_request {}",
                prompt_tokens.len(), limits.max_tokens_per_request
            )));
        }
        if max_new_tokens == 0 {
            return Err(ServiceError::Validation("max_new_tokens must be greater than 0".to_string()));
        }
        if max_new_tokens > limits.max_tokens_per_request {
            return Err(ServiceError::Validation(format!(
                "max_new_tokens {} exceeds max_tokens_per_request {}",
                max_new_tokens, limits.max_tokens_per_request
            )));
        }

        Ok(())
    }

    fn validate_batch(&self, items: &[Vec<i64>]) -> Result<(), ServiceError> {
        let limits = self.limits();

        if items.is_empty() {
            return Err(ServiceError::Validation("batch items must not be empty".to_string()));
        }
        if items.len() > limits.max_batch_size {
            return Err(ServiceError::Validation(format!(
                "batch size {} exceeds max_batch_size {}",
                items.len(), limits.max_batch_size
            )));
        }

        for tokens in items {
            self.validate_tokens(tokens)?;
        }

        Ok(())
    }
}
