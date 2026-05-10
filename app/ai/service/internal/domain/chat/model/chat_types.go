package model

// SessionStatus 会话状态类型.
type SessionStatus string

const (
	SessionStatusActive   SessionStatus = "active"
	SessionStatusArchived SessionStatus = "archived"
	SessionStatusDeleted  SessionStatus = "deleted"
)

// RoleType 消息角色类型.
type RoleType string

const (
	RoleTypeSystem    RoleType = "system"
	RoleTypeUser      RoleType = "user"
	RoleTypeAssistant RoleType = "assistant"
	RoleTypeTool      RoleType = "tool"
)

// TraceType AI 链路追踪类型.
type TraceType string

const (
	TraceTypeAgentInvoke TraceType = "agent_invoke"
	TraceTypeWorkflowRun TraceType = "workflow_run"
	TraceTypeChainCall   TraceType = "chain_call"
)
