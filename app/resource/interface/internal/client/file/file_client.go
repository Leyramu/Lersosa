package file

import (
	"context"
	v1 "lersosa/api/resource/service/v1/file"
)

// Client 文件客户端层.
type Client struct {
	client v1.FileClient
}

// NewClient 构造文件客户端层.
func NewClient(client v1.FileClient) *Client {
	return &Client{
		client: client,
	}
}

// PageFile 实现分页查询文件方法.
func (client *Client) PageFile(ctx context.Context, request *v1.FilePageRequest) (*v1.FilePageReply, error) {
	return client.client.PageFile(ctx, request)
}

// GetFile 实现查询文件方法.
func (client *Client) GetFile(ctx context.Context, request *v1.FileGetRequest) (*v1.FileGetReply, error) {
	return client.client.GetFile(ctx, request)
}

// SaveFile 实现保存文件方法.
func (client *Client) SaveFile(ctx context.Context, request *v1.FileSaveRequest) (*v1.FileSaveReply, error) {
	return client.client.SaveFile(ctx, request)
}

// ModifyFile 实现修改文件方法.
func (client *Client) ModifyFile(ctx context.Context, request *v1.FileModifyRequest) (*v1.FileModifyReply, error) {
	return client.client.ModifyFile(ctx, request)
}

// RemoveFile 实现删除文件方法.
func (client *Client) RemoveFile(ctx context.Context, request *v1.FileRemoveRequest) (*v1.FileRemoveReply, error) {
	return client.client.RemoveFile(ctx, request)
}
