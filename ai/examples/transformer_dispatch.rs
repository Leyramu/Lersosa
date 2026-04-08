fn main() {
    let options: lersosa::inference::transformer_lm::TransformerLmInferenceRunOptions =
        lersosa::inference::transformer_lm::TransformerLmInferenceRunOptions {
            backend: lersosa::runtime::BackendKind::Tch,
        };

    lersosa::inference::transformer_lm::entry(options);
}
