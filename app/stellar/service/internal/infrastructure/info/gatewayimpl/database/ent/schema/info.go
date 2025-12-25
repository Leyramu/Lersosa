package schema

import (
	"entgo.io/ent"
	"entgo.io/ent/dialect/entsql"
	"entgo.io/ent/schema"
	"entgo.io/ent/schema/field"
	"entgo.io/ent/schema/index"
	"github.com/google/uuid"

	pkgDB "lersosa/pkg/db/ent/schema"
)

// Info 星体表实体结构定义.
type Info struct {
	ent.Schema
}

// Fields 星体表字段定义.
func (Info) Fields() []ent.Field {
	return []ent.Field{
		field.UUID("id", uuid.UUID{}).
			StorageKey("info_id").
			Default(uuid.New).
			Unique().
			Immutable().
			Annotations(entsql.WithComments(true)).
			Comment("天体信息 ID"),

		field.String("file_url").
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("文件 URL"),

		field.String("name").
			MaxLen(100).
			NotEmpty().
			Annotations(entsql.WithComments(true)).
			Comment("天体名称"),

		field.Float("period").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("周期"),

		field.Float("dispersion_measure").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("色散量"),

		field.Float("ra_deg").
			Annotations(entsql.WithComments(true)).
			Comment("赤经（度）"),

		field.Float("dec_deg").
			Annotations(entsql.WithComments(true)).
			Comment("赤纬（度）"),

		field.Float("galactic_longitude").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("银经"),

		field.Float("galactic_latitude").
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("银纬"),

		field.String("survey_name").
			MaxLen(50).
			Optional().
			Annotations(entsql.WithComments(true)).
			Comment("巡天项目名称"),
	}
}

// Mixin 星体表继承字段.
func (Info) Mixin() []ent.Mixin {
	return []ent.Mixin{
		pkgDB.Base{},
	}
}

// Edges 星体表关联关系.
func (Info) Edges() []ent.Edge {
	return nil
}

// Indexes 星体表的数据库索引.
func (Info) Indexes() []ent.Index {
	return []ent.Index{
		index.Fields("name").
			StorageKey("idx_info_name").
			Annotations(),

		index.Fields("survey_name").
			StorageKey("idx_info_survey"),

		index.Fields("ra_deg", "dec_deg").
			StorageKey("idx_info_ra_dec"),

		index.Fields("galactic_longitude", "galactic_latitude").
			StorageKey("idx_info_gal_lon_lat"),

		index.Fields("period").
			StorageKey("idx_info_period"),

		index.Fields("dispersion_measure").
			StorageKey("idx_info_dm"),
	}
}

// Annotations 配置表名和其他 SQL 设置.
func (Info) Annotations() []schema.Annotation {
	return []schema.Annotation{
		entsql.Annotation{
			Table: "stellar_info",
		},
		entsql.WithComments(true),
		schema.Comment("星体信息表"),
	}
}
