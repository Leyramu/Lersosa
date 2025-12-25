package model

import (
	"time"

	"github.com/google/uuid"
)

// Entity 资源配置领域模型层.
type Entity struct {
	ID           uuid.UUID `json:"oss_config_id"`
	TenantID     uuid.UUID `json:"tenant_id"`
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
	CreateDept   int64     `json:"create_dept"`
	CreateBy     int64     `json:"create_by"`
	CreateTime   time.Time `json:"create_time"`
	UpdateBy     int64     `json:"update_by"`
	UpdateTime   time.Time `json:"update_time"`
	Remark       string    `json:"remark"`
	Version      int32     `json:"version"`
}

// Condition 查询资源配置条件模型.
type Condition struct {
	PageNum    int64  `json:"page_num"`
	PageSize   int64  `json:"page_size"`
	ConfigKey  string `json:"config_key"`
	BucketName string `json:"bucket_name"`
	Status     string `json:"status"`
}
