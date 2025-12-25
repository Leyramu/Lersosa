package database

import (
	"context"
	"lersosa/app/stellar/service/internal/conf"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database/ent"
	"lersosa/app/stellar/service/internal/infrastructure/status/gatewayimpl/database/ent/migrate"
	"lersosa/pkg/db"
	pkgElasticsearch "lersosa/pkg/elasticsearch"
	pkgRedis "lersosa/pkg/redis"

	entsql "entgo.io/ent/dialect/sql"
	"github.com/elastic/go-elasticsearch/v9"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/redis/go-redis/v9"

	_ "github.com/lib/pq"
)

// Data 星体状态数据结构体.
type Data struct {
	Database      *ent.Client
	Elasticsearch *ElasticsearchStatusClient
	RedisCli      RedisStatusInfoI
}

// ElasticsearchStatusClient 星体状态 ElasticSearch 客户端.
type ElasticsearchStatusClient struct {
	client *elasticsearch.Client
}

// RedisStatusInfoI 星体状态 Redis 缓存接口.
type RedisStatusInfoI interface {
	redis.Cmdable
}

// NewData 构造星体状态数据结构体.
func NewData(
	ent *ent.Client,
	esClient *ElasticsearchStatusClient,
	redisCmd RedisStatusInfoI,
	logger log.Logger,
) (*Data, func(), error) {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/status-service/infrastructure/data"))

	d := &Data{
		Database:      ent,
		Elasticsearch: esClient,
		RedisCli:      redisCmd,
	}

	return d, func() {
		if err := d.Database.Close(); err != nil {
			logVal.Error(err)
		}
		if err := d.Elasticsearch.client.Close(context.Background()); err != nil {
			logVal.Error(err)
		}
	}, nil
}

// NewEntClient 构造星体状态 Ent 客户端.
func NewEntClient(conf *conf.Data, logger log.Logger) (*ent.Client, error) {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/status-service/data/ent"))

	// 创建星体状态 Ent 客户端
	DB, err := db.OpenDbWithOTel(conf.Database.Driver, conf.Database.Source)
	if err != nil {
		logVal.Fatalf("status db connect failed: %v", err)
	}
	drv := entsql.OpenDB(conf.Database.Driver, DB)
	client := ent.NewClient(ent.Driver(drv))
	if err := client.Schema.Create(context.Background(), migrate.WithForeignKeys(false)); err != nil {
		logVal.Fatalf("status schema migration failed: %v", err)
	}

	return client, err
}

// NewElasticSearchClient 构造星体状态 ElasticSearch 客户端.
func NewElasticSearchClient(conf *conf.Data, logger log.Logger) *ElasticsearchStatusClient {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/info-service/data/elasticsearch"))

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

	return &ElasticsearchStatusClient{
		client: client,
	}
}

// NewRedisCmd 构造星体状态 Redis 连接.
func NewRedisCmd(conf *conf.Data, logger log.Logger) RedisStatusInfoI {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/status--service/data/redis"))

	client, err := pkgRedis.NewCreateClient(
		conf.Redis.Addr,
		conf.Redis.Password,
		conf.Redis.CertFile,
		conf.Redis.KeyFile,
		conf.Redis.CaFile,
		conf.Redis.ReadTimeout.AsDuration(),
		conf.Redis.WriteTimeout.AsDuration(),
	)

	if err != nil {
		logVal.Fatalf("Redis 配置初始化失败: %v", err)
	}

	return client
}
