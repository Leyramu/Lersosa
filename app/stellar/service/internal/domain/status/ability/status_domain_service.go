package ability

import (
	"context"
	"errors"
	"lersosa/app/stellar/service/internal/domain/status/gateway"
	"lersosa/app/stellar/service/internal/domain/status/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// 错误信息.
var (
	// ErrNotFound 系统未找到.
	ErrNotFound = errors.New("system not found")
)

// DomainService 星体状态领域.
type DomainService struct {
	repo gateway.RepoI
	log  *log.Helper
}

// NewDomainService 构造星体状态领域.
func NewDomainService(repo gateway.RepoI, logger log.Logger) *DomainService {
	return &DomainService{
		repo: repo,
		log:  log.NewHelper(log.With(logger, "module", "steller/status-service/domain")),
	}
}

// PageStatus 分页查询星体状态，如果出错返回异常.
func (domainService *DomainService) PageStatus(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.Page(ctx, condition)
}

// ListStatus 获取所有星体状态，如果出错返回异常.
func (domainService *DomainService) ListStatus(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.List(ctx, condition)
}

// GetStatus 获取星体状态，如果出错返回异常.
func (domainService *DomainService) GetStatus(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	return domainService.repo.Get(ctx, id)
}

// SaveStatus 保存星体状态，如果出错返回异常.
func (domainService *DomainService) SaveStatus(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Save(ctx, entity)
}

// ModifyStatus 修改星体状态，如果出错返回异常.
func (domainService *DomainService) ModifyStatus(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Modify(ctx, entity)
}

// RemoveStatus 删除星体状态，如果出错返回异常.
func (domainService *DomainService) RemoveStatus(ctx context.Context, entities *[]model.Entity) error {
	return domainService.repo.Remove(ctx, entities)
}
