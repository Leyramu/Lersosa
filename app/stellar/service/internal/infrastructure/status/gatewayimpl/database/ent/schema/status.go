package schema

import (
	pkgDB "lersosa/pkg/db/ent/schema"

	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/field"
	"entgo.io/ent/schema/index"
	"github.com/google/uuid"
)

// Status 星体状态表实体结构定义.
type Status struct {
	ent.Schema
}

// Fields 星体状态表字段定义.
func (Status) Fields() []ent.Field {
	return []ent.Field{
		field.UUID("id", uuid.UUID{}).
			StorageKey("status_id").
			Default(uuid.New).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("天体状态 ID"),

		field.UUID("info_id", uuid.UUID{}).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("天体信息 ID"),

		field.Float("score").
			Default(0).
			Annotations(entsql.WithComments(true)).
			Comment("得分"),

		field.Int32("flag").
			Default(0).
			Annotations(entsql.WithComments(true)).
			Comment("筛选标记"),

		field.Int32("check").
			Default(0).
			Annotations(entsql.WithComments(true)).
			Comment("审核标记"),
	}
}

// Mixin 星体状态表继承字段.
func (Status) Mixin() []ent.Mixin {
	return []ent.Mixin{
		pkgDB.Base{},
	}
}

// Edges 星体状态表关联关系.
func (Status) Edges() []ent.Edge {
	return nil
}

// Indexes 星体状态表的数据库索引.
func (Status) Indexes() []ent.Index {
	return []ent.Index{
		index.Fields("flag", "check", "score"),
	}
}

// Annotations 配置表名和其他 SQL 设置.
func (Status) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{
			Table: "stellar_status",
		},
		entsql.WithComments(true),
		schema.Comment("星体状态表"),
	}
}
