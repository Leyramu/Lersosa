package generate

//go:generate kratos proto client .

//protoc --proto_path=./api `
//--proto_path=./third_party `
//--go_out=paths=source_relative:./api `
//--go-http_out=paths=source_relative:./api `
//--go-grpc_out=paths=source_relative:./api `
//--openapi_out=fq_schema_naming=true,default_response=false:./api/stellar/interface/v1 `
//api/stellar/interface/v1/info/proto/info.proto api/stellar/interface/v1/info/proto/info_get.proto api/stellar/interface/v1/info/proto/info_modify.proto api/stellar/interface/v1/info/proto/info_page.proto api/stellar/interface/v1/info/proto/info_remove.proto api/stellar/interface/v1/info/proto/info_save.proto
