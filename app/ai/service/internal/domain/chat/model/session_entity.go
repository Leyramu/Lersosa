package model

import (
	"time"

	"github.com/google/uuid"
)

// SessionE Chat 领域会话实体。
type SessionE struct {
	ID          uuid.UUID          `json:"session_id"`
	TenantID    uuid.UUID          `json:"tenant_id"`
	UserID      uuid.UUID          `json:"user_id"`
	Title       string             `json:"title"`
	Config      map[string]any     `json:"config"`
	Status      SessionStatus      `json:"status"`
	TotalTokens int32              `json:"total_tokens"`
	Trace       *TraceValue        `json:"trace"`
	Audit       AuditMetadataValue `json:"audit"`
}

// SessionCondition Chat 会话查询条件。
type SessionCondition struct {
	IsPagination bool          `json:"is_pagination"`
	PageNum      int64         `json:"page_num"`
	PageSize     int64         `json:"page_size"`
	UserID       uuid.UUID     `json:"user_id"`
	Status       SessionStatus `json:"status"`
	Title        string        `json:"title"`
	CreateBy     uuid.UUID     `json:"create_by"`
	UpdateBy     uuid.UUID     `json:"update_by"`
}

// Clone 返回会话实体的深拷贝。
func (session *SessionE) Clone() *SessionE {
	if session == nil {
		return nil
	}
	cloned := *session
	cloned.Config = cloneAnyMap(session.Config)
	cloned.Trace = session.Trace.Clone()
	cloned.Audit = session.Audit.Clone()
	return &cloned
}

// CreatedAt 返回会话创建时间。
func (session *SessionE) CreatedAt() time.Time {
	if session == nil {
		return time.Time{}
	}
	return session.Audit.CreateTime
}

// UpdatedAt 返回会话更新时间。
func (session *SessionE) UpdatedAt() time.Time {
	if session == nil {
		return time.Time{}
	}
	return session.Audit.UpdateTime
}
