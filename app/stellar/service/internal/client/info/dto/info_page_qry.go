package dto

import v1 "lersosa/api/stellar/service/v1/info"

// InfoPageQry 星体信息分页查询.
type InfoPageQry struct {
	PageNum           int64   `json:"page_num"`
	PageSize          int64   `json:"page_size"`
	Period            float64 `json:"period"`
	DispersionMeasure float64 `json:"dispersion_measure"`
	RaDeg             float64 `json:"ra_deg"`
	DecDeg            float64 `json:"dec_deg"`
	GalacticLongitude float64 `json:"galactic_longitude"`
	GalacticLatitude  float64 `json:"galactic_latitude"`
	SurveyName        string  `json:"survey_name"`
}

// NewInfoPageQry 构造星体信息分页查询.
func NewInfoPageQry(request *v1.InfoPageRequest) *InfoPageQry {
	return &InfoPageQry{
		PageNum:           request.PageNum,
		PageSize:          request.PageSize,
		Period:            request.Period,
		DispersionMeasure: request.DispersionMeasure,
		RaDeg:             request.RaDeg,
		DecDeg:            request.DecDeg,
		GalacticLongitude: request.GalacticLongitude,
		GalacticLatitude:  request.GalacticLatitude,
		SurveyName:        request.SurveyName,
	}
}
