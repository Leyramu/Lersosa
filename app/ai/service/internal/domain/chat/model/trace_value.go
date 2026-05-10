package model

// TraceValue AI 链路追踪值对象.
type TraceValue struct {
	Type           TraceType        `json:"type"`
	InputSnapshot  string           `json:"input_snapshot"`
	OutputSnapshot string           `json:"output_snapshot"`
	Error          string           `json:"error,omitempty"`
	DurationMs     float64          `json:"duration_ms"`
	Steps          []map[string]any `json:"steps"`
}

// NewTraceValue 创建追踪值对象.
func NewTraceValue(traceType TraceType, input, output string, duration float64, steps []map[string]any) *TraceValue {
	return &TraceValue{
		Type:           traceType,
		InputSnapshot:  input,
		OutputSnapshot: output,
		DurationMs:     duration,
		Steps:          steps,
	}
}

// Clone 返回追踪值对象的深拷贝。
func (trace *TraceValue) Clone() *TraceValue {
	if trace == nil {
		return nil
	}
	cloned := *trace
	if trace.Steps != nil {
		cloned.Steps = make([]map[string]any, 0, len(trace.Steps))
		for _, step := range trace.Steps {
			cloned.Steps = append(cloned.Steps, cloneAnyMap(step))
		}
	}
	return &cloned
}

// IsEmpty 判断追踪信息是否为空.
func (trace *TraceValue) IsEmpty() bool {
	return trace == nil || trace.Type == ""
}

// HasError 判断是否有错误信息.
func (trace *TraceValue) HasError() bool {
	return trace != nil && trace.Error != ""
}
