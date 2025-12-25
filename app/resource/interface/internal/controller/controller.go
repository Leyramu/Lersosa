package controller

import (
	"lersosa/app/resource/interface/internal/controller/web"

	"github.com/google/wire"
)

// ProviderSet 适配器提供者.
var ProviderSet = wire.NewSet(
	web.NewOssConfigController,
	web.NewFileController,
)
