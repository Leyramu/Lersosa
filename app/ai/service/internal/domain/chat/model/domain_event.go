package model

import (
	"time"

	"github.com/google/uuid"
)

// DomainEvent 定义聚合根产生的领域事件。
type DomainEvent interface {
	EventName() string
	OccurredAt() time.Time
}

// MessageAddedEvent 表示会话中新增了一条消息。
type MessageAddedEvent struct {
	AggregateID uuid.UUID
	MessageID   uuid.UUID
	Role        RoleType
	Content     string
	At          time.Time
}

// EventName 返回事件名称。
func (event MessageAddedEvent) EventName() string { return "chat.message_added" }

// OccurredAt 返回事件发生时间。
func (event MessageAddedEvent) OccurredAt() time.Time { return event.At }

// SessionArchivedEvent 表示会话已归档。
type SessionArchivedEvent struct {
	AggregateID uuid.UUID
	At          time.Time
}

// EventName 返回事件名称。
func (event SessionArchivedEvent) EventName() string { return "chat.session_archived" }

// OccurredAt 返回事件发生时间。
func (event SessionArchivedEvent) OccurredAt() time.Time { return event.At }
