package gateway

import (
	"context"

	"github.com/leyramu/lersosa/app/ai/service/internal/domain/chat/model"
)

// EinoGateway Chat 领域模型推理端口。
type EinoGateway interface {
	// Generate 执行一次聊天模型推理。
	Generate(ctx context.Context, aggregate *model.ChatA) (*model.EinoView, error)

	// Stream 执行一次聊天模型
	Stream(ctx context.Context, aggregate *model.ChatA, onChunk func(string) error) (*model.EinoView, error)
}
