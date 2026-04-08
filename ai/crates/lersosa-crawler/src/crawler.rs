use std::collections::{HashMap, HashSet, VecDeque};
use std::time::{Duration, Instant};

use mongodb::bson::DateTime;
use sha2::{Digest, Sha256};
use url::Url;

use crate::{
    config::CrawlerConfig,
    document::CrawledDocument,
    error::{CrawlerError, Result},
    fetcher::HttpFetcher,
    parser::parse_html,
    repository::DocumentRepository,
    robots::RobotsPolicy,
};

#[derive(Debug, Default)]
pub struct CrawlSummary {
    pub crawled_pages: usize,
    pub skipped_by_robots: usize,
    pub skipped_by_path_rules: usize,
    pub skipped_out_of_scope: usize,
    pub skipped_errors: usize,
}

pub struct WebCrawler<R: DocumentRepository> {
    config: CrawlerConfig,
    fetcher: HttpFetcher,
    repository: R,
}

#[derive(Debug)]
struct QueueItem {
    url: Url,
    depth: usize,
}

impl<R: DocumentRepository> WebCrawler<R> {
    pub fn new(config: CrawlerConfig, repository: R) -> Result<Self> {
        let fetcher = HttpFetcher::new(&config.user_agent, config.request_timeout_secs)?;
        Ok(Self {
            config,
            fetcher,
            repository,
        })
    }

    pub async fn run(&self) -> Result<CrawlSummary> {
        let mut summary = CrawlSummary::default();
        let mut visited = HashSet::new();
        let mut queue = VecDeque::new();
        let mut robots_cache: HashMap<String, RobotsPolicy> = HashMap::new();
        let mut last_request_by_host: HashMap<String, Instant> = HashMap::new();

        let mut allowed_hosts = HashSet::new();
        for seed in &self.config.seed_urls {
            let url = Url::parse(seed).map_err(|source| CrawlerError::Url {
                url: seed.clone(),
                source,
            })?;
            if let Some(host) = url.host_str() {
                allowed_hosts.insert(host.to_string());
            }
            queue.push_back(QueueItem { url, depth: 0 });
        }

        while let Some(item) =queue.pop_front() {
            if visited.len() >= self.config.max_pages {
                break;
            }

            let canonical = normalize_url(&item.url);
            if !visited.insert(canonical.clone()) {
                continue;
            }

            if item.depth > self.config.max_depth {
                continue;
            }

            if !is_in_scope(&item.url, &allowed_hosts) {
                summary.skipped_out_of_scope += 1;
                continue;
            }

            if !is_path_allowed(
                item.url.path(),
                &self.config.allowed_path_prefixes,
                &self.config.blocked_path_prefixes,
            ) {
                summary.skipped_by_path_rules += 1;
                continue;
            }

            let host_key = host_with_scheme(&item.url);
            if self.config.respect_robots {
                let policy = if let Some(policy) = robots_cache.get(&host_key) {
                    policy.clone()
                } else {
                    let policy = self.load_robots_policy(&item.url).await;
                    robots_cache.insert(host_key.clone(), policy.clone());
                    policy
                };

                if !policy.is_allowed(&self.config.user_agent, &item.url) {
                    summary.skipped_by_robots += 1;
                    continue;
                }
            }

            apply_global_delay(self.config.request_delay_ms).await;
            apply_per_host_delay(
                &mut last_request_by_host,
                &host_key,
                self.config.per_host_request_delay_ms,
            )
            .await;

            let response = match self.fetcher.fetch_text(&item.url).await {
                Ok(response) => response,
                Err(err) => {
                    summary.skipped_errors += 1;
                    tracing::warn!("failed to fetch '{}': {}", item.url, err);
                    continue;
                }
            };

            if response.status_code >= 400 {
                summary.skipped_errors += 1;
                tracing::warn!(
                    "non-success status {} for '{}'",
                    response.status_code,
                    response.final_url
                );
                continue;
            }

            let (title, text, links) = if response
                .content_type
                .as_deref()
                .map(|ct| ct.to_ascii_lowercase().contains("text/html"))
                .unwrap_or(false)
            {
                let parsed = parse_html(&response.final_url, &response.body);
                (parsed.title, parsed.text, parsed.links)
            } else {
                (None, response.body.clone(), Vec::new())
            };

            let hash = sha256_hex(&response.body);
            let document = CrawledDocument {
                id: None,
                url: response.final_url.to_string(),
                domain: response
                    .final_url
                    .host_str()
                    .map(ToString::to_string)
                    .unwrap_or_default(),
                title,
                text,
                links: links.clone(),
                status_code: response.status_code,
                content_type: response.content_type.clone(),
                content_hash_sha256: hash,
                fetched_at: DateTime::now(),
            };

            if let Err(err) = self.repository.save(&document).await {
                summary.skipped_errors += 1;
                tracing::warn!("failed to save document '{}': {}", document.url, err);
                continue;
            }

            summary.crawled_pages += 1;

            if item.depth < self.config.max_depth {
                for link in links {
                    if let Ok(url) = Url::parse(&link) {
                        if is_in_scope(&url, &allowed_hosts) {
                            queue.push_back(QueueItem {
                                url,
                                depth: item.depth + 1,
                            });
                        } else {
                            summary.skipped_out_of_scope += 1;
                        }
                    }
                }
            }
        }

        Ok(summary)
    }

