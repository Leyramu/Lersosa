use burn::{
    prelude::*,
    record::{CompactRecorder, Recorder},
    tensor::TensorData,
};
use burn::Dispatch;

use crate::model::transformer::TransformerConfig;
use crate::runtime::BackendKind;
use crate::training::transformer_lm::TransformerLmTrainingConfig;

const BATCH_SIZE: usize = 2;
const SEQ_LENGTH: usize = 8;

#[derive(Debug, Clone)]
pub struct TransformerLmInferenceOutput {
    pub logits_shape: [usize; 3],
    pub next_token_id: i64,
}

#[derive(Debug, Clone)]
pub enum GenerationStopReason {
    MaxNewTokens,
    StopToken(i64),
    ContextWindowFull,
}

impl core::fmt::Display for GenerationStopReason {
    fn fmt(&self, f: &mut core::fmt::Formatter<'_>) -> core::fmt::Result {
        match self {
            Self::MaxNewTokens => write!(f, "max_new_tokens"),
            Self::StopToken(token_id) => write!(f, "stop_token({token_id})"),
            Self::ContextWindowFull => write!(f, "context_window_full"),
        }
    }
}

#[derive(Debug, Clone)]
pub struct TransformerLmGenerationOutput {
    pub prompt_tokens: Vec<i64>,
    pub generated_tokens: Vec<i64>,
    pub logits_shape: [usize; 3],
    pub next_token_id: i64,
    pub stop_reason: GenerationStopReason,
}

pub fn infer<B: Backend>(device: B::Device) {
    let tokens: Vec<i64> = (0..BATCH_SIZE * SEQ_LENGTH)
        .map(|index| (index % 5_000) as i64)
        .collect();

    let output = infer_tokens::<B>(device, tokens);

    println!("Backend type: {}", core::any::type_name::<B>());
    println!("Output logits shape: {:?}", output.logits_shape);
    println!("Predicted next token id: {}", output.next_token_id);
}

fn next_token_for_tokens<B: Backend>(
    model: &crate::model::transformer::TransformerModel<B>,
    device: &B::Device,
    tokens: &[i64],
) -> i64 {
    let seq_length = tokens.len();
    let token_ids = Tensor::<B, 2, Int>::from_data(
        TensorData::new(tokens.to_vec(), [1, seq_length]),
        device,
    );

    let logits = model.forward(token_ids);
    let [batch_size, _, vocab_size] = logits.dims();
    let last_step_logits = logits
        .slice([0..batch_size, (seq_length - 1)..seq_length, 0..vocab_size])
        .reshape([vocab_size]);
    last_step_logits.argmax(0).into_scalar().elem::<i64>()
}

pub fn infer_tokens<B: Backend>(device: B::Device, tokens: Vec<i64>) -> TransformerLmInferenceOutput {
    assert!(!tokens.is_empty(), "tokens must not be empty");

    let config = TransformerConfig::new(5_000, 16, 64, 256, 4, 2);
    assert!(
        tokens.len() <= config.max_seq_length,
        "tokens length ({}) exceeds max_seq_length ({})",
        tokens.len(),
        config.max_seq_length
    );

    let model = config.init::<B>(&device);
    let seq_length = tokens.len();
    let logits_shape: [usize; 3] = [1, seq_length, config.vocab_size];
    let next_token_id = next_token_for_tokens(&model, &device, tokens.as_slice());

    TransformerLmInferenceOutput {
        logits_shape,
        next_token_id,
    }
}

pub fn generate_tokens<B: Backend>(
    device: B::Device,
    prompt_tokens: Vec<i64>,
    max_new_tokens: usize,
    stop_token_id: Option<i64>,
) -> Result<TransformerLmGenerationOutput, String> {
    if prompt_tokens.is_empty() {
        return Err("prompt_tokens must not be empty".to_string());
    }

    let config = TransformerConfig::new(5_000, 16, 64, 256, 4, 2);
    if prompt_tokens.len() > config.max_seq_length {
        return Err(format!(
            "prompt_tokens length ({}) exceeds max_seq_length ({})",
            prompt_tokens.len(),
            config.max_seq_length
        ));
    }

    let model = config.init::<B>(&device);
    let mut context_tokens = prompt_tokens.clone();
    let mut generated_tokens = Vec::with_capacity(max_new_tokens);
    let mut logits_shape: [usize; 3] = [1, context_tokens.len(), config.vocab_size];
    let mut next_token_id = -1;
    let mut stop_reason = GenerationStopReason::MaxNewTokens;

    for _ in 0..max_new_tokens {
        logits_shape = [1, context_tokens.len(), config.vocab_size];
        let token_id = next_token_for_tokens(&model, &device, context_tokens.as_slice());
        next_token_id = token_id;
        context_tokens.push(token_id);
        generated_tokens.push(token_id);

        if stop_token_id == Some(token_id) {
            stop_reason = GenerationStopReason::StopToken(token_id);
            break;
        }

        if context_tokens.len() >= config.max_seq_length {
            stop_reason = GenerationStopReason::ContextWindowFull;
            break;
        }
    }

    Ok(TransformerLmGenerationOutput {
        prompt_tokens,
        generated_tokens,
        logits_shape,
        next_token_id,
        stop_reason,
    })
}

