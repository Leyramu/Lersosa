param(
    [string]$BaseUrl = "http://127.0.0.1:8080",
    [string]$PayloadDir = "apps/inference-server/smoke/payloads"
)
Set-StrictMode -Version Latest
$ErrorActionPreference = "Stop"
function Invoke-CurlJson {
    param(
        [string]$Path,
        [string]$PayloadFile
    )
    $fullPayload = Join-Path (Get-Location) $PayloadFile
    if (-not (Test-Path $fullPayload)) {
        throw "Payload file not found: $fullPayload"
    }
    $raw = curl.exe --fail-with-body --silent --show-error -X POST "$BaseUrl$Path" -H "Content-Type: application/json" --data-binary "@$fullPayload"
    if (-not $raw) {
        throw "Empty response from $Path"
    }
    return $raw
}
Write-Host "[1/6] Health check..."
$health = Invoke-RestMethod -Method Get -Uri "$BaseUrl/healthz"
if ($health.status -ne "ok") {
    throw "healthz status is not ok"
}
Write-Host "[2/6] /v1/chat..."
$chatRaw = Invoke-CurlJson -Path "/v1/chat" -PayloadFile (Join-Path $PayloadDir "chat.json")
$chat = $chatRaw | ConvertFrom-Json
foreach ($required in @("prompt", "reply", "prompt_tokens", "generated_tokens", "stop_reason", "backend", "tokenizer")) {
    if (-not $chat.PSObject.Properties.Name.Contains($required)) {
        throw "/v1/chat missing required key: $required"
    }
}
Write-Host "[3/6] /v1/chat/continue..."
$contRaw = Invoke-CurlJson -Path "/v1/chat/continue" -PayloadFile (Join-Path $PayloadDir "chat_continue.json")
$cont = $contRaw | ConvertFrom-Json
foreach ($required in @("prompt", "reply", "stop_reason", "backend")) {
    if (-not $cont.PSObject.Properties.Name.Contains($required)) {
        throw "/v1/chat/continue missing required key: $required"
    }
}

Write-Host "[4/6] /v1/chat/agent-loop..."
$loopRaw = Invoke-CurlJson -Path "/v1/chat/agent-loop" -PayloadFile (Join-Path $PayloadDir "chat_agent_loop.json")
$loop = $loopRaw | ConvertFrom-Json
foreach ($required in @("used_tool_call", "final_response")) {
    if (-not $loop.PSObject.Properties.Name.Contains($required)) {
        throw "/v1/chat/agent-loop missing required key: $required"
    }
}

Write-Host "[5/6] /v1/chat/stream..."
$streamPayload = Join-Path (Get-Location) (Join-Path $PayloadDir "chat_stream.json")
$streamRaw = curl.exe --fail-with-body --silent --show-error -N -X POST "$BaseUrl/v1/chat/stream" -H "Content-Type: application/json" --data-binary "@$streamPayload"
$streamText = ($streamRaw | Out-String)
if ($streamText -notmatch "event:\s*done") {
    throw "/v1/chat/stream did not emit done event"
}

Write-Host "[6/6] Summary"
Write-Host "chat.stop_reason=$($chat.stop_reason)"
Write-Host "continue.stop_reason=$($cont.stop_reason)"
Write-Host "agent_loop.final.stop_reason=$($loop.final_response.stop_reason)"
Write-Host "All smoke checks passed."