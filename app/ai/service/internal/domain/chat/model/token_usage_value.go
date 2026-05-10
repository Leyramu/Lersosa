package model

// TokenUsageValue 表示一次消息或推理产生的 token 消耗。
type TokenUsageValue struct {
	PromptTokens     int32 `json:"prompt_tokens"`
	CompletionTokens int32 `json:"completion_tokens"`
}

// Total 返回总 token 数。
func (usage TokenUsageValue) Total() int32 {
	return usage.PromptTokens + usage.CompletionTokens
}

// Clone 返回 token 使用快照。
func (usage TokenUsageValue) Clone() TokenUsageValue {
	return usage
}
