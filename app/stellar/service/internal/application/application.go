package application

import (
	"lersosa/app/stellar/service/internal/application/info"
	"lersosa/app/stellar/service/internal/application/status"

	"github.com/google/wire"
)

// ProviderSet 业务提供者.
var ProviderSet = wire.NewSet(
	status.NewServiceImpl,
	info.NewServiceImpl,
)
