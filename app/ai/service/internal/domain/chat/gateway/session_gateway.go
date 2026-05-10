package gateway

import (
	"context"

	"github.com/google/uuid"

	"github.com/leyramu/lersosa/app/ai/service/internal/domain/chat/model"
)

// SessionGateway Chat 会话仓储接口。
type SessionGateway interface {
	// Page 分页查询 Chat 会话。
	Page(ctx context.Context, userID uuid.UUID, title string, pageNum int64, pageSize int64) ([]*model.SessionView, int64, error)

	// Get 获取 Chat 会话。
	Get(ctx context.Context, id uuid.UUID) (*model.SessionE, error)

	// Save 保存 Chat 会话。
	Save(ctx context.Context, entity *model.SessionE) error

	// Modify 修改 Chat 会话。
	Modify(ctx context.Context, entity *model.SessionE) error

	// Remove 删除 Chat 会话。
	Remove(ctx context.Context, entities []*model.SessionE) error
}
