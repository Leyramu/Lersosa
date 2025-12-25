// 数据库连接
variable "db_url" {
  type    = string
  default = "postgres://postgres:Zcx@223852//@localhost:5432/lersosa-cloud?sslmode=disable"
}

// 从 Ent Go schema 加载结构
data "ent_schema" "main" {
  path = "../ent/schema"
}

// 声明期望状态
schema "public" {
  src = data.ent_schema.main.url
}