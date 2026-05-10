//go:build wireinject
// +build wireinject

// The build tag makes sure the stub is not built in the final build.

package main

import (
	"github.com/go-kratos/kratos/v2"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/wire"

	"github.com/leyramu/lersosa/app/ai/service/internal/adapter"
	"github.com/leyramu/lersosa/app/ai/service/internal/application"
	"github.com/leyramu/lersosa/app/ai/service/internal/conf"
	"github.com/leyramu/lersosa/app/ai/service/internal/domain"
	"github.com/leyramu/lersosa/app/ai/service/internal/infrastructure"
	"github.com/leyramu/lersosa/app/ai/service/internal/server"
)

// wireApp init kratos application.
func wireApp(*conf.Server, *conf.Data, log.Logger) (*kratos.App, func(), error) {
	panic(wire.Build(server.ProviderSet, adapter.ProviderSet, application.ProviderSet, domain.ProviderSet, infrastructure.ProviderSet, newApp))
}
