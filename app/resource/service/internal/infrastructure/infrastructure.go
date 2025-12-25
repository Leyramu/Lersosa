package infrastructure

import (
	fileGatewayImpl "lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl"
	fileDatabase "lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database"
	ossconfigGatewayImpl "lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl"
	ossconfigDatabase "lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database"

	"github.com/google/wire"
)

// ProviderSet 基础设施提供者.
var ProviderSet = wire.NewSet(
	fileDatabase.NewData,
	fileDatabase.NewEntClient,
	fileDatabase.NewRedisCmd,
	fileGatewayImpl.NewRepoImpl,
	ossconfigDatabase.NewData,
	ossconfigDatabase.NewEntClient,
	ossconfigDatabase.NewRedisCmd,
	ossconfigGatewayImpl.NewRepoImpl,
	ossconfigGatewayImpl.NewRedisImpl,
)
