package entity

import (
	"errors"
	"io"
)

// ErrFileNameEmpty 文件名不能为空.
var ErrFileNameEmpty = errors.New("file name is empty")

// FileInfo 表示上传的文件信息
type FileInfo struct {
	FileSize    int64     `json:"file_size"`
	FileName    string    `json:"file_name"`
	FileSuffix  string    `json:"file_suffix"`
	ContentType string    `json:"content_type"`
	Chuck       io.Reader `json:"chuck"`
}

// NewFileInfo 从 multipart.File 创建 FileInfo.
func NewFileInfo(fileSize int64, fileName, fileSuffix, contentType string, chuck io.Reader) (*FileInfo, error) {

	if fileName == "" {
		return nil, ErrFileNameEmpty
	}

	return &FileInfo{
		FileSize:    fileSize,
		FileName:    fileName,
		FileSuffix:  fileSuffix,
		ContentType: contentType,
		Chuck:       chuck,
	}, nil
}
