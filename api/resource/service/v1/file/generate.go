package generate

//go:generate kratos proto client .

//protoc --proto_path=./api `
//--proto_path=./third_party `
//--go_out=paths=source_relative:./api `
//--go-http_out=paths=source_relative:./api `
//--go-grpc_out=paths=source_relative:./api `
//--openapi_out=fq_schema_naming=true,default_response=false:./api/resource/service/v1 `
//api/resource/service/v1/file/proto/file.proto api/resource/service/v1/file/proto/file_get.proto api/resource/service/v1/file/proto/file_modify.proto api/resource/service/v1/file/proto/file_page.proto api/resource/service/v1/file/proto/file_remove.proto api/resource/service/v1/file/proto/file_save.proto
