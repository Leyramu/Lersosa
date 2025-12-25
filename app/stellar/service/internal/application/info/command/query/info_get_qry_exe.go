package query

import (
	"context"
	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/client/info/dto/co"
	"lersosa/app/stellar/service/internal/domain/info/ability"
	"lersosa/app/stellar/service/internal/infrastructure/info/convertor"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// InfoGetQryExe 获取星体信息查询执行器.
type InfoGetQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoGetQryExe 创建获取星体信息查询执行器.
func NewInfoGetQryExe(domainService *ability.DomainService, logger log.Logger) *InfoGetQryExe {
	return &InfoGetQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/query/get")),
	}
}

// Execute 执行命令.
func (exe *InfoGetQryExe) Execute(ctx context.Context, dto *dto.InfoGetQry) (*co.InfoCo, error) {
	InfoID, err := uuid.Parse(dto.InfoID)
	if err != nil {
		exe.log.Errorf("星体信息 ID 无效：%v", err)
		return nil, status.Errorf(codes.InvalidArgument, "星体信息 ID 无效：%v", err)
	}

	rv, err := exe.domainService.GetInfo(
		ctx,
		InfoID,
	)

	if err != nil {
		exe.log.Errorf("获取星体信息失败：%v", err)
		return nil, err
	}

	return convertor.ToInfoCo(rv), nil
}
