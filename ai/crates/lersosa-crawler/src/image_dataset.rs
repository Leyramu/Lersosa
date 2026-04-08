use std::{
    collections::HashSet,
    fs,
    io::Write,
    path::{Path, PathBuf},
    time::Duration,
};

use reqwest::Client;
use scraper::{Html, Selector};
use serde::{Deserialize, Serialize};
use sha2::{Digest, Sha256};
use url::Url;

use crate::error::{CrawlerError, Result};

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct DatasetSearchHit {
    pub title: String,
    pub url: String,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ImageCrawlRequest {
    pub query: String,
    #[serde(default)]
    pub source_urls: Option<Vec<String>>,
    pub output_dir: String,
    pub max_source_pages: usize,
    pub max_images: usize,
    pub user_agent: String,
    pub timeout_secs: u64,
    #[serde(default)]
    pub blocked_domains: Vec<String>,
    #[serde(default)]
    pub allowed_license_keywords: Vec<String>,
    #[serde(default)]
    pub allow_unknown_license: bool,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct CrawledImage {
    pub source_page: String,
    pub image_url: String,
    pub local_path: String,
    pub content_type: Option<String>,
    pub sha256: String,
    pub bytes: usize,
}

#[derive(Debug, Clone, Serialize, Deserialize)]
pub struct ImageCrawlSummary {
    pub query: String,
    pub output_dir: String,
    pub searched_sources: usize,
    pub attempted_images: usize,
    pub downloaded_images: usize,
    pub deduped_images: usize,
    pub failed_images: usize,
    pub skipped_by_domain: usize,
    pub skipped_by_license: usize,
    pub dataset_manifest: String,
    pub metadata_jsonl: String,
}

pub async fn search_image_datasets(query: &str, limit: usize) -> Result<Vec<DatasetSearchHit>> {
    if query.trim().is_empty() {
        return Err(CrawlerError::Config("query cannot be empty".to_string()));
    }

    let q = format!("{query} image dataset");
    let encoded: String = url::form_urlencoded::byte_serialize(q.as_bytes()).collect();
    let search_url = format!("https://duckduckgo.com/html/?q={encoded}");

    let client = Client::builder()
        .timeout(Duration::from_secs(20))
        .user_agent("LersosaImageCrawler/1.0")
        .build()
        .map_err(|source| CrawlerError::Http {
            context: "building dataset search HTTP client".to_string(),
            source,
        })?;

    let body = client
        .get(&search_url)
        .send()
        .await
        .map_err(|source| CrawlerError::Http {
            context: format!("requesting dataset search URL '{search_url}'"),
            source,
        })?
        .text()
        .await
        .map_err(|source| CrawlerError::Http {
            context: format!("reading dataset search response from '{search_url}'"),
            source,
        })?;

    let document = Html::parse_document(&body);
    let selector = Selector::parse("a.result__a").expect("selector is valid");

    let mut hits = Vec::new();
    let mut seen = HashSet::new();

    for node in document.select(&selector) {
        if hits.len() >= limit {
            break;
        }

        let title = node
            .text()
            .collect::<Vec<_>>()
            .join(" ")
            .trim()
            .to_string();
        let Some(raw_href) = node.value().attr("href") else {
            continue;
        };
        let resolved = unwrap_duckduckgo_redirect(raw_href).unwrap_or_else(|| raw_href.to_string());
        if !resolved.starts_with("http") || !seen.insert(resolved.clone()) {
            continue;
        }

        hits.push(DatasetSearchHit {
            title,
            url: resolved,
        });
    }

    Ok(hits)
}

pub async fn crawl_image_dataset(request: &ImageCrawlRequest) -> Result<ImageCrawlSummary> {
    if request.max_source_pages == 0 {
        return Err(CrawlerError::Config(
            "max_source_pages must be greater than 0".to_string(),
        ));
    }
    if request.max_images == 0 {
        return Err(CrawlerError::Config(
            "max_images must be greater than 0".to_string(),
        ));
    }

    let source_hits = if let Some(source_urls) = &request.source_urls {
        let hits = source_urls
            .iter()
            .take(request.max_source_pages)
            .map(|url| DatasetSearchHit {
                title: "offline_source".to_string(),
                url: url.clone(),
            })
            .collect::<Vec<_>>();
        if hits.is_empty() {
            search_image_datasets(&request.query, request.max_source_pages).await?
        } else {
            hits
        }
    } else {
        search_image_datasets(&request.query, request.max_source_pages).await?
    };

    fs::create_dir_all(&request.output_dir).map_err(|source| CrawlerError::Io {
        context: format!("creating output_dir '{}'", request.output_dir),
        source,
    })?;

    let image_dir = Path::new(&request.output_dir).join("images");
    fs::create_dir_all(&image_dir).map_err(|source| CrawlerError::Io {
        context: format!("creating image dir '{}'", image_dir.display()),
        source,
    })?;

    let metadata_jsonl = Path::new(&request.output_dir).join("images.jsonl");
    let mut jsonl_writer = fs::File::create(&metadata_jsonl).map_err(|source| CrawlerError::Io {
        context: format!("creating '{}'", metadata_jsonl.display()),
        source,
    })?;

    let client = Client::builder()
        .timeout(Duration::from_secs(request.timeout_secs))
        .user_agent(request.user_agent.clone())
        .build()
        .map_err(|source| CrawlerError::Http {
            context: "building image crawler HTTP client".to_string(),
            source,
        })?;

    let mut summary = ImageCrawlSummary {
        query: request.query.clone(),
        output_dir: request.output_dir.clone(),
        searched_sources: source_hits.len(),
        attempted_images: 0,
        downloaded_images: 0,
        deduped_images: 0,
        failed_images: 0,
        skipped_by_domain: 0,
        skipped_by_license: 0,
        dataset_manifest: String::new(),
        metadata_jsonl: metadata_jsonl.display().to_string(),
    };

    let blocked_domains = request
        .blocked_domains
        .iter()
        .map(|item| item.trim().to_ascii_lowercase())
        .filter(|item| !item.is_empty())
        .collect::<HashSet<_>>();

    let allowed_license_keywords = request
        .allowed_license_keywords
        .iter()
        .map(|item| item.trim().to_ascii_lowercase())
        .filter(|item| !item.is_empty())
        .collect::<Vec<_>>();

    let mut downloaded_hashes = HashSet::new();

    for source in source_hits {
        if summary.downloaded_images >= request.max_images {
            break;
        }

        let page_url = match Url::parse(&source.url) {
            Ok(url) => url,
            Err(_) => continue,
        };

        if is_blocked_domain(&page_url, &blocked_domains) {
            summary.skipped_by_domain += 1;
            continue;
        }

        let page_html = match fetch_text(&client, &page_url).await {
            Ok(html) => html,
            Err(_) => {
                summary.failed_images += 1;
                continue;
            }
        };

        if !is_license_allowed(
            &page_html,
            &allowed_license_keywords,
            request.allow_unknown_license,
        ) {
            summary.skipped_by_license += 1;
            continue;
        }

        let image_urls = parse_image_urls(&page_url, &page_html);
        for image_url in image_urls {
            if summary.downloaded_images >= request.max_images {
                break;
            }

            summary.attempted_images += 1;

            let image = match download_image(&client, &source.url, &image_url, &image_dir).await {
                Ok(image) => image,
                Err(_) => {
                    summary.failed_images += 1;
                    continue;
                }
            };

            if let Ok(parsed_image_url) = Url::parse(&image.image_url) {
                if is_blocked_domain(&parsed_image_url, &blocked_domains) {
                    summary.skipped_by_domain += 1;
                    let _ = fs::remove_file(&image.local_path);
                    continue;
                }
            }

            if !downloaded_hashes.insert(image.sha256.clone()) {
                summary.deduped_images += 1;
                let _ = fs::remove_file(&image.local_path);
                continue;
            }

            let line = serde_json::to_string(&image).map_err(|source| CrawlerError::Json {
                context: "serializing image metadata line".to_string(),
                source,
            })?;
            writeln!(&mut jsonl_writer, "{line}").map_err(|source| CrawlerError::Io {
                context: format!("writing metadata JSONL at '{}'", metadata_jsonl.display()),
                source,
            })?;
            summary.downloaded_images += 1;
        }
    }

    let manifest_path = Path::new(&request.output_dir).join("dataset_manifest.json");
    summary.dataset_manifest = manifest_path.display().to_string();
    let manifest = serde_json::to_string_pretty(&summary).map_err(|source| CrawlerError::Json {
        context: "serializing dataset manifest".to_string(),
        source,
    })?;
    fs::write(&manifest_path, manifest).map_err(|source| CrawlerError::Io {
        context: format!("writing dataset manifest '{}'", manifest_path.display()),
        source,
    })?;

    Ok(summary)
}

async fn fetch_text(client: &Client, url: &Url) -> Result<String> {
    client
        .get(url.clone())
        .send()
        .await
        .map_err(|source| CrawlerError::Http {
            context: format!("requesting source page '{url}'"),
            source,
        })?
        .text()
        .await
        .map_err(|source| CrawlerError::Http {
            context: format!("reading source page '{url}'"),
            source,
        })
}

fn parse_image_urls(base_url: &Url, html: &str) -> Vec<Url> {
    let document = Html::parse_document(html);
    let selector = Selector::parse("img").expect("selector is valid");
    let mut urls = Vec::new();
    let mut seen = HashSet::new();

    for image in document.select(&selector) {
        let src = image
            .value()
            .attr("src")
            .or_else(|| image.value().attr("data-src"));
        let Some(src) = src else {
            continue;
        };
        if src.starts_with("data:") {
            continue;
        }

        let resolved = if src.starts_with("http") {
            Url::parse(src).ok()
        } else {
            base_url.join(src).ok()
        };

        if let Some(url) = resolved {
            let key = url.as_str().to_string();
            if seen.insert(key) {
                urls.push(url);
            }
        }
    }

    urls
}

async fn download_image(
    client: &Client,
    source_page: &str,
    image_url: &Url,
    image_dir: &Path,
) -> Result<CrawledImage> {
    let response = client
        .get(image_url.clone())
        .send()
        .await
        .map_err(|source| CrawlerError::Http {
            context: format!("requesting image URL '{image_url}'"),
            source,
        })?;

    let status = response.status();
    if !status.is_success() {
        return Err(CrawlerError::Config(format!(
            "image URL '{image_url}' returned status {status}"
        )));
    }

    let content_type = response
        .headers()
        .get(reqwest::header::CONTENT_TYPE)
        .and_then(|v| v.to_str().ok())
        .map(|v| v.to_string());

    if !content_type
        .as_deref()
        .unwrap_or_default()
        .to_ascii_lowercase()
        .starts_with("image/")
    {
        return Err(CrawlerError::Config(format!(
            "URL '{image_url}' does not look like image content"
        )));
    }

    let bytes = response.bytes().await.map_err(|source| CrawlerError::Http {
        context: format!("reading image bytes from '{image_url}'"),
        source,
    })?;

    let mut hasher = Sha256::new();
    hasher.update(&bytes);
    let sha256 = format!("{:x}", hasher.finalize());

    let extension = infer_image_extension(&content_type, image_url);
    let local_path = image_dir.join(format!("{}.{}", &sha256[..16], extension));

    fs::write(&local_path, &bytes).map_err(|source| CrawlerError::Io {
        context: format!("writing image file '{}'", local_path.display()),
        source,
    })?;

    Ok(CrawledImage {
        source_page: source_page.to_string(),
        image_url: image_url.to_string(),
        local_path: local_path.display().to_string(),
        content_type,
        sha256,
        bytes: bytes.len(),
    })
}

fn infer_image_extension(content_type: &Option<String>, image_url: &Url) -> String {
    if let Some(content_type) = content_type {
        let lower = content_type.to_ascii_lowercase();
        if lower.contains("jpeg") || lower.contains("jpg") {
            return "jpg".to_string();
        }
        if lower.contains("png") {
            return "png".to_string();
        }
        if lower.contains("webp") {
            return "webp".to_string();
        }
    }

    PathBuf::from(image_url.path())
        .extension()
        .and_then(|ext| ext.to_str())
        .filter(|ext| !ext.is_empty())
        .map(|ext| ext.to_ascii_lowercase())
        .unwrap_or_else(|| "img".to_string())
}

fn is_blocked_domain(url: &Url, blocked_domains: &HashSet<String>) -> bool {
    let Some(host) = url.host_str() else {
        return false;
    };
    let host = host.to_ascii_lowercase();
    blocked_domains
        .iter()
        .any(|blocked| host == *blocked || host.ends_with(&format!(".{blocked}")))
}

fn is_license_allowed(
    html: &str,
    allowed_keywords: &[String],
    allow_unknown_license: bool,
) -> bool {
    let lower = html.to_ascii_lowercase();

    if allowed_keywords.is_empty() {
        return true;
    }

    if allowed_keywords
        .iter()
        .any(|keyword| lower.contains(keyword))
    {
        return true;
    }

    if allow_unknown_license {
        let generic = ["license", "licence", "creativecommons", "public domain"];
        return generic.iter().any(|keyword| lower.contains(keyword));
    }

    false
}

fn unwrap_duckduckgo_redirect(href: &str) -> Option<String> {
    let parsed = Url::parse(href).ok()?;
    if !parsed.path().starts_with("/l/") {
        return None;
    }

    for (key, value) in parsed.query_pairs() {
        if key == "uddg" {
            return Some(value.to_string());
        }
    }

    None
}

#[cfg(test)]
mod tests {
    use super::unwrap_duckduckgo_redirect;

    #[test]
    fn unwrap_duckduckgo_redirect_works() {
        let href = "https://duckduckgo.com/l/?uddg=https%3A%2F%2Fexample.com%2Fdataset";
        let resolved = unwrap_duckduckgo_redirect(href);
        assert_eq!(resolved.as_deref(), Some("https://example.com/dataset"));
    }
}

