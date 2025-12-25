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

// StatusListQryExe 星体状态分页查询执行器.
type StatusListQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusListQryExe 创建星体状态分页查询执行器.
func NewStatusListQryExe(domainService *ability.DomainService, logger log.Logger) *StatusListQryExe {
	return &StatusListQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *StatusListQryExe) Execute(ctx context.Context, dto *dto.StatusListQry) ([]*co.StatusCo, error) {
	rv, err := exe.domainService.ListStatus(
		ctx,
		&model.Condition{
			Check: dto.Check,
			Flag:  dto.Flag,
		},
	)

	if err != nil {
		exe.log.Errorf("分页查询错误：%v", err)
		return nil, err
	}

	return convertor.ToStatusCos(rv), nil
}
