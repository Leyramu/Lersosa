package server

import (
	v1Info "lersosa/api/stellar/service/v1/info"
	v1Status "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/service/internal/adapter/web"
	"lersosa/app/stellar/service/internal/conf"
	pkgTls "lersosa/pkg/tls"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/go-kratos/kratos/v2/middleware/logging"
	"github.com/go-kratos/kratos/v2/middleware/recovery"
	"github.com/go-kratos/kratos/v2/transport/grpc"
)

// NewGRPCServer gRPC 服务器.
func NewGRPCServer(
	c *conf.Server,
	status *web.StatusAdapter,
	info *web.InfoAdapter,
	logger log.Logger,
) *grpc.Server {
	var opts = []grpc.ServerOption{
		grpc.Middleware(
			recovery.Recovery(),
			logging.Server(logger),
		),
	}
	if c.Grpc.Network != "" {
		opts = append(opts, grpc.Network(c.Grpc.Network))
	}
	if c.Grpc.Addr != "" {
		opts = append(opts, grpc.Address(c.Grpc.Addr))
	}
	if c.Grpc.Timeout != nil {
		opts = append(opts, grpc.Timeout(c.Grpc.Timeout.AsDuration()))
	}
	if c.Grpc.TlsEnable == true {
		log.Info("已启用 gRPC mTLS")
		if tls, err := pkgTls.NewServerTlsConfig(c.Grpc.CertFile, c.Grpc.KeyFile, c.Grpc.CaFile); err == nil {
			opts = append(opts, grpc.TLSConfig(tls))
		} else {
			log.Error("服务器 TLS 配置错误：%v", err)
		}
	} else {
		log.Info("未启用 gRPC mTLS")
	}

	srv := grpc.NewServer(opts...)
	v1Status.RegisterStatusServer(
		srv,
		status,
	)
	v1Info.RegisterInfoServer(
		srv,
		info,
	)
	return srv
}
