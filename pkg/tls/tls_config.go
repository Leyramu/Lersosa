package tls

import (
	"crypto/tls"
	"crypto/x509"
	"errors"
	"fmt"
	"os"
)

// insecureSkipVerify 默认开启证书验证.
const insecureSkipVerify = true

// SSLInfo 存储SSL证书路径。
type SSLInfo struct {
	CertFile           string `json:"cert_file"`
	KeyFile            string `json:"key_file"`
	CaFile             string `json:"ca_file"`
	InsecureSkipVerify bool   `json:"insecure_skip_verify"`
}

// Scheme 获取服务协议.
func (info SSLInfo) Scheme() string {
	if info.KeyFile != "" && info.CertFile != "" {
		return "https"
	}
	return "http"
}

// ServerConfig 获取服务端TLS配置.
func (info SSLInfo) ServerConfig() (*tls.Config, error) {
	if info.KeyFile == "" || info.CertFile == "" {
		return nil, errors.New("请提供有效的证书文件和密钥文件")
	}

	cert, err := tls.LoadX509KeyPair(info.CertFile, info.KeyFile)
	if err != nil {
		return nil, fmt.Errorf("未能加载服务器 TLS 证书/密钥: %w", err)
	}

	if info.CaFile != "" {
		// 启用双向 TLS
		caPool, err := newCertPool(info.CaFile)
		if err != nil {
			return nil, fmt.Errorf("未能读取 CA 文件用于客户端验证: %w", err)
		}
		return &tls.Config{
			Certificates: []tls.Certificate{cert},
			ClientAuth:   tls.RequireAndVerifyClientCert,
			ClientCAs:    caPool,
			MinVersion:   tls.VersionTLS13,
		}, nil
	}

	// 仅单向 HTTPS
	return &tls.Config{
		Certificates: []tls.Certificate{cert},
		ClientAuth:   tls.NoClientCert,
		MinVersion:   tls.VersionTLS13,
	}, nil
}

// ClientConfig 获取客户端TLS配置.
func (info SSLInfo) ClientConfig() (*tls.Config, error) {
	var tlsConfig = &tls.Config{
		InsecureSkipVerify: info.InsecureSkipVerify,
		MinVersion:         tls.VersionTLS13,
	}

	if info.CaFile != "" {
		caPool, err := newCertPool(info.CaFile)
		if err != nil {
			return nil, fmt.Errorf("读取 CA 证书文件错误: %w", err)
		}
		tlsConfig.RootCAs = caPool
	}

	if info.CertFile != "" && info.KeyFile != "" {
		tlsCert, err := tls.LoadX509KeyPair(info.CertFile, info.KeyFile)
		if err != nil {
			return nil, fmt.Errorf("读取客户端证书/私钥文件错误: %w", err)
		}
		tlsConfig.Certificates = []tls.Certificate{tlsCert}
	}

	return tlsConfig, nil
}

// newCertPool 创建 x509 certPool，提供 CA 文件.
func newCertPool(caFile string) (*x509.CertPool, error) {
	caCert, err := os.ReadFile(caFile)
	if err != nil {
		return nil, fmt.Errorf("未能读取 CA 文件 %q: %w", caFile, err)
	}

	caPool := x509.NewCertPool()
	if !caPool.AppendCertsFromPEM(caCert) {
		return nil, fmt.Errorf("CA 文件 %q 中未找到有效的 PEM 证书", caFile)
	}
	return caPool, nil
}

// NewServerTlsConfig 创建服务端 TLS 配置.
func NewServerTlsConfig(certFile, keyFile, caFile string) (*tls.Config, error) {
	info := SSLInfo{
		CertFile:           certFile,
		KeyFile:            keyFile,
		CaFile:             caFile,
		InsecureSkipVerify: insecureSkipVerify,
	}
	return info.ServerConfig()
}

// NewClientTlsConfig 创建客户端 TLS 配置.
func NewClientTlsConfig(certFile, keyFile, caFile string) (*tls.Config, error) {
	info := SSLInfo{
		CertFile:           certFile,
		KeyFile:            keyFile,
		CaFile:             caFile,
		InsecureSkipVerify: insecureSkipVerify,
	}
	return info.ClientConfig()
}
