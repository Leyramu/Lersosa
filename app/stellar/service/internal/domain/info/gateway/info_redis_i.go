package gateway

import (
	"context"
	"errors"
	"lersosa/app/stellar/service/internal/domain/info/model"
)

var (
	// ErrCacheMiss 缓存未命中.
	ErrCacheMiss = errors.New("缓存未中")
)

// RedisI 星体信息缓存接口.
type RedisI interface {
	// GetList 获取列表缓存.
	GetList(ctx context.Context, condition *model.Condition) ([]*model.Entity, error)

	// SetList 设置列表缓存.
	SetList(ctx context.Context, condition *model.Condition, entities []*model.Entity) error
}
