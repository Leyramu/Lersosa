package domain

import (
	fileAbility "lersosa/app/resource/service/internal/domain/file/ability"
	ossconfigAbility "lersosa/app/resource/service/internal/domain/ossconfig/ability"
	ossconfigFactory "lersosa/app/resource/service/internal/domain/ossconfig/factory"

	"github.com/google/wire"
)

// ProviderSet 领域提供者.
var ProviderSet = wire.NewSet(
	ossconfigAbility.NewDomainService,
	ossconfigFactory.NewDomainFactory,
	fileAbility.NewDomainService,
)
