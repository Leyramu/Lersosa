package query

import (
	"context"
	"lersosa/app/stellar/service/internal/domain/info/ability"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"lersosa/app/stellar/service/internal/infrastructure/info/convertor"

	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/client/info/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// InfoListQryExe 星体信息列表查询执行器.
type InfoListQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoListQryExe 创建星体信息列表查询执行器.
func NewInfoListQryExe(domainService *ability.DomainService, logger log.Logger) *InfoListQryExe {
	return &InfoListQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *InfoListQryExe) Execute(ctx context.Context, dto *dto.InfoListQry) ([]*co.InfoCo, error) {
	rv, err := exe.domainService.ListInfo(
		ctx,
		&model.Condition{
			Period:            dto.Period,
			DispersionMeasure: dto.DispersionMeasure,
			RaDeg:             dto.RaDeg,
			DecDeg:            dto.DecDeg,
			GalacticLongitude: dto.GalacticLongitude,
			GalacticLatitude:  dto.GalacticLatitude,
			SurveyName:        dto.SurveyName,
		},
	)

	if err != nil {
		exe.log.Errorf("列表查询错误：%v", err)
		return nil, err
	}

	return convertor.ToInfoCos(rv), nil
}
