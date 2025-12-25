package gatewayimpl

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lersosa/app/resource/service/internal/domain/ossconfig/gateway"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database"
	"sync/atomic"
	"time"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/redis/go-redis/v9"
)

// RedisImpl 资源配置缓存实现.
var _ gateway.RedisI = (*RedisImpl)(nil)

const (
	ossConfigCacheKey = "global:sys_oss:default_config"
	cacheExpireTime   = time.Hour * 24
)

// RedisImpl 资源配置缓存.
type RedisImpl struct {
	data   *database.Data
	config atomic.Value
	log    *log.Helper
}

// NewRedisImpl 构造资源配置缓存.
func NewRedisImpl(data *database.Data, logger log.Logger) gateway.RedisI {
	return &RedisImpl{
		data: data,
		log:  log.NewHelper(logger),
	}
}

// GetDefault 获取默认资源配置.
func (r RedisImpl) GetDefault(ctx context.Context) (*model.Entity, error) {
	// 先从内存缓存中获取
	// TODO 跳过执行

	// 内存未命中，尝试从 Redis 获取
	cached, err := r.data.RedisCli.Get(ctx, ossConfigCacheKey).Result()
	if err == nil {
		if errors.Is(err, redis.Nil) {
			// Key 不存在：明确缓存未中
			return nil, gateway.ErrCacheMiss
		}
		return nil, fmt.Errorf("redis 错误：%w", err)
	}

	// 缓存存在
	if cached == "" {
		// 空结果缓存（防穿透）：返回空 slice
		return &model.Entity{}, nil
	}

	var entities []*model.Entity
	if err := json.Unmarshal([]byte(cached), &entities); err != nil {
		// 缓存数据损坏：删除脏 key，并视为 miss
		_ = r.data.RedisCli.Del(ctx, ossConfigCacheKey)
		return nil, gateway.ErrCacheMiss
	}

	// 写入内存缓存
	// TODO 跳过执行

	return entities[0], nil
}

// SetDefault 设置默认资源配置.
func (r RedisImpl) SetDefault(ctx context.Context, entity *model.Entity) error {
	// 序列化数据
	data, err := json.Marshal(entity)
	if err != nil {
		return fmt.Errorf("序列化缓存数据失败: %w", err)
	}

	// 写入 Redis
	err = r.data.RedisCli.Set(ctx, ossConfigCacheKey, string(data), cacheExpireTime).Err()
	if err != nil {
		return fmt.Errorf("写入 Redis 缓存失败 (key=%s): %w", ossConfigCacheKey, err)
	}

	return nil
}
