package query

import (
	"context"
	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/client/status/dto/co"
	"lersosa/app/stellar/service/internal/domain/status/ability"
	"lersosa/app/stellar/service/internal/domain/status/model"
	"lersosa/app/stellar/service/internal/infrastructure/status/convertor"

	"github.com/go-kratos/kratos/v2/log"
)

// StatusPageQryExe 星体状态分页查询执行器.
type StatusPageQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusPageQryExe 创建星体状态分页查询执行器.
func NewStatusPageQryExe(domainService *ability.DomainService, logger log.Logger) *StatusPageQryExe {
	return &StatusPageQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *StatusPageQryExe) Execute(ctx context.Context, dto *dto.StatusPageQry) ([]*co.StatusCo, error) {
	rv, err := exe.domainService.PageStatus(
		ctx,
		&model.Condition{
			PageNum:  dto.PageNum,
			PageSize: dto.PageSize,
			Flag:     dto.Flag,
			Check:    dto.Check,
			CreateBy: dto.CreateBy,
			UpdateBy: dto.UpdateBy,
		},
	)

	if err != nil {
		exe.log.Errorf("分页查询错误：%v", err)
		return nil, err
	}

	return convertor.ToStatusCos(rv), nil
}
