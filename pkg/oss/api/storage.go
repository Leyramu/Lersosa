package api

import "lersosa/pkg/oss/entity"

// Storage 存储接口.
type Storage interface {
	Upload() (*Result, error)
}

// BaseStorage 存储基础字段.
type BaseStorage struct {
	FileInfo *entity.FileInfo
	OssInfo  *entity.OssInfo
}

// Result 存储结果.
type Result struct {
	OriginalName string `json:"original_name"`
	FileName     string `json:"file_name"`
	FileSuffix   string `json:"file_suffix"`
	URL          string `json:"url"`
	Service      string `json:"service"`
}
