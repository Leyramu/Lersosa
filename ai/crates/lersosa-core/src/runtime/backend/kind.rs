#[derive(Debug, Clone, Copy, PartialEq, Eq)]
pub enum BackendKind {
    Auto,
    Ndarray,
    Tch,
    Wgpu,
    Vulkan,
    Cuda,
}

impl BackendKind {
    pub fn parse(value: &str) -> Option<Self> {
        match value.trim().to_ascii_lowercase().as_str() {
            "auto" | "" => Some(Self::Auto),
            "ndarray" => Some(Self::Ndarray),
            "tch" => Some(Self::Tch),
            "wgpu" => Some(Self::Wgpu),
            "vulkan" => Some(Self::Vulkan),
            "cuda" => Some(Self::Cuda),
            _ => None,
        }
    }
}

#[derive(Debug, Clone, Copy)]
pub struct RuntimeConfig {
    pub backend: BackendKind,
}

impl RuntimeConfig {
    pub fn from_env() -> Result<Self, String> {
        let backend = std::env::var("LERSOSA_BACKEND").unwrap_or_else(|_| "auto".to_string());
        let backend = BackendKind::parse(&backend).ok_or_else(|| {
            format!(
                "Unsupported LERSOSA_BACKEND='{backend}'. Use one of: auto, ndarray, tch, wgpu, vulkan, cuda"
            )
        })?;

        Ok(Self { backend })
    }
}

