package model

import (
	"time"

	"github.com/google/uuid"
)

// Event 星体信息事件模型.
type Event struct {
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
