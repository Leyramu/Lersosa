package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusModifyCmd 修改星体状态命令.
type StatusModifyCmd struct {
	StatusID string `json:"status_id"`
	Flag     int32  `json:"flag"`
	Check    int32  `json:"check"`
	UpdateBy int64  `json:"update_by"`
	Remark   string `json:"remark"`
	Version  int32  `json:"version"`
}

// NewStatusModifyCmd 构造修改星体状态命令.
func NewStatusModifyCmd(request *v1.StatusModifyRequest) *StatusModifyCmd {
	return &StatusModifyCmd{
		StatusID: request.GetStatusId(),
		Flag:     request.GetFlag(),
		Check:    request.GetCheck(),
		UpdateBy: request.GetUpdateBy(),
		Remark:   request.GetRemark(),
		Version:  request.GetVersion(),
	}
}
