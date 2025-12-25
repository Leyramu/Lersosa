package dto

import v1 "lersosa/api/stellar/service/v1/status"

// StatusRemoveCmd 删除星体状态命令.
type StatusRemoveCmd struct {
	Statuses []Status `json:"statuses"`
}

// Status 星体状态.
type Status struct {
	StatusID string `json:"status_id"`
	Version  int32  `json:"version"`
}

// NewStatusRemoveCmd 构造删除星体状态命令.
func NewStatusRemoveCmd(request *v1.StatusRemoveRequest) *StatusRemoveCmd {
	statuses := make([]Status, len(request.GetStatuses()))
	for i, s := range request.GetStatuses() {
		statuses[i] = Status{
			StatusID: s.GetStatusId(),
			Version:  s.GetVersion(),
		}
	}

	return &StatusRemoveCmd{
		Statuses: statuses,
	}
}
