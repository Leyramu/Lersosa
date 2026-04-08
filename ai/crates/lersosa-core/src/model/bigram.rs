use burn::{nn::{Embedding, EmbeddingConfig}, prelude::*};

#[derive(Config, Debug)]
pub struct BigramConfig {
    pub vocab_size: usize,
}

#[derive(Module, Debug)]
pub struct BigramModel<B: Backend> {
    token_to_logits: Embedding<B>,
}

impl BigramConfig {
    pub fn init<B: Backend>(&self, device: &B::Device) -> BigramModel<B> {
        let token_to_logits = EmbeddingConfig::new(self.vocab_size, self.vocab_size).init(device);

        BigramModel { token_to_logits }
    }
}

impl<B: Backend> BigramModel<B> {
    pub fn forward(&self, token_ids: Tensor<B, 2, Int>) -> Tensor<B, 3> {
        self.token_to_logits.forward(token_ids)
    }
}
