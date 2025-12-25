package model

import (
	"time"

	"github.com/google/uuid"
)

// Entity 星体状态领域模型.
type Entity struct {
	ID         uuid.UUID `json:"status_id"`
	TenantID   uuid.UUID `json:"tenant_id"`
	InfoID     uuid.UUID `json:"info_id"`
	Score      float64   `json:"score"`
	Flag       int32     `json:"flag"`
	Check      int32     `json:"check"`
	CreateBy   int64     `json:"create_by"`
	CreateTime time.Time `json:"create_time"`
	UpdateBy   int64     `json:"update_by"`
	UpdateTime time.Time `json:"update_time"`
	Remark     string    `json:"remark"`
	Version    int32     `json:"version"`
}

// Condition 查询星体状态条件模型.
type Condition struct {
	PageNum  int64 `json:"page_num"`
	PageSize int64 `json:"page_size"`
	Flag     int32 `json:"flag"`
	Check    int32 `json:"check"`
	CreateBy int64 `json:"create_by"`
	UpdateBy int64 `json:"update_by"`
}
