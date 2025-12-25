package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigSaveCmd 保存资源配置命令.
type OssConfigSaveCmd struct {
	ConfigKey    string `json:"config_key"`
	AccessKey    string `json:"access_key"`
	SecretKey    string `json:"secret_key"`
	BucketName   string `json:"bucket_name"`
	Prefix       string `json:"prefix"`
	Endpoint     string `json:"endpoint"`
	Domain       string `json:"domain"`
	IsHTTPS      string `json:"is_https"`
	Region       string `json:"region"`
	AccessPolicy string `json:"access_policy"`
	Status       string `json:"status"`
	Ext1         string `json:"ext1"`
	CreateDept   int64  `json:"create_dept"`
	CreateBy     int64  `json:"create_by"`
	Remark       string `json:"remark"`
}

// NewOssConfigSaveCmd 构造保存资源配置命令.
func NewOssConfigSaveCmd(request *v1.OssConfigSaveRequest) *OssConfigSaveCmd {
	return &OssConfigSaveCmd{
		ConfigKey:    request.ConfigKey,
		AccessKey:    request.AccessKey,
		SecretKey:    request.SecretKey,
		BucketName:   request.BucketName,
		Prefix:       request.Prefix,
		Endpoint:     request.Endpoint,
		Domain:       request.Domain,
		IsHTTPS:      request.IsHttps,
		Region:       request.Region,
		AccessPolicy: request.AccessPolicy,
		Status:       request.Status,
		Ext1:         request.Ext1,
		CreateDept:   request.CreateDept,
		CreateBy:     request.CreateBy,
		Remark:       request.Remark,
	}
}
