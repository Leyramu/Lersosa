package gateway

import (
	"context"
	"errors"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
)

var (
	// ErrCacheMiss 缓存未命中.
	ErrCacheMiss = errors.New("缓存未中")
)

// RedisI 资源配置基础设施层缓存接口.
type RedisI interface {
	// GetDefault 获取默认资源配置接口.
	GetDefault(ctx context.Context) (*model.Entity, error)

	// SetDefault 设置默认资源配置接口.
	SetDefault(ctx context.Context, entity *model.Entity) error
}
