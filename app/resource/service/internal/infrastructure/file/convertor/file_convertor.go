package convertor

import (
	"lersosa/app/resource/service/internal/client/file/dto/co"
	"lersosa/app/resource/service/internal/domain/file/model"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database/ent"
)

// ToFileCo 领域模型转换为客户端模型.
func ToFileCo(entity *model.Entity) *co.FileCo {
	return &co.FileCo{
		ID:           entity.ID,
		FileName:     entity.FileName,
		OriginalName: entity.OriginalName,
		FileSuffix:   entity.FileSuffix,
		URL:          entity.URL,
		Ext1:         entity.Ext1,
		Service:      entity.Service,
		Version:      entity.Version,
	}
}

// ToFileCos 领域模型列表转换为客户端模型列表.
func ToFileCos(entities []*model.Entity) []*co.FileCo {
	models := make([]*co.FileCo, len(entities))
	for i, entity := range entities {
		models[i] = ToFileCo(entity)
	}

	return models
}

// ToFileEntity 数据模型转换为领域模型.
func ToFileEntity(do *ent.File) *model.Entity {
	return &model.Entity{
		ID:           do.ID,
		TenantID:     do.TenantID,
		FileName:     do.FileName,
		OriginalName: do.OriginalName,
		FileSuffix:   do.FileSuffix,
		URL:          do.URL,
		Ext1:         do.Ext1,
		CreateDept:   do.CreateDept,
		CreateBy:     do.CreateBy,
		CreateTime:   do.CreateTime,
		UpdateBy:     do.UpdateBy,
		UpdateTime:   do.UpdateTime,
		Service:      do.Service,
		Version:      do.Version,
	}
}

// ToFileEntities 领域模型列表转换为数据模型列表.
func ToFileEntities(dos []*ent.File) []*model.Entity {
	entities := make([]*model.Entity, len(dos))
	for i, do := range dos {
		entities[i] = ToFileEntity(do)
	}

	return entities
}
