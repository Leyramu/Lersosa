use burn::Dispatch;
use lersosa_core::{
    inference::transformer_lm::{
        TransformerLmGenerationOutput, TransformerLmInferenceOutput,
        generate_tokens_from_artifact, infer_tokens_from_artifact,
    },
    runtime::BackendKind,
};

use crate::error::ServiceError;

pub struct ModelManager {
    backend: BackendKind,
    artifact_dir: String,
}

impl ModelManager {
    pub fn new(backend: BackendKind, artifact_dir: String) -> Self {
        Self {
            backend,
            artifact_dir,
        }
    }

    pub fn backend(&self) -> BackendKind {
        self.backend
    }

    pub fn artifact_dir(&self) -> &str {
        &self.artifact_dir
    }

    pub fn reload(&mut self, backend: BackendKind, artifact_dir: String) {
        self.backend = backend;
        self.artifact_dir = artifact_dir;
    }

    pub fn infer(&self, tokens: Vec<i64>) -> Result<TransformerLmInferenceOutput, ServiceError> {
        if tokens.is_empty() {
            return Err(ServiceError::Validation("tokens must not be empty".to_string()));
        }

        match self.backend {
            BackendKind::Tch => {
                #[cfg(feature = "tch")]
                {
                    use burn::backend::libtorch::LibTorch;

                    let device = lersosa_core::runtime::backend::tch_device()
                        .map_err(ServiceError::Backend)?;
                    infer_tokens_from_artifact::<LibTorch>(
                        device,
                        self.artifact_dir.as_str(),
                        tokens,
                    )
                    .map_err(ServiceError::Backend)
                }

                #[cfg(not(feature = "tch"))]
                {
                    Err(ServiceError::Backend(
                        "backend tch is not enabled in Cargo features".to_string(),
                    ))
                }
            }
            backend => {
                let device = lersosa_core::runtime::backend::select_device(backend)
                    .map_err(ServiceError::Backend)?;
                infer_tokens_from_artifact::<Dispatch>(device, self.artifact_dir.as_str(), tokens)
                    .map_err(ServiceError::Backend)
            }
        }
    }

    pub fn generate(
        &self,
        prompt_tokens: Vec<i64>,
        max_new_tokens: usize,
        stop_token_id: Option<i64>,
    ) -> Result<TransformerLmGenerationOutput, ServiceError> {
        if prompt_tokens.is_empty() {
            return Err(ServiceError::Validation(
                "prompt_tokens must not be empty".to_string(),
            ));
        }
        if max_new_tokens == 0 {
            return Err(ServiceError::Validation(
                "max_new_tokens must be greater than 0".to_string(),
            ));
        }

        match self.backend {
            BackendKind::Tch => {
                #[cfg(feature = "tch")]
                {
                    use burn::backend::libtorch::LibTorch;

                    let device = lersosa_core::runtime::backend::tch_device()
                        .map_err(ServiceError::Backend)?;
                    generate_tokens_from_artifact::<LibTorch>(
                        device,
                        self.artifact_dir.as_str(),
                        prompt_tokens,
                        max_new_tokens,
                        stop_token_id,
                    )
                    .map_err(ServiceError::Backend)
                }

                #[cfg(not(feature = "tch"))]
                {
                    Err(ServiceError::Backend(
                        "backend tch is not enabled in Cargo features".to_string(),
                    ))
                }
            }
            backend => {
                let device = lersosa_core::runtime::backend::select_device(backend)
                    .map_err(ServiceError::Backend)?;
                generate_tokens_from_artifact::<Dispatch>(
                    device,
                    self.artifact_dir.as_str(),
                    prompt_tokens,
                    max_new_tokens,
                    stop_token_id,
                )
                .map_err(ServiceError::Backend)
            }
        }
    }
}
