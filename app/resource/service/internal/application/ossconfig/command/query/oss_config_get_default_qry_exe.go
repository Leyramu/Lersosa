package query

import (
	"context"
	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/convertor"

	"github.com/go-kratos/kratos/v2/log"
)

// OssConfigGetDefaultQryExe 获取默认资源配置查询执行器.
type OssConfigGetDefaultQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigGetDefaultQryExe 构造获取默认资源配置查询执行器.
func NewOssConfigGetDefaultQryExe(domainService *ability.DomainService, logger log.Logger) *OssConfigGetDefaultQryExe {
	return &OssConfigGetDefaultQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/query/getDefault")),
	}
}

// Execute 执行命令.
func (exe *OssConfigGetDefaultQryExe) Execute(ctx context.Context, _ *dto.OssConfigGetDefaultQry) (*co.OssConfigCo, error) {
	rv, err := exe.domainService.GetDefaultOssConfig(ctx)
	if err != nil {
		exe.log.Errorf("获取默认资源配置信息失败：%v", err)
		return nil, err
	}

	return convertor.ToOssConfigCo(rv), nil
}
