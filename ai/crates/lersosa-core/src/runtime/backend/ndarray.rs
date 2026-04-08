use burn::DispatchDevice;

pub fn select() -> Result<DispatchDevice, String> {
    #[cfg(feature = "ndarray")]
    {
        use burn::backend::ndarray::NdArrayDevice;
        return Ok(NdArrayDevice::Cpu.into());
    }

    #[cfg(not(feature = "ndarray"))]
    {
        Err("Backend 'ndarray' is not enabled in Cargo features".to_string())
    }
}

