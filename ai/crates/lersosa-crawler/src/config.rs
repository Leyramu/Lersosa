use serde::{Deserialize, Serialize};

use crate::error::{CrawlerError, Result};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct CrawlerConfig {
    pub seed_urls: Vec<String>,
    pub user_agent: String,
    pub max_pages: usize,
    pub max_depth: usize,
    pub request_timeout_secs: u64,
    #[serde(default)]
    pub request_delay_ms: u64,
    #[serde(default)]
    pub per_host_request_delay_ms: u64,
    pub respect_robots: bool,
    #[serde(default)]
    pub allowed_path_prefixes: Vec<String>,
    #[serde(default)]
    pub blocked_path_prefixes: Vec<String>,
    pub mongo: MongoConfig,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct MongoConfig {
    pub uri: String,
    pub database: String,
    pub collection: String,
}

impl CrawlerConfig {
    pub fn from_file(path: &str) -> Result<Self> {
        let content = std::fs::read_to_string(path).map_err(|source| CrawlerError::Io {
            context: format!("failed to read crawler config at '{path}'"),
            source,
        })?;

        let config: CrawlerConfig = toml::from_str(&content).map_err(|source| CrawlerError::Toml {
            context: format!("failed to parse crawler config at '{path}'"),
            source,
        })?;

        config.validate()?;
        Ok(config)
    }

    pub fn validate(&self) -> Result<()> {
        if self.seed_urls.is_empty() {
            return Err(CrawlerError::Config(
                "seed_urls cannot be empty; provide at least one URL".to_string(),
            ));
        }
        if self.max_pages == 0 {
            return Err(CrawlerError::Config("max_pages must be greater than 0".to_string()));
        }
        if self.request_timeout_secs == 0 {
            return Err(CrawlerError::Config(
                "request_timeout_secs must be greater than 0".to_string(),
            ));
        }
        if self.request_delay_ms > 60_000 {
            return Err(CrawlerError::Config(
                "request_delay_ms must be <= 60000".to_string(),
            ));
        }
        if self.per_host_request_delay_ms > 60_000 {
            return Err(CrawlerError::Config(
                "per_host_request_delay_ms must be <= 60000".to_string(),
            ));
        }
        if self.user_agent.trim().is_empty() {
            return Err(CrawlerError::Config(
                "user_agent cannot be empty".to_string(),
            ));
        }
        for prefix in &self.allowed_path_prefixes {
            if !prefix.starts_with('/') {
                return Err(CrawlerError::Config(format!(
                    "allowed_path_prefixes must start with '/': '{prefix}'"
                )));
            }
        }
        for prefix in &self.blocked_path_prefixes {
            if !prefix.starts_with('/') {
                return Err(CrawlerError::Config(format!(
                    "blocked_path_prefixes must start with '/': '{prefix}'"
                )));
            }
        }
        if self.mongo.database.trim().is_empty() || self.mongo.collection.trim().is_empty() {
            return Err(CrawlerError::Config(
                "mongo.database and mongo.collection cannot be empty".to_string(),
            ));
        }

        Ok(())
    }
}

#[cfg(test)]
mod tests {
    use super::{CrawlerConfig, MongoConfig};

    fn base_config() -> CrawlerConfig {
        CrawlerConfig {
            seed_urls: vec!["https://example.com".to_string()],
            user_agent: "LersosaCrawler/1.0".to_string(),
            max_pages: 10,
            max_depth: 2,
            request_timeout_secs: 10,
            request_delay_ms: 100,
            per_host_request_delay_ms: 200,
            respect_robots: true,
            allowed_path_prefixes: Vec::new(),
            blocked_path_prefixes: Vec::new(),
            mongo: MongoConfig {
                uri: "mongodb://localhost:27017".to_string(),
                database: "lersosa".to_string(),
                collection: "crawled_pages".to_string(),
            },
        }
    }

    #[test]
    fn validate_accepts_reasonable_delay_values() {
        let config = base_config();
        assert!(config.validate().is_ok());
    }

    #[test]
    fn validate_rejects_too_large_per_host_delay() {
        let mut config = base_config();
        config.per_host_request_delay_ms = 60_001;
        assert!(config.validate().is_err());
    }
}

