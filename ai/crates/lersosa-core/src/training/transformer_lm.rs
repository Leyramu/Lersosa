use burn::{
    Dispatch,
    backend::Autodiff,
    data::{dataloader::DataLoaderBuilder, dataset::Dataset},
    nn::loss::CrossEntropyLossConfig,
    optim::AdamConfig,
    prelude::*,
    record::CompactRecorder,
    tensor::{TensorData, backend::AutodiffBackend},
    train::{
        ClassificationOutput, InferenceStep, Learner, SupervisedTraining, TrainOutput, TrainStep,
        metric::{AccuracyMetric, LossMetric},
    },
};

use crate::model::transformer::{TransformerConfig, TransformerModel};
use crate::runtime::BackendKind;

#[derive(Clone, Debug)]
pub struct TokenSample {
    pub input: Vec<i64>,
    pub target: Vec<i64>,
    pub response_start: usize,
}

#[derive(Clone, Debug)]
pub struct TokenBatch<B: Backend> {
    pub inputs: Tensor<B, 2, Int>,
    pub targets: Tensor<B, 2, Int>,
    pub pad_token_id: usize,
}

#[derive(Clone)]
pub struct TokenBatcher {
    pad_token_id: usize,
}

impl TokenBatcher {
    pub fn new(pad_token_id: usize) -> Self {
        Self { pad_token_id }
    }
}

impl<B: Backend> burn::data::dataloader::batcher::Batcher<B, TokenSample, TokenBatch<B>> for TokenBatcher {
    fn batch(&self, items: Vec<TokenSample>, device: &B::Device) -> TokenBatch<B> {
        let batch_size = items.len();
        let seq_length = items.first().map_or(1, |sample| sample.input.len());

        let mut inputs = Vec::with_capacity(batch_size * seq_length);
        let mut targets = Vec::with_capacity(batch_size * seq_length);

        for sample in items {
            assert_eq!(sample.input.len(), seq_length, "all samples must share the same input length");
            assert_eq!(sample.target.len(), seq_length, "all samples must share the same target length");
            assert!(sample.response_start <= seq_length, "response_start must be within the sequence length");

            inputs.extend(sample.input.iter().copied());

            let mut masked_target = sample.target;
            for token in masked_target.iter_mut().take(sample.response_start) {
                *token = self.pad_token_id as i64;
            }
            targets.extend(masked_target);
        }

        let inputs = Tensor::<B, 2, Int>::from_data(
            TensorData::new(inputs, [batch_size, seq_length]),
            device,
        );
        let targets = Tensor::<B, 2, Int>::from_data(
            TensorData::new(targets, [batch_size, seq_length]),
            device,
        );

        TokenBatch {
            inputs,
            targets,
            pad_token_id: self.pad_token_id,
        }
    }
}

pub struct ToyDialogueDataset {
    size: usize,
    seq_length: usize,
    vocab_size: usize,
}

impl ToyDialogueDataset {
    pub fn new(size: usize, seq_length: usize, vocab_size: usize) -> Self {
        Self {
            size,
            seq_length,
            vocab_size,
        }
    }
}

impl Dataset<TokenSample> for ToyDialogueDataset {
    fn get(&self, index: usize) -> Option<TokenSample> {
        if index >= self.size {
            return None;
        }

        let seq_length = self.seq_length.max(2);
        let prompt_length = (seq_length / 2).max(1);
        let response_start = prompt_length.saturating_sub(1);
        let prompt_base = index % self.vocab_size;
        let response_base = (index * 17 + 3) % self.vocab_size;

        let mut tokens = Vec::with_capacity(seq_length + 1);
        for position in 0..=seq_length {
            let token = if position < prompt_length {
                ((prompt_base + position) % self.vocab_size) as i64
            } else {
                ((response_base + position - prompt_length) % self.vocab_size) as i64
            };
            tokens.push(token);
        }

        let input = tokens[..seq_length].to_vec();
        let target = tokens[1..].to_vec();

        Some(TokenSample {
            input,
            target,
            response_start,
        })
    }

    fn len(&self) -> usize {
        self.size
    }
}

impl<B: Backend> TransformerModel<B> {
    pub fn forward_classification(&self, batch: TokenBatch<B>) -> ClassificationOutput<B> {
        let TokenBatch {
            inputs,
            targets,
            pad_token_id,
        } = batch;
        let logits = self.forward(inputs);
        let [batch_size, seq_length, vocab_size] = logits.dims();

        let logits_flat = logits.reshape([batch_size * seq_length, vocab_size]);
        let targets_flat = targets.reshape([batch_size * seq_length]);

        let loss = CrossEntropyLossConfig::new()
            .with_pad_tokens(Some(vec![pad_token_id]))
            .init(&logits_flat.device())
            .forward(logits_flat.clone(), targets_flat.clone());

        ClassificationOutput::new(loss, logits_flat, targets_flat)
    }
}

