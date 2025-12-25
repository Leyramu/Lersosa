package executor

import (
	"lersosa/app/resource/interface/internal/executor/file"
	"lersosa/app/resource/interface/internal/executor/ossconfig"

	"github.com/google/wire"
)

// ProviderSet 业务提供者.
var ProviderSet = wire.NewSet(
	ossconfig.NewExecutor,
	file.NewExecutor,
)
