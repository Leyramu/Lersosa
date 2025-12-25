package dto

import v1 "lersosa/api/resource/service/v1/file"

// FileSaveCmd 保存文件命令.
type FileSaveCmd struct {
	FileName     string `json:"file_name"`
	FileSuffix   string `json:"file_suffix"`
	OriginalName string `json:"original_name"`
	URL          string `json:"url"`
	Ext1         string `json:"ext1"`
	CreateDept   int64  `json:"create_dept"`
	CreateBy     int64  `json:"create_by"`
	Service      string `json:"service"`
	Version      int32  `json:"version"`
}

// NewFileSaveCmd 构造保存文件命令.
func NewFileSaveCmd(request *v1.FileSaveRequest) *FileSaveCmd {
	return &FileSaveCmd{
		FileName:     request.FileName,
		FileSuffix:   request.FileSuffix,
		OriginalName: request.OriginalName,
		URL:          request.Url,
		Ext1:         request.Ext1,
		CreateDept:   request.CreateDept,
		CreateBy:     request.CreateBy,
		Service:      request.Service,
	}
}
