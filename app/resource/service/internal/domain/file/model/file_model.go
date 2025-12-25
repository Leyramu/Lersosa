package model

import (
	"time"

	"github.com/google/uuid"
)

// Entity 文件领域模型层.
type Entity struct {
	ID           uuid.UUID `json:"oss_id"`
	TenantID     uuid.UUID `json:"tenant_id"`
	FileName     string    `json:"file_name"`
	OriginalName string    `json:"original_name"`
	FileSuffix   string    `json:"file_suffix"`
	URL          string    `json:"url"`
	Ext1         string    `json:"ext1"`
	CreateDept   int64     `json:"create_dept"`
	CreateBy     int64     `json:"create_by"`
	CreateTime   time.Time `json:"create_time"`
	UpdateBy     int64     `json:"update_by"`
	UpdateTime   time.Time `json:"update_time"`
	Service      string    `json:"service"`
	Version      int32     `json:"version"`
}

// Condition 查询文件条件模型.
type Condition struct {
	PageNum  int64
	PageSize int64
}
