package model

import (
	"strings"
	"time"

	"github.com/google/uuid"
)

// ChatA Chat 聚合根。
type ChatA struct {
	ID          uuid.UUID      `json:"session_id"`
	TenantID    uuid.UUID      `json:"tenant_id"`
	UserID      uuid.UUID      `json:"user_id"`
	Title       string         `json:"title"`
	Config      map[string]any `json:"config"`
	Status      SessionStatus  `json:"status"`
	TotalTokens int32          `json:"total_tokens"`

	// 值对象
	Trace *TraceValue        `json:"trace"`
	Audit AuditMetadataValue `json:"audit"`

	// 消息列表
	messages []*MessageE

	// 领域事件
	events []DomainEvent
}

// NewChatA 创建新的 Chat 聚合根。
func NewChatA(userID uuid.UUID, title string, config map[string]any) *ChatA {
	now := time.Now()
	return &ChatA{
		ID:          uuid.New(),
		TenantID:    uuid.Nil,
		UserID:      userID,
		Title:       title,
		Config:      config,
		Status:      SessionStatusActive,
		TotalTokens: 0,
		Trace:       nil,
		Audit:       NewAuditMetadataValue(userID, now),
		messages:    make([]*MessageE, 0),
		events:      make([]DomainEvent, 0),
	}
}

// RehydrateChatA 从聚合根状态快照与消息实体重建聚合根。
func RehydrateChatA(snapshot *ChatA, messages []*MessageE) *ChatA {
	if snapshot == nil {
		return nil
	}
	restored := &ChatA{
		ID:          snapshot.ID,
		TenantID:    snapshot.TenantID,
		UserID:      snapshot.UserID,
		Title:       snapshot.Title,
		Config:      cloneAnyMap(snapshot.Config),
		Status:      snapshot.Status,
		TotalTokens: snapshot.TotalTokens,
		Trace:       snapshot.Trace.Clone(),
		Audit:       snapshot.Audit.Clone(),
		messages:    make([]*MessageE, 0, len(messages)),
		events:      make([]DomainEvent, 0),
	}
	for _, message := range messages {
		if message == nil {
			continue
		}
		restored.messages = append(restored.messages, message.Clone())
	}
	return restored
}

// NewUserMessage 创建用户消息子实体并挂载到聚合根。
func (c *ChatA) NewUserMessage(actorID uuid.UUID, content string) (*MessageE, error) {
	if strings.TrimSpace(content) == "" {
		return nil, ErrMessageContentEmpty
	}
	message := &MessageE{
		Role:    RoleTypeUser,
		Content: content,
		Audit:   NewAuditMetadataValue(actorID, time.Now()),
	}
	if err := c.AddMessage(message); err != nil {
		return nil, err
	}
	c.Touch(actorID)
	return c.lastMessageClone(), nil
}

// NewAssistantMessage 创建助手消息子实体并挂载到聚合根。
func (c *ChatA) NewAssistantMessage(actorID uuid.UUID, eino *EinoView) (*MessageE, error) {
	if eino == nil {
		return nil, ErrInferenceResultEmpty
	}
	message := &MessageE{
		Role:      RoleTypeAssistant,
		Content:   eino.Content,
		ToolCall:  eino.ToolCall.Clone(),
		Usage:     eino.Usage.Clone(),
		ModelName: eino.ModelName,
		LatencyMs: eino.LatencyMs,
		Audit:     NewAuditMetadataValue(actorID, time.Now()),
	}
	if err := c.AddMessage(message); err != nil {
		return nil, err
	}
	c.Touch(actorID)
	if eino.Trace != nil {
		c.SetTrace(eino.Trace)
	}
	return c.lastMessageClone(), nil
}

