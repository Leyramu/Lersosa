package storage

import (
	"context"
	"errors"
	"fmt"
	"lersosa/pkg/oss/api"
	"lersosa/pkg/oss/entity"
	"net"
	"net/http"
	"path"
	"strings"
	"time"

	"github.com/google/uuid"
	"github.com/minio/minio-go/v7"
	"github.com/minio/minio-go/v7/pkg/credentials"
)

const (
	defaultUploadTimeout = 300 * time.Second
)

// MinioStorage Minio 存储实现.
type MinioStorage struct {
	api.BaseStorage
	client     *minio.Client
	httpClient *http.Client
}

// NewMinioStorage 创建 Minio 存储实现实例.
func NewMinioStorage(fileInfo *entity.FileInfo, ossInfo *entity.OssInfo) (api.Storage, error) {
	if ossInfo == nil {
		return nil, errors.New("需要 ossInfo")
	}

	// 1. 构建带连接池和超时的 HTTP 客户端
	httpClient := &http.Client{
		Timeout: 60 * time.Second,
		Transport: &http.Transport{
			MaxIdleConns:        100,
			MaxIdleConnsPerHost: 10,
			IdleConnTimeout:     90 * time.Second,
			TLSHandshakeTimeout: 10 * time.Second,
			DialContext: (&net.Dialer{
				Timeout:   30 * time.Second,
				KeepAlive: 30 * time.Second,
			}).DialContext,
		},
	}

	// 2. 解析 HTTPS
	isHTTPS := false
	switch strings.ToLower(strings.TrimSpace(ossInfo.IsHTTPS)) {
	case "1", "true", "yes", "on":
		isHTTPS = true
	}

	// 3. 创建 MinIO 客户端
	client, err := minio.New(ossInfo.Endpoint, &minio.Options{
		Creds:  credentials.NewStaticV4(ossInfo.AccessKey, ossInfo.SecretKey, ""),
		Secure: isHTTPS,
		// 注入自定义 HTTP 客户端
		Transport: httpClient.Transport,
	})
	if err != nil {
		return nil, fmt.Errorf("创建 Minio 客户端失败： %w", err)
	}

	return &MinioStorage{
		BaseStorage: api.BaseStorage{FileInfo: fileInfo, OssInfo: ossInfo},
		client:      client,
		httpClient:  httpClient,
	}, nil
}

// Upload 上传文件.
func (s *MinioStorage) Upload() (*api.Result, error) {
	if s == nil || s.client == nil || s.OssInfo == nil || s.FileInfo == nil {
		return nil, errors.New("存储状态无效：缺少必填项")
	}

	// 1. 校验输入
	bucket := strings.TrimSpace(s.OssInfo.BucketName)
	if bucket == "" {
		return nil, errors.New("桶名为空")
	}
	if s.FileInfo.Chuck == nil {
		return nil, errors.New("文件读取器无效")
	}
	if s.FileInfo.FileSize <= 0 {
		return nil, errors.New("文件大小必须是大于0")
	}

	// 2. 生成唯一对象键
	uuidName := uuid.New().String()
	ext := strings.ToLower(path.Ext(s.FileInfo.FileName))
	objectKey := uuidName + ext
	if prefix := strings.TrimSpace(s.OssInfo.Prefix); prefix != "" {
		objectKey = path.Join(prefix, objectKey) // 安全拼接
	}

	// 3. 带超时和重试的上传上下文
	ctx, cancel := context.WithTimeout(context.Background(), defaultUploadTimeout)
	defer cancel()

	// 4. 幂等桶检查
	if err := s.ensureBucketExists(ctx, bucket); err != nil {
		return nil, fmt.Errorf("ensure bucket '%s' failed: %w", bucket, err)
	}

	// 5. 文件上传
	_, err := s.client.PutObject(
		ctx,
		bucket,
		objectKey,
		s.FileInfo.Chuck,
		s.FileInfo.FileSize,
		minio.PutObjectOptions{
			ContentType:    s.FileInfo.ContentType,
			SendContentMd5: true,
		},
	)
	if err != nil {
		return nil, fmt.Errorf("上传失败：%w", err)
	}

	// 6. 生成访问 URL
	return &api.Result{
		OriginalName: s.FileInfo.FileName,
		FileName:     uuidName + s.FileInfo.FileSuffix,
		FileSuffix:   s.FileInfo.FileSuffix,
		URL:          s.buildAccessURL(bucket, objectKey),
		Service:      s.OssInfo.ConfigKey,
	}, nil
}

// ensureBucketExists 幂等创建桶
func (s *MinioStorage) ensureBucketExists(ctx context.Context, bucket string) error {
	exists, err := s.client.BucketExists(ctx, bucket)
	if err != nil {
		return fmt.Errorf("检查桶存在情况： %w", err)
	}
	if !exists {
		if err := s.client.MakeBucket(ctx, bucket, minio.MakeBucketOptions{}); err != nil {
			return fmt.Errorf("创建桶：%w", err)
		}
	}
	return nil
}

// buildAccessURL 构造安全的访问 URL
func (s *MinioStorage) buildAccessURL(bucket, objectKey string) string {
	if domain := strings.TrimSpace(s.OssInfo.Domain); domain != "" {
		return fmt.Sprintf("%s/%s/%s", strings.TrimSuffix(domain, "/"), bucket, objectKey)
	}

	scheme := "http"
	if v := strings.ToLower(strings.TrimSpace(s.OssInfo.IsHTTPS)); v == "1" || v == "true" {
		scheme = "https"
	}
	endpoint := strings.TrimSpace(s.OssInfo.Endpoint)
	return fmt.Sprintf("%s://%s/%s/%s", scheme, endpoint, bucket, objectKey)
}
