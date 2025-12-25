package gatewayimpl

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/resource/service/internal/domain/file/gateway"
	"lersosa/app/resource/service/internal/domain/file/model"
	"lersosa/app/resource/service/internal/infrastructure/file/convertor"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database/ent"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database/ent/file"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// RepoImpl 文件基础设施层仓储.
type RepoImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRepoImpl 构造文件仓储.
func NewRepoImpl(data *database.Data, logger log.Logger) gateway.RepoI {
	return &RepoImpl{
		data: data,
		log:  log.NewHelper(logger),
	}
}

// Page 分页查询文件信息.
func (r RepoImpl) Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.File.Query()

	// TODO 无动态模糊查询条件

	offset := (condition.PageNum - 1) * condition.PageSize

	// 执行分页查询
	files, err := query.
		Offset(int(offset)).
		Limit(int(condition.PageSize)).
		All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query files: %w", err)
	}

	return convertor.ToFileEntities(files), nil
}

// List 查询文件信息.
func (r RepoImpl) List(ctx context.Context) ([]*model.Entity, error) {
	files, err := r.data.Database.File.Query().All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query statuses: %w", err)
	}

	return convertor.ToFileEntities(files), nil
}

// Get 获取文件信息.
func (r RepoImpl) Get(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	f, err := r.data.Database.File.Query().Where(file.ID(id)).First(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query status: %w", err)
	}

	return convertor.ToFileEntity(f), nil
}

// Save 保存文件信息.
func (r RepoImpl) Save(ctx context.Context, entity *model.Entity) error {
	_, err := r.data.Database.File.Create().
		SetTenantID(entity.TenantID).
		SetFileName(entity.FileName).
		SetOriginalName(entity.OriginalName).
		SetFileSuffix(entity.FileSuffix).
		SetURL(entity.URL).
		SetExt1(entity.Ext1).
		SetCreateDept(entity.CreateDept).
		SetCreateBy(entity.CreateBy).
		SetService(entity.Service).
		Save(ctx)

	r.log.Info("保存文件信息:", "文件信息", entity)
	return err
}

// Modify 修改文件信息.
func (r RepoImpl) Modify(ctx context.Context, entity *model.Entity) error {
	update := r.data.Database.File.UpdateOneID(entity.ID).
		Where(file.VersionEQ(entity.Version)).
		SetVersion(entity.Version + 1).
		SetUpdateBy(entity.UpdateBy)

	if entity.OriginalName != "" {
		update.SetOriginalName(entity.OriginalName)
	}
	if entity.FileSuffix != "" {
		update.SetFileSuffix(entity.FileSuffix)
	}
	if entity.URL != "" {
		update.SetURL(entity.URL)
	}
	if entity.Ext1 != "" {
		update.SetExt1(entity.Ext1)
	}
	if entity.Service != "" {
		update.SetService(entity.Service)
	}

	_, err := update.Save(ctx)
	if ent.IsNotFound(err) {
		return errors.New("数据已被他人修改")
	}

	return err
}

// Remove 删除文件信息.
func (r RepoImpl) Remove(ctx context.Context, entities *[]model.Entity) error {
	for _, entity := range *entities {
		err := r.data.Database.File.DeleteOneID(entity.ID).
			Where(file.VersionEQ(entity.Version)).
			Exec(ctx)
		if err != nil {
			if ent.IsNotFound(err) {
				return fmt.Errorf("删除失败：ID=%d 的记录不存在或已被修改", entity.ID)
			}
			return err
		}
	}
	return nil
}
