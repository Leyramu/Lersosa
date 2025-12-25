package redis

import (
	"context"
	"lersosa/pkg/tls"
	"sync"
	"time"

	"github.com/redis/go-redis/extra/redisotel/v9"
	"github.com/redis/go-redis/v9"
)

var (
	client *redis.Client
	once   sync.Once
)

// NewCreateClient 创建带 Redis 客户端.
func NewCreateClient(addr, password, certFile, keyFile, caFile string, readTimeout, writeTimeout time.Duration) (*redis.Client, error) {
	var initErr error
	once.Do(func() {
		tlsConfig, err := tls.NewClientTlsConfig(certFile, keyFile, caFile)
		if err != nil {
			initErr = err
			return
		}

		client = redis.NewClient(&redis.Options{
			Addr:         addr,
			Password:     password,
			ReadTimeout:  readTimeout,
			WriteTimeout: writeTimeout,
			DialTimeout:  time.Second * 2,
			PoolSize:     10,
			TLSConfig:    tlsConfig,
		})

		timeout, cancelFunc := context.WithTimeout(context.Background(), time.Second*2)
		defer cancelFunc()

		initErr = client.Ping(timeout).Err()
	})

	// 初始化失败
	if initErr != nil {
		return nil, initErr
	}

	// 使用 InstrumentTracing 启用追踪
	if err := redisotel.InstrumentTracing(client); err != nil {
		return nil, err
	}

	// 使用 InstrumentMetrics 启用指标
	if err := redisotel.InstrumentMetrics(client); err != nil {
		return nil, err
	}

	return client, nil
}

// NewCloseClient 关闭 Redis 客户端.
func NewCloseClient() error {
	if client != nil {
		if client != nil {
			return client.Close()
		}
	}
	return nil
}
