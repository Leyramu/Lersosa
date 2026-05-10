package model

import (
	"time"

	"github.com/google/uuid"
)

// MessageView Chat 消息查询视图。
type MessageView struct {
	ID        uuid.UUID `json:"message_id"`
	SessionID uuid.UUID `json:"session_id"`
	Role      RoleType  `json:"role"`
	Content   string    `json:"content"`
	CreatedAt time.Time `json:"created_at"`
	ModelName string    `json:"model_name,omitempty"`
	LatencyMs float64   `json:"latency_ms,omitempty"`
}

// Clone 返回消息视图副本。
func (view *MessageView) Clone() *MessageView {
	if view == nil {
		return nil
	}
	return new(*view)
}
