package gateway

import (
	"context"
	"lersosa/app/resource/service/internal/domain/file/model"

	"github.com/google/uuid"
)

// RepoI 文件基础设施层仓储接口.
type RepoI interface {
	// Page 分页查询文件接口.
	Page(ctx context.Context, condition *model.Condition) ([]*model.Entity, error)

	// List 查询所有文件接口.
	List(ctx context.Context) ([]*model.Entity, error)

	// Get 获取文件接口.
	Get(ctx context.Context, id uuid.UUID) (*model.Entity, error)

	// Save 保存文件接口.
	Save(ctx context.Context, entity *model.Entity) error

	// Modify 修改文件接口.
	Modify(ctx context.Context, entity *model.Entity) error

	// Remove 删除文件接口.
	Remove(ctx context.Context, entities *[]model.Entity) error
}
