package model

import "errors"

// Chat 领域错误定义.
var (
	ErrSessionNotFound      = errors.New("会话不存在")
	ErrSessionNotActive     = errors.New("会话未激活，无法添加消息")
	ErrSessionDeleted       = errors.New("会话已删除，无法操作")
	ErrMessageNotFound      = errors.New("消息不存在")
	ErrMessageContentEmpty  = errors.New("消息内容不能为空")
	ErrInferenceResultEmpty = errors.New("推理结果不能为空")
)
