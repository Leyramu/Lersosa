package server

import (
	"encoding/json"
	"fmt"

	nethttp "net/http"
	"strconv"

	"github.com/bytedance/gopkg/util/logger"
	"github.com/go-kratos/kratos/v2/transport/http"
	chatv1 "github.com/leyramu/lersosa/api/ai/service/chat/v1"
	"github.com/leyramu/lersosa/app/ai/service/internal/adapter/chat/rpc"
	"github.com/leyramu/lersosa/app/ai/service/internal/conf"
	pkgTls "github.com/leyramu/lersosa/pkg/tls"

	"github.com/go-kratos/kratos/v2/log"
	"github.com/go-kratos/kratos/v2/middleware/logging"
	"github.com/go-kratos/kratos/v2/middleware/recovery"
)

// NewHTTPServer HTTP 服务器.
func NewHTTPServer(
	c *conf.Server,
	chat *rpc.ChatAdapter,
	logger log.Logger,
) *http.Server {
	var opts = []http.ServerOption{
		http.Middleware(
			recovery.Recovery(),
			logging.Server(logger),
		),
	}

	if c.Http.Network != "" {
		opts = append(opts, http.Network(c.Http.Network))
	}
	if c.Http.Addr != "" {
		opts = append(opts, http.Address(c.Http.Addr))
	}
	if c.Http.Timeout != nil {
		opts = append(opts, http.Timeout(c.Http.Timeout.AsDuration()))
	}
	if c.Http.TlsEnable == true {
		log.Info("已启用 HTTP mTLS")
		if tls, err := pkgTls.NewServerTlsConfig(c.Http.CertFile, c.Http.KeyFile, c.Http.CaFile); err == nil {
			opts = append(opts, http.TLSConfig(tls))
		} else {
			log.Error("服务器 HTTP mTLS 配置错误：", err)
		}
	} else {
		log.Info("未启用 TLS")
	}

	srv := http.NewServer(opts...)
	chatv1.RegisterChatServiceHTTPServer(srv, chat)

	r := srv.Route("/")
	r.GET("/v1/chat/sessions", listSessionsHTTPHandler(chat))
	r.POST("/v1/chat/messages/stream", sendMessageSSEHandler(chat))
	r.POST("/v1/chat/messages/send", sendMessageHTTPHandler(chat))

	return srv
}

func listSessionsHTTPHandler(chatAdapter *rpc.ChatAdapter) func(ctx http.Context) error {
	return func(ctx http.Context) error {
		queryValues := ctx.Request().URL.Query()
		in := &chatv1.ListSessionsRequest{
			Page:     parseQueryInt32(queryValues.Get("page"), 1),
			PageSize: parseQueryInt32(firstNonEmpty(queryValues.Get("pageSize"), queryValues.Get("page_size")), 20),
			CreateBy: firstNonEmpty(queryValues.Get("createBy"), queryValues.Get("create_by")),
			Title:    queryValues.Get("title"),
		}
		out, err := chatAdapter.ListSessions(ctx, in)
		if err != nil {
			return err
		}
		return ctx.Result(200, out)
	}
}

func sendMessageSSEHandler(chat *rpc.ChatAdapter) func(ctx http.Context) error {
	return func(ctx http.Context) error {
		var in chatv1.SendMessageRequest
		if err := ctx.Bind(&in); err != nil {
			return err
		}
		if err := ctx.BindQuery(&in); err != nil {
			return err
		}
		if err := chat.ValidateSendMessageRequest(&in); err != nil {
			return err
		}

		response := ctx.Response()
		flusher, ok := response.(http.Flusher)
		if !ok {
			return fmt.Errorf("response writer does not support flushing")
		}
		response.Header().Set("Content-Type", "text/event-stream")
		response.Header().Set("Cache-Control", "no-cache")
		response.Header().Set("Connection", "keep-alive")
		response.Header().Set("X-Accel-Buffering", "no")
		response.WriteHeader(nethttp.StatusOK)
		flusher.Flush()

		if err := chat.StreamSendMessageHTTP(ctx, &in, response, flusher); err != nil {
			logger.Errorf("http sse stream aborted after response started: %v", err)
			if writeErr := writeSSEErrorFrame(response, flusher, err); writeErr != nil {
				logger.Errorf("http sse error frame write failed: %v", writeErr)
			}
			return nil
		}

		return nil
	}
}

func writeSSEErrorFrame(writer http.ResponseWriter, flusher http.Flusher, err error) error {
	if err == nil {
		return nil
	}
	payload, marshalErr := json.Marshal(map[string]any{
		"isEnd": true,
		"error": err.Error(),
	})
	if marshalErr != nil {
		return fmt.Errorf("marshal sse error reply: %w", marshalErr)
	}
	if _, writeErr := fmt.Fprintf(writer, "data: %s\n\n", payload); writeErr != nil {
		return fmt.Errorf("write sse error reply: %w", writeErr)
	}
	flusher.Flush()
	return nil
}

func sendMessageHTTPHandler(chatAdapter *rpc.ChatAdapter) func(ctx http.Context) error {
	return func(ctx http.Context) error {
		var in chatv1.SendMessageRequest
		if err := ctx.Bind(&in); err != nil {
			return err
		}
		if err := ctx.BindQuery(&in); err != nil {
			return err
		}
		out, err := chatAdapter.SendMessageHTTP(ctx, &in)
		if err != nil {
			return err
		}
		return ctx.Result(200, out)
	}
}

func parseQueryInt32(value string, fallback int32) int32 {
	if value == "" {
		return fallback
	}
	parsed, err := strconv.ParseInt(value, 10, 32)
	if err != nil {
		return fallback
	}
	return int32(parsed)
}

func firstNonEmpty(values ...string) string {
	for _, value := range values {
		if value != "" {
			return value
		}
	}
	return ""
}
