package ability

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/stellar/service/internal/domain/info/ability/validator"
	"lersosa/app/stellar/service/internal/domain/info/factory"
	"lersosa/app/stellar/service/internal/domain/info/gateway"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"time"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

var (
	// ErrNotFound 系统未找到.
	ErrNotFound = errors.New("系统未找到")

	// ErrDBQueryFailed 数据库查询失败.
	ErrDBQueryFailed = errors.New("数据库查询失败")
)

// DomainService 星体信息领域.
type DomainService struct {
	factory       *factory.DomainFactory
	repo          gateway.RepoI
	redis         gateway.RedisI
	elasticSearch gateway.ElasticSearchI
	log           *log.Helper
}

// NewDomainService 构造星体信息领域.
func NewDomainService(
	factory *factory.DomainFactory,
	repo gateway.RepoI,
	redis gateway.RedisI,
	elasticSearch gateway.ElasticSearchI,
	logger log.Logger,
) *DomainService {
	return &DomainService{
		factory:       factory,
		repo:          repo,
		redis:         redis,
		elasticSearch: elasticSearch,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/domain")),
	}
}

// PageInfo 分页查询星体信息，如果出错返回异常.
func (domainService *DomainService) PageInfo(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.Page(ctx, condition)
}

// ListInfo 获取所有星体信息，优先读缓存，未命中则查 DB 并回填缓存.
func (domainService *DomainService) ListInfo(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	var (
		entities []*model.Entity
		err      error
	)

	// 尝试从缓存获取（含空列表）
	if entities, err = domainService.redis.GetList(ctx, condition); err == nil {
		return entities, nil
	}

	// 校验是否需要降级
	if validator.ShouldFallbackToDB(err) {
		domainService.log.Warn("Redis 缓存异常，降级查询数据库", "error", err)
	}

	// 回源数据库
	if entities, err = domainService.repo.List(ctx, condition); err != nil {
		return nil, fmt.Errorf("%w: %v", ErrDBQueryFailed, err)
	}

	// 异步回填缓存
	domainService.factory.FireSetListCache(condition, entities)

	// 返回结果
	return entities, nil
}

// GetInfo 获取星体信息，如果出错返回异常.
func (domainService *DomainService) GetInfo(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	return domainService.repo.Get(ctx, id)
}

// SaveInfo 保存星体信息，如果出错返回异常.
func (domainService *DomainService) SaveInfo(ctx context.Context, entity *model.Entity) error {
	if err := domainService.repo.Save(ctx, entity); err != nil {
		domainService.log.WithContext(ctx).Errorf("未能保存到数据库：%v", err)
		return err
	}

	if domainService.elasticSearch != nil {
		go func() {
			esCtx, cancel := context.WithTimeout(context.Background(), 3*time.Second)
			defer cancel()

			if err := domainService.elasticSearch.Save(esCtx, entity); err != nil {
				domainService.log.Errorf("异步同步与 ES 失败，ID：%s，错误：%v", entity.ID, err)
				// TODO: 可选：发送到重试队列（如 Redis Stream）
			} else {
				domainService.log.Debugf("异步同步到ES成功，ID：%s", entity.ID)
			}
		}()
	}

	return nil
}

// ModifyInfo 修改星体信息，如果出错返回异常.
func (domainService *DomainService) ModifyInfo(ctx context.Context, entity *model.Entity) error {
	if err := domainService.repo.Modify(ctx, entity); err != nil {
		domainService.log.WithContext(ctx).Errorf("数据库中未能修改：%v", err)
		return err
	}

	if domainService.elasticSearch != nil {
		if err := domainService.elasticSearch.Modify(ctx, entity); err != nil {
			domainService.log.WithContext(ctx).Errorf("修改后未能同步到 ES，ID：%s， 错误：%v", entity.ID.String(), err)
			// TODO: 加入重试队列或告警
		} else {
			domainService.log.WithContext(ctx).Debugf("修改后成功同步到 ES，ID：%s", entity.ID.String())
		}
	}

	return nil
}

// RemoveInfo 删除星体信息，如果出错返回异常.
func (domainService *DomainService) RemoveInfo(ctx context.Context, entities *[]model.Entity) error {
	if entities == nil || len(*entities) == 0 {
		return nil
	}

	if err := domainService.repo.Remove(ctx, entities); err != nil {
		domainService.log.WithContext(ctx).Errorf("未能从数据库中移除：%v", err)
		return err
	}

	if domainService.elasticSearch != nil {
		if err := domainService.elasticSearch.Remove(ctx, entities); err != nil {
			domainService.log.WithContext(ctx).Errorf("未能从 ES 中移除：%v", err)
			// TODO: 加入重试队列或告警
		} else {
			domainService.log.WithContext(ctx).Debugf("成功从 ES 中移除")
		}
	}
	return nil
}
