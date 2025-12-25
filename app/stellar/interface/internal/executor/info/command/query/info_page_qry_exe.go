package query

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	infoclient "lersosa/api/stellar/service/v1/info"
	statusclient "lersosa/api/stellar/service/v1/status"
	"lersosa/app/stellar/interface/internal/client/info"
	"lersosa/app/stellar/interface/internal/client/status"
	"time"

	"golang.org/x/sync/errgroup"
)

// InfoPageQryExe 星体信息分页查询执行器.
type InfoPageQryExe struct {
	infoClient   *info.Client
	statusClient *status.Client
}

// NewInfoPageQryExe 构造星体信息分页查询执行器.
func NewInfoPageQryExe(infoClient *info.Client, statusClient *status.Client) *InfoPageQryExe {
	return &InfoPageQryExe{
		infoClient:   infoClient,
		statusClient: statusClient,
	}
}

// Execute 执行命令.
func (exe *InfoPageQryExe) Execute(ctx context.Context, request *v1.InfoPageRequest) (*v1.InfoPageReply, error) {
	// 设置兜底超时
	ctx, cancel := context.WithTimeout(ctx, 5*time.Second)
	defer cancel()

	var infoList *infoclient.InfoListReply
	var statusList *statusclient.StatusListReply

	g, ctx := errgroup.WithContext(ctx)

	// 获取星体信息列表
	g.Go(func() error {
		var err error
		infoList, err = exe.infoClient.ListInfo(ctx, &infoclient.InfoListRequest{
			Period:            request.GetPeriod(),
			DispersionMeasure: request.GetDispersionMeasure(),
			RaDeg:             request.GetRaDeg(),
			DecDeg:            request.GetDecDeg(),
			GalacticLongitude: request.GetGalacticLongitude(),
			GalacticLatitude:  request.GetGalacticLatitude(),
			SurveyName:        request.GetSurveyName(),
		})
		return err
	})

	// 获取星体状态列表
	g.Go(func() error {
		var err error
		statusList, err = exe.statusClient.ListStatus(ctx, &statusclient.StatusListRequest{
			Flag:  request.GetFlag(),
			Check: request.GetCheck(),
		})
		return err
	})

	if err := g.Wait(); err != nil {
		return nil, err
	}

	// 构建星体信息映射表
	infoMap := make(map[string]*infoclient.InfoListReply_Info)
	for _, item := range infoList.GetLists() {
		if item != nil && item.GetInfoId() != "" {
			infoMap[item.GetInfoId()] = item
		}
	}

	// 构建有序星体信息列表
	orderedInfoList := make([]*infoclient.InfoListReply_Info, 0)
	for _, statusItem := range statusList.GetLists() {
		if statusItem == nil {
			continue
		}

		infoItem := infoMap[statusItem.GetInfoId()]
		if infoItem == nil {
			continue
		}

		orderedInfoList = append(orderedInfoList, infoItem)
	}

	// 构建星体信息分页响应
	total := len(orderedInfoList)

	// 分页参数
	pageNum := int(request.GetPageNum())
	pageSize := int(request.GetPageSize())

	// 校验并设置默认值
	if pageNum <= 0 {
		pageNum = 1
	}
	if pageSize <= 0 {
		pageSize = 20
	}
	if pageSize > 1000 {
		pageSize = 1000
	}

	// 计算分页切片范围
	offset := (pageNum - 1) * pageSize
	if offset >= total {
		// 页码超出范围，返回空列表
		return exe.toServerModel(&infoclient.InfoListReply{
			Lists: []*infoclient.InfoListReply_Info{},
		}), nil
	}

	end := offset + pageSize
	if end > total {
		end = total
	}

	// 切片
	pageItems := orderedInfoList[offset:end]

	return exe.toServerModel(&infoclient.InfoListReply{Lists: pageItems}), nil
}

// toClientModel 转换为客户端模型.
func (exe *InfoPageQryExe) toClientModel(request *v1.InfoPageRequest) *infoclient.InfoPageRequest {
	return &infoclient.InfoPageRequest{
		PageNum:           request.GetPageNum(),
		PageSize:          request.GetPageSize(),
		Period:            request.GetPeriod(),
		DispersionMeasure: request.GetDispersionMeasure(),
		RaDeg:             request.GetRaDeg(),
		DecDeg:            request.GetDecDeg(),
		GalacticLongitude: request.GetGalacticLongitude(),
		GalacticLatitude:  request.GetGalacticLatitude(),
		SurveyName:        request.GetSurveyName(),
	}
}

// toServerModel 转换为服务端模型.
func (exe *InfoPageQryExe) toServerModel(reply *infoclient.InfoListReply) *v1.InfoPageReply {
	rows := make([]*v1.InfoPageReply_Info, len(reply.GetLists()))
	for i, item := range reply.GetLists() {
		rows[i] = &v1.InfoPageReply_Info{
			InfoId:            item.GetInfoId(),
			FileUrl:           item.GetFileUrl(),
			Name:              item.GetName(),
			Period:            item.GetPeriod(),
			DispersionMeasure: item.GetDispersionMeasure(),
			RaDeg:             item.GetRaDeg(),
			DecDeg:            item.GetDecDeg(),
			GalacticLongitude: item.GetGalacticLongitude(),
			GalacticLatitude:  item.GetGalacticLatitude(),
			SurveyName:        item.GetSurveyName(),
			CreateBy:          item.GetCreateBy(),
			CreateTime:        item.GetCreateTime(),
			UpdateBy:          item.GetUpdateBy(),
			UpdateTime:        item.GetUpdateTime(),
			Remark:            item.GetRemark(),
			Version:           item.GetVersion(),
		}
	}

	return &v1.InfoPageReply{
		Rows:  rows,
		Total: int64(len(rows)),
	}
}
