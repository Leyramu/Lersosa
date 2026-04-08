use lersosa_crawler::{
    CrawlerConfig, WebCrawler,
    repository::{DocumentRepository, RecordingRepository, mongo::MongoRepository},
};

const DEFAULT_CONFIG_PATH: &str = "apps/web-crawler/config/crawler.toml";
const DRY_RUN_SAMPLE_LIMIT: usize = 10;

#[derive(Debug, Clone)]
struct CliArgs {
    config_path: String,
    seed_url: Option<String>,
    dry_run: bool,
    show_help: bool,
    max_pages: Option<usize>,
    max_depth: Option<usize>,
    request_delay_ms: Option<u64>,
    per_host_request_delay_ms: Option<u64>,
    allowed_path_prefixes: Vec<String>,
    blocked_path_prefixes: Vec<String>,
}

fn parse_cli_args<I>(args: I) -> CliArgs
where
    I: IntoIterator<Item = String>,
{
    let mut cli = CliArgs {
        config_path: DEFAULT_CONFIG_PATH.to_string(),
        seed_url: None,
        dry_run: false,
        show_help: false,
        max_pages: None,
        max_depth: None,
        request_delay_ms: None,
        per_host_request_delay_ms: None,
        allowed_path_prefixes: Vec::new(),
        blocked_path_prefixes: Vec::new(),
    };

    let mut args = args.into_iter();
    while let Some(arg) = args.next() {
        match arg.as_str() {
            "--config" => {
                if let Some(path) = args.next() {
                    cli.config_path = path;
                } else {
                    panic!("--config requires a file path value");
                }
            }
            "--seed-url" => {
                if let Some(url) = args.next() {
                    cli.seed_url = Some(url);
                } else {
                    panic!("--seed-url requires a URL value");
                }
            }
            "--dry-run" => cli.dry_run = true,
            "--help" | "-h" => cli.show_help = true,
            "--max-pages" => {
                let value = args
                    .next()
                    .unwrap_or_else(|| panic!("--max-pages requires a numeric value"));
                cli.max_pages = Some(parse_usize_arg("--max-pages", &value));
            }
            "--max-depth" => {
                let value = args
                    .next()
                    .unwrap_or_else(|| panic!("--max-depth requires a numeric value"));
                cli.max_depth = Some(parse_usize_arg("--max-depth", &value));
            }
            "--request-delay-ms" => {
                let value = args
                    .next()
                    .unwrap_or_else(|| panic!("--request-delay-ms requires a numeric value"));
                cli.request_delay_ms = Some(parse_u64_arg("--request-delay-ms", &value));
            }
            "--per-host-request-delay-ms" => {
                let value = args.next().unwrap_or_else(|| {
                    panic!("--per-host-request-delay-ms requires a numeric value")
                });
                cli.per_host_request_delay_ms =
                    Some(parse_u64_arg("--per-host-request-delay-ms", &value));
            }
            "--allowed-path-prefix" => {
                let value = args.next().unwrap_or_else(|| {
                    panic!("--allowed-path-prefix requires a path prefix value")
                });
                cli.allowed_path_prefixes.push(value);
            }
            "--blocked-path-prefix" => {
                let value = args.next().unwrap_or_else(|| {
                    panic!("--blocked-path-prefix requires a path prefix value")
                });
                cli.blocked_path_prefixes.push(value);
            }
            _ => panic!("unknown argument: {arg}. Use --help to see supported options."),
        }
    }

    cli
}

fn print_help() {
    println!(
        "web-crawler options:\n\
  --config <path>                     Path to crawler TOML config\n\
  --seed-url <url>                    Override seed_urls with a single URL\n\
  --dry-run                           Run crawl without MongoDB writes\n\
  --max-pages <n>                     Override max_pages\n\
  --max-depth <n>                     Override max_depth\n\
  --request-delay-ms <n>              Override global delay between requests\n\
  --per-host-request-delay-ms <n>     Override minimum interval per host\n\
  --allowed-path-prefix <path>        Add allowlist prefix (repeatable)\n\
  --blocked-path-prefix <path>        Add blocklist prefix (repeatable)\n\
  --help, -h                          Show this help text"
    );
}

