package command

import (
	"context"
	"lersosa/app/stellar/service/internal/client/info/dto"
	"lersosa/app/stellar/service/internal/domain/info/ability"
	"lersosa/app/stellar/service/internal/domain/info/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// InfoRemoveCmdExe 星体信息删除命令执行器.
type InfoRemoveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewInfoRemoveCmdExe 创建星体信息删除命令执行器.
func NewInfoRemoveCmdExe(domainService *ability.DomainService, logger log.Logger) *InfoRemoveCmdExe {
	return &InfoRemoveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "steller/info-service/application/command/remove")),
	}
}

// ExecuteVoid 执行命令.
func (exe *InfoRemoveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.InfoRemoveCmd) error {
	var entities []model.Entity
	for _, entity := range dto.Infos {
		InfoID, err := uuid.Parse(entity.InfoID)
		if err != nil {
			exe.log.Errorf("星体信息 ID 无效：%v", err)
			return status.Errorf(codes.InvalidArgument, "星体信息 ID 无效：%v", err)
		}

		entities = append(entities, model.Entity{
			ID:      InfoID,
			Version: entity.Version,
		})
	}

	return exe.domainService.RemoveInfo(
		ctx,
		&entities,
	)
}
