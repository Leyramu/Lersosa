package convertor

import (
	"lersosa/app/stellar/service/internal/client/info/dto/co"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database/ent"
)

// ToInfoCo 领域模型转换为客户端模型.
func ToInfoCo(entity *model.Entity) *co.InfoCo {
	return &co.InfoCo{
		InfoID:            entity.ID,
		FileURL:           entity.FileURL,
		Name:              entity.Name,
		Period:            entity.Period,
		DispersionMeasure: entity.DispersionMeasure,
		RaDeg:             entity.RaDeg,
		DecDeg:            entity.DecDeg,
		GalacticLongitude: entity.GalacticLongitude,
		GalacticLatitude:  entity.GalacticLatitude,
		SurveyName:        entity.SurveyName,
		CreateBy:          entity.CreateBy,
		CreateTime:        entity.CreateTime,
		UpdateBy:          entity.UpdateBy,
		UpdateTime:        entity.UpdateTime,
		Remark:            entity.Remark,
		Version:           entity.Version,
	}
}

// ToInfoCos 领域模型列表转换为客户端模型列表.
func ToInfoCos(entities []*model.Entity) []*co.InfoCo {
	models := make([]*co.InfoCo, len(entities))
	for i, entity := range entities {
		models[i] = ToInfoCo(entity)
	}

	return models
}

// ToInfoEntity 数据模型转换为领域模型.
func ToInfoEntity(do *ent.Info) *model.Entity {
	return &model.Entity{
		ID:                do.ID,
		TenantID:          do.TenantID,
		FileURL:           do.FileURL,
		Name:              do.Name,
		Period:            do.Period,
		DispersionMeasure: do.DispersionMeasure,
		RaDeg:             do.RaDeg,
		DecDeg:            do.DecDeg,
		GalacticLongitude: do.GalacticLongitude,
		GalacticLatitude:  do.GalacticLatitude,
		SurveyName:        do.SurveyName,
		CreateBy:          do.CreateBy,
		CreateTime:        do.CreateTime,
		UpdateBy:          do.UpdateBy,
		UpdateTime:        do.UpdateTime,
		Remark:            do.Remark,
		Version:           do.Version,
	}
}

// ToInfoEntities 领域模型列表转换为数据模型列表.
func ToInfoEntities(dos []*ent.Info) []*model.Entity {
	entities := make([]*model.Entity, len(dos))
	for i, do := range dos {
		entities[i] = ToInfoEntity(do)
	}

	return entities
}
