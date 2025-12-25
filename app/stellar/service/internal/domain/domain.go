package domain

import (
	infoAbility "lersosa/app/stellar/service/internal/domain/info/ability"
	infoFactory "lersosa/app/stellar/service/internal/domain/info/factory"
	statusAbility "lersosa/app/stellar/service/internal/domain/status/ability"

	"github.com/google/wire"
)

// ProviderSet 领域提供者.
var ProviderSet = wire.NewSet(
	infoAbility.NewDomainService,
	infoFactory.NewDomainFactory,
	statusAbility.NewDomainService,
)
