use burn::{
    nn::{Embedding, EmbeddingConfig, Gru, GruConfig, Linear, LinearConfig},
    prelude::*,
};

#[derive(Config, Debug)]
pub struct GruModelConfig {
    pub vocab_size: usize,
    pub max_seq_length: usize,
    pub d_model: usize,
    pub d_hidden: usize,
}

#[derive(Module, Debug)]
pub struct GruModel<B: Backend> {
    token_embedding: Embedding<B>,
    position_embedding: Embedding<B>,
    gru: Gru<B>,
    lm_head: Linear<B>,
    max_seq_length: usize,
}

impl GruModelConfig {
    pub fn init<B: Backend>(&self, device: &B::Device) -> GruModel<B> {
        let token_embedding = EmbeddingConfig::new(self.vocab_size, self.d_model).init(device);
        let position_embedding = EmbeddingConfig::new(self.max_seq_length, self.d_model).init(device);
        let gru = GruConfig::new(self.d_model, self.d_hidden, true).init(device);
        let lm_head = LinearConfig::new(self.d_hidden, self.vocab_size).init(device);

        GruModel {
            token_embedding,
            position_embedding,
            gru,
            lm_head,
            max_seq_length: self.max_seq_length,
        }
    }
}

impl<B: Backend> GruModel<B> {
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

        let hidden_states = self.gru.forward(embedding, None);
        self.lm_head.forward(hidden_states)
    }
}
