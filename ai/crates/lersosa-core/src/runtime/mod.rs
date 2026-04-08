pub mod backend;

pub use backend::BackendKind;

pub fn device(backend: BackendKind) -> burn::DispatchDevice {
    backend::select_device(backend).unwrap_or_else(|err| panic!("{err}"))
}
