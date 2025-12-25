package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigModifyCmd 修改资源配置命令.
type OssConfigModifyCmd struct {
	OssConfigID  string `json:"oss_config_id"`
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
	UpdateBy     int64  `json:"update_by"`
	Remark       string `json:"remark"`
	Version      int32  `json:"version"`
}

// NewOssConfigModifyCmd 构造修改资源配置命令.
func NewOssConfigModifyCmd(request *v1.OssConfigModifyRequest) *OssConfigModifyCmd {
	return &OssConfigModifyCmd{
		OssConfigID:  request.OssConfigId,
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
		UpdateBy:     request.UpdateBy,
		Remark:       request.Remark,
		Version:      request.Version,
	}
}
