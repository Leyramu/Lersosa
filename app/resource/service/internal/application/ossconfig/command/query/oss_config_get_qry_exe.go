package query

import (
	"context"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/convertor"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// OssConfigGetQryExe 获取资源配置查询执行器.
type OssConfigGetQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigGetQryExe 创建获取资源配置查询执行器.
func NewOssConfigGetQryExe(domainService *ability.DomainService, logger log.Logger) *OssConfigGetQryExe {
	return &OssConfigGetQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/query/get")),
	}
}

// Execute 执行命令.
func (exe *OssConfigGetQryExe) Execute(ctx context.Context, dto *dto.OssConfigGetQry) (*co.OssConfigCo, error) {
	OssConfigID, err := uuid.Parse(dto.OssConfigID)
	if err != nil {
		exe.log.Errorf("资源配置信息 ID 无效：%v", err)
		return nil, err
	}

	rv, err := exe.domainService.GetOssConfig(
		ctx,
		OssConfigID,
	)

	if err != nil {
		exe.log.Errorf("获取资源配置信息失败：%v", err)
		return nil, err
	}

	return convertor.ToOssConfigCo(rv), nil
}
