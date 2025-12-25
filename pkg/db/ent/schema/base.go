package schema

import (
	"time"

	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/field"
	"github.com/google/uuid"
)

// Base 基本表实体结构定义.
type Base struct{}

// Fields 基本表字段定义.
func (Base) Fields() []ent.Field {
	return []ent.Field{
		field.UUID("id", uuid.UUID{}).
			Default(uuid.New).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("编号 ID"),

		field.UUID("tenant_id", uuid.UUID{}).
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("租户编号"),

		field.Int64("create_by").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("创建者"),

		field.Time("create_time").
			Optional().
			Default(time.Now).
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("创建时间"),

		field.Int64("update_by").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("更新者"),

		field.Time("update_time").
			Optional().
			Default(time.Now).
			UpdateDefault(time.Now).
			Annotations(entsql.WithComments(true)).
			Comment("更新时间"),

		field.String("remark").
			MaxLen(500).
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("备注"),

		field.Int32("version").
			Default(0).
			NonNegative().
			Annotations(entsql.WithComments(true)).
			Comment("版本"),
	}
}

// Edges 基本表关联关系.
func (Base) Edges() []ent.Edge {
	return nil
}

// Indexes 基本表索引定义.
func (Base) Indexes() []ent.Index {
	return nil
}

// Annotations 基本表其他设置.
func (Base) Annotations() []schema.Annotation {
	return nil
}

// Policy 基本表权限定义.
func (Base) Policy() ent.Policy {
	return nil
}

// Interceptors 基本表拦截器定义.
func (Base) Interceptors() []ent.Interceptor {
	return nil
}

// Hooks 基本表钩子定义.
func (Base) Hooks() []ent.Hook {
	return nil
}
