package co

import (
	v1 "lersosa/api/stellar/service/v1/info"
	"time"

	"github.com/google/uuid"
)

// InfoCo 星体信息客户端模型.
type InfoCo struct {
	InfoID            uuid.UUID `json:"info_id"`
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

// NewInfoPageCo 构造星体信息客户端模型.
func NewInfoPageCo(co *InfoCo) *v1.InfoPageReply_Info {
	return &v1.InfoPageReply_Info{
		InfoId:            co.InfoID.String(),
		FileUrl:           co.FileURL,
		Name:              co.Name,
		Period:            co.Period,
		DispersionMeasure: co.DispersionMeasure,
		RaDeg:             co.RaDeg,
		DecDeg:            co.DecDeg,
		GalacticLongitude: co.GalacticLongitude,
		GalacticLatitude:  co.GalacticLatitude,
		SurveyName:        co.SurveyName,
		CreateBy:          co.CreateBy,
		CreateTime:        co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:          co.UpdateBy,
		UpdateTime:        co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:            co.Remark,
		Version:           co.Version,
	}
}

// NewInfoListCo 构造星体信息列表客户端模型.
func NewInfoListCo(co *InfoCo) *v1.InfoListReply_Info {
	return &v1.InfoListReply_Info{
		InfoId:            co.InfoID.String(),
		FileUrl:           co.FileURL,
		Name:              co.Name,
		Period:            co.Period,
		DispersionMeasure: co.DispersionMeasure,
		RaDeg:             co.RaDeg,
		DecDeg:            co.DecDeg,
		GalacticLongitude: co.GalacticLongitude,
		GalacticLatitude:  co.GalacticLatitude,
		SurveyName:        co.SurveyName,
		CreateBy:          co.CreateBy,
		CreateTime:        co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:          co.UpdateBy,
		UpdateTime:        co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:            co.Remark,
		Version:           co.Version,
	}
}

// NewInfoGetCo 构造星体信息客户端模型.
func NewInfoGetCo(co *InfoCo) *v1.InfoGetReply {
	return &v1.InfoGetReply{
		InfoId:            co.InfoID.String(),
		FileUrl:           co.FileURL,
		Name:              co.Name,
		Period:            co.Period,
		DispersionMeasure: co.DispersionMeasure,
		RaDeg:             co.RaDeg,
		DecDeg:            co.DecDeg,
		GalacticLongitude: co.GalacticLongitude,
		GalacticLatitude:  co.GalacticLatitude,
		SurveyName:        co.SurveyName,
		CreateBy:          co.CreateBy,
		CreateTime:        co.CreateTime.Format("2006-01-02 15:04:05"),
		UpdateBy:          co.UpdateBy,
		UpdateTime:        co.UpdateTime.Format("2006-01-02 15:04:05"),
		Remark:            co.Remark,
		Version:           co.Version,
	}
}
