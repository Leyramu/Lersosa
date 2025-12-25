package web

import (
	"context"
	"encoding/json"
	v1 "lersosa/api/resource/interface/v1/file"
	"lersosa/app/resource/interface/internal/executor/file"

	"github.com/go-kratos/kratos/v2/transport/http"
)

// FileController 文件控制层.
type FileController struct {
	// v1.UnimplementedFileServer 未实现文件控制层接口.
	v1.UnimplementedFileServer

	// executor 文件服务执行者.
	executor *file.Executor
}

// NewFileController 构造文件控制层.
func NewFileController(executor *file.Executor) *FileController {
	return &FileController{executor: executor}
}

// PageFile 实现分页查询文件方法.
func (controller *FileController) PageFile(ctx context.Context, request *v1.FilePageRequest) (*v1.FilePageReply, error) {
	page, err := controller.executor.Page(ctx, request)

	if err != nil {
		return &v1.FilePageReply{
			Code: 500,
			Msg:  err.Error(),
			Rows: nil,
		}, err
	}

	return &v1.FilePageReply{
		Code:  200,
		Msg:   "查询成功",
		Rows:  page.Rows,
		Total: page.Total,
	}, nil
}

// GetFile 实现查询文件方法.
func (controller *FileController) GetFile(ctx context.Context, request *v1.FileGetRequest) (*v1.FileGetReply, error) {
	get, err := controller.executor.Get(ctx, request)

	if err != nil {
		return &v1.FileGetReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.FileGetReply{
		Code: 200,
		Msg:  "查询成功",
		Data: get.Data,
	}, nil
}

// SaveFile 实现保存文件方法.
func (controller *FileController) SaveFile(ctx context.Context, request *v1.FileSaveRequest) (*v1.FileSaveReply, error) {
	if err := controller.executor.Save(ctx, request); err != nil {
		return &v1.FileSaveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.FileSaveReply{
		Code: 200,
		Msg:  "保存成功",
		Data: nil,
	}, nil
}

// ModifyFile 实现修改文件方法.
func (controller *FileController) ModifyFile(ctx context.Context, request *v1.FileModifyRequest) (*v1.FileModifyReply, error) {
	if err := controller.executor.Modify(ctx, request); err != nil {
		return &v1.FileModifyReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.FileModifyReply{
		Code: 200,
		Msg:  "修改成功",
		Data: nil,
	}, nil
}

// RemoveFile 实现删除文件方法.
func (controller *FileController) RemoveFile(ctx context.Context, request *v1.FileRemoveRequest) (*v1.FileRemoveReply, error) {
	if err := controller.executor.Remove(ctx, request); err != nil {
		return &v1.FileRemoveReply{
			Code: 500,
			Msg:  err.Error(),
			Data: nil,
		}, err
	}

	return &v1.FileRemoveReply{
		Code: 200,
		Msg:  "删除成功",
		Data: nil,
	}, nil
}

// UploadFile 实现上传文件方法.
func (controller *FileController) UploadFile(stream v1.File_UploadFileServer) error {
	return controller.executor.Upload(stream.Context(), stream)
}

// UploadMultipartFile 上传多部份文件方法.
func (controller *FileController) UploadMultipartFile(ctx http.Context) error {
	resp := struct {
		Code int         `json:"code"`
		Msg  string      `json:"msg"`
		Data interface{} `json:"data,omitempty"`
	}{
		Code: 200,
		Msg:  "上传成功",
		Data: nil,
	}

	err := controller.executor.UploadMultipart(ctx, ctx.Request())
	if err != nil {
		resp.Code = 500
		resp.Msg = err.Error()
		return err
	}

	w := ctx.Response()
	w.Header().Set("Content-Type", "application/json; charset=utf-8")
	if encodeErr := json.NewEncoder(w).Encode(resp); encodeErr != nil {
		return encodeErr
	}

	return nil
}
