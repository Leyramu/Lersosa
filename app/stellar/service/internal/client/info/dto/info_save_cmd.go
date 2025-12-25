package dto

import (
	v1 "lersosa/api/stellar/service/v1/info"

	"github.com/google/uuid"
)

// InfoSaveCmd 保存星体信息命令.
type InfoSaveCmd struct {
	Name              string  `json:"name"`
	FileURL           string  `json:"file_url"`
	Period            float64 `json:"period"`
	DispersionMeasure float64 `json:"dispersion_measure"`
	RaDeg             float64 `json:"ra_deg"`
	DecDeg            float64 `json:"dec_deg"`
	GalacticLongitude float64 `json:"galactic_longitude"`
	GalacticLatitude  float64 `json:"galactic_latitude"`
	SurveyName        string  `json:"survey_name"`
	CreateBy          int64   `json:"create_by"`
	Remark            string  `json:"remark"`
}

// NewInfoSaveCmd 构造保存星体信息命令.
func NewInfoSaveCmd(request *v1.InfoSaveRequest) *InfoSaveCmd {
	return &InfoSaveCmd{
		Name: request.Name,
		//FileURL:           request.FileUrl,
		FileURL:           uuid.NewString(),
		Period:            request.Period,
		DispersionMeasure: request.DispersionMeasure,
		RaDeg:             request.RaDeg,
		DecDeg:            request.DecDeg,
		GalacticLongitude: request.GalacticLongitude,
		GalacticLatitude:  request.GalacticLatitude,
		SurveyName:        request.SurveyName,
		CreateBy:          request.CreateBy,
		Remark:            request.Remark,
	}
}
