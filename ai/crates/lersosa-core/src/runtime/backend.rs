use burn::DispatchDevice;

#[path = "backend/auto.rs"]
mod auto;
#[path = "backend/cuda.rs"]
mod cuda;
#[path = "backend/kind.rs"]
mod kind;
#[path = "backend/ndarray.rs"]
mod ndarray;
#[path = "backend/tch.rs"]
mod tch;
#[path = "backend/vulkan.rs"]
mod vulkan;
#[path = "backend/wgpu.rs"]
mod wgpu;

pub use kind::{BackendKind, RuntimeConfig};

#[allow(unreachable_code)]
pub fn select_device(kind: BackendKind) -> Result<DispatchDevice, String> {
    match kind {
        BackendKind::Auto => auto::select(),
        BackendKind::Ndarray => ndarray::select(),
        BackendKind::Tch => tch::dispatch_device(),
        BackendKind::Wgpu => wgpu::select(),
        BackendKind::Vulkan => vulkan::select(),
        BackendKind::Cuda => cuda::select(),
    }
}

#[cfg(feature = "tch")]
pub use tch::tch_device;

#[cfg(test)]
mod tests {
    use super::BackendKind;

    #[test]
    fn parse_backend_kind_from_string() {
        assert_eq!(BackendKind::parse("auto"), Some(BackendKind::Auto));
        assert_eq!(BackendKind::parse("ndarray"), Some(BackendKind::Ndarray));
        assert_eq!(BackendKind::parse("bad"), None);
    }
}
