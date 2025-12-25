package web

import (
	"context"

	v1 "lersosa/api/stellar/service/v1/info"
	info "lersosa/app/stellar/service/internal/client/info/api"

	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/client/info/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// InfoAdapter 星体信息适配层.
type InfoAdapter struct {
	// v1.UnimplementedInfoServer 未实现的星体信息适配层接口.
	v1.UnimplementedInfoServer

	// service 星体信息应用层接口.
	service info.ServiceI

	// log 日志记录器.
	log *log.Helper
}

// NewInfoAdapter 构造星体信息适配层.
func NewInfoAdapter(service info.ServiceI, logger log.Logger) *InfoAdapter {
	return &InfoAdapter{
		service: service,
		log:     log.NewHelper(log.With(logger, "module", "steller/info-service/adapter")),
	}
}

// PageInfo 实现分页查询星体信息方法.
func (adapter *InfoAdapter) PageInfo(ctx context.Context, request *v1.InfoPageRequest) (*v1.InfoPageReply, error) {
	page, err := adapter.service.Page(ctx, dto.NewInfoPageQry(request))

	if err != nil {
		return &v1.InfoPageReply{Rows: nil}, err
	}

	var rows []*v1.InfoPageReply_Info
	for _, item := range page {
		rows = append(rows, co.NewInfoPageCo(item))
	}

	return &v1.InfoPageReply{Rows: rows}, nil
}

// ListInfo 实现查询星体信息列表方法.
func (adapter *InfoAdapter) ListInfo(ctx context.Context, request *v1.InfoListRequest) (*v1.InfoListReply, error) {
	list, err := adapter.service.List(ctx, dto.NewInfoListQry(request))

	if err != nil {
		return &v1.InfoListReply{Lists: nil}, err
	}

	var lists []*v1.InfoListReply_Info
	for _, item := range list {
		lists = append(lists, co.NewInfoListCo(item))
	}

	return &v1.InfoListReply{Lists: lists}, nil
}

// GetInfo 实现查询星体信息方法.
func (adapter *InfoAdapter) GetInfo(ctx context.Context, request *v1.InfoGetRequest) (*v1.InfoGetReply, error) {
	get, err := adapter.service.Get(ctx, dto.NewInfoGetQry(request))

	if err != nil {
		return &v1.InfoGetReply{}, err
	}

	return co.NewInfoGetCo(get), nil
}

// SaveInfo 实现保存星体信息方法.
func (adapter *InfoAdapter) SaveInfo(ctx context.Context, request *v1.InfoSaveRequest) (*v1.InfoSaveReply, error) {
	return &v1.InfoSaveReply{}, adapter.service.Save(ctx, dto.NewInfoSaveCmd(request))
}

// ModifyInfo 实现修改星体信息方法.
func (adapter *InfoAdapter) ModifyInfo(ctx context.Context, request *v1.InfoModifyRequest) (*v1.InfoModifyReply, error) {
	return &v1.InfoModifyReply{}, adapter.service.Modify(ctx, dto.NewInfoModifyCmd(request))
}

// RemoveInfo 实现删除星体信息方法.
func (adapter *InfoAdapter) RemoveInfo(ctx context.Context, request *v1.InfoRemoveRequest) (*v1.InfoRemoveReply, error) {
	return &v1.InfoRemoveReply{}, adapter.service.Remove(ctx, dto.NewInfoRemoveCmd(request))
}
