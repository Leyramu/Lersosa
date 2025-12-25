package db

import (
	"database/sql"

	"github.com/XSAM/otelsql"
	"go.opentelemetry.io/otel/semconv/v1.4.0"
)

// OpenDbWithOTel 创建带 OTel 的数据库连接
func OpenDbWithOTel(driver, source string) (*sql.DB, error) {
	// 自动注入 OTel 语义属性
	attributes := otelsql.WithAttributes(
		semconv.DBSystemKey.String(driver),
	)
	spanOptions := otelsql.WithSpanOptions(
		otelsql.SpanOptions{DisableErrSkip: true},
	)
	return otelsql.Open(driver, source, attributes, spanOptions)
}
