package ossconfig

import (
	"context"
	v1 "lersosa/api/resource/service/v1/ossconfig"
)

// Client 资源配置客户端层.
type Client struct {
	client v1.OssConfigClient
}

// NewClient 构造资源配置客户端层.
func NewClient(client v1.OssConfigClient) *Client {
	return &Client{
		client: client,
	}
}

// PageOssConfig 实现分页查询资源配置方法.
func (client *Client) PageOssConfig(ctx context.Context, request *v1.OssConfigPageRequest) (*v1.OssConfigPageReply, error) {
	return client.client.PageOssConfig(ctx, request)
}

// GetOssConfig 实现获取资源配置方法.
func (client *Client) GetOssConfig(ctx context.Context, request *v1.OssConfigGetRequest) (*v1.OssConfigGetReply, error) {
	return client.client.GetOssConfig(ctx, request)
}

// GetDefaultOssConfig 实现获取默认资源配置方法.
func (client *Client) GetDefaultOssConfig(ctx context.Context, request *v1.OssConfigGetDefaultRequest) (*v1.OssConfigGetDefaultReply, error) {
	return client.client.GetDefaultOssConfig(ctx, request)
}

// SaveOssConfig 实现保存资源配置方法.
func (client *Client) SaveOssConfig(ctx context.Context, request *v1.OssConfigSaveRequest) (*v1.OssConfigSaveReply, error) {
	return client.client.SaveOssConfig(ctx, request)
}

// ModifyOssConfig 实现修改资源配置方法.
func (client *Client) ModifyOssConfig(ctx context.Context, request *v1.OssConfigModifyRequest) (*v1.OssConfigModifyReply, error) {
	return client.client.ModifyOssConfig(ctx, request)
}

// RemoveOssConfig 实现删除资源配置方法.
func (client *Client) RemoveOssConfig(ctx context.Context, request *v1.OssConfigRemoveRequest) (*v1.OssConfigRemoveReply, error) {
	return client.client.RemoveOssConfig(ctx, request)
}
