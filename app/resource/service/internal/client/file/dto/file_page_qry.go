package dto

import v1 "lersosa/api/resource/service/v1/file"

// FilePageQry 文件分页查询.
type FilePageQry struct {
	PageNum  int64 `json:"page_num"`
	PageSize int64 `json:"page_size"`
}

// NewFilePageQry 构造文件分页查询.
func NewFilePageQry(request *v1.FilePageRequest) *FilePageQry {
	return &FilePageQry{
		PageNum:  request.PageNum,
		PageSize: request.PageSize,
	}
}
