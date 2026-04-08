# lersosa workspace

Rust + Burn workspace for model development and HTTP inference serving.

## Workspace layout

- `crates/lersosa-core/`: pure algorithm layer (`model`, `training`, `inference`, `runtime`).
- `crates/lersosa-serving/`: web serving layer (`api`, `dto`, `app_state`, `model_manager`, `error`).
- `crates/lersosa-crawler/`: robots-aware crawling and document persistence layer.
- `apps/inference-server/`: production server binary.
- `apps/web-crawler/`: robots-aware web crawler binary.
- `examples/`: experiment/training/inference entrypoints.

Top-level crate `lersosa` is a thin facade re-exporting `lersosa-core` so existing `examples/*.rs`
remain usable.

## Naming convention

- `model/*`: architecture names (`transformer`, `gru`, `mlp`, `bigram`).
- `training/*` and `inference/*`: task names (`*_lm`, `*_cls`, ...).

## Build

```powershell
cargo check --workspace
```

## Run experiments

```powershell
cargo run --example transformer_dispatch --features tch
cargo run --example train_transformer --features tch
# optional custom train entry config
cargo run --example train_transformer --features tch -- --config examples/config/train_transformer.toml
```

Training hyperparameters are loaded from `training_config_path` in
`examples/config/train_transformer.toml` (default points to `artifacts/transformer/config.json`).

## Run inference microservice (HTTP + gRPC)

```powershell
cargo run -p inference-server
# optional custom server config
cargo run -p inference-server -- --config apps/inference-server/config/inference.toml
```

Server network/backend/model/logging are all configured in
`apps/inference-server/config/inference.toml`.

## Test service endpoints

```powershell
Invoke-RestMethod -Method Get -Uri "http://127.0.0.1:8080/healthz"
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8080/v1/infer" -ContentType "application/json" -Body '{"tokens":[1,2,3,4]}'
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8080/v1/infer/batch" -ContentType "application/json" -Body '{"items":[{"tokens":[1,2,3]},{"tokens":[4,5]}]}'
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8080/v1/chat" -ContentType "application/json" -Body '{"messages":[{"role":"user","content":"hi"}],"max_new_tokens":8}'
# curl.exe smoke tests using payload files (avoid shell escaping issues)
curl.exe -sS -X POST "http://127.0.0.1:8080/v1/chat" -H "Content-Type: application/json" --data-binary "@apps/inference-server/smoke/payloads/chat.json"
curl.exe -sS -X POST "http://127.0.0.1:8080/v1/chat/continue" -H "Content-Type: application/json" --data-binary "@apps/inference-server/smoke/payloads/chat_continue.json"
curl.exe -sS -X POST "http://127.0.0.1:8080/v1/chat/agent-loop" -H "Content-Type: application/json" --data-binary "@apps/inference-server/smoke/payloads/chat_agent_loop.json"
curl.exe -N -sS -X POST "http://127.0.0.1:8080/v1/chat/stream" -H "Content-Type: application/json" --data-binary "@apps/inference-server/smoke/payloads/chat_stream.json"
# one-shot smoke suite (health + chat + continue + agent-loop + stream)
powershell -ExecutionPolicy Bypass -File apps/inference-server/smoke/chat_smoke_windows.ps1
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8080/v1/reload" -ContentType "application/json" -Body '{"max_tokens_per_request":8,"max_batch_size":4,"artifact_dir":"artifacts/transformer"}'
```

`/v1/chat/stream` emits SSE `delta` and `done` events with JSON payloads.
When model output contains `<tool_call>{...}</tool_call>`, `tool_call` is parsed into structured fields in chat responses.
`/v1/chat/continue` and `/v1/chat/agent-loop` enforce strict JSON Schema validation against `tools[].parameters_json`.
The default artifact is a toy model with very small `max_seq_length`, so generated text can be empty or nonsensical and continuation prompts must stay short; this still verifies the full request/response path.
Use `curl.exe` (not `curl`) in PowerShell to avoid alias/parsing surprises.

## Test gRPC endpoint

```powershell
$env:LERSOSA_GRPC_ENDPOINT = "http://127.0.0.1:50051"
cargo run -p inference-server --bin grpc_probe
```

`grpc_probe` now checks `Health`, `Chat`, and `ChatStream` in one run.

## Run robots-aware crawler

```powershell
cargo run -p web-crawler -- --config apps/web-crawler/config/crawler.toml
# safe scope validation without Mongo writes
cargo run -p web-crawler -- --dry-run --config apps/web-crawler/config/crawler.toml
# temporary runtime overrides without editing config
cargo run -p web-crawler -- --dry-run --max-pages 5 --max-depth 1 --request-delay-ms 100 --per-host-request-delay-ms 300
# repeatable path-prefix overrides
cargo run -p web-crawler -- --dry-run --allowed-path-prefix /docs --allowed-path-prefix /blog --blocked-path-prefix /docs/private
# show CLI help
cargo run -p web-crawler -- --help
```

Before running, set `mongo.uri` in `apps/web-crawler/config/crawler.toml`.
The crawler fetches and applies `https://<host>/robots.txt` first, then crawls only allowed paths and stores normalized documents in MongoDB.
CLI flags override `crawler.toml` values at runtime.

