package web

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	"lersosa/app/stellar/interface/internal/executor/info"
)

// InfoController 星体信息控制层.
type InfoController struct {
	// v1.UnimplementedInfoServer 未实现星体信息控制层接口.
	v1.UnimplementedInfoServer

	// executor 星体信息服务执行者.
	executor *info.Executor
}

// NewInfoController 构造星体信息控制层.
func NewInfoController(executor *info.Executor) *InfoController {
	return &InfoController{executor: executor}
}

// PageInfo 实现分页查询星体信息方法.
func (controller *InfoController) PageInfo(ctx context.Context, request *v1.InfoPageRequest) (*v1.InfoPageReply, error) {
	page, err := controller.executor.Page(ctx, request)

	if err != nil {
		return &v1.InfoPageReply{
			Code: 500,
			Msg:  err.Error(),
			Rows: nil,
		}, err
	}

	return &v1.InfoPageReply{
		Code:  200,
		Msg:   "查询成功",
		Rows:  page.Rows,
		Total: page.Total,
	}, nil
}

// GetInfo 实现查询星体信息方法.
func (controller *InfoController) GetInfo(ctx context.Context, request *v1.InfoGetRequest) (*v1.InfoGetReply, error) {
	get, err := controller.executor.Get(ctx, request)

	if err != nil {
		return &v1.InfoGetReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.InfoGetReply{
		Code: 200,
		Msg:  "查询成功",
		Data: get.Data,
	}, nil
}

// SaveInfo 实现保存星体信息方法.
func (controller *InfoController) SaveInfo(ctx context.Context, request *v1.InfoSaveRequest) (*v1.InfoSaveReply, error) {
	if err := controller.executor.Save(ctx, request); err != nil {
		return &v1.InfoSaveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.InfoSaveReply{
		Code: 200,
		Msg:  "保存成功",
		Data: nil,
	}, nil
}

// ModifyInfo 实现修改星体信息方法.
func (controller *InfoController) ModifyInfo(ctx context.Context, request *v1.InfoModifyRequest) (*v1.InfoModifyReply, error) {
	if err := controller.executor.Modify(ctx, request); err != nil {
		return &v1.InfoModifyReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.InfoModifyReply{
		Code: 200,
		Msg:  "修改成功",
		Data: nil,
	}, nil
}

// RemoveInfo 实现删除星体信息方法.
func (controller *InfoController) RemoveInfo(ctx context.Context, request *v1.InfoRemoveRequest) (*v1.InfoRemoveReply, error) {
	if err := controller.executor.Remove(ctx, request); err != nil {
		return &v1.InfoRemoveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.InfoRemoveReply{
		Code: 200,
		Msg:  "删除成功",
		Data: nil,
	}, nil
}
