package command

import (
	"context"
	v1 "lersosa/api/stellar/interface/v1/info"
	client "lersosa/api/stellar/service/v1/info"
	"lersosa/app/stellar/interface/internal/client/info"
)

// InfoModifyCmdExe 星体信息修改命令执行器.
type InfoModifyCmdExe struct {
	client *info.Client
}

// NewInfoModifyCmdExe 构造星体信息修改命令执行器.
func NewInfoModifyCmdExe(client *info.Client) *InfoModifyCmdExe {
	return &InfoModifyCmdExe{client: client}
}

// ExecuteVoid 执行命令.
func (exe *InfoModifyCmdExe) ExecuteVoid(ctx context.Context, request *v1.InfoModifyRequest) error {
	_, err := exe.client.ModifyInfo(ctx, exe.toClientModel(request))

	return err
}

// toClientModel 转换为客户端模型.
func (exe *InfoModifyCmdExe) toClientModel(request *v1.InfoModifyRequest) *client.InfoModifyRequest {
	return &client.InfoModifyRequest{
		InfoId:            request.GetInfoId(),
		Name:              request.GetName(),
		Period:            request.GetPeriod(),
		DispersionMeasure: request.GetDispersionMeasure(),
		RaDeg:             request.GetRaDeg(),
		DecDeg:            request.GetDecDeg(),
		GalacticLongitude: request.GetGalacticLongitude(),
		GalacticLatitude:  request.GetGalacticLatitude(),
		SurveyName:        request.GetSurveyName(),
		UpdateBy:          request.GetUpdateBy(),
		Remark:            request.GetRemark(),
		Version:           request.GetVersion(),
	}
}
