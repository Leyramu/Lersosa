package web

import (
	"context"

	v1 "lersosa/api/stellar/service/v1/status"
	status "lersosa/app/stellar/service/internal/client/status/api"

	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/client/status/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// StatusAdapter 星体状态适配层.
type StatusAdapter struct {
	// v1.UnimplementedStatusServer 未实现的星体状态适配层接口.
	v1.UnimplementedStatusServer

	// service 星体状态应用层接口.
	service status.ServiceI

	// log 日志记录器.
	log *log.Helper
}

// NewStatusAdapter 构造星体状态适配层.
func NewStatusAdapter(service status.ServiceI, logger log.Logger) *StatusAdapter {
	return &StatusAdapter{
		service: service,
		log:     log.NewHelper(log.With(logger, "module", "steller/status-service/adapter")),
	}
}

// PageStatus 实现分页查询星体状态方法.
func (adapter *StatusAdapter) PageStatus(ctx context.Context, request *v1.StatusPageRequest) (*v1.StatusPageReply, error) {
	page, err := adapter.service.Page(ctx, dto.NewStatusPageQry(request))

	if err != nil {
		return &v1.StatusPageReply{Rows: nil}, err
	}

	var rows []*v1.StatusPageReply_Status
	for _, item := range page {
		rows = append(rows, co.NewStatusPageCo(item))
	}

	return &v1.StatusPageReply{Rows: rows}, nil
}

// ListStatus 实现查询星体状态列表方法.
func (adapter *StatusAdapter) ListStatus(ctx context.Context, request *v1.StatusListRequest) (*v1.StatusListReply, error) {
	list, err := adapter.service.List(ctx, dto.NewStatusListQry(request))

	if err != nil {
		return &v1.StatusListReply{Lists: nil}, err
	}

	var lists []*v1.StatusListReply_Status
	for _, item := range list {
		lists = append(lists, co.NewStatusListCo(item))
	}

	return &v1.StatusListReply{Lists: lists}, err
}

// GetStatus 实现查询星体状态方法.
func (adapter *StatusAdapter) GetStatus(ctx context.Context, request *v1.StatusGetRequest) (*v1.StatusGetReply, error) {
	get, err := adapter.service.Get(ctx, dto.NewStatusGetQry(request))

	if err != nil {
		return &v1.StatusGetReply{}, err
	}

	return co.NewStatusGetCo(get), nil
}

// SaveStatus 实现保存星体状态方法.
func (adapter *StatusAdapter) SaveStatus(ctx context.Context, request *v1.StatusSaveRequest) (*v1.StatusSaveReply, error) {
	return &v1.StatusSaveReply{}, adapter.service.Save(ctx, dto.NewStatusSaveCmd(request))
}

// ModifyStatus 实现修改星体状态方法.
func (adapter *StatusAdapter) ModifyStatus(ctx context.Context, request *v1.StatusModifyRequest) (*v1.StatusModifyReply, error) {
	return &v1.StatusModifyReply{}, adapter.service.Modify(ctx, dto.NewStatusModifyCmd(request))
}

// RemoveStatus 实现删除星体状态方法.
func (adapter *StatusAdapter) RemoveStatus(ctx context.Context, request *v1.StatusRemoveRequest) (*v1.StatusRemoveReply, error) {
	return &v1.StatusRemoveReply{}, adapter.service.Remove(ctx, dto.NewStatusRemoveCmd(request))
}
