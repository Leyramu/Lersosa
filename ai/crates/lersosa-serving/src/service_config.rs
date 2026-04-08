use lersosa_core::runtime::BackendKind;
use serde::{Deserialize, Serialize};
use std::net::SocketAddr;

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct InferenceLimits {
    pub max_batch_size: usize,
    pub max_tokens_per_request: usize,
    pub max_payload_bytes: usize,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ServiceNetworkConfig {
    pub http_addr: String,
    pub grpc_addr: String,
}

impl Default for ServiceNetworkConfig {
    fn default() -> Self {
        Self {
            http_addr: "127.0.0.1:8080".to_string(),
            grpc_addr: "127.0.0.1:50051".to_string(),
        }
    }
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ModelRuntimeConfig {
    pub artifact_dir: String,
}

impl Default for ModelRuntimeConfig {
    fn default() -> Self {
        Self {
            artifact_dir: "artifacts/transformer".to_string(),
        }
    }
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ServiceLoggingConfig {
    pub filter: String,
}

impl Default for ServiceLoggingConfig {
    fn default() -> Self {
        Self {
            filter: "inference_server=info,lersosa_serving=info".to_string(),
        }
    }
}

impl Default for InferenceLimits {
    fn default() -> Self {
        Self {
            max_batch_size: 8,
            max_tokens_per_request: 16,
            max_payload_bytes: 64 * 1024,
        }
    }
}

#[derive(Debug, Clone)]
pub struct ServiceRuntimeConfig {
    pub backend: BackendKind,
    pub network: ServiceNetworkConfig,
    pub model: ModelRuntimeConfig,
    pub limits: InferenceLimits,
    pub logging: ServiceLoggingConfig,
}

impl Default for ServiceRuntimeConfig {
    fn default() -> Self {
        Self {
            backend: BackendKind::Ndarray,
            network: ServiceNetworkConfig::default(),
            model: ModelRuntimeConfig::default(),
            limits: InferenceLimits::default(),
            logging: ServiceLoggingConfig::default(),
        }
    }
}

#[derive(Debug, Deserialize)]
struct PartialServiceRuntimeConfig {
    backend: Option<String>,
    network: Option<ServiceNetworkConfig>,
    model: Option<ModelRuntimeConfig>,
    limits: Option<InferenceLimits>,
    logging: Option<ServiceLoggingConfig>,
}

impl ServiceRuntimeConfig {
    pub fn from_file(path: &str) -> Result<Self, String> {
        let content = std::fs::read_to_string(path)
            .map_err(|err| format!("Failed to read config file '{path}': {err}"))?;
        let partial: PartialServiceRuntimeConfig =
            toml::from_str(&content).map_err(|err| format!("Invalid TOML in '{path}': {err}"))?;

        let mut config = Self::default();
        if let Some(backend) = partial.backend {
            config.backend = BackendKind::parse(&backend).ok_or_else(|| {
                format!(
                    "Invalid backend '{backend}' in config. Use: auto, ndarray, tch, wgpu, vulkan, cuda"
                )
            })?;
        }
        if let Some(network) = partial.network {
            config.network = network;
        }
        if let Some(model) = partial.model {
            config.model = model;
        }
        if let Some(limits) = partial.limits {
            config.limits = limits;
        }
        if let Some(logging) = partial.logging {
            config.logging = logging;
        }

        config.validate()?;

        Ok(config)
    }

    pub fn validate(&self) -> Result<(), String> {
        if self.limits.max_batch_size == 0 {
            return Err("max_batch_size must be greater than 0".to_string());
        }
        if self.limits.max_tokens_per_request == 0 {
            return Err("max_tokens_per_request must be greater than 0".to_string());
        }
        if self.limits.max_payload_bytes == 0 {
            return Err("max_payload_bytes must be greater than 0".to_string());
        }
        if self.model.artifact_dir.trim().is_empty() {
            return Err("model.artifact_dir must not be empty".to_string());
        }
        let _ = self.http_addr()?;
        let _ = self.grpc_addr()?;

        Ok(())
    }

    pub fn http_addr(&self) -> Result<SocketAddr, String> {
        self.network
            .http_addr
            .parse::<SocketAddr>()
            .map_err(|err| format!("Invalid HTTP server address '{}': {err}", self.network.http_addr))
    }

    pub fn grpc_addr(&self) -> Result<SocketAddr, String> {
        self.network
            .grpc_addr
            .parse::<SocketAddr>()
            .map_err(|err| format!("Invalid gRPC server address '{}': {err}", self.network.grpc_addr))
    }
}
