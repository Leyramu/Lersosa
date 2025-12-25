package server

import (
	v1Info "lersosa/api/stellar/interface/v1/info"
	v1Status "lersosa/api/stellar/interface/v1/status"
	"lersosa/app/stellar/interface/internal/controller/web"
	"lersosa/app/stellar/interface/internal/infrastructure/conf"
	pkgTls "lersosa/pkg/tls"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/go-kratos/kratos/v2/middleware/logging"
	"github.com/go-kratos/kratos/v2/middleware/recovery"
	"github.com/go-kratos/kratos/v2/transport/http"
)

// NewHTTPServer HTTP 服务器.
func NewHTTPServer(
	c *conf.Server,
	info *web.InfoController,
	status *web.StatusController,
	logger log.Logger,
) *http.Server {
	var opts = []http.ServerOption{
		http.Middleware(
			recovery.Recovery(),
			logging.Server(logger),
		),
	}
	if c.Http.Network != "" {
		opts = append(opts, http.Network(c.Http.Network))
	}
	if c.Http.Addr != "" {
		opts = append(opts, http.Address(c.Http.Addr))
	}
	if c.Http.Timeout != nil {
		opts = append(opts, http.Timeout(c.Http.Timeout.AsDuration()))
	}
	if c.Http.TlsEnable == true {
		log.Info("已启用 HTTP mTLS")
		if tls, err := pkgTls.NewServerTlsConfig(c.Http.CertFile, c.Http.KeyFile, c.Http.CaFile); err == nil {
			opts = append(opts, http.TLSConfig(tls))
		} else {
			log.Error("服务器 HTTP mTLS 配置错误：", err)
		}
	} else {
		log.Info("未启用 TLS")
	}

	srv := http.NewServer(opts...)
	v1Info.RegisterInfoHTTPServer(
		srv,
		info,
	)
	v1Status.RegisterStatusHTTPServer(
		srv,
		status,
	)

	return srv
}
