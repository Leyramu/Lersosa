package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"

	"github.com/go-kratos/kratos/v2/log"
)

// OssConfigSaveCmdExe 资源配置保存命令执行器.
type OssConfigSaveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigSaveCmdExe 创建资源配置保存命令执行器.
func NewOssConfigSaveCmdExe(domainService *ability.DomainService, logger log.Logger) *OssConfigSaveCmdExe {
	return &OssConfigSaveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/command/save")),
	}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigSaveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.OssConfigSaveCmd) error {
	return exe.domainService.SaveOssConfig(
		ctx,
		&model.Entity{
			ConfigKey:    dto.ConfigKey,
			AccessKey:    dto.AccessKey,
			SecretKey:    dto.SecretKey,
			BucketName:   dto.BucketName,
			Prefix:       dto.Prefix,
			Endpoint:     dto.Endpoint,
			Domain:       dto.Domain,
			IsHTTPS:      dto.IsHTTPS,
			Region:       dto.Region,
			AccessPolicy: dto.AccessPolicy,
			Status:       dto.Status,
			Ext1:         dto.Ext1,
			CreateDept:   dto.CreateDept,
			CreateBy:     dto.CreateBy,
			Remark:       dto.Remark,
		},
	)
}
