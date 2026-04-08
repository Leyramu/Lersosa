pub mod mongo;

use std::sync::{Arc, Mutex};

use async_trait::async_trait;

use crate::{document::CrawledDocument, error::Result};

#[derive(Debug, Default, Clone, Copy)]
pub struct NoopRepository;

#[derive(Debug, Clone)]
pub struct RecordingRepository {
    urls: Arc<Mutex<Vec<String>>>,
    sample_limit: usize,
}

impl RecordingRepository {
    pub fn new(sample_limit: usize) -> Self {
        Self {
            urls: Arc::new(Mutex::new(Vec::new())),
            sample_limit,
        }
    }

    pub fn sampled_urls(&self) -> Vec<String> {
        self.urls.lock().expect("recording mutex poisoned").clone()
    }
}

#[async_trait]
pub trait DocumentRepository: Send + Sync {
    async fn save(&self, document: &CrawledDocument) -> Result<()>;
}

#[async_trait]
impl DocumentRepository for NoopRepository {
    async fn save(&self, _document: &CrawledDocument) -> Result<()> {
        Ok(())
    }
}

#[async_trait]
impl DocumentRepository for RecordingRepository {
    async fn save(&self, document: &CrawledDocument) -> Result<()> {
        let mut urls = self.urls.lock().expect("recording mutex poisoned");
        if urls.len() < self.sample_limit {
            urls.push(document.url.clone());
        }
        Ok(())
    }
}

