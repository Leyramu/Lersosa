package dto

import v1 "lersosa/api/resource/service/v1/file"

// FileGetQry 获取文件查询.
type FileGetQry struct {
	OssID string `json:"oss_id"`
}

// NewFileGetQry 构造获取文件查询.
func NewFileGetQry(request *v1.FileGetRequest) *FileGetQry {
	return &FileGetQry{
		OssID: request.OssId,
	}
}
