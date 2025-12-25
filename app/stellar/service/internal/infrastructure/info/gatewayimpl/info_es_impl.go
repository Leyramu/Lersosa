package gatewayimpl

import (
	"bytes"
	"context"
	"encoding/json"
	"fmt"
	"io"
	"lersosa/app/stellar/service/internal/domain/info/gateway"
	"lersosa/app/stellar/service/internal/domain/info/model"
	"lersosa/app/stellar/service/internal/infrastructure/info/gatewayimpl/database"

	"github.com/elastic/go-elasticsearch/v9/esapi"
	"github.com/go-kratos/kratos/v2/log"
	"github.com/google/uuid"
)

// ElasticSearchImpl 星体信息 ElasticSearch 仓储实现.
var _ gateway.ElasticSearchI = (*ElasticSearchImpl)(nil)

// ElasticSearchImpl 星体信息 ElasticSearch 实现.
type ElasticSearchImpl struct {
	data *database.Data
	log  *log.Helper
}

// NewElasticSearchImpl 构造星体信息 ElasticSearch 仓储.
func NewElasticSearchImpl(data *database.Data, logger log.Logger) gateway.ElasticSearchI {
	return &ElasticSearchImpl{
		data: data,
		log:  log.NewHelper(log.With(logger, "module", "stellar/service/internal/infrastructure/data/info")),
	}
}

// Get 获取星体信息.
func (elasticSearch ElasticSearchImpl) Get(ctx context.Context, id uuid.UUID) (*model.Entity, error) {
	elasticSearch.log.WithContext(ctx).Debugf("Getting entity from Elasticsearch, id: %s", id.String())

	req := esapi.GetRequest{
		Index:      "stellar_info",
		DocumentID: id.String(),
	}

	res, err := req.Do(ctx, elasticSearch.data.Elasticsearch.Client)
	if err != nil {
		elasticSearch.log.WithContext(ctx).Errorf("ES Get request failed: %v", err)
		return nil, fmt.Errorf("failed to get document from ES: %w", err)
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			elasticSearch.log.WithContext(ctx).Errorf("Failed to close ES response body: %v", err)
			return
		}
	}(res.Body)

	if res.IsError() {
		if res.StatusCode == 404 {
			elasticSearch.log.WithContext(ctx).Debugf("Document not found, id: %s", id.String())
			return nil, nil
		}
		elasticSearch.log.WithContext(ctx).Errorf("ES Get returned error: %s", res.String())
		return nil, fmt.Errorf("ES get error: %s", res.String())
	}

	var doc struct {
		Source model.Entity `json:"_source"`
	}
	if err := json.NewDecoder(res.Body).Decode(&doc); err != nil {
		elasticSearch.log.WithContext(ctx).Errorf("Failed to decode ES response: %v", err)
		return nil, fmt.Errorf("failed to parse ES response: %w", err)
	}

	return &doc.Source, nil
}

// Save 保存星体信息.
func (elasticSearch ElasticSearchImpl) Save(ctx context.Context, e *model.Entity) error {
	elasticSearch.log.WithContext(ctx).Debugf("Saving entity to Elasticsearch, id: %s", e.ID.String())

	body, err := json.Marshal(e)
	if err != nil {
		elasticSearch.log.WithContext(ctx).Errorf("Failed to marshal entity: %v", err)
		return fmt.Errorf("marshal entity failed: %w", err)
	}

	req := esapi.IndexRequest{
		Index:      "stellar_info",
		DocumentID: e.ID.String(),
		Body:       bytes.NewReader(body),
		Refresh:    "false",
	}

	res, err := req.Do(ctx, elasticSearch.data.Elasticsearch.Client)
	if err != nil {
		elasticSearch.log.WithContext(ctx).Errorf("ES Index request failed: %v", err)
		return fmt.Errorf("failed to index document in ES: %w", err)
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			elasticSearch.log.WithContext(ctx).Errorf("Failed to close ES response body: %v", err)
			return
		}
	}(res.Body)

	if res.IsError() {
		elasticSearch.log.WithContext(ctx).Errorf("ES Index returned error: %s", res.String())
		return fmt.Errorf("ES index error: %s", res.String())
	}

	elasticSearch.log.WithContext(ctx).Infof("Entity saved successfully, id: %s", e.ID.String())
	return nil
}

// Modify 修改星体信息.
func (elasticSearch ElasticSearchImpl) Modify(ctx context.Context, e *model.Entity) error {
	return elasticSearch.Save(ctx, e)
}

// Remove 删除星体信息.
func (elasticSearch ElasticSearchImpl) Remove(ctx context.Context, es *[]model.Entity) error {
	if es == nil || len(*es) == 0 {
		elasticSearch.log.WithContext(ctx).Debug("Remove called with empty or nil slice")
		return nil
	}

	entities := *es
	var ids []string
	for _, e := range entities {
		ids = append(ids, e.ID.String())
	}

	elasticSearch.log.WithContext(ctx).Debugf("Removing %d entities from Elasticsearch", len(ids))

	var buf bytes.Buffer
	index := "stellar_info"

	for _, id := range ids {
		meta := fmt.Sprintf(`{ "delete" : { "_index" : "%s", "_id" : "%s" } }%s`, index, id, "\n")
		buf.WriteString(meta)
	}

	req := esapi.BulkRequest{
		Index: index,
		Body:  &buf,
	}

	res, err := req.Do(ctx, elasticSearch.data.Elasticsearch.Client)
	if err != nil {
		elasticSearch.log.WithContext(ctx).Errorf("ES Bulk delete request failed: %v", err)
		return fmt.Errorf("bulk delete failed: %w", err)
	}
	defer func(Body io.ReadCloser) {
		err := Body.Close()
		if err != nil {
			elasticSearch.log.WithContext(ctx).Errorf("Failed to close ES response body: %v", err)
			return
		}
	}(res.Body)

	if res.IsError() {
		elasticSearch.log.WithContext(ctx).Errorf("ES Bulk delete returned error: %s", res.String())
		return fmt.Errorf("ES bulk delete error: %s", res.String())
	}

	elasticSearch.log.WithContext(ctx).Infof("Bulk delete completed for %d documents", len(ids))
	return nil
}
