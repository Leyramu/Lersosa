//go:build wireinject
// +build wireinject

// The build tag makes sure the stub is not built in the final build.

package main

import (
	"lersosa/app/stellar/interface/internal/client"
	"lersosa/app/stellar/interface/internal/controller"
	"lersosa/app/stellar/interface/internal/executor"
	"lersosa/app/stellar/interface/internal/infrastructure/conf"
	"lersosa/app/stellar/interface/internal/server"

	"github.com/go-kratos/kratos/v2"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/wire"
)

// wireApp init kratos application.
func wireApp(*conf.Server, *conf.Client, *conf.Data, log.Logger) (*kratos.App, func(), error) {
	panic(wire.Build(server.ProviderSet, controller.ProviderSet, executor.ProviderSet, client.ProviderSet, newApp))
}