pub fn infer_tokens_from_artifact<B: Backend>(
    device: B::Device,
    artifact_dir: &str,
    tokens: Vec<i64>,
) -> Result<TransformerLmInferenceOutput, String> {
    if tokens.is_empty() {
        return Err("tokens must not be empty".to_string());
    }

    let config_path = format!("{artifact_dir}/config.json");
    let model_path = format!("{artifact_dir}/model");
    let config = TransformerLmTrainingConfig::load(config_path.as_str())
        .map_err(|err| format!("failed to load training config from '{config_path}': {err}"))?;

    if tokens.len() > config.model.max_seq_length {
        return Err(format!(
            "tokens length ({}) exceeds max_seq_length ({})",
            tokens.len(),
            config.model.max_seq_length
        ));
    }

    let record = CompactRecorder::new()
        .load(model_path.clone().into(), &device)
        .map_err(|err| format!("failed to load model weights from '{model_path}': {err}"))?;

    let model = config.model.init::<B>(&device).load_record(record);
    let seq_length = tokens.len();
    let logits_shape: [usize; 3] = [1, seq_length, config.model.vocab_size];
    let next_token_id = next_token_for_tokens(&model, &device, tokens.as_slice());

    Ok(TransformerLmInferenceOutput {
        logits_shape,
        next_token_id,
    })
}

pub fn generate_tokens_from_artifact<B: Backend>(
    device: B::Device,
    artifact_dir: &str,
    prompt_tokens: Vec<i64>,
    max_new_tokens: usize,
    stop_token_id: Option<i64>,
) -> Result<TransformerLmGenerationOutput, String> {
    if prompt_tokens.is_empty() {
        return Err("prompt_tokens must not be empty".to_string());
    }

    let config_path = format!("{artifact_dir}/config.json");
    let model_path = format!("{artifact_dir}/model");
    let config = TransformerLmTrainingConfig::load(config_path.as_str())
        .map_err(|err| format!("failed to load training config from '{config_path}': {err}"))?;

    if prompt_tokens.len() > config.model.max_seq_length {
        return Err(format!(
            "prompt_tokens length ({}) exceeds max_seq_length ({})",
            prompt_tokens.len(),
            config.model.max_seq_length
        ));
    }

    let record = CompactRecorder::new()
        .load(model_path.clone().into(), &device)
        .map_err(|err| format!("failed to load model weights from '{model_path}': {err}"))?;

    let model = config.model.init::<B>(&device).load_record(record);
    let mut context_tokens = prompt_tokens.clone();
    let mut generated_tokens = Vec::with_capacity(max_new_tokens);
    let mut logits_shape: [usize; 3] = [1, context_tokens.len(), config.model.vocab_size];
    let mut next_token_id = -1;
    let mut stop_reason = GenerationStopReason::MaxNewTokens;

    for _ in 0..max_new_tokens {
        logits_shape = [1, context_tokens.len(), config.model.vocab_size];
        let token_id = next_token_for_tokens(&model, &device, context_tokens.as_slice());
        next_token_id = token_id;
        context_tokens.push(token_id);
        generated_tokens.push(token_id);

        if stop_token_id == Some(token_id) {
            stop_reason = GenerationStopReason::StopToken(token_id);
            break;
        }

        if context_tokens.len() >= config.model.max_seq_length {
            stop_reason = GenerationStopReason::ContextWindowFull;
            break;
        }
    }

    Ok(TransformerLmGenerationOutput {
        prompt_tokens,
        generated_tokens,
        logits_shape,
        next_token_id,
        stop_reason,
    })
}

pub struct TransformerLmInferenceRunOptions {
    pub backend: BackendKind,
}

impl Default for TransformerLmInferenceRunOptions {
    fn default() -> Self {
        Self {
            backend: BackendKind::Auto,
        }
    }
}

pub fn entry(options: TransformerLmInferenceRunOptions) {
    match options.backend {
        BackendKind::Tch => {
            #[cfg(feature = "tch")]
            {
                use burn::backend::libtorch::LibTorch;

                let device = crate::runtime::backend::tch_device()
                    .unwrap_or_else(|err| panic!("{err}"));
                infer::<LibTorch>(device);
                return;
            }

            #[cfg(not(feature = "tch"))]
            {
                panic!("Backend 'tch' is not enabled in Cargo features");
            }
        }
        _ => {
            let device = crate::runtime::device(options.backend);
            infer::<Dispatch>(device);
        }
    }
}
