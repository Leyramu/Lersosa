package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusGetQry 获取星体状态查询.
type StatusGetQry struct {
	StatusID string `json:"status_id"`
}

// NewStatusGetQry 构造获取星体状态查询.
func NewStatusGetQry(request *v1.StatusGetRequest) *StatusGetQry {
	return &StatusGetQry{
		StatusID: request.GetStatusId(),
	}
}
