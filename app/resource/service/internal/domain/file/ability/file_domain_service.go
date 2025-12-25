package ability

import (
	"context"
	"errors"
	"lersosa/app/resource/service/internal/domain/file/gateway"
	"lersosa/app/resource/service/internal/domain/file/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// 错误信息.
var (
	// ErrNotFound 系统未找到.
	ErrNotFound = errors.New("system not found")
)

// DomainService 文件领域职能层.
type DomainService struct {
	repo gateway.RepoI
	log  *log.Helper
}

// NewDomainService 构造文件领域职能层.
func NewDomainService(repo gateway.RepoI, logger log.Logger) *DomainService {
	return &DomainService{repo: repo, log: log.NewHelper(logger)}
}

// PageFile 分页查询文件，如果出错返回异常.
func (domainService *DomainService) PageFile(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	return domainService.repo.Page(ctx, condition)
}

// ListFile 获取所有文件，如果出错返回异常.
func (domainService *DomainService) ListFile(ctx context.Context) ([]*model.Entity, error) {
	return domainService.repo.List(ctx)
}

// GetFile 获取文件，如果出错返回异常.
func (domainService *DomainService) GetFile(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	return domainService.repo.Get(ctx, id)
}

// SaveFile 保存文件，如果出错返回异常.
func (domainService *DomainService) SaveFile(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Save(ctx, entity)
}

// ModifyFile 修改文件，如果出错返回异常.
func (domainService *DomainService) ModifyFile(ctx context.Context, entity *model.Entity) error {
	return domainService.repo.Modify(ctx, entity)
}

// RemoveFile 删除文件，如果出错返回异常.
func (domainService *DomainService) RemoveFile(ctx context.Context, entities *[]model.Entity) error {
	return domainService.repo.Remove(ctx, entities)
}
