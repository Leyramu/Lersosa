use burn::{
    nn::{
        Embedding, EmbeddingConfig, Linear, LinearConfig,
        attention::generate_autoregressive_mask,
        transformer::{TransformerEncoder, TransformerEncoderConfig, TransformerEncoderInput},
    },
    prelude::*,
};

#[derive(Config, Debug)]
pub struct TransformerConfig {
    pub vocab_size: usize,
    pub max_seq_length: usize,
    pub d_model: usize,
    pub d_ff: usize,
    pub n_heads: usize,
    pub n_layers: usize,
}

#[derive(Module, Debug)]
pub struct TransformerModel<B: Backend> {
    encoder: TransformerEncoder<B>,
    token_embedding: Embedding<B>,
    position_embedding: Embedding<B>,
    lm_head: Linear<B>,
    max_seq_length: usize,
}

impl TransformerConfig {
    pub fn init<B: Backend>(&self, device: &B::Device) -> TransformerModel<B> {
        let encoder = TransformerEncoderConfig::new(self.d_model, self.d_ff, self.n_heads, self.n_layers)
            .with_norm_first(true)
            .init(device);
        let token_embedding = EmbeddingConfig::new(self.vocab_size, self.d_model).init(device);
        let position_embedding = EmbeddingConfig::new(self.max_seq_length, self.d_model).init(device);
        let lm_head = LinearConfig::new(self.d_model, self.vocab_size).init(device);

        TransformerModel {
            encoder,
            token_embedding,
            position_embedding,
            lm_head,
            max_seq_length: self.max_seq_length,
        }
    }
}

impl<B: Backend> TransformerModel<B> {
    pub fn forward(&self, token_ids: Tensor<B, 2, Int>) -> Tensor<B, 3> {
        let [batch_size, seq_length] = token_ids.dims();
        assert!(
            seq_length <= self.max_seq_length,
            "Input seq_length ({seq_length}) exceeds max_seq_length ({})",
            self.max_seq_length
        );

        let device = &token_ids.device();
        let positions = Tensor::arange(0..seq_length as i64, device)
            .reshape([1, seq_length])
            .repeat_dim(0, batch_size);

        let token_embedding = self.token_embedding.forward(token_ids);
        let position_embedding = self.position_embedding.forward(positions);
        let embedding = (token_embedding + position_embedding) / 2.0;

        let mask_attn = generate_autoregressive_mask::<B>(batch_size, seq_length, device);
        let encoded = self.encoder.forward(
            TransformerEncoderInput::new(embedding).mask_attn(mask_attn),
        );

        self.lm_head.forward(encoded)
    }
}
