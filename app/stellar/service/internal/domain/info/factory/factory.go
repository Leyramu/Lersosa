package factory

import (
	"context"
	"time"

	"lersosa/app/stellar/service/internal/domain/info/gateway"
	"lersosa/app/stellar/service/internal/domain/info/model"

	"github.com/go-kratos/kratos/v2/log"
)

// DomainFactory 领域工厂.
type DomainFactory struct {
	redis gateway.RedisI
	log   *log.Helper
}

// NewDomainFactory 构造领域工厂.
func NewDomainFactory(redis gateway.RedisI, logger log.Logger) *DomainFactory {
	return &DomainFactory{
		redis: redis,
		log:   log.NewHelper(log.With(logger, "module", "steller/info-service/domain/factory")),
	}
}

// FireSetListCache 异步回填列表缓存
func (factory *DomainFactory) FireSetListCache(condition *model.Condition, entities []*model.Entity) {
	go func() {
		ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
		defer cancel()

		if err := factory.redis.SetList(ctx, condition, entities); err != nil {
			factory.log.Warn("缓存回填失败", "error", err)
		}
	}()
}
