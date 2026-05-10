package model

import (
	"time"

	"github.com/google/uuid"
)

// AuditMetadataValue 表示领域对象的审计元数据。
type AuditMetadataValue struct {
	CreateBy   uuid.UUID `json:"create_by"`
	CreateTime time.Time `json:"create_time"`
	UpdateBy   uuid.UUID `json:"update_by"`
	UpdateTime time.Time `json:"update_time"`
	Version    int32     `json:"version"`
}

// NewAuditMetadataValue 创建默认审计信息。
func NewAuditMetadataValue(actorID uuid.UUID, now time.Time) AuditMetadataValue {
	return AuditMetadataValue{
		CreateBy:   actorID,
		CreateTime: now,
		UpdateBy:   actorID,
		UpdateTime: now,
		Version:    1,
	}
}

// Clone 返回审计元数据副本。
func (metadata *AuditMetadataValue) Clone() AuditMetadataValue {
	if metadata == nil {
		return AuditMetadataValue{}
	}
	return *metadata
}

// Touch 更新修改元数据。
func (metadata *AuditMetadataValue) Touch(actorID uuid.UUID, now time.Time) {
	if metadata == nil {
		return
	}
	if actorID != uuid.Nil {
		metadata.UpdateBy = actorID
	}
	metadata.UpdateTime = now
	if metadata.Version <= 0 {
		metadata.Version = 1
	}
}
