package query

import (
	"context"
	"lersosa/app/resource/service/internal/domain/ossconfig/ability"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/convertor"

	"lersosa/app/resource/service/internal/client/ossconfig/dto"
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"

	"github.com/go-kratos/kratos/v2/log"
)

// OssConfigPageQryExe 资源配置分页查询执行器.
type OssConfigPageQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewOssConfigPageQryExe 创建资源配置分页查询执行器.
func NewOssConfigPageQryExe(domainService *ability.DomainService, logger log.Logger) *OssConfigPageQryExe {
	return &OssConfigPageQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/oss-config-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *OssConfigPageQryExe) Execute(ctx context.Context, dto *dto.OssConfigPageQry) ([]*co.OssConfigCo, error) {
	rv, err := exe.domainService.PageOssConfig(
		ctx,
		&model.Condition{
			PageNum:    dto.PageNum,
			PageSize:   dto.PageSize,
			ConfigKey:  dto.ConfigKey,
			BucketName: dto.BucketName,
			Status:     dto.Status,
		},
	)

	if err != nil {
		exe.log.Errorf("分页查询错误：%v", err)
		return nil, err
	}

	return convertor.ToOssConfigCos(rv), nil
}
