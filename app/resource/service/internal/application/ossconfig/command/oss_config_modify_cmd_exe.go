package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// OssConfigModifyCmdExe 资源配置修改命令执行器.
type OssConfigModifyCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigModifyCmdExe 创建资源配置修改命令执行器.
func NewOssConfigModifyCmdExe(domainService *ability.DomainService, logger log.Logger) *OssConfigModifyCmdExe {
	return &OssConfigModifyCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/command/modify")),
	}
}

// ExecuteVoid 执行命令.
func (exe *OssConfigModifyCmdExe) ExecuteVoid(ctx context.Context, dto *dto.OssConfigModifyCmd) error {
	OssConfigID, err := uuid.Parse(dto.OssConfigID)
	if err != nil {
		exe.log.Errorf("资源配置信息 ID 无效：%v", err)
		return err
	}

	return exe.domainService.ModifyOssConfig(
		ctx,
		&model.Entity{
			ID:           OssConfigID,
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
			UpdateBy:     dto.UpdateBy,
			Remark:       dto.Remark,
			Version:      dto.Version,
		},
	)
}
