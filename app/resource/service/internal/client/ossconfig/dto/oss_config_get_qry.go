package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigGetQry 获取资源配置查询.
type OssConfigGetQry struct {
	OssConfigID string `json:"oss_config_id"`
}

// NewOssConfigGetQry 构造获取资源配置查询.
func NewOssConfigGetQry(request *v1.OssConfigGetRequest) *OssConfigGetQry {
	return &OssConfigGetQry{
		OssConfigID: request.OssConfigId,
	}
}
