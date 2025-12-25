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

// StatusRemoveCmdExe 星体状态删除命令执行器.
type StatusRemoveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusRemoveCmdExe 创建星体状态删除命令执行器.
func NewStatusRemoveCmdExe(domainService *ability.DomainService, logger log.Logger) *StatusRemoveCmdExe {
	return &StatusRemoveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/command/remove")),
	}
}

// ExecuteVoid 执行命令.
func (exe *StatusRemoveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.StatusRemoveCmd) error {
	var es []model.Entity
	for _, e := range dto.Statuses {
		StatusID, err := uuid.Parse(e.StatusID)
		if err != nil {
			exe.log.Errorf("星体状态 ID 无效：%v", err)
			return status.Errorf(codes.InvalidArgument, "星体状态 ID 无效：%v", err)
		}

		es = append(es, model.Entity{
			ID:      StatusID,
			Version: e.Version,
		})
	}

	return exe.domainService.RemoveStatus(
		ctx,
		&es,
	)
}
