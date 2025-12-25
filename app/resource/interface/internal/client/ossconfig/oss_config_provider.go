package ossconfig

import (
	"context"
	"fmt"
	"lersosa/app/resource/interface/internal/infrastructure/conf"
	"lersosa/pkg/tls"
	"net"
	"time"

	v1 "lersosa/api/resource/service/v1/ossconfig"

	"google.golang.org/grpc"
	"google.golang.org/grpc/credentials"
	"google.golang.org/grpc/credentials/insecure"
)

// ProvideClient 构造资源配置客户端提供者.
func ProvideClient(conf *conf.Client) (v1.OssConfigClient, error) {
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
		Timeout:   3 * time.Second,
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
		return nil, fmt.Errorf("未能连接到资源配置服务：%w", err)
	}

	return v1.NewOssConfigClient(conn), nil
}
