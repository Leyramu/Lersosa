package factory

import (
	"context"
	"lersosa/app/resource/service/internal/domain/ossconfig/gateway"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
	"time"

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

// FireSetDefaultCache 异步回填默认资源配置缓存
func (factory *DomainFactory) FireSetDefaultCache(entity *model.Entity) {
	go func() {
		ctx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
		defer cancel()

		if err := factory.redis.SetDefault(ctx, entity); err != nil {
			factory.log.Warn("缓存回填失败", "error", err)
		}
	}()
}
