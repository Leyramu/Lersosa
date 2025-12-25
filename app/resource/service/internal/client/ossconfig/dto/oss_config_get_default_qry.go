package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigGetDefaultQry 获取默认资源配置查询.
type OssConfigGetDefaultQry struct{}

// NewOssConfigGetDefaultQry 构造获取默认资源配置查询.
func NewOssConfigGetDefaultQry(_ *v1.OssConfigGetDefaultRequest) *OssConfigGetDefaultQry {
	return &OssConfigGetDefaultQry{}
}
