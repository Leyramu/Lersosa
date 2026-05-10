package model

import (
	"time"

	"github.com/google/uuid"
)

// SessionView Chat 会话查询视图。
type SessionView struct {
	ID          uuid.UUID     `json:"session_id"`
	UserID      uuid.UUID     `json:"user_id"`
	Title       string        `json:"title"`
	Status      SessionStatus `json:"status"`
	TotalTokens int32         `json:"total_tokens"`
	CreatedAt   time.Time     `json:"created_at"`
	UpdatedAt   time.Time     `json:"updated_at"`
}

// Clone 返回会话视图副本。
func (view *SessionView) Clone() *SessionView {
	if view == nil {
		return nil
	}
	cloned := *view
	return &cloned
}
