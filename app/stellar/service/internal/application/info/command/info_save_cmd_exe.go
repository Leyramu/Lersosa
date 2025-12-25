package command

import (
	"context"
	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/domain/info/ability"
	"lersosa/app/stellar/service/internal/domain/info/model"

	"github.com/go-kratos/kratos/v2/log"
)

// InfoSaveCmdExe 星体信息保存命令执行器.
type InfoSaveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoSaveCmdExe 创建星体信息保存命令执行器.
func NewInfoSaveCmdExe(domainService *ability.DomainService, logger log.Logger) *InfoSaveCmdExe {
	return &InfoSaveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/command/save")),
	}
}

// ExecuteVoid 执行命令.
func (exe *InfoSaveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.InfoSaveCmd) error {
	return exe.domainService.SaveInfo(
		ctx,
		&model.Entity{
			Name:              dto.Name,
			FileURL:           dto.FileURL,
			Period:            dto.Period,
			DispersionMeasure: dto.DispersionMeasure,
			RaDeg:             dto.RaDeg,
			DecDeg:            dto.DecDeg,
			GalacticLongitude: dto.GalacticLongitude,
			GalacticLatitude:  dto.GalacticLatitude,
			SurveyName:        dto.SurveyName,
			CreateBy:          dto.CreateBy,
			Remark:            dto.Remark,
		},
	)
}
