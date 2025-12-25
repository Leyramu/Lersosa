package web

import (
	"context"
	"lersosa/app/resource/interface/internal/executor/ossconfig"

	v1 "lersosa/api/resource/interface/v1/ossconfig"
)

// OssConfigController 资源配置控制层.
type OssConfigController struct {
	// v1.UnimplementedOssConfigServer 未实现的资源配置控制层接口.
	v1.UnimplementedOssConfigServer

	// executor 资源配置执行者.
	executor *ossconfig.Executor
}

// NewOssConfigController 构造资源配置控制层.
func NewOssConfigController(executor *ossconfig.Executor) *OssConfigController {
	return &OssConfigController{executor: executor}
}

// PageOssConfig 实现分页查询资源配置方法.
func (controller *OssConfigController) PageOssConfig(ctx context.Context, request *v1.OssConfigPageRequest) (*v1.OssConfigPageReply, error) {
	page, err := controller.executor.Page(ctx, request)

	if err != nil {
		return &v1.OssConfigPageReply{
			Code: 500,
			Msg:  err.Error(),
			Rows: nil,
		}, err
	}

	return &v1.OssConfigPageReply{
		Code:  200,
		Msg:   "查询成功",
		Rows:  page.Rows,
		Total: page.Total,
	}, nil
}

// GetOssConfig 实现查询资源配置方法.
func (controller *OssConfigController) GetOssConfig(ctx context.Context, request *v1.OssConfigGetRequest) (*v1.OssConfigGetReply, error) {
	get, err := controller.executor.Get(ctx, request)

	if err != nil {
		return &v1.OssConfigGetReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.OssConfigGetReply{
		Code: 200,
		Msg:  "查询成功",
		Data: get.Data,
	}, nil
}

// SaveOssConfig 实现保存资源配置方法.
func (controller *OssConfigController) SaveOssConfig(ctx context.Context, request *v1.OssConfigSaveRequest) (*v1.OssConfigSaveReply, error) {
	err := controller.executor.Save(ctx, request)

	if err != nil {
		return &v1.OssConfigSaveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.OssConfigSaveReply{
		Code: 200,
		Msg:  "保存成功",
		Data: nil,
	}, nil
}

// ModifyOssConfig 实现修改资源配置方法.
func (controller *OssConfigController) ModifyOssConfig(ctx context.Context, request *v1.OssConfigModifyRequest) (*v1.OssConfigModifyReply, error) {
	if err := controller.executor.Modify(ctx, request); err != nil {
		return &v1.OssConfigModifyReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.OssConfigModifyReply{
		Code: 200,
		Msg:  "修改成功",
		Data: nil,
	}, nil
}

// RemoveOssConfig 实现删除资源配置方法.
func (controller *OssConfigController) RemoveOssConfig(ctx context.Context, request *v1.OssConfigRemoveRequest) (*v1.OssConfigRemoveReply, error) {
	if err := controller.executor.Remove(ctx, request); err != nil {
		return &v1.OssConfigRemoveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.OssConfigRemoveReply{
		Code: 200,
		Msg:  "删除成功",
		Data: nil,
	}, nil
}
