package model

import "strings"

// ToolCallValue 表示工具调用相关信息。
type ToolCallValue struct {
	Name   string         `json:"tool_name"`
	Args   map[string]any `json:"tool_args"`
	Result map[string]any `json:"tool_result"`
}

// Clone 返回工具调用值对象副本。
func (tool ToolCallValue) Clone() ToolCallValue {
	return ToolCallValue{
		Name:   tool.Name,
		Args:   cloneAnyMap(tool.Args),
		Result: cloneAnyMap(tool.Result),
	}
}

// IsEmpty 判断是否没有工具调用信息。
func (tool ToolCallValue) IsEmpty() bool {
	return strings.TrimSpace(tool.Name) == "" && len(tool.Args) == 0 && len(tool.Result) == 0
}
