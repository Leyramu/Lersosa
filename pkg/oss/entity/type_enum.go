package entity

import "fmt"

// Type 存储类型枚举.
type Type string

// 存储类型枚举值.
const (
	TypeLocal Type = "local"
	TypeMinio Type = "minio"
)

// IsValid 判断是否是有效的存储类型枚举值.
func (t Type) IsValid() bool {
	return t == TypeLocal || t == TypeMinio
}

// ParseType 解析存储类型枚举值.
func ParseType(code string) (Type, error) {
	t := Type(code)
	if !t.IsValid() {
		return "", fmt.Errorf("无效的OSS类型： %s", code)
	}
	return t, nil
}
