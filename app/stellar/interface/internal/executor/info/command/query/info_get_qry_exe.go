package query

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	client "lersosa/api/stellar/service/v1/info"
	"lersosa/app/stellar/interface/internal/client/info"
)

// InfoGetQryExe 获取星体信息查询执行器.
type InfoGetQryExe struct {
	client *info.Client
}

// NewInfoGetQryExe 构造获取星体信息查询执行器.
func NewInfoGetQryExe(client *info.Client) *InfoGetQryExe {
	return &InfoGetQryExe{client: client}
}

// Execute 执行命令.
func (exe *InfoGetQryExe) Execute(ctx context.Context, request *v1.InfoGetRequest) (*v1.InfoGetReply, error) {
	return exe.toServerModel(
		exe.client.GetInfo(
			ctx, exe.toClientModel(request),
		),
	)
}

// toClientModel 转换为客户端模型.
func (exe *InfoGetQryExe) toClientModel(request *v1.InfoGetRequest) *client.InfoGetRequest {
	return &client.InfoGetRequest{
		InfoId: request.GetInfoId(),
	}
}

// toServerModel 转换为服务端模型.
func (exe *InfoGetQryExe) toServerModel(reply *client.InfoGetReply, err error) (*v1.InfoGetReply, error) {
	return &v1.InfoGetReply{
		Data: &v1.InfoGetReply_Info{
			InfoId:            reply.GetInfoId(),
			FileUrl:           reply.GetFileUrl(),
			Name:              reply.GetName(),
			Period:            reply.GetPeriod(),
			DispersionMeasure: reply.GetDispersionMeasure(),
			RaDeg:             reply.GetRaDeg(),
			DecDeg:            reply.GetDecDeg(),
			GalacticLongitude: reply.GetGalacticLongitude(),
			GalacticLatitude:  reply.GetGalacticLatitude(),
			SurveyName:        reply.GetSurveyName(),
			CreateBy:          reply.GetCreateBy(),
			CreateTime:        reply.GetCreateTime(),
			UpdateBy:          reply.GetUpdateBy(),
			UpdateTime:        reply.GetUpdateTime(),
			Remark:            reply.GetRemark(),
			Version:           reply.GetVersion(),
		},
	}, err
}
