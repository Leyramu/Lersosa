package database

import (
	"context"
	"lersosa/app/stellar/service/internal/conf"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database/ent"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database/ent/migrate"
	pkgDB "lersosa/pkg/db"
	pkgElasticsearch "lersosa/pkg/elasticsearch"
	pkgRedis "lersosa/pkg/redis"

	entsql "entgo.io/ent/dialect/sql"
	"github.com/elastic/go-elasticsearch/v9"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/redis/go-redis/v9"

	_ "github.com/lib/pq"
)

// Data 星体信息数据结构体.
type Data struct {
	Database      *ent.Client
	Elasticsearch *ElasticsearchInfoClient
	RedisCli      RedisInfoI
}

// ElasticsearchInfoClient 星体信息 ElasticSearch 客户端.
type ElasticsearchInfoClient struct {
	*elasticsearch.Client
}

// RedisInfoI 星体信息 Redis 缓存接口.
type RedisInfoI interface {
	redis.Cmdable
}

// NewData 构造星体信息数据结构体.
func NewData(
	ent *ent.Client,
	esClient *ElasticsearchInfoClient,
	redisCmd RedisInfoI,
	logger log.Logger,
) (*Data, func(), error) {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/info--service/data"))

	d := &Data{
		Database:      ent,
		Elasticsearch: esClient,
		RedisCli:      redisCmd,
	}

	return d, func() {
		if err := d.Database.Close(); err != nil {
			logVal.Error(err)
		}
		if err := d.Elasticsearch.Close(context.Background()); err != nil {
			logVal.Error(err)
		}
	}, nil
}

// NewEntClient 构造星体信息 Ent 客户端.
func NewEntClient(conf *conf.Data, logger log.Logger) (*ent.Client, error) {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/info-service/data/ent"))

	// 创建星体信息 Ent 客户端
	DB, err := pkgDB.OpenDbWithOTel(conf.Database.Driver, conf.Database.Source)
	if err != nil {
		logVal.Fatalf("info db connect failed: %v", err)
	}
	drv := entsql.OpenDB(conf.Database.Driver, DB)
	client := ent.NewClient(ent.Driver(drv))
	if err := client.Schema.Create(context.Background(), migrate.WithForeignKeys(false)); err != nil {
		logVal.Fatalf("info schema migration failed: %v", err)
	}

	return client, err
}

// NewElasticSearchClient 构造星体信息 ElasticSearch 客户端.
func NewElasticSearchClient(conf *conf.Data, logger log.Logger) *ElasticsearchInfoClient {
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

	return &ElasticsearchInfoClient{
		Client: client,
	}
}

// NewRedisCmd 构造星体信息 Redis 连接.
func NewRedisCmd(conf *conf.Data, logger log.Logger) RedisInfoI {
	logVal := log.NewHelper(log.With(logger, "module", "stellar/info-service/data/redis"))

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
