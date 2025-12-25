package query

import (
	"context"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/client/file/dto/co"
	"lersosa/app/resource/service/internal/domain/file/ability"
	"lersosa/app/resource/service/internal/infrastructure/file/convertor"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// FileGetQryExe 获取文件查询执行器.
type FileGetQryExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewFileGetQryExe 创建获取文件查询执行器.
func NewFileGetQryExe(domainService *ability.DomainService, logger log.Logger) *FileGetQryExe {
	return &FileGetQryExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/file-service/application/query/get")),
	}
}

// Execute 执行命令.
func (exe *FileGetQryExe) Execute(ctx context.Context, dto *dto.FileGetQry) (*co.FileCo, error) {
	OssID, err := uuid.Parse(dto.OssID)
	if err != nil {
		exe.log.Errorf("文件信息 ID 无效：%v", err)
		return nil, err
	}

	rv, err := exe.domainService.GetFile(
		ctx,
		OssID,
	)

	if err != nil {
		exe.log.Errorf("获取文件信息失败：%v", err)
		return nil, err
	}

	return convertor.ToFileCo(rv), nil
}
