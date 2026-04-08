use thiserror::Error;

pub type Result<T> = std::result::Result<T, CrawlerError>;

#[derive(Debug, Error)]
pub enum CrawlerError {
    #[error("configuration error: {0}")]
    Config(String),

    #[error("I/O error while {context}: {source}")]
    Io {
        context: String,
        #[source]
        source: std::io::Error,
    },

    #[error("TOML parse error while {context}: {source}")]
    Toml {
        context: String,
        #[source]
        source: toml::de::Error,
    },

    #[error("JSON parse error while {context}: {source}")]
    Json {
        context: String,
        #[source]
        source: serde_json::Error,
    },

    #[error("invalid URL '{url}': {source}")]
    Url {
        url: String,
        #[source]
        source: url::ParseError,
    },

    #[error("http error while {context}: {source}")]
    Http {
        context: String,
        #[source]
        source: reqwest::Error,
    },

    #[error("mongodb error while {context}: {source}")]
    Mongo {
        context: String,
        #[source]
        source: mongodb::error::Error,
    },
}

