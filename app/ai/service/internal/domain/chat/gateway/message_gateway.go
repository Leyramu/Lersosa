package gateway

import (
	"context"

	"github.com/google/uuid"

	"github.com/leyramu/lersosa/app/ai/service/internal/domain/chat/model"
)

// MessageGateway Chat 消息仓储接口。
type MessageGateway interface {
	// Page 分页查询 Chat 消息。
	Page(ctx context.Context, sessionID uuid.UUID, role model.RoleType, modelName string, pageNum int64, pageSize int64) ([]*model.MessageView, int64, error)

	// ListBySession 按会话加载消息实体，用于命令侧聚合重建。
	ListBySession(ctx context.Context, sessionID uuid.UUID) ([]*model.MessageE, error)

	// Get 获取 Chat 消息。
	Get(ctx context.Context, id uuid.UUID) (*model.MessageE, error)

	// Save 保存 Chat 消息。
	Save(ctx context.Context, entity *model.MessageE) error

	// Modify 修改 Chat 消息。
	Modify(ctx context.Context, entity *model.MessageE) error

	// Remove 删除 Chat 消息。
	Remove(ctx context.Context, entities []*model.MessageE) error

	// RemoveBySession 按会话删除消息。
	RemoveBySession(ctx context.Context, sessionID uuid.UUID) error
}
