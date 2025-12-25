package status

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/status"
	"lersosa/app/stellar/interface/internal/client/status"

	"lersosa/app/stellar/interface/internal/executor/status/command"
	"lersosa/app/stellar/interface/internal/executor/status/command/query"
)

// Executor 星体状态执行层.
type Executor struct {
	// pageQryExe 处理星体状态分页查询的执行器.
	pageQryExe *query.StatusPageQryExe

	// getQryExe 处理星体状态查询的执行器.
	getQryExe *query.StatusGetQryExe

	// saveCmdExe 处理星体状态保存的执行器.
	saveCmdExe *command.StatusSaveCmdExe

	// modifyCmdExe 处理星体状态修改的执行器.
	modifyCmdExe *command.StatusModifyCmdExe

	// removeCmdExe 处理星体状态删除的执行器.
	removeCmdExe *command.StatusRemoveCmdExe
}

// NewExecutor 构造星体状态执行层.
func NewExecutor(client *status.Client) *Executor {
	return &Executor{
		pageQryExe:   query.NewStatusPageQryExe(client),
		getQryExe:    query.NewStatusGetQryExe(client),
		saveCmdExe:   command.NewStatusSaveCmdExe(client),
		modifyCmdExe: command.NewStatusModifyCmdExe(client),
		removeCmdExe: command.NewStatusRemoveCmdExe(client),
	}
}

// Page 分页查询星体状态方法.
func (service *Executor) Page(ctx context.Context, request *v1.StatusPageRequest) (*v1.StatusPageReply, error) {
	return service.pageQryExe.Execute(ctx, request)
}

// Get 查询星体状态方法.
func (service *Executor) Get(ctx context.Context, request *v1.StatusGetRequest) (*v1.StatusGetReply, error) {
	return service.getQryExe.Execute(ctx, request)
}

// Save 保存星体状态方法.
func (service *Executor) Save(ctx context.Context, request *v1.StatusSaveRequest) error {
	return service.saveCmdExe.ExecuteVoid(ctx, request)
}

// Modify 修改星体状态方法.
func (service *Executor) Modify(ctx context.Context, request *v1.StatusModifyRequest) error {
	return service.modifyCmdExe.ExecuteVoid(ctx, request)
}

// Remove 删除星体状态方法.
func (service *Executor) Remove(ctx context.Context, request *v1.StatusRemoveRequest) error {
	return service.removeCmdExe.ExecuteVoid(ctx, request)
}
