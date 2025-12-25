package command

import (
	"context"
	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/domain/info/ability"
	"lersosa/app/stellar/service/internal/domain/info/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// InfoModifyCmdExe 星体信息修改命令执行器.
type InfoModifyCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoModifyCmdExe 创建星体信息修改命令执行器.
func NewInfoModifyCmdExe(domainService *ability.DomainService, logger log.Logger) *InfoModifyCmdExe {
	return &InfoModifyCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/command/modify")),
	}
}

// ExecuteVoid 执行命令.
func (exe *InfoModifyCmdExe) ExecuteVoid(ctx context.Context, dto *dto.InfoModifyCmd) error {
	InfoID, err := uuid.Parse(dto.InfoID)
	if err != nil {
		exe.log.Errorf("星体信息 ID 无效：%v", err)
		return status.Errorf(codes.InvalidArgument, "星体信息 ID 无效：%v", err)
	}

	return exe.domainService.ModifyInfo(
		ctx,
		&model.Entity{
			ID:                InfoID,
			Name:              dto.Name,
			Period:            dto.Period,
			DispersionMeasure: dto.DispersionMeasure,
			RaDeg:             dto.RaDeg,
			DecDeg:            dto.DecDeg,
			GalacticLongitude: dto.GalacticLongitude,
			GalacticLatitude:  dto.GalacticLatitude,
			SurveyName:        dto.SurveyName,
			UpdateBy:          dto.UpdateBy,
			Remark:            dto.Remark,
			Version:           dto.Version,
		},
	)
}
