package web

import (
	"context"
	"lersosa/app/stellar/interface/internal/executor/status"

	v1 "lersosa/api/stellar/interface/v1/status"
)

// StatusController 星体状态控制层.
type StatusController struct {
	// v1.UnimplementedStatusServer 未实现的星体状态控制层接口.
	v1.UnimplementedStatusServer

	// executor 星体状态执行者.
	executor *status.Executor
}

// NewStatusController 构造星体状态控制层.
func NewStatusController(executor *status.Executor) *StatusController {
	return &StatusController{executor: executor}
}

// PageStatus 实现分页查询星体状态方法.
func (controller *StatusController) PageStatus(ctx context.Context, request *v1.StatusPageRequest) (*v1.StatusPageReply, error) {
	page, err := controller.executor.Page(ctx, request)

	if err != nil {
		return &v1.StatusPageReply{
			Code: 500,
			Msg:  err.Error(),
			Rows: nil,
		}, err
	}

	return &v1.StatusPageReply{
		Code:  200,
		Msg:   "查询成功",
		Rows:  page.Rows,
		Total: page.Total,
	}, nil
}

// GetStatus 实现查询星体状态方法.
func (controller *StatusController) GetStatus(ctx context.Context, request *v1.StatusGetRequest) (*v1.StatusGetReply, error) {
	get, err := controller.executor.Get(ctx, request)

	if err != nil {
		return &v1.StatusGetReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.StatusGetReply{
		Code: 200,
		Msg:  "查询成功",
		Data: get.Data,
	}, nil
}

// SaveStatus 实现保存星体状态方法.
func (controller *StatusController) SaveStatus(ctx context.Context, request *v1.StatusSaveRequest) (*v1.StatusSaveReply, error) {
	err := controller.executor.Save(ctx, request)

	if err != nil {
		return &v1.StatusSaveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.StatusSaveReply{
		Code: 200,
		Msg:  "保存成功",
		Data: nil,
	}, nil
}

// ModifyStatus 实现修改星体状态方法.
func (controller *StatusController) ModifyStatus(ctx context.Context, request *v1.StatusModifyRequest) (*v1.StatusModifyReply, error) {
	if err := controller.executor.Modify(ctx, request); err != nil {
		return &v1.StatusModifyReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.StatusModifyReply{
		Code: 200,
		Msg:  "修改成功",
		Data: nil,
	}, nil
}

// RemoveStatus 实现删除星体状态方法.
func (controller *StatusController) RemoveStatus(ctx context.Context, request *v1.StatusRemoveRequest) (*v1.StatusRemoveReply, error) {
	if err := controller.executor.Remove(ctx, request); err != nil {
		return &v1.StatusRemoveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.StatusRemoveReply{
		Code: 200,
		Msg:  "删除成功",
		Data: nil,
	}, nil
}
