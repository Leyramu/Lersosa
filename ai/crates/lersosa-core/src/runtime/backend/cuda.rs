use burn::DispatchDevice;

pub fn select() -> Result<DispatchDevice, String> {
    #[cfg(feature = "cuda")]
    {
        use burn::backend::cuda::CudaDevice;
        return Ok(CudaDevice::default().into());
    }

    #[cfg(not(feature = "cuda"))]
    {
        Err("Backend 'cuda' is not enabled in Cargo features".to_string())
    }
}

