package command

import (
	"context"
	"lersosa/app/stellar/service/internal/client/status/dto"
	"lersosa/app/stellar/service/internal/domain/status/ability"
	"lersosa/app/stellar/service/internal/domain/status/model"

	"github.com/go-kratos/kratos/v2/log"
)

// StatusSaveCmdExe 星体状态保存命令执行器.
type StatusSaveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewStatusSaveCmdExe 创建星体状态保存命令执行器.
func NewStatusSaveCmdExe(domainService *ability.DomainService, logger log.Logger) *StatusSaveCmdExe {
	return &StatusSaveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/status-service/application/command/save")),
	}
}

// ExecuteVoid 执行命令.
func (exe *StatusSaveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.StatusSaveCmd) error {
	return exe.domainService.SaveStatus(
		ctx,
		&model.Entity{
			Score:    dto.Score,
			Flag:     dto.Flag,
			Check:    dto.Check,
			CreateBy: dto.CreateBy,
			Remark:   dto.Remark,
		},
	)
}
