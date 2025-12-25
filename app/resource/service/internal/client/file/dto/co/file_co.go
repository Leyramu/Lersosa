package co

import (
	v1 "lersosa/api/resource/service/v1/file"

	"github.com/google/uuid"
)

// FileCo 文件客户端模型.
type FileCo struct {
	ID           uuid.UUID `json:"oss_id"`
	FileName     string    `json:"file_name"`
	OriginalName string    `json:"original_name"`
	FileSuffix   string    `json:"file_suffix"`
	URL          string    `json:"url"`
	Ext1         string    `json:"ext1"`
	Service      string    `json:"service"`
	Version      int32     `json:"version"`
}

// NewFilePageCo 构造文件客户端模型.
func NewFilePageCo(co *FileCo) *v1.FilePageReply_File {
	return &v1.FilePageReply_File{
		OssId:        co.ID.String(),
		FileName:     co.FileName,
		OriginalName: co.OriginalName,
		FileSuffix:   co.FileSuffix,
		Url:          co.URL,
		Ext1:         co.Ext1,
		Service:      co.Service,
		Version:      co.Version,
	}
}

// NewFileGetCo 构造文件客户端模型.
func NewFileGetCo(co *FileCo) *v1.FileGetReply {
	return &v1.FileGetReply{
		OssId:        co.ID.String(),
		FileName:     co.FileName,
		OriginalName: co.OriginalName,
		FileSuffix:   co.FileSuffix,
		Url:          co.URL,
		Ext1:         co.Ext1,
		Service:      co.Service,
		Version:      co.Version,
	}
}
