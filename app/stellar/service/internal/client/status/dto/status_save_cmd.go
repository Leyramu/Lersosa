package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusSaveCmd 保存星体状态命令.
type StatusSaveCmd struct {
	Score    float64 `json:"score"`
	Flag     int32   `json:"flag"`
	Check    int32   `json:"check"`
	CreateBy int64   `json:"create_by"`
	Remark   string  `json:"remark"`
}

// NewStatusSaveCmd 构造保存星体状态命令.
func NewStatusSaveCmd(request *v1.StatusSaveRequest) *StatusSaveCmd {
	return &StatusSaveCmd{
		Score:    request.GetScore(),
		Flag:     request.GetFlag(),
		Check:    request.GetCheck(),
		CreateBy: request.GetCreateBy(),
		Remark:   request.GetRemark(),
	}
}
