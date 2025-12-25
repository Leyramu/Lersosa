package storage

import (
	"lersosa/pkg/oss/api"
	"lersosa/pkg/oss/entity"
)

// LocalStorage 本地存储.
type LocalStorage struct {
	api.BaseStorage
}

// NewLocalStorage 创建 LocalStorage 实例.
func NewLocalStorage(fileInfo *entity.FileInfo, ossInfo *entity.OssInfo) (api.Storage, error) {
	return &LocalStorage{
		BaseStorage: api.BaseStorage{FileInfo: fileInfo, OssInfo: ossInfo},
	}, nil
}

// Upload 上传文件.
func (l *LocalStorage) Upload() (*api.Result, error) {
	//fullPath := filepath.Join(l.OssInfo.Directory, l.FileInfo.FileName)
	//
	//// 础保目录存在
	//if err := os.MkdirAll(l.OssInfo.Directory, 0755); err != nil {
	//	return "", err
	//}
	//
	//file, err := os.Create(fullPath)
	//if err != nil {
	//	return "", err
	//}
	//defer func(file *os.File) {
	//	err := file.Close()
	//	if err != nil {
	//		_ = file.Close()
	//	}
	//}(file)
	//
	//// 写入内容
	//_, err = file.Write(l.FileInfo.Chuck)
	//
	//// 返回 URL
	//return l.OssInfo.Domain + l.OssInfo.Path + l.FileInfo.FileName, nil

	return nil, nil
}
