package gatewayimpl

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lersosa/app/stellar/service/internal/domain/status/gateway"
	"lersosa/app/stellar/service/internal/domain/status/model"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database"
	"strconv"
	"strings"
	"time"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/redis/go-redis/v9"
)

// RedisImpl 星体状态缓存实现.
var _ gateway.RedisI = (*RedisImpl)(nil)

const (
	statusListCacheTTL      = 5 * time.Minute
	statusListEmptyCacheTTL = 30 * time.Second
)

// RedisImpl 星体状态缓存.
type RedisImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRedisImpl 构造星体状态缓存.
func NewRedisImpl(data *database.Data, logger log.Logger) gateway.RedisI {
	return &RedisImpl{
		data: data,
		log:  log.NewHelper(log.With(logger, "module", "stellar/service/internal/infrastructure/status/gatewayimpl/redis")),
	}
}

// GetList 获取星体状态列表缓存.
func (r RedisImpl) GetList(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 生成缓存 key
	cacheKey := r.buildInfoListCacheKey(condition)

	cached, err := r.data.RedisCli.Get(ctx, cacheKey).Result()
	if err != nil {
		if errors.Is(err, redis.Nil) {
			// Key 不存在：明确缓存未中
			return nil, gateway.ErrCacheMiss
		}
		return nil, fmt.Errorf("redis 错误：%w", err)
	}

	// 缓存存在
	if cached == "" {
		// 空结果缓存（防穿透）：返回空 slice
		return []*model.Entity{}, nil
	}

	var entities []*model.Entity
	if err := json.Unmarshal([]byte(cached), &entities); err != nil {
		// 缓存数据损坏：删除脏 key，并视为 miss
		_ = r.data.RedisCli.Del(ctx, cacheKey)
		return nil, gateway.ErrCacheMiss
	}

	return entities, nil
}

// SetList 设置星体状态列表缓存.
func (r RedisImpl) SetList(ctx context.Context, condition *model.Condition, entities []*model.Entity) error {
	// 生成缓存 key
	cacheKey := r.buildInfoListCacheKey(condition)

	// 选择 TTL：空结果用短 TTL 防穿透，非空用长 TTL
	ttl := statusListCacheTTL
	if len(entities) == 0 {
		ttl = statusListEmptyCacheTTL
	}

	// 序列化数据
	data, err := json.Marshal(entities)
	if err != nil {
		return fmt.Errorf("序列化缓存数据失败: %w", err)
	}

	// 写入 Redis
	err = r.data.RedisCli.Set(ctx, cacheKey, string(data), ttl).Err()
	if err != nil {
		return fmt.Errorf("写入 Redis 缓存失败 (key=%s): %w", cacheKey, err)
	}

	return nil
}

// 构建缓存 key.
func (r RedisImpl) buildInfoListCacheKey(condition *model.Condition) string {
	parts := []string{"status_list"}

	if condition.Flag != 0 {
		parts = append(parts, "flag="+formatInt(condition.Flag))
	}
	if condition.Check != 0 {
		parts = append(parts, "check="+formatInt(condition.Check))
	}

	return strings.Join(parts, ":")
}

// formatInt 格式化整型为字符串.
func formatInt(i int32) string {
	// 兼容 int32
	return strconv.FormatInt(int64(i), 10)
}
