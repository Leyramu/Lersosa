package database

import (
	"context"
	"lersosa/app/resource/service/internal/conf"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database/ent"
	"lersosa/app/resource/service/internal/infrastructure/ossconfig/gatewayimpl/database/ent/migrate"
	pkgDB "lersosa/pkg/db"
	pkgRedis "lersosa/pkg/redis"

	entsql "entgo.io/ent/dialect/sql"
	"github.com/go-kratos/kratos/v2/log"
	_ "github.com/lib/pq"
	"github.com/redis/go-redis/v9"
)

// Data 资源配置数据结构体.
type Data struct {
	Database *ent.Client
	RedisCli RedisOssConfigI
}

// RedisOssConfigI 资源配置 Redis 缓存接口.
type RedisOssConfigI interface {
	redis.Cmdable
}

// NewData 构造资源配置数据结构体.
func NewData(
	ent *ent.Client,
	redisCmd RedisOssConfigI,
	logger log.Logger,
) (*Data, func(), error) {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data"))

	d := &Data{
		Database: ent,
		RedisCli: redisCmd,
	}

	//ctx, cancel := context.WithTimeout(context.Background(), 15*time.Second)
	//defer cancel()

	//if err := NewRedisCache(d, logger).InitCache(ctx); err != nil {
	//	logVal.Error(err)
	//	return nil, nil, err
	//}
	//logVal.Debug("Redis 缓存初始化完成")

	return d, func() {
		if err := d.Database.Close(); err != nil {
			logVal.Error(err)
		}
	}, nil
}

// NewEntClient 构造资源配置 Ent 客户端.
func NewEntClient(conf *conf.Data, logger log.Logger) *ent.Client {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data/oss-config/ent"))

	// 创建资源配置 Ent 客户端
	DB, err := pkgDB.OpenDbWithOTel(conf.Database.Driver, conf.Database.Source)
	if err != nil {
		logVal.Fatalf("ossConfig  数据库连接失败: %v", err)
	}
	drv := entsql.OpenDB(conf.Database.Driver, DB)
	client := ent.NewClient(ent.Driver(drv))
	if err := client.Schema.Create(context.Background(), migrate.WithForeignKeys(false)); err != nil {
		logVal.Fatalf("ossConfig schema migration failed: %v", err)
	}

	logVal.Debug("ossConfig 数据库连接成功")
	return client
}

// NewRedisCmd 构造资源配置 Redis 连接.
func NewRedisCmd(conf *conf.Data, logger log.Logger) RedisOssConfigI {
	logVal := log.NewHelper(log.With(logger, "module", "resource-service/data/oss-config/redis"))

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
