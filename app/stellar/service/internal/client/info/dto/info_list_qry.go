package dto

import v1 "lersosa/api/stellar/service/v1/info"

// InfoListQry 星体信息列表查询.
type InfoListQry struct {
	Period            float64 `json:"period"`
	DispersionMeasure float64 `json:"dispersion_measure"`
	RaDeg             float64 `json:"ra_deg"`
	DecDeg            float64 `json:"dec_deg"`
	GalacticLongitude float64 `json:"galactic_longitude"`
	GalacticLatitude  float64 `json:"galactic_latitude"`
	SurveyName        string  `json:"survey_name"`
}

// NewInfoListQry 构造星体信息列表查询.
func NewInfoListQry(request *v1.InfoListRequest) *InfoListQry {
	return &InfoListQry{
		Period:            request.GetPeriod(),
		DispersionMeasure: request.GetDispersionMeasure(),
		RaDeg:             request.GetRaDeg(),
		DecDeg:            request.GetDecDeg(),
		GalacticLongitude: request.GetGalacticLongitude(),
		GalacticLatitude:  request.GetGalacticLatitude(),
		SurveyName:        request.GetSurveyName(),
	}
}
