package elasticsearch

import (
	"context"
	"crypto/tls"
	"net/http"
	"sync"
	"time"

	pkgTls "lersosa/pkg/tls"

	"github.com/elastic/go-elasticsearch/v9"
	"go.opentelemetry.io/contrib/instrumentation/net/http/otelhttp"
)

var (
	client *elasticsearch.Client
	once   sync.Once
)

// NewCreateClient 创建 ElasticSearch 客户端.
func NewCreateClient(addr []string, username, password, cloudID, APIKey, certFile, keyFile, caFile string) (*elasticsearch.Client, error) {
	var initErr error
	once.Do(func() {
		// 创建带 OpenTelemetry 的 HTTP 传输层
		var transport *otelhttp.Transport
		if certFile != "" && keyFile != "" && caFile != "" {
			tlsConfig, err := pkgTls.NewClientTlsConfig(certFile, keyFile, caFile)
			if err != nil {
				initErr = err
				return
			}

			transport = otelhttp.NewTransport(
				&http.Transport{
					TLSClientConfig: tlsConfig,
				},
			)
		} else {
			transport = otelhttp.NewTransport(
				&http.Transport{
					TLSClientConfig: &tls.Config{
						InsecureSkipVerify: false,
					},
				},
			)
		}

		options := elasticsearch.Config{
			Addresses: addr,
			Transport: transport,
			CloudID:   cloudID,
			APIKey:    APIKey,
		}

		if username != "" && password != "" {
			options.Username = username
			options.Password = password
		}

		var err error
		client, err = elasticsearch.NewClient(options)
		if err != nil {
			initErr = err
			return
		}

		// 执行健康检查
		ctx, cancel := context.WithTimeout(context.Background(), 2*time.Second)
		defer cancel()

		done := make(chan error, 1)
		go func() {
			res, err := client.Ping()
			if res != nil {
				_ = res.Body.Close()
			}
			done <- err
		}()

		select {
		case pingErr := <-done:
			if pingErr != nil {
				initErr = pingErr
			}
		case <-ctx.Done():
			initErr = ctx.Err()
		}
	})

	if initErr != nil {
		return nil, initErr
	}

	return client, nil
}
