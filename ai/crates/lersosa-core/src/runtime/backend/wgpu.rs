use burn::DispatchDevice;

pub fn select() -> Result<DispatchDevice, String> {
    #[cfg(feature = "wgpu")]
    {
        use burn::backend::wgpu::WgpuDevice;
        return Ok(WgpuDevice::default().into());
    }

    #[cfg(not(feature = "wgpu"))]
    {
        Err("Backend 'wgpu' is not enabled in Cargo features".to_string())
    }
}

