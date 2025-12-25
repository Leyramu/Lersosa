package gatewayimpl

import (
	"context"
	"encoding/json"
	"errors"
	"fmt"
	"lersosa/app/stellar/service/internal/domain/info/gateway"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database"
	"net/url"
	"strconv"
	"strings"
	"time"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/redis/go-redis/v9"
)

// RedisImpl 星体信息缓存实现.
var _ gateway.RedisI = (*RedisImpl)(nil)

const (
	infoListCacheTTL      = 5 * time.Minute
	infoListEmptyCacheTTL = 30
)

// RedisImpl 星体信息缓存.
type RedisImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRedisImpl 构造星体信息缓存.
func NewRedisImpl(data *database.Data, logger log.Logger) gateway.RedisI {
	return &RedisImpl{
		data: data,
		log:  log.NewHelper(log.With(logger, "module", "stellar/service/internal/infrastructure/info/gatewayimpl/redis")),
	}
}

// GetList 获取列表缓存.
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

// SetList 设置列表缓存.
func (r RedisImpl) SetList(ctx context.Context, condition *model.Condition, entities []*model.Entity) error {
	// 生成缓存 key
	cacheKey := r.buildInfoListCacheKey(condition)

	// 选择 TTL：空结果用短 TTL 防穿透，非空用长 TTL
	ttl := infoListCacheTTL
	if len(entities) == 0 {
		ttl = infoListEmptyCacheTTL
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
	parts := []string{"info_list"}

	if condition.Name != "" {
		parts = append(parts, "name="+url.QueryEscape(condition.Name))
	}
	if condition.Period != 0 {
		parts = append(parts, "period="+formatFloat(condition.Period))
	}
	if condition.DispersionMeasure != 0 {
		parts = append(parts, "dm="+formatFloat(condition.DispersionMeasure))
	}
	if condition.RaDeg != 0 {
		parts = append(parts, "ra="+formatFloat(condition.RaDeg))
	}
	if condition.DecDeg != 0 {
		parts = append(parts, "dec="+formatFloat(condition.DecDeg))
	}
	if condition.GalacticLongitude != 0 {
		parts = append(parts, "glon="+formatFloat(condition.GalacticLongitude))
	}
	if condition.GalacticLatitude != 0 {
		parts = append(parts, "glat="+formatFloat(condition.GalacticLatitude))
	}
	if condition.SurveyName != "" {
		parts = append(parts, "survey="+url.QueryEscape(condition.SurveyName))
	}

	return strings.Join(parts, ":")
}

// formatFloat 格式化浮点数为字符串.
func formatFloat(f float64) string {
	// 使用 'g' 格式保证相同数值生成相同字符串
	return strconv.FormatFloat(f, 'g', -1, 64)
}
