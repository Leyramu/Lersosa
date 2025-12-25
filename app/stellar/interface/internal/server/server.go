package server

import (
	"github.com/google/wire"
)

// ProviderSet 服务器提供者.
var ProviderSet = wire.NewSet(
	NewGRPCServer,
	NewHTTPServer,
)
