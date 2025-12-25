package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusPageQry 星体状态分页查询.
type StatusPageQry struct {
	PageNum  int64 `json:"page_num"`
	PageSize int64 `json:"page_size"`
	Flag     int32 `json:"flag"`
	Check    int32 `json:"check"`
	CreateBy int64 `json:"create_by"`
	UpdateBy int64 `json:"update_by"`
}

// NewStatusPageQry 构造星体状态分页查询.
func NewStatusPageQry(request *v1.StatusPageRequest) *StatusPageQry {
	return &StatusPageQry{
		PageNum:  request.GetPageNum(),
		PageSize: request.GetPageSize(),
		Flag:     request.GetFlag(),
		Check:    request.GetCheck(),
		CreateBy: request.GetCreateBy(),
		UpdateBy: request.GetUpdateBy(),
	}
}
