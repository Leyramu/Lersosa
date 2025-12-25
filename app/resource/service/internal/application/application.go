package application

import (
	"lersosa/app/resource/service/internal/application/file"
	"lersosa/app/resource/service/internal/application/ossconfig"

	pkgOss "lersosa/pkg/oss/template"

	"github.com/google/wire"
)

// ProviderSet 业务提供者.
var ProviderSet = wire.NewSet(
	ossconfig.NewServiceImpl,
	file.NewServiceImpl,
	pkgOss.NewStorageTemplate,
)
