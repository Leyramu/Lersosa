package template

import (
	"fmt"
	"lersosa/pkg/oss/api"
	"lersosa/pkg/oss/entity"
	"lersosa/pkg/oss/factory"
)

// StorageTemplate 模板模式.
type StorageTemplate struct{}

// NewStorageTemplate 创建 StorageTemplate 实例.
func NewStorageTemplate() *StorageTemplate {
	return &StorageTemplate{}
}

// Upload 上传文件.
func (t *StorageTemplate) Upload(fileInfo *entity.FileInfo, ossInfo *entity.OssInfo) (*api.Result, error) {
	parsedType, err := entity.ParseType(ossInfo.ConfigKey)
	if err != nil {
		return nil, fmt.Errorf("无效的OSS存储类型 '%s': %w", ossInfo.ConfigKey, err)
	}

	// 将解析后的类型写回
	ossInfo.Type = parsedType

	// 创建存储实例并上传
	storage, err := factory.GetStorage(fileInfo, ossInfo)
	if err != nil {
		return nil, fmt.Errorf("创建存储实例失败: %w", err)
	}

	return storage.Upload()
}
