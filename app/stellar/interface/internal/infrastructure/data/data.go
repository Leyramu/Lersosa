package data

import (
	"context"
	"lersosa/app/stellar/interface/internal/infrastructure/conf"
	pkgElasticsearch "lersosa/pkg/elasticsearch"

	"github.com/elastic/go-elasticsearch/v9"
	"github.com/go-kratos/kratos/v2/log"
)

// Data 星体数据结构体.
type Data struct {
	es *ElasticsearchClient
}

// ElasticsearchClient 星体 ElasticSearch 客户端.
type ElasticsearchClient struct {
	client *elasticsearch.Client
}

// NewData 构造星体数据结构体.
func NewData(
	esClient *ElasticsearchClient,
	logger log.Logger,
) (*Data, func(), error) {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/interface/infrastructure/data"))

	d := &Data{
		es: esClient,
	}

	return d, func() {
		if err := d.es.client.Close(context.Background()); err != nil {
			logVal.Error(err)
		}
	}, nil
}

// NewElasticSearchClient 构造 ElasticSearch 客户端.
func NewElasticSearchClient(conf *conf.Data, logger log.Logger) *ElasticsearchClient {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/interface/data/elasticsearch"))

	client, err := pkgElasticsearch.NewCreateClient(
		conf.Elasticsearch.Addr,
		conf.Elasticsearch.Username,
		conf.Elasticsearch.Password,
		conf.Elasticsearch.CloudId,
		conf.Elasticsearch.ApiKey,
		conf.Elasticsearch.CertFile,
		conf.Elasticsearch.KeyFile,
		conf.Elasticsearch.CaFile,
	)

	if err != nil {
		logVal.Fatalf("ElasticSearch 配置初始化失败: %v", err)
	}

	return &ElasticsearchClient{
		client: client,
	}
}
