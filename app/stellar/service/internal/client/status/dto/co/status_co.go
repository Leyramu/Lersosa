package co

import (
	v1 "lersosa/api/stellar/service/v1/status"
	"time"

	"github.com/google/uuid"
)

// StatusCo 星体状态客户端模型.
type StatusCo struct {
	ID         uuid.UUID `json:"status_id"`
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

// NewStatusPageCo 构造星体状态客户端模型.
func NewStatusPageCo(co *StatusCo) *v1.StatusPageReply_Status {
	return &v1.StatusPageReply_Status{
		StatusId:   co.ID.String(),
		Score:      co.Score,
		Flag:       co.Flag,
		Check:      co.Check,
		CreateBy:   co.CreateBy,
		CreateTime: co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:   co.UpdateBy,
		UpdateTime: co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:     co.Remark,
		Version:    co.Version,
	}
}

// NewStatusListCo 构造星体状态客户端模型.
func NewStatusListCo(co *StatusCo) *v1.StatusListReply_Status {
	return &v1.StatusListReply_Status{
		StatusId:   co.ID.String(),
		InfoId:     co.InfoID.String(),
		Score:      co.Score,
		Flag:       co.Flag,
		Check:      co.Check,
		CreateBy:   co.CreateBy,
		CreateTime: co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:   co.UpdateBy,
		UpdateTime: co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:     co.Remark,
		Version:    co.Version,
	}
}

// NewStatusGetCo 构造星体状态客户端模型.
func NewStatusGetCo(co *StatusCo) *v1.StatusGetReply {
	return &v1.StatusGetReply{
		StatusId:   co.ID.String(),
		Score:      co.Score,
		Flag:       co.Flag,
		Check:      co.Check,
		CreateBy:   co.CreateBy,
		CreateTime: co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:   co.UpdateBy,
		UpdateTime: co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:     co.Remark,
		Version:    co.Version,
	}
}
