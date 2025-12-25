package factory

import (
	"errors"
	"fmt"
	"lersosa/pkg/oss/api"
	"lersosa/pkg/oss/entity"
	"lersosa/pkg/oss/storage"
)

var ErrUnsupportedType = errors.New("unsupported OSS type")

// 工厂函数类型.
type storageFactory func(fileInfo *entity.FileInfo, ossInfo *entity.OssInfo) (api.Storage, error)

var storageFactories = map[entity.Type]storageFactory{
	entity.TypeLocal: storage.NewLocalStorage,
	entity.TypeMinio: storage.NewMinioStorage,
}

// GetStorage 根据类型创建 Storage 实例.
func GetStorage(fileInfo *entity.FileInfo, ossInfo *entity.OssInfo) (api.Storage, error) {
	factory, ok := storageFactories[ossInfo.Type]
	if !ok {
		return nil, fmt.Errorf("%w: %s", ErrUnsupportedType, ossInfo.Type)
	}
	return factory(fileInfo, ossInfo)
}
