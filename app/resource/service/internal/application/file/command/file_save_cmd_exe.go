package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/domain/file/ability"
	"lersosa/app/resource/service/internal/domain/file/model"

	"github.com/go-kratos/kratos/v2/log"
)

// FileSaveCmdExe 文件保存命令执行器.
type FileSaveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewFileSaveCmdExe 创建文件保存命令执行器.
func NewFileSaveCmdExe(domainService *ability.DomainService, logger log.Logger) *FileSaveCmdExe {
	return &FileSaveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/file-service/application/command/save"))}
}

// ExecuteVoid 执行命令.
func (exe *FileSaveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.FileSaveCmd) error {
	return exe.domainService.SaveFile(
		ctx,
		&model.Entity{
			FileName:     dto.FileName,
			FileSuffix:   dto.FileSuffix,
			OriginalName: dto.OriginalName,
			URL:          dto.URL,
			Ext1:         dto.Ext1,
			CreateDept:   dto.CreateDept,
			CreateBy:     dto.CreateBy,
			Service:      dto.Service,
		},
	)
}
