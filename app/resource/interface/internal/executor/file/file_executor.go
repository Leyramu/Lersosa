package file

import (
	"context"
	v1 "lersosa/api/resource/interface/v1/file"
	"lersosa/app/resource/interface/internal/client/file"
	"lersosa/app/resource/interface/internal/client/ossconfig"
	"lersosa/app/resource/interface/internal/executor/file/command"
	"lersosa/app/resource/interface/internal/executor/file/command/query"

	"github.com/go-kratos/kratos/v2/transport/http"
)

// Executor 文件服务执行层.
type Executor struct {
	// pageQryExe 处理文件分页查询的执行器.
	pageQryExe *query.FilePageQryExe

	// getQryExe 处理文件查询的执行器.
	getQryExe *query.FileGetQryExe

	// saveCmdExe 处理文件保存的执行器.
	saveCmdExe *command.FileSaveCmdExe

	// modifyCmdExe 处理文件修改的执行器.
	modifyCmdExe *command.FileModifyCmdExe

	// removeCmdExe 处理文件删除的执行器.
	removeCmdExe *command.FileRemoveCmdExe

	// uploadCmdExe 处理文件上传的执行器.
	uploadCmdExe *command.FileUploadCmdExe
}

// NewExecutor 构造文件服务执行层.
func NewExecutor(fileClient *file.Client, ossConfig *ossconfig.Client) *Executor {
	return &Executor{
		// pageQryExe 处理文件分页查询的执行.
		pageQryExe: query.NewFilePageQryExe(fileClient),
		// getQryExe 处理文件查询的执行.
		getQryExe: query.NewFileGetQryExe(fileClient),
		// saveCmdExe 处理文件保存的执行.
		saveCmdExe: command.NewFileSaveCmdExe(fileClient),
		// modifyCmdExe 处理文件修改的执行.
		modifyCmdExe: command.NewFileModifyCmdExe(fileClient),
		// removeCmdExe 处理文件删除的执行.
		removeCmdExe: command.NewFileRemoveCmdExe(fileClient),
		// uploadCmdExe 处理文件上传的执行.
		uploadCmdExe: command.NewFileUploadCmdExe(fileClient, ossConfig),
	}
}

// Page 分页查询文件方法.
func (executor *Executor) Page(ctx context.Context, request *v1.FilePageRequest) (*v1.FilePageReply, error) {
	return executor.pageQryExe.Execute(ctx, request)
}

// Get 查询文件方法.
func (executor *Executor) Get(ctx context.Context, request *v1.FileGetRequest) (*v1.FileGetReply, error) {
	return executor.getQryExe.Execute(ctx, request)
}

// Save 保存文件方法.
func (executor *Executor) Save(ctx context.Context, request *v1.FileSaveRequest) error {
	return executor.saveCmdExe.ExecuteVoid(ctx, request)
}

// Modify 修改文件方法.
func (executor *Executor) Modify(ctx context.Context, request *v1.FileModifyRequest) error {
	return executor.modifyCmdExe.ExecuteVoid(ctx, request)
}

// Remove 删除文件方法.
func (executor *Executor) Remove(ctx context.Context, request *v1.FileRemoveRequest) error {
	return executor.removeCmdExe.ExecuteVoid(ctx, request)
}

// Upload 上传文件方法.
func (executor *Executor) Upload(ctx context.Context, stream v1.File_UploadFileServer) error {
	return executor.uploadCmdExe.ExecuteStream(ctx, stream)
}

// UploadMultipart 上传多部份文件方法.
func (executor *Executor) UploadMultipart(ctx context.Context, request *http.Request) error {
	return executor.uploadCmdExe.ExecuteVoid(ctx, request)
}
