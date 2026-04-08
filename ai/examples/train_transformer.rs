use serde::Deserialize;

const DEFAULT_CONFIG_PATH: &str = "examples/config/train_transformer.toml";

#[derive(Debug, Deserialize)]
struct TrainEntryConfig {
    backend: String,
    artifact_dir: String,
    training_config_path: String,
}

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

fn load_entry_config(path: &str) -> TrainEntryConfig {
    let content = std::fs::read_to_string(path)
        .unwrap_or_else(|err| panic!("Failed to read train config '{path}': {err}"));
    toml::from_str::<TrainEntryConfig>(&content)
        .unwrap_or_else(|err| panic!("Invalid train config TOML '{path}': {err}"))
}

fn main() {
    let config_path = read_config_path();
    let entry = load_entry_config(&config_path);

    let backend = lersosa::runtime::BackendKind::parse(&entry.backend)
        .unwrap_or_else(|| panic!("Invalid backend '{}' in '{}'.", entry.backend, config_path));
    let config = lersosa::training::transformer_lm::load_training_config(
        entry.training_config_path.as_str(),
    )
    .unwrap_or_else(|err| panic!("{err}"));

    let options = lersosa::training::transformer_lm::TrainingRunOptions {
        backend,
        artifact_dir: entry.artifact_dir,
        config,
    };

    lersosa::training::transformer_lm::entry(options);
}
