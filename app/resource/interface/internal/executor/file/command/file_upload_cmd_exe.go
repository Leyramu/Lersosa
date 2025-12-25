package command

import (
	"bytes"
	"context"
	"errors"
	"io"
	v1 "lersosa/api/resource/interface/v1/file"
	fileClient "lersosa/api/resource/service/v1/file"
	ossconfigClient "lersosa/api/resource/service/v1/ossconfig"
	"lersosa/app/resource/interface/internal/client/file"
	"lersosa/app/resource/interface/internal/client/ossconfig"
	"lersosa/pkg/oss/api"
	"mime/multipart"
	"path/filepath"

	pkgOssEntity "lersosa/pkg/oss/entity"
	pkgOss "lersosa/pkg/oss/template"

	"github.com/go-kratos/kratos/v2/transport/http"
	"google.golang.org/grpc/codes"
	"google.golang.org/grpc/status"
)

// FileUploadCmdExe 文件上传命令执行器.
type FileUploadCmdExe struct {
	fileClient *file.Client
	ossConfig  *ossconfig.Client
	template   *pkgOss.StorageTemplate
}

// NewFileUploadCmdExe 创建文件上传命令执行器.
func NewFileUploadCmdExe(
	fileClient *file.Client,
	ossConfig *ossconfig.Client,
) *FileUploadCmdExe {
	return &FileUploadCmdExe{
		fileClient: fileClient,
		ossConfig:  ossConfig,
	}
}

// ExecuteVoid 执行命令.
func (exe *FileUploadCmdExe) ExecuteVoid(ctx context.Context, request *http.Request) error {
	// 获取文件数据
	fileData, handler, err := request.FormFile("fileData")
	if err != nil {
		return err
	}
	defer func(file multipart.File) {
		err := file.Close()
		if err != nil {
			_ = err
		}
	}(fileData)

	// 获取默认 OSS 配置
	ossConfig, err := exe.ossConfig.GetDefaultOssConfig(ctx, &ossconfigClient.OssConfigGetDefaultRequest{})
	if err != nil {
		return err
	}

	// 上传文件
	var rv *api.Result
	if rv, err = exe.template.Upload(
		&pkgOssEntity.FileInfo{
			FileSize:    handler.Size,
			FileName:    handler.Filename,
			FileSuffix:  filepath.Ext(handler.Filename),
			ContentType: handler.Header.Get("Content-Type"),
			Chuck:       fileData,
		},
		&pkgOssEntity.OssInfo{
			ConfigKey:    ossConfig.ConfigKey,
			AccessKey:    ossConfig.AccessKey,
			SecretKey:    ossConfig.SecretKey,
			BucketName:   ossConfig.BucketName,
			Prefix:       ossConfig.Prefix,
			Endpoint:     ossConfig.Endpoint,
			Domain:       ossConfig.Domain,
			IsHTTPS:      ossConfig.IsHttps,
			Region:       ossConfig.Region,
			AccessPolicy: ossConfig.AccessPolicy,
			Status:       ossConfig.Status,
			Ext1:         ossConfig.Ext1,
		},
	); err != nil {
		return err
	}

	// 保存文件
	if _, err := exe.fileClient.SaveFile(ctx, exe.toClientModel(rv)); err != nil {
		return err
	}

	return nil
}

// ExecuteStream 执行命令.
func (exe *FileUploadCmdExe) ExecuteStream(ctx context.Context, stream v1.File_UploadFileServer) error {
	firstReq, err := stream.Recv()
	if err != nil {
		if errors.Is(err, io.EOF) {
			return status.Error(codes.InvalidArgument, "空上传流")
		}
		return status.Errorf(codes.Internal, "未能收到第一条消息： %v", err)
	}

	info := firstReq.GetInfo()
	if info == nil {
		return status.Error(codes.InvalidArgument, "第一条消息必须是文件元数据（信息）")
	}

	fileName := info.GetFileName()
	contentType := info.GetContentType()
	fileSize := info.GetFileSize()

	if fileName == "" || fileSize <= 0 {
		return status.Error(codes.InvalidArgument, "无效文件元数据")
	}

	// 初始化文件数据
	fileData := make([]byte, 0, fileSize)

	// 接收后续所有数据块
	for {
		req, err := stream.Recv()
		if err != nil {
			if errors.Is(err, io.EOF) {
				break
			}
			return status.Errorf(codes.Internal, "未能接收块： %v", err)
		}

		chunk := req.GetChunk()
		if chunk == nil {
			return status.Error(codes.InvalidArgument, "非第一消息必须是数据块")
		}

		fileData = append(fileData, chunk.GetChunkData()...)
	}

	// 校验总大小
	if int64(len(fileData)) != fileSize {
		return status.Errorf(codes.InvalidArgument,
			"文件大小不匹配：预期的 %d 字节，结果是 %d",
			fileSize, len(fileData))
	}

	// 获取默认 OSS 配置
	ossConfig, err := exe.ossConfig.GetDefaultOssConfig(ctx, &ossconfigClient.OssConfigGetDefaultRequest{})
	if err != nil {
		return err
	}

	// 上传文件
	var rv *api.Result
	if rv, err = exe.template.Upload(
		&pkgOssEntity.FileInfo{
			FileSize:    fileSize,
			FileName:    fileName,
			FileSuffix:  filepath.Ext(fileName),
			ContentType: contentType,
			Chuck:       bytes.NewReader(fileData),
		},
		&pkgOssEntity.OssInfo{
			ConfigKey:    ossConfig.ConfigKey,
			AccessKey:    ossConfig.AccessKey,
			SecretKey:    ossConfig.SecretKey,
			BucketName:   ossConfig.BucketName,
			Prefix:       ossConfig.Prefix,
			Endpoint:     ossConfig.Endpoint,
			Domain:       ossConfig.Domain,
			IsHTTPS:      ossConfig.IsHttps,
			Region:       ossConfig.Region,
			AccessPolicy: ossConfig.AccessPolicy,
			Status:       ossConfig.Status,
			Ext1:         ossConfig.Ext1,
		},
	); err != nil {
		return err
	}

	// 保存文件
	if _, err := exe.fileClient.SaveFile(ctx, exe.toClientModel(rv)); err != nil {
		return err
	}

	// 发送成功响应
	if err := stream.SendAndClose(&v1.FileUploadReply{
		OriginalName: rv.OriginalName,
		FileName:     rv.FileName,
		FileSuffix:   rv.FileSuffix,
		Url:          rv.URL,
		Service:      rv.Service,
	}); err != nil {
		return err
	}

	return nil
}

// toClientModel 转换为客户端模型.
func (exe *FileUploadCmdExe) toClientModel(rv *api.Result) *fileClient.FileSaveRequest {
	return &fileClient.FileSaveRequest{
		FileName:     rv.FileName,
		OriginalName: rv.OriginalName,
		FileSuffix:   rv.FileSuffix,
		Url:          rv.URL,
		Ext1:         "",
		CreateDept:   0,
		CreateBy:     0,
		Service:      rv.Service,
	}
}
