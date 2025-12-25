package status

import (
	"context"
	v1 "lersosa/api/stellar/service/v1/status"
)

// Client 星体状态客户端层.
type Client struct {
	client v1.StatusClient
}

// NewClient 构造星体状态客户端层.
func NewClient(client v1.StatusClient) *Client {
	return &Client{
		client: client,
	}
}

// PageStatus 分页查询星体状态方法.
func (client *Client) PageStatus(ctx context.Context, request *v1.StatusPageRequest) (*v1.StatusPageReply, error) {
	return client.client.PageStatus(ctx, request)
}

// ListStatus 获取星体状态列表方法.
func (client *Client) ListStatus(ctx context.Context, request *v1.StatusListRequest) (*v1.StatusListReply, error) {
	return client.client.ListStatus(ctx, request)
}

// GetStatus 获取星体状态方法.
func (client *Client) GetStatus(ctx context.Context, request *v1.StatusGetRequest) (*v1.StatusGetReply, error) {
	return client.client.GetStatus(ctx, request)
}

// SaveStatus 保存星体状态方法.
func (client *Client) SaveStatus(ctx context.Context, request *v1.StatusSaveRequest) (*v1.StatusSaveReply, error) {
	return client.client.SaveStatus(ctx, request)
}

// ModifyStatus 修改星体状态方法.
func (client *Client) ModifyStatus(ctx context.Context, request *v1.StatusModifyRequest) (*v1.StatusModifyReply, error) {
	return client.client.ModifyStatus(ctx, request)
}

// RemoveStatus 删除星体状态方法.
func (client *Client) RemoveStatus(ctx context.Context, request *v1.StatusRemoveRequest) (*v1.StatusRemoveReply, error) {
	return client.client.RemoveStatus(ctx, request)
}
