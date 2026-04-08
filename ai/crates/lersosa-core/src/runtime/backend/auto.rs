use burn::DispatchDevice;

use super::{cuda, ndarray, tch, vulkan, wgpu};

pub fn select() -> Result<DispatchDevice, String> {
    if let Ok(device) = ndarray::select() {
        return Ok(device);
    }
    if let Ok(device) = tch::dispatch_device() {
        return Ok(device);
    }
    if let Ok(device) = wgpu::select() {
        return Ok(device);
    }
    if let Ok(device) = vulkan::select() {
        return Ok(device);
    }
    if let Ok(device) = cuda::select() {
        return Ok(device);
    }

    Err("No backend is available. Enable one of: ndarray, tch, wgpu, vulkan, cuda".to_string())
}

