package gatewayimpl

import (
	"context"
	"errors"
	"fmt"
	"lersosa/app/stellar/service/internal/domain/info/gateway"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"lersosa/app/stellar/service/internal/infrastructure/info/convertor"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database/ent"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database/ent/info"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// RepoImpl 星体信息仓储实现.
var _ gateway.RepoI = (*RepoImpl)(nil)

// RepoImpl 星体信息仓储.
type RepoImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewRepoImpl 构造星体信息仓储.
func NewRepoImpl(data *database.Data, logger log.Logger) gateway.RepoI {
	return &RepoImpl{
		data: data,
		log:  log.NewHelper(logger),
	}
}

// Page 分页查询星体信息.
func (r RepoImpl) Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.Info.Query()

	// 模糊查询条件
	query = r.applyFilters(query, condition)

	offset := (condition.PageNum - 1) * condition.PageSize

	// 执行分页查询
	infos, err := query.
		Offset(int(offset)).
		Limit(int(condition.PageSize)).
		All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query infos: %w", err)
	}

	return convertor.ToInfoEntities(infos), nil
}

// List 查询星体信息.
func (r RepoImpl) List(ctx context.Context, condition *model.Condition) ([]*model.Entity, error) {
	// 构建基础查询
	query := r.data.Database.Info.Query()

	// 模糊查询条件
	query = r.applyFilters(query, condition)

	infos, err := query.All(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query infos: %w", err)
	}

	return convertor.ToInfoEntities(infos), nil
}

// Get 获取星体信息.
func (r RepoImpl) Get(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	i, err := r.data.Database.Info.Query().Where(info.ID(id)).First(ctx)
	if err != nil {
		return nil, fmt.Errorf("failed to query info: %w", err)
	}

	return convertor.ToInfoEntity(i), nil
}

// Save 保存星体信息.
func (r RepoImpl) Save(ctx context.Context, e *model.Entity) error {
	_, err := r.data.Database.Info.Create().
		SetTenantID(e.TenantID).
		SetFileURL(e.FileURL).
		SetName(e.Name).
		SetPeriod(e.Period).
		SetDispersionMeasure(e.DispersionMeasure).
		SetRaDeg(e.RaDeg).
		SetDecDeg(e.DecDeg).
		SetGalacticLongitude(e.GalacticLongitude).
		SetGalacticLatitude(e.GalacticLatitude).
		SetSurveyName(e.SurveyName).
		SetCreateBy(e.CreateBy).
		SetRemark(e.Remark).
		Save(ctx)

	r.log.Info("保存星体信息:", "星体信息", e)
	return err
}

// Modify 修改星体信息.
func (r RepoImpl) Modify(ctx context.Context, e *model.Entity) error {
	update := r.data.Database.Info.UpdateOneID(e.ID).
		Where(info.VersionEQ(e.Version)).
		SetVersion(e.Version + 1).
		SetUpdateBy(e.UpdateBy)

	if e.Name != "" {
		update.SetName(e.Name)
	}

	if e.Period != 0 {
		update.SetPeriod(e.Period)
	}

	if e.DispersionMeasure != 0 {
		update.SetDispersionMeasure(e.DispersionMeasure)
	}

	if e.RaDeg != 0 {
		update.SetRaDeg(e.RaDeg)
	}

	if e.DecDeg != 0 {
		update.SetDecDeg(e.DecDeg)
	}

	if e.GalacticLongitude != 0 {
		update.SetGalacticLongitude(e.GalacticLongitude)
	}

	if e.GalacticLatitude != 0 {
		update.SetGalacticLatitude(e.GalacticLatitude)
	}

	if e.SurveyName != "" {
		update.SetSurveyName(e.SurveyName)
	}

	if e.Remark != "" {
		update.SetRemark(e.Remark)
	}

	_, err := update.Save(ctx)
	if ent.IsNotFound(err) {
		return errors.New("并发冲突：数据已被他人修改")
	}

	return err
}

// Remove 删除星体信息.
func (r RepoImpl) Remove(ctx context.Context, es *[]model.Entity) error {
	for _, e := range *es {
		err := r.data.Database.Info.DeleteOneID(e.ID).
			Where(info.VersionEQ(e.Version)).
			Exec(ctx)
		if err != nil {
			if ent.IsNotFound(err) {
				return fmt.Errorf("删除失败：ID=%d 的记录不存在或已被修改（乐观锁冲突）", e.ID)
			}
			return err
		}
	}
	return nil
}

// applyFilters 应用查询条件.
func (r RepoImpl) applyFilters(query *ent.InfoQuery, condition *model.Condition) *ent.InfoQuery {
	if condition.PageNum == 0 {
		condition.PageNum = 1
	}
	if condition.PageSize == 0 {
		condition.PageSize = 20
	}
	if condition.Name != "" {
		query = query.Where(info.NameContains(condition.Name))
	}
	if condition.Period != 0 {
		query = query.Where(info.PeriodEQ(condition.Period))
	}
	if condition.DispersionMeasure != 0 {
		query = query.Where(info.DispersionMeasureEQ(condition.DispersionMeasure))
	}
	if condition.RaDeg != 0 {
		query = query.Where(info.RaDegEQ(condition.RaDeg))
	}
	if condition.DecDeg != 0 {
		query = query.Where(info.DecDegEQ(condition.DecDeg))
	}
	if condition.GalacticLongitude != 0 {
		query = query.Where(info.GalacticLongitudeEQ(condition.GalacticLongitude))
	}
	if condition.GalacticLatitude != 0 {
		query = query.Where(info.GalacticLatitudeEQ(condition.GalacticLatitude))
	}
	if condition.SurveyName != "" {
		query = query.Where(info.SurveyNameContains(condition.SurveyName))
	}
	if condition.Remark != "" {
		query = query.Where(info.SurveyNameContains(condition.Remark))
	}

	return query
}
