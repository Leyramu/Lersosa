package gatewayimpl

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/resource/service/internal/domain/ossconfig/gateway"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/convertor"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database/ent"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database/ent/ossconfig"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// RepoImpl 资源配置基础设施层仓储.
type RepoImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRepoImpl 构造资源配置仓储.
func NewRepoImpl(data *database.Data, logger log.Logger) gateway.RepoI {
	return &RepoImpl{
		data: data,
		log:  log.NewHelper(logger),
	}
}

// Page 分页查询资源配置.
func (r RepoImpl) Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.OssConfig.Query()

	// 模糊查询条件
	query = r.applyFilters(query, condition)

	offset := (condition.PageNum - 1) * condition.PageSize

	// 执行分页查询
	ossConfigs, err := query.
		Offset(int(offset)).
		Limit(int(condition.PageSize)).
		All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query ossConfigs: %w", err)
	}

	return convertor.ToOssConfigEntities(ossConfigs), nil
}

// List 查询资源配置.
func (r RepoImpl) List(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.OssConfig.Query()

	// 模糊查询条件
	query = r.applyFilters(query, condition)

	ossConfigs, err := query.All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query ossConfigs: %w", err)
	}

	return convertor.ToOssConfigEntities(ossConfigs), nil
}

// Get 获取资源配置.
func (r RepoImpl) Get(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	oc, err := r.data.Database.OssConfig.Query().Where(ossconfig.ID(id)).First(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query ossConfig: %w", err)
	}

	return convertor.ToOssConfigEntity(oc), nil
}

// Save 保存资源配置.
func (r RepoImpl) Save(ctx context.Context, entity *model.Entity) error {
	_, err := r.data.Database.OssConfig.Create().
		SetTenantID(entity.TenantID).
		SetConfigKey(entity.ConfigKey).
		SetAccessKey(entity.AccessKey).
		SetSecretKey(entity.SecretKey).
		SetBucketName(entity.BucketName).
		SetPrefix(entity.Prefix).
		SetEndpoint(entity.Endpoint).
		SetDomain(entity.Domain).
		SetIsHTTPS(entity.IsHTTPS).
		SetRegion(entity.Region).
		SetAccessPolicy(entity.AccessPolicy).
		SetStatus(entity.Status).
		SetExt1(entity.Ext1).
		SetCreateDept(entity.CreateDept).
		SetCreateBy(entity.CreateBy).
		SetRemark(entity.Remark).
		Save(ctx)

	r.log.Info("保存资源配置:", "资源配置", entity)
	return err
}

// Modify 修改资源配置.
func (r RepoImpl) Modify(ctx context.Context, entity *model.Entity) error {
	update := r.data.Database.OssConfig.UpdateOneID(entity.ID).
		Where(ossconfig.VersionEQ(entity.Version)).
		SetVersion(entity.Version + 1).
		SetUpdateBy(entity.UpdateBy)

	if entity.ConfigKey != "" {
		update.SetConfigKey(entity.ConfigKey)
	}
	if entity.AccessKey != "" {
		update.SetAccessKey(entity.AccessKey)
	}
	if entity.SecretKey != "" {
		update.SetSecretKey(entity.SecretKey)
	}
	if entity.BucketName != "" {
		update.SetBucketName(entity.BucketName)
	}
	if entity.Prefix != "" {
		update.SetPrefix(entity.Prefix)
	}
	if entity.Endpoint != "" {
		update.SetEndpoint(entity.Endpoint)
	}
	if entity.Domain != "" {
		update.SetDomain(entity.Domain)
	}
	if entity.IsHTTPS != "" {
		update.SetIsHTTPS(entity.IsHTTPS)
	}
	if entity.Region != "" {
		update.SetRegion(entity.Region)
	}
	if entity.AccessPolicy != "" {
		update.SetAccessPolicy(entity.AccessPolicy)
	}
	if entity.Status != "" {
		update.SetStatus(entity.Status)
	}
	if entity.Ext1 != "" {
		update.SetExt1(entity.Ext1)
	}
	if entity.Remark != "" {
		update.SetRemark(entity.Remark)
	}

	_, err := update.Save(ctx)
	if ent.IsNotFound(err) {
		return errors.New("数据已被他人修改")
	}

	return err
}

// Remove 删除资源配置.
func (r RepoImpl) Remove(ctx context.Context, entities *[]model.Entity) error {
	for _, entity := range *entities {
		err := r.data.Database.OssConfig.DeleteOneID(entity.ID).
			Where(ossconfig.VersionEQ(entity.Version)).
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

// applyFilters 应用查询条件.
func (r RepoImpl) applyFilters(query *ent.OssConfigQuery, condition *model.Condition) *ent.OssConfigQuery {
	if condition.PageNum == 0 {
		condition.PageNum = 1
	}
	if condition.PageSize == 0 {
		condition.PageSize = 20
	}
	if condition.ConfigKey != "" {
		query = query.Where(ossconfig.ConfigKeyContains(condition.ConfigKey))
	}
	if condition.BucketName != "" {
		query = query.Where(ossconfig.BucketNameContains(condition.BucketName))
	}
	if condition.Status != "" {
		query = query.Where(ossconfig.StatusEQ(condition.Status))
	}

	return query
}
