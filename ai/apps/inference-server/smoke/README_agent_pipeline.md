# Agent Pipeline Smoke Guide

## Sync pipeline call

Use `agent_chat.json` to run the full pipeline in one request.

## Async pipeline call

1. Submit:

```powershell
Invoke-RestMethod -Method Post -Uri "http://127.0.0.1:8080/v1/agent/tasks" -ContentType "application/json" -InFile "apps/inference-server/smoke/payloads/agent_task_submit.json"
```

2. Poll status:

```powershell
Invoke-RestMethod -Method Get -Uri "http://127.0.0.1:8080/v1/agent/jobs/demo-fire-run-async"
```

Task responses now include a `stage` field (`searching`, `crawling`, `training`, `selecting`).

When status becomes `completed`, the response includes `result.best_model_path` and `result.leaderboard_path`.

If online search fails or returns no results, the pipeline automatically falls back to built-in source URLs.

