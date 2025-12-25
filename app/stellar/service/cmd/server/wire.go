//go:build wireinject
// +build wireinject

// The build tag makes sure the stub is not built in the final build.

package main

import (
	"lersosa/app/stellar/service/internal/adapter"
	"lersosa/app/stellar/service/internal/application"
	"lersosa/app/stellar/service/internal/conf"
	"lersosa/app/stellar/service/internal/domain"
	"lersosa/app/stellar/service/internal/infrastructure"
	"lersosa/app/stellar/service/internal/server"

	"github.com/go-kratos/kratos/v2"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/wire"
)

// wireApp init kratos application.
func wireApp(*conf.Server, *conf.Data, log.Logger) (*kratos.App, func(), error) {
	panic(wire.Build(server.ProviderSet, adapter.ProviderSet, application.ProviderSet, domain.ProviderSet, infrastructure.ProviderSet, newApp))
}
