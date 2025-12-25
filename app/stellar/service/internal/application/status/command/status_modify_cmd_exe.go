package command

import (
	"context"
	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/domain/status/ability"
	"lersosa/app/stellar/service/internal/domain/status/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// StatusModifyCmdExe 星体状态修改命令执行器.
type StatusModifyCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusModifyCmdExe 创建星体状态修改命令执行器.
func NewStatusModifyCmdExe(domainService *ability.DomainService, logger log.Logger) *StatusModifyCmdExe {
	return &StatusModifyCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/command/modify")),
	}
}

// ExecuteVoid 执行命令.
func (exe *StatusModifyCmdExe) ExecuteVoid(ctx context.Context, dto *dto.StatusModifyCmd) error {
	StatusID, err := uuid.Parse(dto.StatusID)
	if err != nil {
		exe.log.Errorf("星体状态 ID 无效：%v", err)
		return status.Errorf(codes.InvalidArgument, "星体状态 ID 无效：%v", err)
	}

	return exe.domainService.ModifyStatus(
		ctx,
		&model.Entity{
			ID:       StatusID,
			Flag:     dto.Flag,
			Check:    dto.Check,
			UpdateBy: dto.UpdateBy,
			Remark:   dto.Remark,
			Version:  dto.Version,
		},
	)
}
