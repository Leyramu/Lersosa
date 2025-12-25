package client

import (
	"lersosa/app/stellar/interface/internal/client/info"
	"lersosa/app/stellar/interface/internal/client/status"

	"github.com/google/wire"
)

// ProviderSet 客户端提供者.
var ProviderSet = wire.NewSet(
	info.NewClient,
	info.ProvideClient,
	status.NewClient,
	status.ProvideClient,
)
