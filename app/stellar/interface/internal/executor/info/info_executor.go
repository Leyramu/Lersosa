package info

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	"lersosa/app/stellar/interface/internal/client/info"
	"lersosa/app/stellar/interface/internal/client/status"
	"lersosa/app/stellar/interface/internal/executor/info/command"
	"lersosa/app/stellar/interface/internal/executor/info/command/query"
)

// Executor 星体信息服务执行层.
type Executor struct {
	// pageQryExe 处理星体信息分页查询的执行器.
	pageQryExe *query.InfoPageQryExe

	// getQryExe 处理星体信息查询的执行器.
	getQryExe *query.InfoGetQryExe

	// saveCmdExe 处理星体信息保存的执行器.
	saveCmdExe *command.InfoSaveCmdExe

	// modifyCmdExe 处理星体信息修改的执行器.
	modifyCmdExe *command.InfoModifyCmdExe

	// removeCmdExe 处理星体信息删除的执行器.
	removeCmdExe *command.InfoRemoveCmdExe
}

// NewExecutor 构造星体信息服务执行层.
func NewExecutor(infoClient *info.Client, statusClient *status.Client) *Executor {
	return &Executor{
		pageQryExe:   query.NewInfoPageQryExe(infoClient, statusClient),
		getQryExe:    query.NewInfoGetQryExe(infoClient),
		saveCmdExe:   command.NewInfoSaveCmdExe(infoClient),
		modifyCmdExe: command.NewInfoModifyCmdExe(infoClient),
		removeCmdExe: command.NewInfoRemoveCmdExe(infoClient),
	}
}

// Page 分页查询星体信息方法.
func (executor *Executor) Page(ctx context.Context, request *v1.InfoPageRequest) (*v1.InfoPageReply, error) {
	return executor.pageQryExe.Execute(ctx, request)
}

// Get 查询星体信息方法.
func (executor *Executor) Get(ctx context.Context, request *v1.InfoGetRequest) (*v1.InfoGetReply, error) {
	return executor.getQryExe.Execute(ctx, request)
}

// Save 保存星体信息方法.
func (executor *Executor) Save(ctx context.Context, request *v1.InfoSaveRequest) error {
	return executor.saveCmdExe.ExecuteVoid(ctx, request)
}

// Modify 修改星体信息方法.
func (executor *Executor) Modify(ctx context.Context, request *v1.InfoModifyRequest) error {
	return executor.modifyCmdExe.ExecuteVoid(ctx, request)
}

// Remove 删除星体信息方法.
func (executor *Executor) Remove(ctx context.Context, request *v1.InfoRemoveRequest) error {
	return executor.removeCmdExe.ExecuteVoid(ctx, request)
}
