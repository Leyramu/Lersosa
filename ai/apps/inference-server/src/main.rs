use lersosa_serving::{
    app_state::AppState,
    build_router,
    grpc,
    service_config::ServiceRuntimeConfig,
};

const DEFAULT_CONFIG_PATH: &str = "apps/inference-server/config/inference.toml";

fn read_config_path() -> String {
    let mut args = std::env::args().skip(1);
    while let Some(arg) = args.next() {
        if arg == "--config" {
            if let Some(path) = args.next() {
                return path;
            }
            panic!("--config requires a file path value");
        }
    }

    DEFAULT_CONFIG_PATH.to_string()
}

fn load_runtime_config() -> Result<ServiceRuntimeConfig, String> {
    let config_path = read_config_path();

    if std::path::Path::new(&config_path).exists() {
        return ServiceRuntimeConfig::from_file(&config_path);
    }

    Err(format!(
        "Config file not found at '{config_path}'. Pass one with --config <path>."
    ))
}

#[tokio::main]
async fn main() {
    let config = load_runtime_config().unwrap_or_else(|err| panic!("{err}"));

    tracing_subscriber::fmt()
        .with_env_filter(config.logging.filter.clone())
        .init();

    let http_addr = config.http_addr().unwrap_or_else(|err| panic!("{err}"));
    let grpc_addr = config.grpc_addr().unwrap_or_else(|err| panic!("{err}"));

    tracing::info!(
        "Runtime config: backend={:?}, artifact_dir={}, limits={{max_batch_size: {}, max_tokens_per_request: {}, max_payload_bytes: {}}}",
        config.backend,
        config.model.artifact_dir,
        config.limits.max_batch_size,
        config.limits.max_tokens_per_request,
        config.limits.max_payload_bytes
    );

    let state = AppState::new(config);
    let app = build_router(state.clone());

    let listener = tokio::net::TcpListener::bind(http_addr)
        .await
        .unwrap_or_else(|err| panic!("Failed to bind HTTP server: {err}"));

    tracing::info!("Inference HTTP service listening on http://{}", http_addr);
    tracing::info!("Inference gRPC service listening on {}", grpc_addr);

    let http_task = tokio::spawn(async move {
        axum::serve(listener, app)
            .await
            .map_err(|err| format!("HTTP server error: {err}"))
    });

    let grpc_task = tokio::spawn(async move {
        grpc::serve(state, grpc_addr)
            .await
            .map_err(|err| format!("gRPC server error: {err}"))
    });

    tokio::select! {
        result = http_task => {
            match result {
                Ok(Ok(())) => {}
                Ok(Err(err)) => panic!("{err}"),
                Err(err) => panic!("HTTP task join error: {err}"),
            }
        }
        result = grpc_task => {
            match result {
                Ok(Ok(())) => {}
                Ok(Err(err)) => panic!("{err}"),
                Err(err) => panic!("gRPC task join error: {err}"),
            }
        }
    }
}
