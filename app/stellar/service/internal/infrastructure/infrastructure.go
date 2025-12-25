package infrastructure

import (
	infoGatewayImpl "lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl"
	infoDatabase "lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database"
	statusGatewayImpl "lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl"
	statusDatabase "lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database"

	"github.com/google/wire"
)

// ProviderSet 数据提供者.
var ProviderSet = wire.NewSet(
	statusDatabase.NewData,
	statusDatabase.NewEntClient,
	statusDatabase.NewElasticSearchClient,
	statusDatabase.NewRedisCmd,
	statusGatewayImpl.NewRepoImpl,
	statusGatewayImpl.NewRedisImpl,
	infoDatabase.NewData,
	infoDatabase.NewEntClient,
	infoDatabase.NewElasticSearchClient,
	infoDatabase.NewRedisCmd,
	infoGatewayImpl.NewRepoImpl,
	infoGatewayImpl.NewRedisImpl,
	infoGatewayImpl.NewElasticSearchImpl,
)
