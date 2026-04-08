use burn::DispatchDevice;

pub fn dispatch_device() -> Result<DispatchDevice, String> {
    #[cfg(feature = "tch")]
    {
        return Err(
            "Backend 'tch' uses a dedicated LibTorch path, not DispatchDevice routing".to_string(),
        );
    }

    #[cfg(not(feature = "tch"))]
    {
        Err("Backend 'tch' is not enabled in Cargo features".to_string())
    }
}

#[cfg(feature = "tch")]
pub fn tch_device() -> Result<burn::backend::libtorch::LibTorchDevice, String> {
    Ok(burn::backend::libtorch::LibTorchDevice::Cpu)
}