fn read_cli_args() -> CliArgs {
    parse_cli_args(std::env::args().skip(1))
}

fn parse_usize_arg(flag: &str, value: &str) -> usize {
    value
        .parse::<usize>()
        .unwrap_or_else(|_| panic!("{flag} expects an unsigned integer, got '{value}'"))
}

fn parse_u64_arg(flag: &str, value: &str) -> u64 {
    value
        .parse::<u64>()
        .unwrap_or_else(|_| panic!("{flag} expects an unsigned integer, got '{value}'"))
}

fn apply_cli_overrides(config: &mut CrawlerConfig, args: &CliArgs) {
    if let Some(seed_url) = args.seed_url.clone() {
        config.seed_urls = vec![seed_url];
    }
    if let Some(max_pages) = args.max_pages {
        config.max_pages = max_pages;
    }
    if let Some(max_depth) = args.max_depth {
        config.max_depth = max_depth;
    }
    if let Some(request_delay_ms) = args.request_delay_ms {
        config.request_delay_ms = request_delay_ms;
    }
    if let Some(per_host_request_delay_ms) = args.per_host_request_delay_ms {
        config.per_host_request_delay_ms = per_host_request_delay_ms;
    }
    if !args.allowed_path_prefixes.is_empty() {
        config.allowed_path_prefixes = args.allowed_path_prefixes.clone();
    }
    if !args.blocked_path_prefixes.is_empty() {
        config.blocked_path_prefixes = args.blocked_path_prefixes.clone();
    }
}

async fn run_with_repository<R: DocumentRepository>(
    config: &CrawlerConfig,
    repository: R,
) -> lersosa_crawler::Result<lersosa_crawler::CrawlSummary> {
    let crawler = WebCrawler::new(config.clone(), repository)?;
    crawler.run().await
}

#[tokio::main]
async fn main() {
    let args = read_cli_args();

    if args.show_help {
        print_help();
        return;
    }

    let mut config = CrawlerConfig::from_file(&args.config_path)
        .unwrap_or_else(|err| panic!("failed to load crawler config: {err}"));

    apply_cli_overrides(&mut config, &args);

    config
        .validate()
        .unwrap_or_else(|err| panic!("invalid crawler config: {err}"));

    tracing_subscriber::fmt()
        .with_env_filter("info,lersosa_crawler=info")
        .init();

    tracing::info!(
        "crawler starting with {} seed urls, max_pages={}, max_depth={}, request_delay_ms={}, per_host_request_delay_ms={}, user_agent='{}', dry_run={}",
        config.seed_urls.len(),
        config.max_pages,
        config.max_depth,
        config.request_delay_ms,
        config.per_host_request_delay_ms,
        config.user_agent,
        args.dry_run
    );

    let (summary, dry_run_urls) = if args.dry_run {
        tracing::info!("dry-run enabled, crawler will not write to MongoDB");
        let repository = RecordingRepository::new(DRY_RUN_SAMPLE_LIMIT);
        let summary = run_with_repository(&config, repository.clone())
            .await
            .unwrap_or_else(|err| panic!("crawler failed in dry-run mode: {err}"));
        (summary, Some(repository.sampled_urls()))
    } else {
        let repository = MongoRepository::connect(&config.mongo)
            .await
            .unwrap_or_else(|err| panic!("failed to connect MongoDB: {err}"));
        let summary = run_with_repository(&config, repository)
            .await
            .unwrap_or_else(|err| panic!("crawler failed: {err}"));
        (summary, None)
    };

    tracing::info!(
        "crawler finished: crawled_pages={}, skipped_by_robots={}, skipped_by_path_rules={}, skipped_out_of_scope={}, skipped_errors={}",
        summary.crawled_pages,
        summary.skipped_by_robots,
        summary.skipped_by_path_rules,
        summary.skipped_out_of_scope,
        summary.skipped_errors
    );

    if let Some(urls) = dry_run_urls {
        if urls.is_empty() {
            tracing::info!("dry-run sample: no pages were accepted for storage");
        } else {
            tracing::info!("dry-run sample: showing up to {} URLs", DRY_RUN_SAMPLE_LIMIT);
            for (idx, url) in urls.iter().enumerate() {
                tracing::info!("dry-run sample [{}]: {}", idx + 1, url);
            }
        }
    }
}

