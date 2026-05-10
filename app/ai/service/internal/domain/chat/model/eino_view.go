package model

// EinoView 表示模型推理后的领域结果。
type EinoView struct {
	Content   string          `json:"content"`
	ToolCall  ToolCallValue   `json:"tool_call"`
	Usage     TokenUsageValue `json:"usage"`
	ModelName string          `json:"model_name"`
	LatencyMs float64         `json:"latency_ms"`
	Trace     *TraceValue     `json:"trace"`
}

// Clone 返回推理结果副本。
func (result *EinoView) Clone() *EinoView {
	if result == nil {
		return nil
	}
	cloned := *result
	cloned.ToolCall = result.ToolCall.Clone()
	cloned.Usage = result.Usage.Clone()
	cloned.Trace = result.Trace.Clone()
	return &cloned
}
