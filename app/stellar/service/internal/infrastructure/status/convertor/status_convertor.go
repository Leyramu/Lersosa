package convertor

import (
	"lersosa/app/stellar/service/internal/client/status/dto/co"
	"lersosa/app/stellar/service/internal/domain/status/model"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database/ent"
)

// ToStatusCo 领域模型转换为客户端模型.
func ToStatusCo(entity *model.Entity) *co.StatusCo {
	return &co.StatusCo{
		ID:         entity.ID,
		InfoID:     entity.InfoID,
		Score:      entity.Score,
		Flag:       entity.Flag,
		Check:      entity.Check,
		CreateBy:   entity.CreateBy,
		CreateTime: entity.CreateTime,
		UpdateBy:   entity.UpdateBy,
		UpdateTime: entity.UpdateTime,
		Remark:     entity.Remark,
		Version:    entity.Version,
	}
}

// ToStatusCos 领域模型列表转换为客户端模型列表.
func ToStatusCos(entities []*model.Entity) []*co.StatusCo {
	models := make([]*co.StatusCo, len(entities))
	for i, entity := range entities {
		models[i] = ToStatusCo(entity)
	}

	return models
}

// ToStatusEntity 数据模型转换为领域模型.
func ToStatusEntity(do *ent.Status) *model.Entity {
	return &model.Entity{
		ID:         do.ID,
		TenantID:   do.TenantID,
		InfoID:     do.InfoID,
		Score:      do.Score,
		Flag:       do.Flag,
		Check:      do.Check,
		CreateBy:   do.CreateBy,
		CreateTime: do.CreateTime,
		UpdateBy:   do.UpdateBy,
		UpdateTime: do.UpdateTime,
		Remark:     do.Remark,
		Version:    do.Version,
	}
}

// ToStatusEntities 领域模型列表转换为数据模型列表.
func ToStatusEntities(dos []*ent.Status) []*model.Entity {
	entities := make([]*model.Entity, len(dos))
	for i, do := range dos {
		entities[i] = ToStatusEntity(do)
	}

	return entities
}
