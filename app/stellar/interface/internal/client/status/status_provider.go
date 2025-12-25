package status

import (
	"context"
	"fmt"
	"lersosa/app/stellar/interface/internal/infrastructure/conf"
	"lersosa/pkg/tls"
	"net"
	"time"

	v1 "lersosa/api/stellar/service/v1/status"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
	"google.golang.org/grpc/credentials/insecure"
)

// ProvideClient 构造星体状态客户端提供者.
func ProvideClient(conf *conf.Client) (v1.StatusClient, error) {
	var creds credentials.TransportCredentials

	if conf.Grpc.TlsEnable == true {
		tlsConfig, err := tls.NewClientTlsConfig(
			conf.Grpc.CertFile,
			conf.Grpc.KeyFile,
			conf.Grpc.CaFile,
		)
		if err != nil {
			return nil, fmt.Errorf("创建客户端 TLS 配置失败: %w", err)
		}
		creds = credentials.NewTLS(tlsConfig)
	} else {
		creds = insecure.NewCredentials()
	}

	dialer := &net.Dialer{
		Timeout:   10 * time.Second,
		KeepAlive: 30 * time.Second,
	}

	conn, err := grpc.NewClient(
		conf.Grpc.Addr[0],
		grpc.WithTransportCredentials(creds),
		grpc.WithContextDialer(func(ctx context.Context, addr string) (net.Conn, error) {
			return dialer.DialContext(ctx, "tcp", addr)
		}),
	)
	if err != nil {
		return nil, fmt.Errorf("未能连接到星体状态服务：%w", err)
	}

	return v1.NewStatusClient(conn), nil
}
