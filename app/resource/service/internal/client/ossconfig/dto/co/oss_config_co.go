package co

import (
	v1 "lersosa/api/resource/service/v1/ossconfig"

	"github.com/google/uuid"
)

// OssConfigCo 资源配置客户端模型.
type OssConfigCo struct {
	ID           uuid.UUID `json:"oss_config_id"`
	ConfigKey    string    `json:"config_key"`
	AccessKey    string    `json:"access_key"`
	SecretKey    string    `json:"secret_key"`
	BucketName   string    `json:"bucket_name"`
	Prefix       string    `json:"prefix"`
	Endpoint     string    `json:"endpoint"`
	Domain       string    `json:"domain"`
	IsHTTPS      string    `json:"is_https"`
	Region       string    `json:"region"`
	AccessPolicy string    `json:"access_policy"`
	Status       string    `json:"status"`
	Ext1         string    `json:"ext1"`
	Service      string    `json:"service"`
	Remark       string    `json:"remark"`
	Version      int32     `json:"version"`
}

// NewOssConfigPageCo 构造资源配置客户端模型.
func NewOssConfigPageCo(co *OssConfigCo) *v1.OssConfigPageReply_OssConfig {
	return &v1.OssConfigPageReply_OssConfig{
		OssConfigId:  co.ID.String(),
		ConfigKey:    co.ConfigKey,
		AccessKey:    co.AccessKey,
		SecretKey:    co.SecretKey,
		BucketName:   co.BucketName,
		Prefix:       co.Prefix,
		Endpoint:     co.Endpoint,
		Domain:       co.Domain,
		IsHttps:      co.IsHTTPS,
		Region:       co.Region,
		AccessPolicy: co.AccessPolicy,
		Status:       co.Status,
		Ext1:         co.Ext1,
		Remark:       co.Remark,
		Version:      co.Version,
	}
}

// NewOssConfigGetCo 构造资源配置客户端模型.
func NewOssConfigGetCo(co *OssConfigCo) *v1.OssConfigGetReply {
	return &v1.OssConfigGetReply{
		OssConfigId:  co.ID.String(),
		ConfigKey:    co.ConfigKey,
		AccessKey:    co.AccessKey,
		SecretKey:    co.SecretKey,
		BucketName:   co.BucketName,
		Prefix:       co.Prefix,
		Endpoint:     co.Endpoint,
		Domain:       co.Domain,
		IsHttps:      co.IsHTTPS,
		Region:       co.Region,
		AccessPolicy: co.AccessPolicy,
		Status:       co.Status,
		Ext1:         co.Ext1,
		Remark:       co.Remark,
		Version:      co.Version,
	}
}

// NewOssConfigGetDefaultCo 构造默认资源配置客户端模型.
func NewOssConfigGetDefaultCo(co *OssConfigCo) *v1.OssConfigGetDefaultReply {
	return &v1.OssConfigGetDefaultReply{
		OssConfigId:  co.ID.String(),
		ConfigKey:    co.ConfigKey,
		AccessKey:    co.AccessKey,
		SecretKey:    co.SecretKey,
		BucketName:   co.BucketName,
		Prefix:       co.Prefix,
		Endpoint:     co.Endpoint,
		Domain:       co.Domain,
		IsHttps:      co.IsHTTPS,
		Region:       co.Region,
		AccessPolicy: co.AccessPolicy,
		Status:       co.Status,
		Ext1:         co.Ext1,
		// Service:      co.Service,
	}
}
