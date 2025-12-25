package entity

// OssInfo 存储信息.
type OssInfo struct {
	Type                   Type   `json:"type"`
	ConfigKey              string `json:"config_key"`
	AccessKey              string `json:"access_key"`
	SecretKey              string `json:"secret_key"`
	BucketName             string `json:"bucket_name"`
	Prefix                 string `json:"prefix"`
	Endpoint               string `json:"endpoint"`
	Domain                 string `json:"domain"`
	IsHTTPS                string `json:"is_https"`
	Region                 string `json:"region"`
	AccessPolicy           string `json:"access_policy"`
	Status                 string `json:"status"`
	Ext1                   string `json:"ext1"`
	Remark                 string `json:"remark"`
	Version                int32  `json:"version"`
	PathStyleAccessEnabled *bool  `json:"pathStyleAccessEnabled,omitempty"`
}