// AddMessage 添加消息到聚合根。
func (c *ChatA) AddMessage(msg *MessageE) error {
	now := time.Now()
	if msg == nil {
		return ErrMessageNotFound
	}
	// 验证业务规则：会话必须处于激活状态
	if c.Status != SessionStatusActive {
		return ErrSessionNotActive
	}

	message := msg.Clone()

	// 设置消息的会话 ID 和序列号
	if message.ID == uuid.Nil {
		message.ID = uuid.New()
	}
	if message.TenantID == uuid.Nil {
		message.TenantID = c.TenantID
	}
	message.SessionID = c.ID
	message.SequenceID = int32(len(c.messages) + 1)
	if message.Audit.CreateBy == uuid.Nil {
		message.Audit.CreateBy = c.Audit.CreateBy
	}
	if message.Audit.UpdateBy == uuid.Nil {
		message.Audit.UpdateBy = c.Audit.UpdateBy
	}
	if message.Audit.CreateTime.IsZero() {
		message.Audit.CreateTime = now
	}
	message.Audit.Touch(message.Audit.UpdateBy, now)
	if message.Audit.Version == 0 {
		message.Audit.Version = 1
	}

	// 添加到消息列表
	c.messages = append(c.messages, message)
	c.events = append(c.events, MessageAddedEvent{
		AggregateID: c.ID,
		MessageID:   message.ID,
		Role:        message.Role,
		Content:     message.Content,
		At:          now,
	})

	// 更新聚合根的总 Token 数
	c.TotalTokens += message.Usage.Total()
	c.Audit.Touch(message.Audit.UpdateBy, now)

	return nil
}

// Messages 返回消息快照，避免外部直接持有聚合内部实体引用。
func (c *ChatA) Messages() []*MessageE {
	if c == nil || len(c.messages) == 0 {
		return nil
	}
	result := make([]*MessageE, 0, len(c.messages))
	for _, message := range c.messages {
		result = append(result, message.Clone())
	}
	return result
}

// GetLastMessages 获取最近的 N 条消息。
func (c *ChatA) GetLastMessages(count int) []*MessageE {
	if count <= 0 || len(c.messages) == 0 {
		return nil
	}

	if count >= len(c.messages) {
		return c.Messages()
	}

	start := len(c.messages) - count
	result := make([]*MessageE, 0, count)
	for _, message := range c.messages[start:] {
		result = append(result, message.Clone())
	}
	return result
}

// Archive 归档会话。
func (c *ChatA) Archive() error {
	// 验证业务规则：已删除的会话不能再次归档
	if c.Status == SessionStatusDeleted {
		return ErrSessionDeleted
	}

	c.Status = SessionStatusArchived
	c.Audit.Touch(c.Audit.UpdateBy, time.Now())
	c.events = append(c.events, SessionArchivedEvent{AggregateID: c.ID, At: c.Audit.UpdateTime})
	return nil
}

// GetMessageCount 获取消息数量。
func (c *ChatA) GetMessageCount() int {
	return len(c.messages)
}

// HasMessages 判断是否有消息.
func (c *ChatA) HasMessages() bool {
	return len(c.messages) > 0
}

// SetTrace 设置执行的追踪信息。
func (c *ChatA) SetTrace(trace *TraceValue) {
	c.Trace = trace.Clone()
	c.Audit.Touch(c.Audit.UpdateBy, time.Now())
}

// GetTrace 获取执行的追踪信息。
func (c *ChatA) GetTrace() *TraceValue {
	return c.Trace.Clone()
}

// HasTrace 判断是否有追踪信息
func (c *ChatA) HasTrace() bool {
	return c.Trace != nil && !c.Trace.IsEmpty()
}

// Touch 更新聚合根的修改审计信息。
func (c *ChatA) Touch(actorID uuid.UUID) {
	if c == nil {
		return
	}
	c.Audit.Touch(actorID, time.Now())
}

// Events 返回当前聚合累计但尚未处理的领域事件快照。
func (c *ChatA) Events() []DomainEvent {
	if c == nil || len(c.events) == 0 {
		return nil
	}
	result := make([]DomainEvent, len(c.events))
	copy(result, c.events)
	return result
}

// ClearEvents 清空已处理的领域事件。
func (c *ChatA) ClearEvents() {
	if c == nil {
		return
	}
	c.events = c.events[:0]
}

// Clone 返回聚合根深拷贝，避免聚合内部状态对外泄露。
func (c *ChatA) Clone() *ChatA {
	if c == nil {
		return nil
	}
	cloned := RehydrateChatA(c, c.messages)
	if len(c.events) > 0 {
		cloned.events = make([]DomainEvent, len(c.events))
		copy(cloned.events, c.events)
	}
	return cloned
}

func (c *ChatA) lastMessageClone() *MessageE {
	if c == nil || len(c.messages) == 0 {
		return nil
	}
	return c.messages[len(c.messages)-1].Clone()
}
