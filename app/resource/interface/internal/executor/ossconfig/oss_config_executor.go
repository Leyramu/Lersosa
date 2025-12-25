package ossconfig

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/ossconfig"

	"lersosa/app/resource/interface/internal/executor/ossconfig/command"
	"lersosa/app/resource/interface/internal/executor/ossconfig/command/query"
)

// Executor 资源配置执行层.
type Executor struct {
	// pageQryExe 处理资源配置分页查询的执行器.
	pageQryExe *query.OssConfigPageQryExe

	// getQryExe 处理资源配置查询的执行器.
	getQryExe *query.OssConfigGetQryExe

	// saveCmdExe 处理资源配置保存的执行器.
	saveCmdExe *command.OssConfigSaveCmdExe

	// modifyCmdExe 处理资源配置修改的执行器.
	modifyCmdExe *command.OssConfigModifyCmdExe

	// removeCmdExe 处理资源配置删除的执行器.
	removeCmdExe *command.OssConfigRemoveCmdExe
}

// NewExecutor 构造资源配置执行层.
func NewExecutor(client *ossconfig.Client) *Executor {
	return &Executor{
		// pageQryExe 处理星信息态分页查询的执行.
		pageQryExe: query.NewOssConfigPageQryExe(client),
		// getQryExe 处理星信息态查询的执行.
		getQryExe: query.NewOssConfigGetQryExe(client),
		// saveCmdExe 处理星信息态保存的执行.
		saveCmdExe: command.NewOssConfigSaveCmdExe(client),
		// modifyCmdExe 处理星信息态修改的执行.
		modifyCmdExe: command.NewOssConfigModifyCmdExe(client),
		// removeCmdExe 处理星信息态删除的执行.
		removeCmdExe: command.NewOssConfigRemoveCmdExe(client),
	}
}

// Page 实现分页查询资源配置方法.
func (service *Executor) Page(ctx context.Context, request *v1.OssConfigPageRequest) (*v1.OssConfigPageReply, error) {
	return service.pageQryExe.Execute(ctx, request)
}

// Get 实现查询资源配置方法.
func (service *Executor) Get(ctx context.Context, request *v1.OssConfigGetRequest) (*v1.OssConfigGetReply, error) {
	return service.getQryExe.Execute(ctx, request)
}

// Save 实现保存资源配置方法.
func (service *Executor) Save(ctx context.Context, request *v1.OssConfigSaveRequest) error {
	return service.saveCmdExe.ExecuteVoid(ctx, request)
}

// Modify 实现修改资源配置方法.
func (service *Executor) Modify(ctx context.Context, request *v1.OssConfigModifyRequest) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, request)
}

// Remove 实现删除资源配置方法.
func (service *Executor) Remove(ctx context.Context, request *v1.OssConfigRemoveRequest) error {
	return service.removeCmdExe.ExecuteVoid(ctx, request)
}
