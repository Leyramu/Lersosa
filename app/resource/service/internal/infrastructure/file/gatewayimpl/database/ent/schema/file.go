package schema

import (
	pkgDB "lersosa/pkg/db/ent/schema"

	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/field"
	"github.com/google/uuid"
)

// File 文件表实体结构定义.
type File struct {
	ent.Schema
}

// Fields 文件表字段定义.
func (File) Fields() []ent.Field {
	return []ent.Field{
		field.UUID("id", uuid.UUID{}).
			StorageKey("oss_id").
			Default(uuid.New).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("对象存储主键"),

		field.String("file_name").
			MaxLen(255).
			Default("").
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("文件名"),

		field.String("original_name").
			MaxLen(255).
			Default("").
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("原名"),

		field.String("file_suffix").
			MaxLen(10).
			Default("").
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("文件后缀名"),

		field.String("url").
			MaxLen(500).
			Default("").
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("URL 地址"),

		field.String("ext1").
			MaxLen(500).
			Default("").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("扩展字段"),

		field.Int64("create_dept").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("创建部门"),

		field.String("service").
			MaxLen(20).
			Default("minio").
			Annotations(entsql.WithComments(true)).
			Comment("服务商"),
	}
}

// Mixin 文件表继承字段.
func (File) Mixin() []ent.Mixin {
	return []ent.Mixin{
		pkgDB.Base{},
	}
}

// Edges 文件表关联关系.
func (File) Edges() []ent.Edge {
	return nil
}

// Indexes 文件表的数据库索引.
func (File) Indexes() []ent.Index {
	return nil
}

// Annotations 配置表名和其他 SQL 设置.
func (File) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{
			Table: "sys_file",
		},
		entsql.WithComments(true),
		schema.Comment("系统文件表"),
	}
}
