package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigPageQry 资源配置分页查询.
type OssConfigPageQry struct {
	PageNum    int64  `json:"page_num"`
	PageSize   int64  `json:"page_size"`
	ConfigKey  string `json:"config_key"`
	BucketName string `json:"bucket_name"`
	Status     string `json:"status"`
}

// NewOssConfigPageQry 构造资源配置分页查询.
func NewOssConfigPageQry(request *v1.OssConfigPageRequest) *OssConfigPageQry {
	return &OssConfigPageQry{
		PageNum:    request.PageNum,
		PageSize:   request.PageSize,
		ConfigKey:  request.ConfigKey,
		BucketName: request.BucketName,
		Status:     request.Status,
	}
}
