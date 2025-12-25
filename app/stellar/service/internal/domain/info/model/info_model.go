package model

import (
	"time"

	"github.com/google/uuid"
)

// Entity 星体信息领域模型.
type Entity struct {
	ID                uuid.UUID `json:"info_id"`
	TenantID          uuid.UUID `json:"tenant_id"`
	FileURL           string    `json:"file_url"`
	Name              string    `json:"name"`
	Period            float64   `json:"period"`
	DispersionMeasure float64   `json:"dispersion_measure"`
	RaDeg             float64   `json:"ra_deg"`
	DecDeg            float64   `json:"dec_deg"`
	GalacticLongitude float64   `json:"galactic_longitude"`
	GalacticLatitude  float64   `json:"galactic_latitude"`
	SurveyName        string    `json:"survey_name"`
	CreateBy          int64     `json:"create_by"`
	CreateTime        time.Time `json:"create_time"`
	UpdateBy          int64     `json:"update_by"`
	UpdateTime        time.Time `json:"update_time"`
	Remark            string    `json:"remark"`
	Version           int32     `json:"version"`
}

// Condition 查询星体信息条件模型.
type Condition struct {
	PageNum           int64   `json:"page_num"`
	PageSize          int64   `json:"page_size"`
	Name              string  `json:"name"`
	Period            float64 `json:"period"`
	DispersionMeasure float64 `json:"dispersion_measure"`
	RaDeg             float64 `json:"ra_deg"`
	DecDeg            float64 `json:"dec_deg"`
	GalacticLongitude float64 `json:"galactic_longitude"`
	GalacticLatitude  float64 `json:"galactic_latitude"`
	SurveyName        string  `json:"survey_name"`
	Remark            string  `json:"remark"`
}
