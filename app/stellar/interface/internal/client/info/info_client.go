package info

import (
	"context"
	v1 "lersosa/api/stellar/service/v1/info"
)

// Client 星体信息客户端层.
type Client struct {
	client v1.InfoClient
}

// NewClient 构造星体信息客户端层.
func NewClient(client v1.InfoClient) *Client {
	return &Client{
		client: client,
	}
}

// PageInfo 分页查询星体信息方法.
func (client *Client) PageInfo(ctx context.Context, request *v1.InfoPageRequest) (*v1.InfoPageReply, error) {
	return client.client.PageInfo(ctx, request)
}

// ListInfo 查询星体信息列表方法.
func (client *Client) ListInfo(ctx context.Context, request *v1.InfoListRequest) (*v1.InfoListReply, error) {
	return client.client.ListInfo(ctx, request)
}

// GetInfo 查询星体信息方法.
func (client *Client) GetInfo(ctx context.Context, request *v1.InfoGetRequest) (*v1.InfoGetReply, error) {
	return client.client.GetInfo(ctx, request)
}

// SaveInfo 保存星体信息方法.
func (client *Client) SaveInfo(ctx context.Context, request *v1.InfoSaveRequest) (*v1.InfoSaveReply, error) {
	return client.client.SaveInfo(ctx, request)
}

// ModifyInfo 修改星体信息方法.
func (client *Client) ModifyInfo(ctx context.Context, request *v1.InfoModifyRequest) (*v1.InfoModifyReply, error) {
	return client.client.ModifyInfo(ctx, request)
}

// RemoveInfo 删除星体信息方法.
func (client *Client) RemoveInfo(ctx context.Context, request *v1.InfoRemoveRequest) (*v1.InfoRemoveReply, error) {
	return client.client.RemoveInfo(ctx, request)
}