#[cfg(test)]
mod tests {
    use super::parse_cli_args;
    use lersosa_crawler::CrawlerConfig;

    #[test]
    fn parse_dry_run_and_seed_url() {
        let args = vec![
            "--dry-run".to_string(),
            "--seed-url".to_string(),
            "https://example.com/docs".to_string(),
        ];

        let cli = parse_cli_args(args);
        assert!(cli.dry_run);
        assert_eq!(cli.seed_url.as_deref(), Some("https://example.com/docs"));
    }

    #[test]
    fn parse_config_override() {
        let args = vec!["--config".to_string(), "custom.toml".to_string()];
        let cli = parse_cli_args(args);
        assert_eq!(cli.config_path, "custom.toml");
        assert!(!cli.dry_run);
    }

    #[test]
    fn parse_runtime_overrides() {
        let args = vec![
            "--max-pages".to_string(),
            "3".to_string(),
            "--max-depth".to_string(),
            "1".to_string(),
            "--request-delay-ms".to_string(),
            "50".to_string(),
            "--per-host-request-delay-ms".to_string(),
            "120".to_string(),
            "--allowed-path-prefix".to_string(),
            "/docs".to_string(),
            "--allowed-path-prefix".to_string(),
            "/blog".to_string(),
            "--blocked-path-prefix".to_string(),
            "/admin".to_string(),
        ];

        let cli = parse_cli_args(args);
        assert_eq!(cli.max_pages, Some(3));
        assert_eq!(cli.max_depth, Some(1));
        assert_eq!(cli.request_delay_ms, Some(50));
        assert_eq!(cli.per_host_request_delay_ms, Some(120));
        assert_eq!(cli.allowed_path_prefixes, vec!["/docs".to_string(), "/blog".to_string()]);
        assert_eq!(cli.blocked_path_prefixes, vec!["/admin".to_string()]);
    }

    #[test]
    fn apply_cli_overrides_updates_config() {
        let mut config = CrawlerConfig {
            seed_urls: vec!["https://example.com".to_string()],
            user_agent: "UA".to_string(),
            max_pages: 20,
            max_depth: 5,
            request_timeout_secs: 10,
            request_delay_ms: 200,
            per_host_request_delay_ms: 300,
            respect_robots: true,
            allowed_path_prefixes: Vec::new(),
            blocked_path_prefixes: Vec::new(),
            mongo: lersosa_crawler::config::MongoConfig {
                uri: String::new(),
                database: "lersosa".to_string(),
                collection: "crawled_pages".to_string(),
            },
        };

        let cli = parse_cli_args(vec![
            "--seed-url".to_string(),
            "https://bing.com".to_string(),
            "--max-pages".to_string(),
            "8".to_string(),
            "--max-depth".to_string(),
            "2".to_string(),
            "--request-delay-ms".to_string(),
            "40".to_string(),
            "--per-host-request-delay-ms".to_string(),
            "90".to_string(),
            "--allowed-path-prefix".to_string(),
            "/docs".to_string(),
            "--blocked-path-prefix".to_string(),
            "/private".to_string(),
        ]);

        super::apply_cli_overrides(&mut config, &cli);

        assert_eq!(config.seed_urls, vec!["https://bing.com".to_string()]);
        assert_eq!(config.max_pages, 8);
        assert_eq!(config.max_depth, 2);
        assert_eq!(config.request_delay_ms, 40);
        assert_eq!(config.per_host_request_delay_ms, 90);
        assert_eq!(config.allowed_path_prefixes, vec!["/docs".to_string()]);
        assert_eq!(config.blocked_path_prefixes, vec!["/private".to_string()]);
    }

    #[test]
    fn parse_help_flag() {
        let cli = parse_cli_args(vec!["--help".to_string()]);
        assert!(cli.show_help);
    }
}

