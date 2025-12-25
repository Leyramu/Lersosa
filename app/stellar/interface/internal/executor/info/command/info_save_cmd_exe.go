package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	client "lersosa/api/stellar/service/v1/info"
	"lersosa/app/stellar/interface/internal/client/info"
)

// InfoSaveCmdExe 星体信息保存命令执行器.
type InfoSaveCmdExe struct {
	client *info.Client
}

// NewInfoSaveCmdExe 构造星体信息保存命令执行器.
func NewInfoSaveCmdExe(client *info.Client) *InfoSaveCmdExe {
	return &InfoSaveCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *InfoSaveCmdExe) ExecuteVoid(ctx context.Context, request *v1.InfoSaveRequest) error {
	_, err := exe.client.SaveInfo(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *InfoSaveCmdExe) toClientModel(request *v1.InfoSaveRequest) *client.InfoSaveRequest {
	return &client.InfoSaveRequest{
		Name:              request.GetName(),
		Period:            request.GetPeriod(),
		DispersionMeasure: request.GetDispersionMeasure(),
		RaDeg:             request.GetRaDeg(),
		DecDeg:            request.GetDecDeg(),
		GalacticLongitude: request.GetGalacticLongitude(),
		GalacticLatitude:  request.GetGalacticLatitude(),
		SurveyName:        request.GetSurveyName(),
		CreateBy:          request.GetCreateBy(),
		Remark:            request.GetRemark(),
	}
}
