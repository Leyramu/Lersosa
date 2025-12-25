package ossconfig

import (
	"context"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"

	"lersosa/app/resource/service/internal/application/ossconfig/command"
	"lersosa/app/resource/service/internal/application/ossconfig/command/query"
	client "lersosa/app/resource/service/internal/client/ossconfig/api"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// ServiceImpl 资源配置应用层.
type ServiceImpl struct {
	// pageQryExe 处理资源配置分页查询的执行器.
	pageQryExe *query.OssConfigPageQryExe

	// getQryExe 处理资源配置查询的执行器.
	getQryExe *query.OssConfigGetQryExe

	// getDefaultQryExe 处理资源配置查询默认的执行器.
	getDefaultQryExe *query.OssConfigGetDefaultQryExe

	// saveCmdExe 处理资源配置保存的执行器.
	saveCmdExe *command.OssConfigSaveCmdExe

	// modifyCmdExe 处理资源配置修改的执行器.
	modifyCmdExe *command.OssConfigModifyCmdExe

	// removeCmdExe 处理资源配置删除的执行器.
	removeCmdExe *command.OssConfigRemoveCmdExe

	// log 日志记录器.
	log *log.Helper
}

// NewServiceImpl 构造资源配置应用层.
func NewServiceImpl(domainService *ability.DomainService, logger log.Logger) client.ServiceI {
	return &ServiceImpl{
		// pageQryExe 处理星信息态分页查询的执行.
		pageQryExe: query.NewOssConfigPageQryExe(domainService, logger),
		// getQryExe 处理星信息态查询的执行.
		getQryExe: query.NewOssConfigGetQryExe(domainService, logger),
		// getDefaultQryExe 处理星信息态查询默认的执行.
		getDefaultQryExe: query.NewOssConfigGetDefaultQryExe(domainService, logger),
		// saveCmdExe 处理星信息态保存的执行.
		saveCmdExe: command.NewOssConfigSaveCmdExe(domainService, logger),
		// modifyCmdExe 处理星信息态修改的执行.
		modifyCmdExe: command.NewOssConfigModifyCmdExe(domainService, logger),
		// removeCmdExe 处理星信息态删除的执行.
		removeCmdExe: command.NewOssConfigRemoveCmdExe(domainService, logger),
	}
}

// Page 实现分页查询资源配置方法.
func (service *ServiceImpl) Page(ctx context.Context, dto *dto.OssConfigPageQry) ([]*co.OssConfigCo, error) {
	return service.pageQryExe.Execute(ctx, dto)
}

// Get 实现查询资源配置方法.
func (service *ServiceImpl) Get(ctx context.Context, dto *dto.OssConfigGetQry) (*co.OssConfigCo, error) {
	return service.getQryExe.Execute(ctx, dto)
}

// GetDefault 实现查询默认资源配置方法.
func (service *ServiceImpl) GetDefault(ctx context.Context, dto *dto.OssConfigGetDefaultQry) (*co.OssConfigCo, error) {
	return service.getDefaultQryExe.Execute(ctx, dto)
}

// Save 实现保存资源配置方法.
func (service *ServiceImpl) Save(ctx context.Context, dto *dto.OssConfigSaveCmd) error {
	return service.saveCmdExe.ExecuteVoid(ctx, dto)
}

// Modify 实现修改资源配置方法.
func (service *ServiceImpl) Modify(ctx context.Context, dto *dto.OssConfigModifyCmd) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, dto)
}

// Remove 实现删除资源配置方法.
func (service *ServiceImpl) Remove(ctx context.Context, dto *dto.OssConfigRemoveCmd) error {
	return service.removeCmdExe.ExecuteVoid(ctx, dto)
}
