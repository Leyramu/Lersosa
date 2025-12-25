package infrastructure

import (
	"lersosa/app/stellar/interface/internal/infrastructure/data"

	"github.com/google/wire"
)

// ProviderSet 基础设施提供者.
var ProviderSet = wire.NewSet(
	data.NewData,
	data.NewElasticSearchClient,
)