    async fn load_robots_policy(&self, page_url: &Url) -> RobotsPolicy {
        let Some(host) = page_url.host_str() else {
            return RobotsPolicy::allow_all();
        };

        let mut robots_base = format!("{}://{}", page_url.scheme(), host);
        if let Some(port) = page_url.port() {
            robots_base.push(':');
            robots_base.push_str(&port.to_string());
        }
        let robots_url = format!("{robots_base}/robots.txt");

        let robots_url = match Url::parse(&robots_url) {
            Ok(url) => url,
            Err(_) => return RobotsPolicy::allow_all(),
        };

        match self.fetcher.fetch_text(&robots_url).await {
            Ok(response) if response.status_code < 400 => {
                tracing::info!("loaded robots policy for host {}", host);
                RobotsPolicy::parse(&response.body)
            }
            Ok(response) => {
                tracing::info!(
                    "robots.txt returned status {}, default allow for host {}",
                    response.status_code,
                    host
                );
                RobotsPolicy::allow_all()
            }
            Err(_) => {
                tracing::info!("robots.txt unavailable, default allow for host {}", host);
                RobotsPolicy::allow_all()
            }
        }
    }
}

fn is_in_scope(url: &Url, allowed_hosts: &HashSet<String>) -> bool {
    match url.host_str() {
        Some(host) => allowed_hosts.contains(host),
        None => false,
    }
}

fn normalize_url(url: &Url) -> String {
    let mut normalized = url.clone();
    normalized.set_fragment(None);
    normalized.to_string()
}

fn host_with_scheme(url: &Url) -> String {
    format!(
        "{}://{}:{}",
        url.scheme(),
        url.host_str().unwrap_or_default(),
        url.port_or_known_default().unwrap_or_default()
    )
}

fn sha256_hex(content: &str) -> String {
    let mut hasher = Sha256::new();
    hasher.update(content.as_bytes());
    format!("{:x}", hasher.finalize())
}

fn is_path_allowed(path: &str, allowed: &[String], blocked: &[String]) -> bool {
    if blocked.iter().any(|prefix| path.starts_with(prefix)) {
        return false;
    }

    if allowed.is_empty() {
        return true;
    }

    allowed.iter().any(|prefix| path.starts_with(prefix))
}

async fn apply_global_delay(delay_ms: u64) {
    if delay_ms > 0 {
        tokio::time::sleep(Duration::from_millis(delay_ms)).await;
    }
}

async fn apply_per_host_delay(
    last_request_by_host: &mut HashMap<String, Instant>,
    host_key: &str,
    delay_ms: u64,
) {
    if delay_ms == 0 {
        return;
    }

    let min_interval = Duration::from_millis(delay_ms);
    if let Some(last_request_at) = last_request_by_host.get(host_key) {
        let elapsed = last_request_at.elapsed();
        if elapsed < min_interval {
            tokio::time::sleep(min_interval - elapsed).await;
        }
    }

    last_request_by_host.insert(host_key.to_string(), Instant::now());
}

#[cfg(test)]
mod tests {
    use super::is_path_allowed;

    #[test]
    fn blocked_prefix_has_priority() {
        let allowed = vec!["/docs".to_string()];
        let blocked = vec!["/docs/private".to_string()];
        assert!(!is_path_allowed("/docs/private/a", &allowed, &blocked));
    }

    #[test]
    fn empty_allowed_means_allow_all_except_blocked() {
        let allowed: Vec<String> = Vec::new();
        let blocked = vec!["/admin".to_string()];
        assert!(is_path_allowed("/public", &allowed, &blocked));
        assert!(!is_path_allowed("/admin/panel", &allowed, &blocked));
    }

    #[test]
    fn allowed_prefix_whitelist_works() {
        let allowed = vec!["/blog".to_string(), "/docs/public".to_string()];
        let blocked: Vec<String> = Vec::new();
        assert!(is_path_allowed("/blog/2026", &allowed, &blocked));
        assert!(!is_path_allowed("/about", &allowed, &blocked));
    }
}

