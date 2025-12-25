package query

import (
	"context"
	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/client/status/dto/co"
	"lersosa/app/stellar/service/internal/domain/status/ability"
	"lersosa/app/stellar/service/internal/infrastructure/status/convertor"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// StatusGetQryExe 获取星体状态查询执行器.
type StatusGetQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusGetQryExe 创建获取星体状态查询执行器.
func NewStatusGetQryExe(domainService *ability.DomainService, logger log.Logger) *StatusGetQryExe {
	return &StatusGetQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/query/get")),
	}
}

// Execute 执行命令.
func (exe *StatusGetQryExe) Execute(ctx context.Context, dto *dto.StatusGetQry) (*co.StatusCo, error) {
	StatusID, err := uuid.Parse(dto.StatusID)
	if err != nil {
		exe.log.Errorf("星体状态 ID 无效：%v", err)
		return nil, status.Errorf(codes.InvalidArgument, "星体状态 ID 无效：%v", err)
	}

	rv, err := exe.domainService.GetStatus(
		ctx,
		StatusID,
	)

	if err != nil {
		exe.log.Errorf("获取星体状态失败：%v", err)
		return nil, err
	}

	return convertor.ToStatusCo(rv), nil
}
