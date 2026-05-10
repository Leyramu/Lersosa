package model

import (
	"time"

	"github.com/google/uuid"
)

// MessageE Chat 领域消息子实体。
type MessageE struct {
	ID         uuid.UUID          `json:"message_id"`
	TenantID   uuid.UUID          `json:"tenant_id"`
	SessionID  uuid.UUID          `json:"session_id"`
	SequenceID int32              `json:"sequence_id"`
	Role       RoleType           `json:"role"`
	Content    string             `json:"content"`
	ToolCall   ToolCallValue      `json:"tool_call"`
	Usage      TokenUsageValue    `json:"usage"`
	ModelName  string             `json:"model_name"`
	LatencyMs  float64            `json:"latency_ms"`
	Audit      AuditMetadataValue `json:"audit"`
}

// MessageCondition Chat 消息查询条件。
type MessageCondition struct {
	IsPagination bool      `json:"is_pagination"`
	PageNum      int64     `json:"page_num"`
	PageSize     int64     `json:"page_size"`
	SessionID    uuid.UUID `json:"session_id"`
	Role         RoleType  `json:"role"`
	ModelName    string    `json:"model_name"`
	CreateBy     uuid.UUID `json:"create_by"`
	UpdateBy     uuid.UUID `json:"update_by"`
}

// Clone 返回消息实体的深拷贝，避免聚合内部状态泄露到外部。
func (message *MessageE) Clone() *MessageE {
	if message == nil {
		return nil
	}
	cloned := *message
	cloned.ToolCall = message.ToolCall.Clone()
	cloned.Usage = message.Usage.Clone()
	cloned.Audit = message.Audit.Clone()
	return &cloned
}

// CreatedAt 返回消息创建时间。
func (message *MessageE) CreatedAt() time.Time {
	if message == nil {
		return time.Time{}
	}
	return message.Audit.CreateTime
}

func cloneAnyMap(source map[string]any) map[string]any {
	if source == nil {
		return nil
	}
	result := make(map[string]any, len(source))
	for key, value := range source {
		result[key] = cloneAnyValue(value)
	}
	return result
}

func cloneAnySlice(source []any) []any {
	if source == nil {
		return nil
	}
	result := make([]any, len(source))
	for index, value := range source {
		result[index] = cloneAnyValue(value)
	}
	return result
}

func cloneAnyValue(value any) any {
	switch typed := value.(type) {
	case map[string]any:
		return cloneAnyMap(typed)
	case []any:
		return cloneAnySlice(typed)
	default:
		return typed
	}
}
