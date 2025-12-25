package info

import (
	"context"
	"lersosa/app/stellar/service/internal/domain/info/ability"

	"lersosa/app/stellar/service/internal/application/info/command"
	"lersosa/app/stellar/service/internal/application/info/command/query"
	client "lersosa/app/stellar/service/internal/client/info/api"
	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/client/info/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// ServiceImpl 星体信息应用层.
type ServiceImpl struct {
	// pageQryExe 处理星体信息分页查询的执行器.
	pageQryExe *query.InfoPageQryExe

	// listQryExe 处理星体信息列表查询的执行器.
	listQryExe *query.InfoListQryExe

	// getQryExe 处理星体信息查询的执行器.
	getQryExe *query.InfoGetQryExe

	// saveCmdExe 处理星体信息保存的执行器.
	saveCmdExe *command.InfoSaveCmdExe

	// modifyCmdExe 处理星体信息修改的执行器.
	modifyCmdExe *command.InfoModifyCmdExe

	// removeCmdExe 处理星体信息删除的执行器.
	removeCmdExe *command.InfoRemoveCmdExe

	// log 日志记录器.
	log *log.Helper
}

// NewServiceImpl 构造星体信息应用层.
func NewServiceImpl(domainService *ability.DomainService, logger log.Logger) client.ServiceI {
	return &ServiceImpl{
		pageQryExe:   query.NewInfoPageQryExe(domainService, logger),
		listQryExe:   query.NewInfoListQryExe(domainService, logger),
		getQryExe:    query.NewInfoGetQryExe(domainService, logger),
		saveCmdExe:   command.NewInfoSaveCmdExe(domainService, logger),
		modifyCmdExe: command.NewInfoModifyCmdExe(domainService, logger),
		removeCmdExe: command.NewInfoRemoveCmdExe(domainService, logger),
		log:          log.NewHelper(log.With(logger, "module", "steller/info-service/application")),
	}
}

// Page 实现分页查询星体信息方法.
func (service *ServiceImpl) Page(ctx context.Context, dto *dto.InfoPageQry) ([]*co.InfoCo, error) {
	return service.pageQryExe.Execute(ctx, dto)
}

// List 实现查询星体信息列表方法.
func (service *ServiceImpl) List(ctx context.Context, dto *dto.InfoListQry) ([]*co.InfoCo, error) {
	return service.listQryExe.Execute(ctx, dto)
}

// Get 实现查询星体信息方法.
func (service *ServiceImpl) Get(ctx context.Context, dto *dto.InfoGetQry) (*co.InfoCo, error) {
	return service.getQryExe.Execute(ctx, dto)
}

// Save 实现保存星体信息方法.
func (service *ServiceImpl) Save(ctx context.Context, dto *dto.InfoSaveCmd) error {
	return service.saveCmdExe.ExecuteVoid(ctx, dto)
}

// Modify 实现修改星体信息方法.
func (service *ServiceImpl) Modify(ctx context.Context, dto *dto.InfoModifyCmd) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, dto)
}

// Remove 实现删除星体信息方法.
func (service *ServiceImpl) Remove(ctx context.Context, dto *dto.InfoRemoveCmd) error {
	return service.removeCmdExe.ExecuteVoid(ctx, dto)
}
