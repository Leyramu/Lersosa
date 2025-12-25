package gatewayimpl

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/stellar/service/internal/domain/status/gateway"
	"lersosa/app/stellar/service/internal/domain/status/model"
	"lersosa/app/stellar/service/internal/infrastructure/status/convertor"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database/ent"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database/ent/status"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// RepoImpl 星体状态仓储实现.
var _ gateway.RepoI = (*RepoImpl)(nil)

// RepoImpl 星体状态仓储.
type RepoImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRepoImpl 构造星体状态仓储.
func NewRepoImpl(data *database.Data, logger log.Logger) gateway.RepoI {
	return &RepoImpl{
		data: data,
		log:  log.NewHelper(logger),
	}
}

// Page 分页查询星体状态信息.
func (r RepoImpl) Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.Status.Query()

	// 动态模糊查询条件
	query = r.applyFilters(query, condition)

	offset := (condition.PageNum - 1) * condition.PageSize

	// 执行分页查询
	statuses, err := query.
		Offset(int(offset)).
		Limit(int(condition.PageSize)).
		All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query statuses: %w", err)
	}

	return convertor.ToStatusEntities(statuses), nil
}

// List 查询星体状态信息.
func (r RepoImpl) List(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.Status.Query()

	// 动态模糊查询条件
	query = r.applyFilters(query, condition)

	// 按照 score 从高到低排序
	query.Order(ent.Desc("score"))

	statuses, err := query.All(ctx)

	if err != nil {
		return nil, fmt.Errorf("failed to query statuses: %w", err)
	}

	return convertor.ToStatusEntities(statuses), nil
}

// Get 获取星体状态信息.
func (r RepoImpl) Get(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	s, err := r.data.Database.Status.Query().Where(status.ID(id)).First(ctx)
	if err != nil {
		return nil, errors.New("未能查询状态")
	}

	return convertor.ToStatusEntity(s), nil
}

// Save 保存星体状态信息.
func (r RepoImpl) Save(ctx context.Context, entity *model.Entity) error {
	_, err := r.data.Database.Status.Create().
		SetInfoID(entity.InfoID).
		SetTenantID(entity.TenantID).
		SetScore(entity.Score).
		SetFlag(entity.Flag).
		SetCheck(entity.Check).
		SetCreateBy(entity.CreateBy).
		SetRemark(entity.Remark).
		Save(ctx)

	r.log.Info("保存星体状态信息:", "星体状态信息", entity)
	return err
}

// Modify 修改星体状态信息.
func (r RepoImpl) Modify(ctx context.Context, entity *model.Entity) error {
	update := r.data.Database.Status.UpdateOneID(entity.ID).
		Where(status.VersionEQ(entity.Version)).
		SetVersion(entity.Version + 1).
		SetUpdateBy(entity.UpdateBy)

	if entity.Flag != 0 {
		update.SetFlag(entity.Flag)
	}
	if entity.Check != 0 {
		update.SetCheck(entity.Check)
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

// Remove 删除星体状态信息.
func (r RepoImpl) Remove(ctx context.Context, entities *[]model.Entity) error {
	for _, entity := range *entities {
		err := r.data.Database.Status.DeleteOneID(entity.ID).
			Where(status.VersionEQ(entity.Version)).
			Exec(ctx)
		if err != nil {
			if ent.IsNotFound(err) {
				return errors.New("删除失败：ID=%d 的记录不存在或已被修改（乐观锁冲突）")
			}
			return err
		}
	}
	return nil
}

// applyFilters 应用查询条件.
func (r RepoImpl) applyFilters(query *ent.StatusQuery, condition *model.Condition) *ent.StatusQuery {
	if condition.PageNum == 0 {
		condition.PageNum = 1
	}
	if condition.PageSize == 0 {
		condition.PageSize = 20
	}
	if condition.Flag != 0 {
		query.Where(status.FlagEQ(condition.Flag))
	}
	if condition.Check != 0 {
		query.Where(status.CheckEQ(condition.Check))
	}
	if condition.CreateBy != 0 {
		query.Where(status.CreateByEQ(condition.CreateBy))
	}
	if condition.UpdateBy != 0 {
		query.Where(status.UpdateByEQ(condition.UpdateBy))
	}

	return query
}
