use async_trait::async_trait;
use mongodb::{
    Client, Collection, IndexModel,
    bson::doc,
    options::{ClientOptions, IndexOptions},
};

use crate::{
    config::MongoConfig,
    document::CrawledDocument,
    error::{CrawlerError, Result},
    repository::DocumentRepository,
};

pub struct MongoRepository {
    collection: Collection<CrawledDocument>,
}

impl MongoRepository {
    pub async fn connect(config: &MongoConfig) -> Result<Self> {
        if config.uri.trim().is_empty() {
            return Err(CrawlerError::Config(
                "mongo.uri is empty. Fill it in apps/web-crawler/config/crawler.toml".to_string(),
            ));
        }

        let options = ClientOptions::parse(&config.uri)
            .await
            .map_err(|source| CrawlerError::Mongo {
                context: "parsing MongoDB URI".to_string(),
                source,
            })?;

        let client = Client::with_options(options).map_err(|source| CrawlerError::Mongo {
            context: "creating MongoDB client".to_string(),
            source,
        })?;

        let collection = client
            .database(&config.database)
            .collection::<CrawledDocument>(&config.collection);

        // Ensure crawler runs are idempotent by URL and keep common query fields indexed.
        collection
            .create_index(
                IndexModel::builder()
                    .keys(doc! { "url": 1 })
                    .options(IndexOptions::builder().unique(true).name(Some("uniq_url".to_string())).build())
                    .build(),
            )
            .await
            .map_err(|source| CrawlerError::Mongo {
                context: "creating unique index on url".to_string(),
                source,
            })?;

        collection
            .create_index(
                IndexModel::builder()
                    .keys(doc! { "domain": 1, "fetched_at": -1 })
                    .options(IndexOptions::builder().name(Some("domain_fetched_at".to_string())).build())
                    .build(),
            )
            .await
            .map_err(|source| CrawlerError::Mongo {
                context: "creating index on domain + fetched_at".to_string(),
                source,
            })?;

        Ok(Self { collection })
    }
}

#[async_trait]
impl DocumentRepository for MongoRepository {
    async fn save(&self, document: &CrawledDocument) -> Result<()> {
        self.collection
            .replace_one(doc! { "url": &document.url }, document)
            .upsert(true)
            .await
            .map_err(|source| CrawlerError::Mongo {
                context: format!("upserting crawled document '{}'", document.url),
                source,
            })?;

        Ok(())
    }
}

