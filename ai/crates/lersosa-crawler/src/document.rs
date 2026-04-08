use mongodb::bson::{DateTime, oid::ObjectId};
use serde::{Deserialize, Serialize};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct CrawledDocument {
    #[serde(rename = "_id", skip_serializing_if = "Option::is_none")]
    pub id: Option<ObjectId>,
    pub url: String,
    pub domain: String,
    pub title: Option<String>,
    pub text: String,
    pub links: Vec<String>,
    pub status_code: u16,
    pub content_type: Option<String>,
    pub content_hash_sha256: String,
    pub fetched_at: DateTime,
}

