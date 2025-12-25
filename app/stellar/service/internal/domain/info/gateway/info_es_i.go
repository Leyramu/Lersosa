package gateway

import (
	"context"
	"lersosa/app/stellar/service/internal/domain/info/model"

	"github.com/google/uuid"
)

type ElasticSearchI interface {
	// Get 获取星体信息接口.
	Get(ctx context.Context, id uuid.UUID) (*model.Entity, error)

	// Save 保存星体信息接口.
	Save(ctx context.Context, entity *model.Entity) error

	// Modify 修改星体信息接口.
	Modify(ctx context.Context, entity *model.Entity) error

	// Remove 删除星体信息接口.
	Remove(ctx context.Context, entities *[]model.Entity) error
}
