package query

import (
	"context"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/client/file/dto/co"
	"lersosa/app/resource/service/internal/domain/file/ability"
	"lersosa/app/resource/service/internal/domain/file/model"
	"lersosa/app/resource/service/internal/infrastructure/file/convertor"

	"github.com/go-kratos/kratos/v2/log"
)

// FilePageQryExe 文件分页查询执行器.
type FilePageQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewFilePageQryExe 创建文件分页查询执行器.
func NewFilePageQryExe(domainService *ability.DomainService, logger log.Logger) *FilePageQryExe {
	return &FilePageQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/file-service/application/query/page")),
	}
}

// Execute 执行命令.
func (exe *FilePageQryExe) Execute(ctx context.Context, dto *dto.FilePageQry) ([]*co.FileCo, error) {
	rv, err := exe.domainService.PageFile(
		ctx,
		&model.Condition{
			PageNum:  dto.PageNum,
			PageSize: dto.PageSize,
		},
	)

	if err != nil {
		exe.log.Errorf("分页查询错误：%v", err)
		return nil, err
	}

	return convertor.ToFileCos(rv), nil
}
