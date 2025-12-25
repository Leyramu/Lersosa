package dto

import v1 "lersosa/api/resource/service/v1/file"

// FileModifyCmd 修改文件命令.
type FileModifyCmd struct {
	OssID        string `json:"oss_id"`
	OriginalName string `json:"original_name"`
	Ext1         string `json:"ext1"`
	UpdateBy     int64  `json:"update_by"`
	Service      string `json:"service"`
	Version      int32  `json:"version"`
}

// NewFileModifyCmd 构造修改文件命令.
func NewFileModifyCmd(request *v1.FileModifyRequest) *FileModifyCmd {
	return &FileModifyCmd{
		OssID:        request.OssId,
		OriginalName: request.OriginalName,
		Ext1:         request.Ext1,
		UpdateBy:     request.UpdateBy,
		Service:      request.Service,
		Version:      request.Version,
	}
}
