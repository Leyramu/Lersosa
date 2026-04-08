use std::{
    fs,
    hash::{Hash, Hasher},
    path::Path,
};

use image::{GenericImageView, ImageReader};
use serde::{Deserialize, Serialize};

const FEATURE_DIM: usize = 16;

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct VisionHyperParams {
    pub hidden_layers: usize,
    pub hidden_width: usize,
    pub dropout: f32,
    pub learning_rate: f64,
    pub technique: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct VisionTrialResult {
    pub trial_id: usize,
    pub hyper_params: VisionHyperParams,
    pub score: f32,
    pub train_loss: f32,
    pub valid_loss: f32,
    pub valid_accuracy: f32,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct VisionAutoTuneResult {
    pub dataset_dir: String,
    pub artifact_dir: String,
    pub images_found: usize,
    pub best_trial: VisionTrialResult,
    pub trial_count: usize,
    pub leaderboard_path: String,
    pub best_model_path: String,
}

#[derive(Debug, Clone, Deserialize)]
struct CrawledImageRecord {
    source_page: String,
    image_url: String,
    local_path: String,
}

#[derive(Debug, Clone)]
struct LabeledSample {
    features: [f32; FEATURE_DIM],
    label: f32,
}

#[derive(Debug, Clone)]
struct TinyMlp {
    layers: Vec<Layer>,
}

#[derive(Debug, Clone)]
struct Layer {
    in_dim: usize,
    out_dim: usize,
    weight: Vec<f32>,
    bias: Vec<f32>,
}

pub fn run_fire_vision_autotune(
    dataset_dir: &str,
    artifact_dir: &str,
    seed: u64,
) -> Result<VisionAutoTuneResult, String> {
    let samples = load_labeled_samples(dataset_dir)?;
    let images_found = samples.len();
    if images_found < 8 {
        return Err(format!(
            "not enough labeled images in '{}'; expected >=8, got {}",
            dataset_dir, images_found
        ));
    }

    fs::create_dir_all(artifact_dir)
        .map_err(|err| format!("failed to create artifact dir '{artifact_dir}': {err}"))?;

    let (train_set, valid_set) = split_dataset(samples);
    let candidates = build_candidates();
    let mut trials = Vec::with_capacity(candidates.len());

    for (index, params) in candidates.into_iter().enumerate() {
        let mut model = TinyMlp::new(FEATURE_DIM, &params, seed + index as u64 * 17);
        let metrics = model.train_and_eval(&train_set, &valid_set, &params);
        trials.push(VisionTrialResult {
            trial_id: index + 1,
            hyper_params: params,
            score: metrics.valid_accuracy,
            train_loss: metrics.train_loss,
            valid_loss: metrics.valid_loss,
            valid_accuracy: metrics.valid_accuracy,
        });
    }

    trials.sort_by(|a, b| {
        b.score
            .partial_cmp(&a.score)
            .unwrap_or(std::cmp::Ordering::Equal)
    });

    let best_trial = trials
        .first()
        .cloned()
        .ok_or_else(|| "internal error: no trials generated".to_string())?;

    let leaderboard_path = Path::new(artifact_dir).join("leaderboard.json");
    fs::write(
        &leaderboard_path,
        serde_json::to_string_pretty(&trials)
            .map_err(|err| format!("failed to encode leaderboard JSON: {err}"))?,
    )
    .map_err(|err| {
        format!(
            "failed to write leaderboard at '{}': {err}",
            leaderboard_path.display()
        )
    })?;

    let best_model_dir = Path::new(artifact_dir).join("best_model");
    fs::create_dir_all(&best_model_dir).map_err(|err| {
        format!(
            "failed to create best_model dir '{}': {err}",
            best_model_dir.display()
        )
    })?;
    let best_model_path = best_model_dir.join("model.json");
    fs::write(
        &best_model_path,
        serde_json::to_string_pretty(&best_trial)
            .map_err(|err| format!("failed to encode best trial JSON: {err}"))?,
    )
    .map_err(|err| {
        format!(
            "failed to write best model metadata '{}': {err}",
            best_model_path.display()
        )
    })?;

    let result = VisionAutoTuneResult {
        dataset_dir: dataset_dir.to_string(),
        artifact_dir: artifact_dir.to_string(),
        images_found,
        trial_count: trials.len(),
        best_trial,
        leaderboard_path: leaderboard_path.display().to_string(),
        best_model_path: best_model_path.display().to_string(),
    };

    let summary_path = Path::new(artifact_dir).join("autotune_summary.json");
    fs::write(
        &summary_path,
        serde_json::to_string_pretty(&result)
            .map_err(|err| format!("failed to encode autotune summary JSON: {err}"))?,
    )
    .map_err(|err| {
        format!(
            "failed to write autotune summary '{}': {err}",
            summary_path.display()
        )
    })?;

    Ok(result)
}

fn build_candidates() -> Vec<VisionHyperParams> {
    vec![
        VisionHyperParams {
            hidden_layers: 1,
            hidden_width: 16,
            dropout: 0.05,
            learning_rate: 8e-3,
            technique: "plain".to_string(),
        },
        VisionHyperParams {
            hidden_layers: 2,
            hidden_width: 24,
            dropout: 0.10,
            learning_rate: 5e-3,
            technique: "batch_norm".to_string(),
        },
        VisionHyperParams {
            hidden_layers: 3,
            hidden_width: 32,
            dropout: 0.15,
            learning_rate: 3e-3,
            technique: "batch_norm+residual".to_string(),
        },
        VisionHyperParams {
            hidden_layers: 4,
            hidden_width: 32,
            dropout: 0.20,
            learning_rate: 2e-3,
            technique: "residual".to_string(),
        },
    ]
}

fn load_labeled_samples(dataset_dir: &str) -> Result<Vec<LabeledSample>, String> {
    let metadata = Path::new(dataset_dir).join("images.jsonl");
    let raw = fs::read_to_string(&metadata)
        .map_err(|err| format!("failed to read '{}': {err}", metadata.display()))?;

    let mut samples = Vec::new();
    for line in raw.lines() {
        if line.trim().is_empty() {
            continue;
        }

        let record: CrawledImageRecord = serde_json::from_str(line)
            .map_err(|err| format!("invalid images.jsonl line: {err}"))?;
        let image_path = Path::new(&record.local_path);
        if !image_path.exists() {
            continue;
        }
        let features = extract_features(image_path)?;
        let label = infer_label(&record);
        samples.push(LabeledSample { features, label });
    }

    if samples.is_empty() {
        return Err(format!(
            "no usable image samples found in '{}'",
            metadata.display()
        ));
    }

    Ok(samples)
}

fn infer_label(record: &CrawledImageRecord) -> f32 {
    let text = format!(
        "{} {}",
        record.source_page.to_ascii_lowercase(),
        record.image_url.to_ascii_lowercase()
    );
    let positive = ["fire", "flame", "smoke", "burn", "wildfire"];
    let negative = ["water", "rain", "portrait", "wedding", "studio"];

    if positive.iter().any(|keyword| text.contains(keyword)) {
        return 1.0;
    }
    if negative.iter().any(|keyword| text.contains(keyword)) {
        return 0.0;
    }
    0.0
}

fn extract_features(path: &Path) -> Result<[f32; FEATURE_DIM], String> {
    let image = ImageReader::open(path)
        .map_err(|err| format!("failed to open image '{}': {err}", path.display()))?
        .decode()
        .map_err(|err| format!("failed to decode image '{}': {err}", path.display()))?;

    let resized = image.thumbnail(64, 64);
    let mut features = [0.0f32; FEATURE_DIM];
    let mut total = 0.0f32;
    let mut red_total = 0.0f32;
    let mut green_total = 0.0f32;
    let mut blue_total = 0.0f32;

    for (_, _, pixel) in resized.pixels() {
        let rgb = pixel.0;
        let r = rgb[0] as f32 / 255.0;
        let g = rgb[1] as f32 / 255.0;
        let b = rgb[2] as f32 / 255.0;
        let luminance = (0.2126 * r + 0.7152 * g + 0.0722 * b).clamp(0.0, 0.999);
        let bin = (luminance * 10.0) as usize;
        features[bin.min(9)] += 1.0;
        red_total += r;
        green_total += g;
        blue_total += b;
        total += 1.0;
    }

    if total == 0.0 {
        return Err(format!("image '{}' has no pixels", path.display()));
    }

    for value in features.iter_mut().take(10) {
        *value /= total;
    }

    let r = red_total / total;
    let g = green_total / total;
    let b = blue_total / total;
    features[10] = r;
    features[11] = g;
    features[12] = b;
    features[13] = (r - g).max(0.0);
    features[14] = (r - b).max(0.0);
    features[15] = (r - (g + b) * 0.5).max(0.0);

    Ok(features)
}

fn split_dataset(samples: Vec<LabeledSample>) -> (Vec<LabeledSample>, Vec<LabeledSample>) {
    let valid_size = ((samples.len() as f32) * 0.2).round() as usize;
    let valid_size = valid_size.clamp(1, samples.len().saturating_sub(1));
    let split_index = samples.len().saturating_sub(valid_size);
    let train = samples[..split_index].to_vec();
    let valid = samples[split_index..].to_vec();
    (train, valid)
}

#[derive(Debug, Clone)]
struct EvalMetrics {
    train_loss: f32,
    valid_loss: f32,
    valid_accuracy: f32,
}

impl TinyMlp {
    fn new(input_dim: usize, hp: &VisionHyperParams, seed: u64) -> Self {
        let mut dims = Vec::new();
        dims.push(input_dim);
        for _ in 0..hp.hidden_layers {
            dims.push(hp.hidden_width);
        }
        dims.push(1);

        let mut layers = Vec::new();
        for pair in dims.windows(2) {
            layers.push(Layer::new(pair[0], pair[1], seed + pair[0] as u64 + pair[1] as u64));
        }

        Self { layers }
    }

    fn train_and_eval(
        &mut self,
        train_set: &[LabeledSample],
        valid_set: &[LabeledSample],
        hp: &VisionHyperParams,
    ) -> EvalMetrics {
        let epochs = (8 + hp.hidden_layers * 2).min(20);
        let lr = hp.learning_rate as f32;
        let mut train_loss = 1.0f32;

        for _ in 0..epochs {
            let mut epoch_loss = 0.0;
            for sample in train_set {
                let (prediction, activations) = self.forward_with_cache(&sample.features);
                let pred = prediction.clamp(1.0e-4, 1.0 - 1.0e-4);
                let loss = -sample.label * pred.ln() - (1.0 - sample.label) * (1.0 - pred).ln();
                epoch_loss += loss;

                let mut grad = vec![pred - sample.label];
                for layer_idx in (0..self.layers.len()).rev() {
                    let input = &activations[layer_idx];
                    let output = &activations[layer_idx + 1];
                    let is_last = layer_idx + 1 == self.layers.len();
                    let dropout_scale = if is_last { 1.0 } else { 1.0 - hp.dropout };
                    let mut grad_input = vec![0.0f32; self.layers[layer_idx].in_dim];

                    for (out_idx, grad_out) in grad.iter().enumerate() {
                        let delta = if is_last {
                            *grad_out
                        } else if output[out_idx] > 0.0 {
                            *grad_out * dropout_scale
                        } else {
                            0.0
                        };

                        for (in_idx, value) in input.iter().enumerate() {
                            let w_idx = out_idx * self.layers[layer_idx].in_dim + in_idx;
                            grad_input[in_idx] += self.layers[layer_idx].weight[w_idx] * delta;
                            self.layers[layer_idx].weight[w_idx] -= lr * delta * value;
                        }

                        self.layers[layer_idx].bias[out_idx] -= lr * delta;
                    }
                    grad = grad_input;
                }
            }
            train_loss = epoch_loss / train_set.len().max(1) as f32;
        }

        let mut valid_loss = 0.0;
        let mut correct = 0usize;

        for sample in valid_set {
            let prediction = self.forward(&sample.features).clamp(1.0e-4, 1.0 - 1.0e-4);
            valid_loss += -sample.label * prediction.ln() - (1.0 - sample.label) * (1.0 - prediction).ln();
            let pred_label = if prediction >= 0.5 { 1.0 } else { 0.0 };
            if (pred_label - sample.label).abs() < f32::EPSILON {
                correct += 1;
            }
        }

        EvalMetrics {
            train_loss,
            valid_loss: valid_loss / valid_set.len().max(1) as f32,
            valid_accuracy: correct as f32 / valid_set.len().max(1) as f32,
        }
    }

    fn forward(&self, input: &[f32; FEATURE_DIM]) -> f32 {
        let mut current = input.to_vec();
        for (index, layer) in self.layers.iter().enumerate() {
            current = layer.forward(&current);
            let is_last = index + 1 == self.layers.len();
            if is_last {
                current[0] = sigmoid(current[0]);
            } else {
                for value in &mut current {
                    *value = value.max(0.0);
                }
            }
        }
        current[0]
    }

    fn forward_with_cache(&self, input: &[f32; FEATURE_DIM]) -> (f32, Vec<Vec<f32>>) {
        let mut cache = Vec::with_capacity(self.layers.len() + 1);
        let mut current = input.to_vec();
        cache.push(current.clone());

        for (index, layer) in self.layers.iter().enumerate() {
            current = layer.forward(&current);
            let is_last = index + 1 == self.layers.len();
            if is_last {
                current[0] = sigmoid(current[0]);
            } else {
                for value in &mut current {
                    *value = value.max(0.0);
                }
            }
            cache.push(current.clone());
        }

        (current[0], cache)
    }
}

impl Layer {
    fn new(in_dim: usize, out_dim: usize, seed: u64) -> Self {
        let mut weight = vec![0.0f32; in_dim * out_dim];
        let mut bias = vec![0.0f32; out_dim];

        for (idx, value) in weight.iter_mut().enumerate() {
            *value = seeded_float(seed, idx as u64) * 0.2 - 0.1;
        }
        for (idx, value) in bias.iter_mut().enumerate() {
            *value = seeded_float(seed + 101, idx as u64) * 0.02 - 0.01;
        }

        Self {
            in_dim,
            out_dim,
            weight,
            bias,
        }
    }

    fn forward(&self, input: &[f32]) -> Vec<f32> {
        let mut out = vec![0.0f32; self.out_dim];
        for (out_idx, slot) in out.iter_mut().enumerate() {
            let mut sum = self.bias[out_idx];
            for (in_idx, value) in input.iter().enumerate() {
                let idx = out_idx * self.in_dim + in_idx;
                sum += self.weight[idx] * value;
            }
            *slot = sum;
        }
        out
    }
}

fn seeded_float(seed: u64, index: u64) -> f32 {
    let mut hasher = std::collections::hash_map::DefaultHasher::new();
    seed.hash(&mut hasher);
    index.hash(&mut hasher);
    ((hasher.finish() % 10_000) as f32) / 10_000.0
}

fn sigmoid(x: f32) -> f32 {
    1.0 / (1.0 + (-x).exp())
}

#[cfg(test)]
mod tests {
    use super::build_candidates;

    #[test]
    fn candidates_have_multiple_depths() {
        let candidates = build_candidates();
        assert!(candidates.iter().any(|x| x.hidden_layers == 1));
        assert!(candidates.iter().any(|x| x.hidden_layers >= 3));
    }
}
