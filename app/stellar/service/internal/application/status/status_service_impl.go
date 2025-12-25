package status

import (
	"context"
	"lersosa/app/stellar/service/internal/application/status/command"
	"lersosa/app/stellar/service/internal/application/status/command/query"
	client "lersosa/app/stellar/service/internal/client/status/api"
	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/client/status/dto/co"
	"lersosa/app/stellar/service/internal/domain/status/ability"

	"github.com/go-kratos/kratos/v2/log"
)

// ServiceImpl 星体状态适配器.
type ServiceImpl struct {
	// pageQryExe 处理星体状态分页查询的执行器.
	pageQryExe *query.StatusPageQryExe

	// listQryExe 处理星体状态列表查询的执行器.
	listQryExe *query.StatusListQryExe

	// getQryExe 处理星体状态查询的执行器.
	getQryExe *query.StatusGetQryExe

	// saveCmdExe 处理星体状态保存的执行器.
	saveCmdExe *command.StatusSaveCmdExe

	// modifyCmdExe 处理星体状态修改的执行器.
	modifyCmdExe *command.StatusModifyCmdExe

	// removeCmdExe 处理星体状态删除的执行器.
	removeCmdExe *command.StatusRemoveCmdExe

	// log 日志记录器.
	log *log.Helper
}

// NewServiceImpl 构造星体状态适配器.
func NewServiceImpl(domainService *ability.DomainService, logger log.Logger) client.ServiceI {
	return &ServiceImpl{
		pageQryExe:   query.NewStatusPageQryExe(domainService, logger),
		listQryExe:   query.NewStatusListQryExe(domainService, logger),
		getQryExe:    query.NewStatusGetQryExe(domainService, logger),
		saveCmdExe:   command.NewStatusSaveCmdExe(domainService, logger),
		modifyCmdExe: command.NewStatusModifyCmdExe(domainService, logger),
		removeCmdExe: command.NewStatusRemoveCmdExe(domainService, logger),
		log:          log.NewHelper(log.With(logger, "module", "steller/status-service/application")),
	}
}

// Page 实现分页查询星体状态方法.
func (service *ServiceImpl) Page(ctx context.Context, dto *dto.StatusPageQry) ([]*co.StatusCo, error) {
	return service.pageQryExe.Execute(ctx, dto)
}

// List 实现列表查询星体状态方法.
func (service *ServiceImpl) List(ctx context.Context, dto *dto.StatusListQry) ([]*co.StatusCo, error) {
	return service.listQryExe.Execute(ctx, dto)
}

// Get 实现查询星体状态方法.
func (service *ServiceImpl) Get(ctx context.Context, dto *dto.StatusGetQry) (*co.StatusCo, error) {
	return service.getQryExe.Execute(ctx, dto)
}

// Save 实现保存星体状态方法.
func (service *ServiceImpl) Save(ctx context.Context, dto *dto.StatusSaveCmd) error {
	return service.saveCmdExe.ExecuteVoid(ctx, dto)
}

// Modify 实现修改星体状态方法.
func (service *ServiceImpl) Modify(ctx context.Context, dto *dto.StatusModifyCmd) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, dto)
}

// Remove 实现删除星体状态方法.
func (service *ServiceImpl) Remove(ctx context.Context, dto *dto.StatusRemoveCmd) error {
	return service.removeCmdExe.ExecuteVoid(ctx, dto)
}