impl<B: AutodiffBackend> TrainStep for TransformerModel<B> {
    type Input = TokenBatch<B>;
    type Output = ClassificationOutput<B>;

    fn step(&self, batch: TokenBatch<B>) -> TrainOutput<ClassificationOutput<B>> {
        let item = self.forward_classification(batch);
        TrainOutput::new(self, item.loss.backward(), item)
    }
}

impl<B: Backend> InferenceStep for TransformerModel<B> {
    type Input = TokenBatch<B>;
    type Output = ClassificationOutput<B>;

    fn step(&self, batch: TokenBatch<B>) -> ClassificationOutput<B> {
        self.forward_classification(batch)
    }
}

#[derive(Config, Debug)]
pub struct TransformerLmTrainingConfig {
    pub model: TransformerConfig,
    pub optimizer: AdamConfig,
    #[config(default = 0)]
    pub pad_token_id: usize,
    #[config(default = 2)]
    pub num_epochs: usize,
    #[config(default = 32)]
    pub batch_size: usize,
    #[config(default = 2)]
    pub num_workers: usize,
    #[config(default = 42)]
    pub seed: u64,
    #[config(default = 1.0e-3)]
    pub learning_rate: f64,
    #[config(default = 1024)]
    pub train_size: usize,
    #[config(default = 256)]
    pub valid_size: usize,
}

impl Default for TransformerLmTrainingConfig {
    fn default() -> Self {
        Self::new(
            TransformerConfig::new(5_000, 16, 64, 256, 4, 2),
            AdamConfig::new(),
        )
    }
}

pub fn load_training_config(path: &str) -> Result<TransformerLmTrainingConfig, String> {
    TransformerLmTrainingConfig::load(path)
        .map_err(|err| format!("Failed to load training config from '{path}': {err}"))
}

fn create_artifact_dir(artifact_dir: &str) {
    std::fs::remove_dir_all(artifact_dir).ok();
    std::fs::create_dir_all(artifact_dir).ok();
}

pub fn train<B: AutodiffBackend>(
    artifact_dir: &str,
    config: TransformerLmTrainingConfig,
    device: B::Device,
) {
    create_artifact_dir(artifact_dir);
    config
        .save(format!("{artifact_dir}/config.json"))
        .expect("Config should be saved successfully");

    B::seed(&device, config.seed);

    let train_dataset = ToyDialogueDataset::new(
        config.train_size,
        config.model.max_seq_length,
        config.model.vocab_size,
    );
    let valid_dataset = ToyDialogueDataset::new(
        config.valid_size,
        config.model.max_seq_length,
        config.model.vocab_size,
    );

    let dataloader_train = DataLoaderBuilder::new(TokenBatcher::new(config.pad_token_id))
        .batch_size(config.batch_size)
        .shuffle(config.seed)
        .num_workers(config.num_workers)
        .build(train_dataset);

    let dataloader_valid = DataLoaderBuilder::new(TokenBatcher::new(config.pad_token_id))
        .batch_size(config.batch_size)
        .num_workers(config.num_workers)
        .build(valid_dataset);

    let training = SupervisedTraining::new(artifact_dir, dataloader_train, dataloader_valid)
        .metrics((AccuracyMetric::new().with_pad_token(config.pad_token_id), LossMetric::new()))
        .with_file_checkpointer(CompactRecorder::new())
        .num_epochs(config.num_epochs)
        .summary();

    let model = config.model.init::<B>(&device);
    let result = training.launch(Learner::new(
        model,
        config.optimizer.init(),
        config.learning_rate,
    ));

    result
        .model
        .save_file(format!("{artifact_dir}/model"), &CompactRecorder::new())
        .expect("Trained model should be saved successfully");
}

pub struct TrainingRunOptions {
    pub backend: BackendKind,
    pub artifact_dir: String,
    pub config: TransformerLmTrainingConfig,
}

impl Default for TrainingRunOptions {
    fn default() -> Self {
        Self {
            backend: BackendKind::Auto,
            artifact_dir: "artifacts/transformer".to_string(),
            config: TransformerLmTrainingConfig::default(),
        }
    }
}

pub fn entry(options: TrainingRunOptions) {
    match options.backend {
        BackendKind::Tch => {
            #[cfg(feature = "tch")]
            {
                use burn::backend::libtorch::LibTorch;

                let device = crate::runtime::backend::tch_device()
                    .unwrap_or_else(|err| panic!("{err}"));
                train::<Autodiff<LibTorch>>(&options.artifact_dir, options.config, device);
                return;
            }

            #[cfg(not(feature = "tch"))]
            {
                panic!("Backend 'tch' is not enabled in Cargo features");
            }
        }
        _ => {
            let device = crate::runtime::device(options.backend);
            train::<Autodiff<Dispatch>>(&options.artifact_dir, options.config, device);
        }
    }
}
