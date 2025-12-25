package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusListQry 星体状态分页查询.
type StatusListQry struct {
	Flag  int32 `json:"flag"`
	Check int32 `json:"check"`
}

// NewStatusListQry 构造星体状态分页查询.
func NewStatusListQry(request *v1.StatusListRequest) *StatusListQry {
	return &StatusListQry{
		Flag:  request.GetFlag(),
		Check: request.GetCheck(),
	}
}
