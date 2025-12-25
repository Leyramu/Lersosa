package validator

import (
	"errors"
	"lersosa/app/stellar/service/internal/domain/info/gateway"
)

// IsCacheMiss 判断是否为缓存未命中（非故障）
func IsCacheMiss(err error) bool {
	return errors.Is(err, gateway.ErrCacheMiss)
}

// ShouldFallbackToDB 判断是否应降级查 DB
func ShouldFallbackToDB(err error) bool {
	// 只要不是缓存未命中，其他 Redis 错误也降级（容错）
	return err != nil && !IsCacheMiss(err)
}
