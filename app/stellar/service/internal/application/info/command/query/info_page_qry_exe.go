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

// InfoPageQryExe 星体信息分页查询执行器.
type InfoPageQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoPageQryExe 创建星体信息分页查询执行器.
func NewInfoPageQryExe(domainService *ability.DomainService, logger log.Logger) *InfoPageQryExe {
	return &InfoPageQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *InfoPageQryExe) Execute(ctx context.Context, dto *dto.InfoPageQry) ([]*co.InfoCo, error) {
	rv, err := exe.domainService.PageInfo(
		ctx,
		&model.Condition{
			PageNum:           dto.PageNum,
			PageSize:          dto.PageSize,
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
		exe.log.Errorf("分页查询错误：%v", err)
		return nil, err
	}

	return convertor.ToInfoCos(rv), nil
}
