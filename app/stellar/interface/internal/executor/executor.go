package executor

import (
	"lersosa/app/stellar/interface/internal/executor/info"
	"lersosa/app/stellar/interface/internal/executor/status"

	"github.com/google/wire"
)

// ProviderSet 业务提供者.
var ProviderSet = wire.NewSet(
	status.NewExecutor,
	info.NewExecutor,
)
