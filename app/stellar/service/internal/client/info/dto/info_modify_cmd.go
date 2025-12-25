package dto

import (
	v1 "lersosa/api/stellar/service/v1/info"
)

// InfoModifyCmd 修改星体信息命令.
type InfoModifyCmd struct {
	InfoID            string  `json:"info_id"`
	Name              string  `json:"name"`
	Period            float64 `json:"period"`
	DispersionMeasure float64 `json:"dispersion_measure"`
	RaDeg             float64 `json:"ra_deg"`
	DecDeg            float64 `json:"dec_deg"`
	GalacticLongitude float64 `json:"galactic_longitude"`
	GalacticLatitude  float64 `json:"galactic_latitude"`
	SurveyName        string  `json:"survey_name"`
	UpdateBy          int64   `json:"update_by"`
	Remark            string  `json:"remark"`
	Version           int32   `json:"version"`
}

// NewInfoModifyCmd 构造修改星体信息命令.
func NewInfoModifyCmd(request *v1.InfoModifyRequest) *InfoModifyCmd {
	return &InfoModifyCmd{
		InfoID:            request.InfoId,
		Name:              request.Name,
		Period:            request.Period,
		DispersionMeasure: request.DispersionMeasure,
		RaDeg:             request.RaDeg,
		DecDeg:            request.DecDeg,
		GalacticLongitude: request.GalacticLongitude,
		GalacticLatitude:  request.GalacticLatitude,
		SurveyName:        request.SurveyName,
		UpdateBy:          request.UpdateBy,
		Remark:            request.Remark,
		Version:           request.Version,
	}
}
