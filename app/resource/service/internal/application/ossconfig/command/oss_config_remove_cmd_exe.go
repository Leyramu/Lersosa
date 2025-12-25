package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// OssConfigRemoveCmdExe 资源配置删除命令执行器.
type OssConfigRemoveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigRemoveCmdExe 创建资源配置删除命令执行器.
func NewOssConfigRemoveCmdExe(domainService *ability.DomainService, logger log.Logger) *OssConfigRemoveCmdExe {
	return &OssConfigRemoveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/command/remove")),
	}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigRemoveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.OssConfigRemoveCmd) error {
	var entities []model.Entity
	for _, entity := range dto.OssConfigs {
		OssConfigID, err := uuid.Parse(entity.OssConfigID)
		if err != nil {
			exe.log.Errorf("资源配置信息 ID 无效：%v", err)
			return err
		}
		entities = append(entities, model.Entity{
			ID:      OssConfigID,
			Version: entity.Version,
		})
	}

	return exe.domainService.RemoveOssConfig(
		ctx,
		&entities,
	)
}
