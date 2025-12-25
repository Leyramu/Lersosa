package command

import (
	"context"
	"lersosa/app/resource/service/internal/client/file/dto"
	"lersosa/app/resource/service/internal/domain/file/ability"
	"lersosa/app/resource/service/internal/domain/file/model"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// FileRemoveCmdExe 文件删除命令执行器.
type FileRemoveCmdExe struct {
	// domainService 领域服务.
	domainService *ability.DomainService

	// log 日志记录器.
	log *log.Helper
}

// NewFileRemoveCmdExe 创建文件删除命令执行器.
func NewFileRemoveCmdExe(domainService *ability.DomainService, logger log.Logger) *FileRemoveCmdExe {
	return &FileRemoveCmdExe{
		domainService: domainService,
		log:           log.NewHelper(log.With(logger, "module", "resource/file-service/application/command/remove")),
	}
}

// ExecuteVoid 执行命令.
func (exe *FileRemoveCmdExe) ExecuteVoid(ctx context.Context, dto *dto.FileRemoveCmd) error {
	var entities []model.Entity
	for _, entity := range dto.Files {
		OssID, err := uuid.Parse(entity.OssID)
		if err != nil {
			exe.log.Errorf("文件信息 ID 无效：%v", err)
			return status.Errorf(codes.InvalidArgument, "文件信息 ID 无效：%v", err)
		}
		entities = append(entities, model.Entity{
			ID:      OssID,
			Version: entity.Version,
		})
	}

	return exe.domainService.RemoveFile(
		ctx,
		&entities,
	)
}
