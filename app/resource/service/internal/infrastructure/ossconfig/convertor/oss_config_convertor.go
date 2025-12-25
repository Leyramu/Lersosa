package convertor

import (
	"lersosa/app/resource/service/internal/client/ossconfig/dto/co"
	"lersosa/app/resource/service/internal/domain/ossconfig/model"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database/ent"
)

// ToOssConfigCo 领域模型转换为客户端模型.
func ToOssConfigCo(entity *model.Entity) *co.OssConfigCo {
	return &co.OssConfigCo{
		ID:           entity.ID,
		ConfigKey:    entity.ConfigKey,
		AccessKey:    entity.AccessKey,
		SecretKey:    entity.SecretKey,
		BucketName:   entity.BucketName,
		Prefix:       entity.Prefix,
		Endpoint:     entity.Endpoint,
		Domain:       entity.Domain,
		IsHTTPS:      entity.IsHTTPS,
		Region:       entity.Region,
		AccessPolicy: entity.AccessPolicy,
		Status:       entity.Status,
		Ext1:         entity.Ext1,
		Remark:       entity.Remark,
		Version:      entity.Version,
	}
}

// ToOssConfigCos 领域模型列表转换为客户端模型列表.
func ToOssConfigCos(entities []*model.Entity) []*co.OssConfigCo {
	models := make([]*co.OssConfigCo, len(entities))
	for i, entity := range entities {
		models[i] = ToOssConfigCo(entity)
	}

	return models
}

// ToOssConfigEntity 数据模型转换为领域模型.
func ToOssConfigEntity(do *ent.OssConfig) *model.Entity {
	return &model.Entity{
		ID:           do.ID,
		TenantID:     do.TenantID,
		ConfigKey:    do.ConfigKey,
		AccessKey:    do.AccessKey,
		SecretKey:    do.SecretKey,
		BucketName:   do.BucketName,
		Prefix:       do.Prefix,
		Endpoint:     do.Endpoint,
		Domain:       do.Domain,
		IsHTTPS:      do.IsHTTPS,
		Region:       do.Region,
		AccessPolicy: do.AccessPolicy,
		Status:       do.Status,
		Ext1:         do.Ext1,
		CreateDept:   do.CreateDept,
		CreateBy:     do.CreateBy,
		CreateTime:   do.CreateTime,
		UpdateBy:     do.UpdateBy,
		UpdateTime:   do.UpdateTime,
		Remark:       do.Remark,
		Version:      do.Version,
	}
}

// ToOssConfigEntities 领域模型列表转换为数据模型列表.
func ToOssConfigEntities(dos []*ent.OssConfig) []*model.Entity {
	entities := make([]*model.Entity, len(dos))
	for i, do := range dos {
		entities[i] = ToOssConfigEntity(do)
	}

	return entities
}
