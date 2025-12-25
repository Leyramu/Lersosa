package dto

import v1 "lersosa/api/stellar/service/v1/info"

// InfoRemoveCmd 删除星体信息命令.
type InfoRemoveCmd struct {
	Infos []Info `json:"infos"`
}

// Info 星体信息.
type Info struct {
	InfoID  string `json:"info_id"`
	Version int32  `json:"version"`
}

// NewInfoRemoveCmd 构造删除星体信息命令.
func NewInfoRemoveCmd(request *v1.InfoRemoveRequest) *InfoRemoveCmd {
	infos := make([]Info, len(request.Infos))
	for i, s := range request.Infos {
		infos[i] = Info{
			InfoID:  s.InfoId,
			Version: s.Version,
		}
	}

	return &InfoRemoveCmd{
		Infos: infos,
	}
}
