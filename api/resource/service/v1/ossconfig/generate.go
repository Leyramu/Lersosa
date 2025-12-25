package generate

//go:generate kratos proto client .

//protoc --proto_path=./api `
//--proto_path=./third_party `
//--go_out=paths=source_relative:./api `
//--go-http_out=paths=source_relative:./api `
//--go-grpc_out=paths=source_relative:./api `
//--openapi_out=fq_schema_naming=true,default_response=false:./api/resource/service/v1 `
//api/resource/service/v1/ossconfig/proto/oss_config.proto api/resource/service/v1/ossconfig/proto/oss_config_get.proto api/resource/service/v1/ossconfig/proto/oss_config_modify.proto api/resource/service/v1/ossconfig/proto/oss_config_page.proto api/resource/service/v1/ossconfig/proto/oss_config_remove.proto api/resource/service/v1/ossconfig/proto/oss_config_save.proto api/resource/service/v1/ossconfig/proto/oss_config_get_default.proto
