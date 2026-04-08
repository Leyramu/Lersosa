use burn::{
    nn::{Embedding, EmbeddingConfig, Linear, LinearConfig, Relu},
    prelude::*,
};

#[derive(Config, Debug)]
pub struct MlpConfig {
    pub vocab_size: usize,
    pub max_seq_length: usize,
    pub d_model: usize,
    pub d_hidden: usize,
}

#[derive(Module, Debug)]
pub struct MlpModel<B: Backend> {
    token_embedding: Embedding<B>,
    position_embedding: Embedding<B>,
    ffn_1: Linear<B>,
    ffn_2: Linear<B>,
    lm_head: Linear<B>,
    relu: Relu,
    max_seq_length: usize,
}

impl MlpConfig {
    pub fn init<B: Backend>(&self, device: &B::Device) -> MlpModel<B> {
        let token_embedding = EmbeddingConfig::new(self.vocab_size, self.d_model).init(device);
        let position_embedding = EmbeddingConfig::new(self.max_seq_length, self.d_model).init(device);
        let ffn_1 = LinearConfig::new(self.d_model, self.d_hidden).init(device);
        let ffn_2 = LinearConfig::new(self.d_hidden, self.d_model).init(device);
        let lm_head = LinearConfig::new(self.d_model, self.vocab_size).init(device);

        MlpModel {
            token_embedding,
            position_embedding,
            ffn_1,
            ffn_2,
            lm_head,
            relu: Relu::new(),
            max_seq_length: self.max_seq_length,
        }
    }
}

impl<B: Backend> MlpModel<B> {
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
        let hidden = (token_embedding + position_embedding) / 2.0;

        let hidden = self.ffn_1.forward(hidden);
        let hidden = self.relu.forward(hidden);
        let hidden = self.ffn_2.forward(hidden);

        self.lm_head.forward(hidden)
    }
}
