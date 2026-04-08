# web-crawler

`web-crawler` is a robots-aware crawler binary for this workspace.

## Features

- Loads and applies `robots.txt` rules before crawling page paths.
- Crawls only in-scope hosts from seed URLs.
- Supports optional path allow/block prefixes (`allowed_path_prefixes`, `blocked_path_prefixes`).
- Supports basic request throttling via `request_delay_ms`.
- Supports per-host throttling via `per_host_request_delay_ms`.
- Converts HTML to parse-friendly records (`title`, `text`, `links`, metadata).
- Saves normalized crawl records into MongoDB with URL-level upsert dedup.

## Configure

Edit `apps/web-crawler/config/crawler.toml`:

- Set `seed_urls` to your target domain(s).
- Set `mongo.uri` to your MongoDB connection string.
- Keep `respect_robots = true` for compliant crawling.
- Use `request_delay_ms` to slow down request pace.
- Use `per_host_request_delay_ms` to enforce host-level spacing.
- Use `allowed_path_prefixes` to restrict crawl scope (empty means allow all paths).
- Use `blocked_path_prefixes` to explicitly deny sensitive paths (takes priority).

Mongo indexes created automatically:

- Unique index on `url` (`uniq_url`) for idempotent reruns.
- Compound index on `domain + fetched_at` (`domain_fetched_at`) for recent-by-domain queries.

## Run

```powershell
cargo run -p web-crawler -- --config apps/web-crawler/config/crawler.toml
```

Override seed URL once:

```powershell
cargo run -p web-crawler -- --seed-url https://example.com/
```

Dry-run (no Mongo writes):

```powershell
cargo run -p web-crawler -- --dry-run --config apps/web-crawler/config/crawler.toml
```

Quick runtime overrides (no config file edits):

```powershell
cargo run -p web-crawler -- --dry-run --max-pages 5 --max-depth 1 --request-delay-ms 100 --per-host-request-delay-ms 300
```

Path prefix overrides from CLI (repeatable flags):

```powershell
cargo run -p web-crawler -- --dry-run --allowed-path-prefix /docs --allowed-path-prefix /blog --blocked-path-prefix /docs/private
```

Show built-in CLI help:

```powershell
cargo run -p web-crawler -- --help
```

CLI overrides take precedence over values in `crawler.toml`.

In dry-run mode, the app prints up to 10 sampled URLs that would have been stored.

