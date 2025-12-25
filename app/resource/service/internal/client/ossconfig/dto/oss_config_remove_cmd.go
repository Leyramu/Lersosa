package dto

import v1 "lersosa/api/resource/service/v1/ossconfig"

// OssConfigRemoveCmd 删除资源配置命令.
type OssConfigRemoveCmd struct {
	OssConfigs []OssConfig `json:"oss_configs"`
}

// OssConfig 资源配置.
type OssConfig struct {
	OssConfigID string `json:"oss_config_id"`
	Version     int32  `json:"version"`
}

// NewOssConfigRemoveCmd 构造删除资源配置命令.
func NewOssConfigRemoveCmd(request *v1.OssConfigRemoveRequest) *OssConfigRemoveCmd {
	ossConfigs := make([]OssConfig, len(request.OssConfigs))
	for i, s := range request.OssConfigs {
		ossConfigs[i] = OssConfig{
			OssConfigID: s.OssConfigId,
			Version:     s.Version,
		}
	}

	return &OssConfigRemoveCmd{
		OssConfigs: ossConfigs,
	}
}
