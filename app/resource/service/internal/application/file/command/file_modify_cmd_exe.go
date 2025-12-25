package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/domain/file/ability"
	"lersosa/app/resource/service/internal/domain/file/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// FileModifyCmdExe 文件修改命令执行器.
type FileModifyCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewFileModifyCmdExe 创建文件修改命令执行器.
func NewFileModifyCmdExe(domainService *ability.DomainService, logger log.Logger) *FileModifyCmdExe {
	return &FileModifyCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/file-service/application/command/modify")),
	}
}

// ExecuteVoid 执行命令.
func (exe *FileModifyCmdExe) ExecuteVoid(ctx context.Context, dto *dto.FileModifyCmd) error {
	OssID, err := uuid.Parse(dto.OssID)
	if err != nil {
		exe.log.Errorf("文件信息 ID 无效：%v", err)
		return err
	}

	return exe.domainService.ModifyFile(
		ctx,
		&model.Entity{
			ID:           OssID,
			OriginalName: dto.OriginalName,
			Ext1:         dto.Ext1,
			UpdateBy:     dto.UpdateBy,
			Service:      dto.Service,
			Version:      dto.Version,
		},
	)
}
