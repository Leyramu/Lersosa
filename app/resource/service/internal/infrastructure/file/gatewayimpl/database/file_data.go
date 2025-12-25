package database

import (
	"context"
	"lersosa/app/resource/service/internal/conf"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database/ent"
	"lersosa/app/resource/service/internal/infrastructure/file/gatewayimpl/database/ent/migrate"
	pkgDB "lersosa/pkg/db"
	pkgRedis "lersosa/pkg/redis"

	entsql "entgo.io/ent/dialect/sql"
	"github.com/go-kratos/kratos/v2/log"
	_ "github.com/lib/pq"
	"github.com/redis/go-redis/v9"
)

// Data 文件数据结构体.
type Data struct {
	Database *ent.Client
	RedisCli RedisFileI
}

// RedisFileI 文件 Redis 缓存接口.
type RedisFileI interface {
	redis.Cmdable
}

// NewData 构造文件数据结构体.
func NewData(
	ent *ent.Client,
	redisCli RedisFileI,
	logger log.Logger,
) (*Data, func(), error) {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data"))

	d := &Data{
		Database: ent,
		RedisCli: redisCli,
	}

	return d, func() {
		if err := d.Database.Close(); err != nil {
			logVal.Error(err)
		}
	}, nil
}

// NewEntClient 构造文件 Ent 客户端.
func NewEntClient(conf *conf.Data, logger log.Logger) *ent.Client {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data/file/ent"))

	// 创建文件 Ent 客户端
	DB, err := pkgDB.OpenDbWithOTel(conf.Database.Driver, conf.Database.Source)
	if err != nil {
		logVal.Fatalf("file 数据库连接失败: %v", err)
	}
	drv := entsql.OpenDB(conf.Database.Driver, DB)
	client := ent.NewClient(ent.Driver(drv))
	if err := client.Schema.Create(context.Background(), migrate.WithForeignKeys(false)); err != nil {
		logVal.Fatalf("file schema migration failed: %v", err)
	}

	logVal.Debug("file 数据库连接成功")
	return client
}

// NewRedisCmd 构造文件 Redis 连接.
func NewRedisCmd(conf *conf.Data, logger log.Logger) RedisFileI {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data/file/redis"))

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

	logVal.Debug("Redis 连接成功")
	return client
}
