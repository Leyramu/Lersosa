package schema

import (
	pkgDB "lersosa/pkg/db/ent/schema"

	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/field"
	"github.com/google/uuid"
)

// OssConfig 星体表实体结构定义.
type OssConfig struct {
	ent.Schema
}

// Fields 系统 OSS 配置表字段定义.
func (OssConfig) Fields() []ent.Field {
	return []ent.Field{
		field.UUID("id", uuid.UUID{}).
			StorageKey("oss_config_id").
			Default(uuid.New).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("系统 OSS 配置 ID"),

		field.String("config_key").
			NotEmpty().
			MaxLen(20).
			Annotations(entsql.WithComments(true)).
			Comment("配置 Key"),

		field.String("access_key").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("访问 Key"),

		field.String("secret_key").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("密钥"),

		field.String("bucket_name").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("桶名称"),

		field.String("prefix").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("前缀"),

		field.String("endpoint").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("访问站点"),

		field.String("domain").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("自定义域名"),

		field.String("is_https").
			Default("N").
			MaxLen(1).
			Annotations(entsql.WithComments(true)).
			Comment("是否https（Y=是,N=否）"),

		field.String("region").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("域"),

		field.String("access_policy").
			Default("1").
			MaxLen(1).
			Annotations(entsql.WithComments(true)).
			Comment("桶权限类型(0=private 1=public 2=custom)"),

		field.String("status").
			Default("1").
			MaxLen(1).
			Annotations(entsql.WithComments(true)).
			Comment("是否默认（0=是,1=否）"),

		field.String("ext1").
			Default("").
			MaxLen(255).
			Annotations(entsql.WithComments(true)).
			Comment("扩展字段"),

		field.Int64("create_dept").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("创建部门"),
	}
}

// Mixin 系统 OSS 配置表继承字段.
func (OssConfig) Mixin() []ent.Mixin {
	return []ent.Mixin{
		pkgDB.Base{},
	}
}

// Edges 系统 OSS 配置表关联关系.
func (OssConfig) Edges() []ent.Edge {
	return nil
}

// Indexes 系统 OSS 配置表的数据库索引.
func (OssConfig) Indexes() []ent.Index {
	return nil
}

// Annotations 配置表名和其他 SQL 设置.
func (OssConfig) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{
			Table: "sys_oss_config",
		},
		entsql.WithComments(true),
		schema.Comment("系统 OSS 配置表"),
	}
}
