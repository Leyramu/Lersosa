package web

import (
	"context"

	v1 "lersosa/api/resource/service/v1/ossconfig"
	ossConfig "lersosa/app/resource/service/internal/client/ossconfig/api"

	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"
)

// OssConfigAdapter 资源配置适配层.
type OssConfigAdapter struct {
	// v1.UnimplementedOssConfigServer 未实现的资源配置适配层接口.
	v1.UnimplementedOssConfigServer

	// service 资源配置客户端层接口.
	service ossConfig.ServiceI
}

// NewOssConfigAdapter 构造资源配置适配层.
func NewOssConfigAdapter(service ossConfig.ServiceI) *OssConfigAdapter {
	return &OssConfigAdapter{service: service}
}

// PageOssConfig 实现分页查询资源配置方法.
func (adapter *OssConfigAdapter) PageOssConfig(ctx context.Context, request *v1.OssConfigPageRequest) (*v1.OssConfigPageReply, error) {
	page, err := adapter.service.Page(ctx, dto.NewOssConfigPageQry(request))

	if err != nil {
		return &v1.OssConfigPageReply{Rows: nil}, err
	}

	var rows []*v1.OssConfigPageReply_OssConfig
	for _, item := range page {
		rows = append(rows, co.NewOssConfigPageCo(item))
	}

	return &v1.OssConfigPageReply{Rows: rows}, nil
}

// GetOssConfig 实现查询资源配置方法.
func (adapter *OssConfigAdapter) GetOssConfig(ctx context.Context, request *v1.OssConfigGetRequest) (*v1.OssConfigGetReply, error) {
	get, err := adapter.service.Get(ctx, dto.NewOssConfigGetQry(request))

	if err != nil {
		return &v1.OssConfigGetReply{}, err
	}

	return co.NewOssConfigGetCo(get), nil
}

// GetDefaultOssConfig 实现获取默认资源配置方法.
func (adapter *OssConfigAdapter) GetDefaultOssConfig(ctx context.Context, request *v1.OssConfigGetDefaultRequest) (*v1.OssConfigGetDefaultReply, error) {
	get, err := adapter.service.GetDefault(ctx, dto.NewOssConfigGetDefaultQry(request))

	if err != nil {
		return &v1.OssConfigGetDefaultReply{}, err
	}

	return co.NewOssConfigGetDefaultCo(get), nil
}

// SaveOssConfig 实现保存资源配置方法.
func (adapter *OssConfigAdapter) SaveOssConfig(ctx context.Context, request *v1.OssConfigSaveRequest) (*v1.OssConfigSaveReply, error) {
	return &v1.OssConfigSaveReply{}, adapter.service.Save(ctx, dto.NewOssConfigSaveCmd(request))
}

// ModifyOssConfig 实现修改资源配置方法.
func (adapter *OssConfigAdapter) ModifyOssConfig(ctx context.Context, request *v1.OssConfigModifyRequest) (*v1.OssConfigModifyReply, error) {
	return &v1.OssConfigModifyReply{}, adapter.service.Modify(ctx, dto.NewOssConfigModifyCmd(request))
}

// RemoveOssConfig 实现删除资源配置方法.
func (adapter *OssConfigAdapter) RemoveOssConfig(ctx context.Context, request *v1.OssConfigRemoveRequest) (*v1.OssConfigRemoveReply, error) {
	return &v1.OssConfigRemoveReply{}, adapter.service.Remove(ctx, dto.NewOssConfigRemoveCmd(request))
}
