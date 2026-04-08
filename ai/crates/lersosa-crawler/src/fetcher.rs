use std::time::Duration;

use reqwest::{Client, redirect::Policy};
use url::Url;

use crate::error::{CrawlerError, Result};

#[derive(Debug, Clone)]
pub struct FetchResponse {
    pub final_url: Url,
    pub status_code: u16,
    pub content_type: Option<String>,
    pub body: String,
}

#[derive(Clone)]
pub struct HttpFetcher {
    client: Client,
}

impl HttpFetcher {
    pub fn new(user_agent: &str, timeout_secs: u64) -> Result<Self> {
        let client = Client::builder()
            .user_agent(user_agent.to_string())
            .timeout(Duration::from_secs(timeout_secs))
            .redirect(Policy::limited(8))
            .build()
            .map_err(|source| CrawlerError::Http {
                context: "building HTTP client".to_string(),
                source,
            })?;

        Ok(Self { client })
    }

    pub async fn fetch_text(&self, url: &Url) -> Result<FetchResponse> {
        let response = self
            .client
            .get(url.clone())
            .send()
            .await
            .map_err(|source| CrawlerError::Http {
                context: format!("requesting '{}'", url),
                source,
            })?;

        let final_url = response.url().clone();
        let status_code = response.status().as_u16();
        let content_type = response
            .headers()
            .get(reqwest::header::CONTENT_TYPE)
            .and_then(|value| value.to_str().ok())
            .map(|s| s.to_string());

        let body = response
            .text()
            .await
            .map_err(|source| CrawlerError::Http {
                context: format!("reading response body for '{}'", final_url),
                source,
            })?;

        Ok(FetchResponse {
            final_url,
            status_code,
            content_type,
            body,
        })
    }
}

