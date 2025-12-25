package ability

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability/validator"
	"lersosa/app/resource/service/internal/domain/ossconfig/factory"
	"lersosa/app/resource/service/internal/domain/ossconfig/gateway"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// 错误信息.
var (
	// ErrNotFound 系统未找到.
	ErrNotFound = errors.New("system not found")

	// ErrDBQueryFailed 数据库查询失败.
	ErrDBQueryFailed = errors.New("数据库查询失败")
)

// DomainService 资源配置领域职能层.
type DomainService struct {
	factory *factory.DomainFactory
	repo    gateway.RepoI
	redis   gateway.RedisI
	log     *log.Helper
}

// NewDomainService 构造资源配置领域职能层.
func NewDomainService(
	factory *factory.DomainFactory,
	repo gateway.RepoI,
	redis gateway.RedisI,
	logger log.Logger) *DomainService {
	return &DomainService{
		factory: factory,
		repo:    repo,
		redis:   redis,
		log:     log.NewHelper(log.With(logger, "module", "resource/oss-config-service/domain")),
	}
}

// PageOssConfig 分页查询资源配置，如果出错返回异常.
func (domainService *DomainService) PageOssConfig(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.Page(ctx, condition)
}

// ListOssConfig 获取所有资源配置，如果出错返回异常.
func (domainService *DomainService) ListOssConfig(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.List(ctx, condition)
}

// GetOssConfig 获取资源配置，如果出错返回异常.
func (domainService *DomainService) GetOssConfig(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	return domainService.repo.Get(ctx, id)
}

// GetDefaultOssConfig 获取默认资源配置，如果出错返回异常.
func (domainService *DomainService) GetDefaultOssConfig(ctx context.Context) (*model.Entity, error) {
	// 尝试从缓存获取（含空列表）
	if entity, err := domainService.redis.GetDefault(ctx); err == nil {
		return entity, nil
	} else if !validator.ShouldFallbackToDB(err) {
		// 不可降级的错误（如业务逻辑错误），直接返回
		return nil, err
	}

	// 回源数据库
	domainService.log.Warn("Redis 缓存异常，降级查询数据库", "error")
	entities, dbErr := domainService.repo.List(ctx, &model.Condition{Status: "0"})
	if dbErr != nil {
		return nil, fmt.Errorf("%w: %v", ErrDBQueryFailed, dbErr)
	}
	if len(entities) == 0 {
		return nil, ErrNotFound
	}

	entity := entities[0]

	// 异步回填缓存
	domainService.factory.FireSetDefaultCache(entity)

	// 返回结果
	return entity, nil
}

// SaveOssConfig 保存资源配置，如果出错返回异常.
func (domainService *DomainService) SaveOssConfig(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Save(ctx, entity)
}

// ModifyOssConfig 修改资源配置，如果出错返回异常.
func (domainService *DomainService) ModifyOssConfig(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Modify(ctx, entity)
}

// RemoveOssConfig 删除资源配置，如果出错返回异常.
func (domainService *DomainService) RemoveOssConfig(ctx context.Context, entities *[]model.Entity) error {
	return domainService.repo.Remove(ctx, entities)
}
