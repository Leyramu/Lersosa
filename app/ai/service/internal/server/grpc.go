package server

import (
	"github.com/go-kratos/kratos/v2/log"
	"github.com/go-kratos/kratos/v2/middleware/logging"
	"github.com/go-kratos/kratos/v2/middleware/recovery"
	"github.com/go-kratos/kratos/v2/transport/grpc"
	chatv1 "github.com/leyramu/lersosa/api/ai/service/chat/v1"
	//v1Knowledge "github.com/leyramu/lersosa/api/ai/service/knowledge/v1" // knowledge 作为独立领域保留，待后续补齐后再接入
	// Deleted:v1Message "github.com/leyramu/lersosa/api/ai/service/message/v1"
	// Deleted:v1Session "github.com/leyramu/lersosa/api/ai/service/session/v1"
	// Deleted:v1Trace "github.com/leyramu/lersosa/api/ai/service/trace/v1" // trace 已并入 chat 聚合
	chat "github.com/leyramu/lersosa/app/ai/service/internal/adapter/chat/rpc"
	//knowledge "github.com/leyramu/lersosa/app/ai/service/internal/adapter/knowledge/rpc" // knowledge 领域待独立接入
	// Deleted:message "github.com/leyramu/lersosa/app/ai/service/internal/adapter/message/rpc"
	// Deleted:session "github.com/leyramu/lersosa/app/ai/service/internal/adapter/session/rpc"
	// Deleted:trace "github.com/leyramu/lersosa/app/ai/service/internal/adapter/trace/rpc"
	"github.com/leyramu/lersosa/app/ai/service/internal/conf"

	pkgTls "github.com/leyramu/lersosa/pkg/tls"
)

// NewGRPCServer gRPC 服务器.
func NewGRPCServer(
	c *conf.Server,
	chat *chat.ChatAdapter,
	//knowledge *knowledge.KnowledgeAdapter,
	// Deleted:message *message.MessageAdapter,
	// Deleted:session *session.SessionAdapter,
	// Deleted:trace *trace.TraceAdapter,
	logger log.Logger,
) *grpc.Server {
	var opts = []grpc.ServerOption{
		grpc.Middleware(
			recovery.Recovery(),
			logging.Server(logger),
		),
	}
	if c.Grpc.Network != "" {
		opts = append(opts, grpc.Network(c.Grpc.Network))
	}
	if c.Grpc.Addr != "" {
		opts = append(opts, grpc.Address(c.Grpc.Addr))
	}
	if c.Grpc.Timeout != nil {
		opts = append(opts, grpc.Timeout(c.Grpc.Timeout.AsDuration()))
	}
	if c.Grpc.TlsEnable == true {
		log.Info("已启用 gRPC mTLS")
		if tls, err := pkgTls.NewServerTlsConfig(c.Grpc.CertFile, c.Grpc.KeyFile, c.Grpc.CaFile); err == nil {
			opts = append(opts, grpc.TLSConfig(tls))
		} else {
			log.Errorf("服务器 TLS 配置错误：%v", err)
		}
	} else {
		log.Info("未启用 gRPC mTLS")
	}

	srv := grpc.NewServer(opts...)
	chatv1.RegisterChatServiceServer(srv, chat)
	//v1Knowledge.RegisterKnowledgeServer(srv, knowledge)
	// Deleted:v1Message.RegisterMessageServer(srv, message)
	// Deleted:v1Session.RegisterSessionServer(srv, session)
	// Deleted:v1Trace.RegisterTraceServer(srv, trace)
	return srv
}
