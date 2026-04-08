pub mod api;
pub mod app_state;
pub mod dto;
pub mod error;
pub mod grpc;
pub mod model_manager;
pub mod service_config;
pub mod tokenization;

use axum::{Router, extract::DefaultBodyLimit};

use app_state::AppState;

pub fn build_router(state: AppState) -> Router {
    let limits = state.limits();
    api::routes::router(state).layer(DefaultBodyLimit::max(limits.max_payload_bytes))
}
