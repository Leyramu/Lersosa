package adapter

import (
	"lersosa/app/resource/service/internal/adapter/web"

	"github.com/google/wire"
)

// ProviderSet 适配器提供者.
var ProviderSet = wire.NewSet(
	web.NewOssConfigAdapter,
	web.NewFileAdapter,
)
