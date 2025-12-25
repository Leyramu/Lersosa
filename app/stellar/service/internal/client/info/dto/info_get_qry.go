package dto

import v1 "lersosa/api/stellar/service/v1/info"

// InfoGetQry 获取星体信息查询.
type InfoGetQry struct {
	InfoID string `json:"info_id"`
}

// NewInfoGetQry 构造获取星体信息查询.
func NewInfoGetQry(request *v1.InfoGetRequest) *InfoGetQry {
	return &InfoGetQry{
		InfoID: request.InfoId,
	}
}
