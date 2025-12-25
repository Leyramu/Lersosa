package client

import (
	"lersosa/app/resource/interface/internal/client/file"
	"lersosa/app/resource/interface/internal/client/ossconfig"

	"github.com/google/wire"
)

// ProviderSet 客户端提供者.
var ProviderSet = wire.NewSet(
	file.NewClient,
	file.ProvideClient,
	ossconfig.NewClient,
	ossconfig.ProvideClient,
)
