use burn::DispatchDevice;

pub fn select() -> Result<DispatchDevice, String> {
    #[cfg(feature = "vulkan")]
    {
        use burn::backend::wgpu::WgpuDevice;
        return Ok(WgpuDevice::default().into());
    }

    #[cfg(not(feature = "vulkan"))]
    {
        Err("Backend 'vulkan' is not enabled in Cargo features".to_string())
    }
}

