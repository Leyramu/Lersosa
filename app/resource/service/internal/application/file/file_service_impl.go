package file

import (
	"context"
	"lersosa/app/resource/service/internal/application/file/command"
	"lersosa/app/resource/service/internal/application/file/command/query"
	client "lersosa/app/resource/service/internal/client/file/api"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/client/file/dto/co"
	"lersosa/app/resource/service/internal/domain/file/ability"

	"github.com/go-kratos/kratos/v2/log"
)

// ServiceImpl 文件应用层.
type ServiceImpl struct {
	// pageQryExe 处理文件分页查询的执行器.
	pageQryExe *query.FilePageQryExe

	// getQryExe 处理文件查询的执行器.
	getQryExe *query.FileGetQryExe

	// saveCmdExe 处理文件保存的执行器.
	saveCmdExe *command.FileSaveCmdExe

	// modifyCmdExe 处理文件修改的执行器.
	modifyCmdExe *command.FileModifyCmdExe

	// removeCmdExe 处理文件删除的执行器.
	removeCmdExe *command.FileRemoveCmdExe

	// log 日志记录器.
	log *log.Helper
}

// NewServiceImpl 构造文件应用层.
func NewServiceImpl(domainService *ability.DomainService, logger log.Logger) client.ServiceI {
	return &ServiceImpl{
		// pageQryExe 处理文件分页查询的执行.
		pageQryExe: query.NewFilePageQryExe(domainService, logger),
		// getQryExe 处理文件查询的执行.
		getQryExe: query.NewFileGetQryExe(domainService, logger),
		// saveCmdExe 处理文件保存的执行.
		saveCmdExe: command.NewFileSaveCmdExe(domainService, logger),
		// modifyCmdExe 处理文件修改的执行.
		modifyCmdExe: command.NewFileModifyCmdExe(domainService, logger),
		// removeCmdExe 处理文件删除的执行.
		removeCmdExe: command.NewFileRemoveCmdExe(domainService, logger),
	}
}

// Page 实现分页查询文件方法.
func (service *ServiceImpl) Page(ctx context.Context, dto *dto.FilePageQry) ([]*co.FileCo, error) {
	return service.pageQryExe.Execute(ctx, dto)
}

// Get 实现查询文件方法.
func (service *ServiceImpl) Get(ctx context.Context, dto *dto.FileGetQry) (*co.FileCo, error) {
	return service.getQryExe.Execute(ctx, dto)
}

// Save 实现保存文件方法.
func (service *ServiceImpl) Save(ctx context.Context, dto *dto.FileSaveCmd) error {
	return service.saveCmdExe.ExecuteVoid(ctx, dto)
}

// Modify 实现修改文件方法.
func (service *ServiceImpl) Modify(ctx context.Context, dto *dto.FileModifyCmd) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, dto)
}

// Remove 实现删除文件方法.
func (service *ServiceImpl) Remove(ctx context.Context, dto *dto.FileRemoveCmd) error {
	return service.removeCmdExe.ExecuteVoid(ctx, dto)
}
