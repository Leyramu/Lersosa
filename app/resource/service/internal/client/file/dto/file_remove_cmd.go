package dto

import v1 "lersosa/api/resource/service/v1/file"

// FileRemoveCmd 删除文件命令.
type FileRemoveCmd struct {
	Files []File `json:"files"`
}

// File 文件.
type File struct {
	OssID   string `json:"oss_id"`
	Version int32  `json:"version"`
}

// NewFileRemoveCmd 构造删除文件命令.
func NewFileRemoveCmd(request *v1.FileRemoveRequest) *FileRemoveCmd {
	files := make([]File, len(request.Files))
	for i, f := range request.Files {
		files[i] = File{
			OssID:   f.OssId,
			Version: f.Version,
		}
	}

	return &FileRemoveCmd{
		Files: files,
	}
}
