package com.dahuaboke.mpda.client;


import com.dahuaboke.mpda.client.convert.DocumentConverter;
import com.dahuaboke.mpda.client.entity.req.C014006Req;
import com.dahuaboke.mpda.client.entity.resp.C014001Resp;
import com.dahuaboke.mpda.client.entity.resp.C014006Resp;
import com.dahuaboke.mpda.client.handle.RagRequestHandle;
import org.jetbrains.annotations.NotNull;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.embedding.EmbeddingOptionsBuilder;
import org.springframework.ai.observation.conventions.VectorStoreProvider;
import org.springframework.ai.vectorstore.AbstractVectorStoreBuilder;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.filter.Filter;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.ai.vectorstore.observation.AbstractObservationVectorStore;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationContext;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.stream.Collectors;

/**
 * @Desc: 新核心数据库调用实现
 * @Author：zhh
 * @Date：2025/9/04 17:32
 */
public class CustomVectorStore extends AbstractObservationVectorStore {

    /**
     * 索引名(表名)
     */
    private String collectionName = "vector_store";

    /**
     * 发送方系统号
     */
    private String sendSysNo = "";

    /**
     * 接收方系统号
     */
    private String targetSysNo = "";

    /**
     * 需要向量查询的字段
     */
    private  String vectorFieldName = "embedding";

    /**
     * 转换接口对象
     */
    private  DocumentConverter converter;


    /**
     * 新核心RAG接口处理类
     */
    private final RagRequestHandle ragRequestHandle;


    public CustomVectorStore(Builder builder) {
        super(builder);
        this.collectionName = builder.collectionName;
        this.sendSysNo = builder.sendSysNo;
        this.targetSysNo = builder.targetSysNo;
        this.converter = builder.converter;
        this.vectorFieldName = builder.vectorFieldName;
        Consumer<HttpHeaders> finalHeaders = (h) -> {
            h.setContentType(MediaType.APPLICATION_JSON);
        };
        RestClient restClient = RestClient.builder().defaultHeaders(finalHeaders).build();
        ragRequestHandle = new RagRequestHandle(new CustomClient(restClient,null), sendSysNo, targetSysNo);
    }

    @Override
    protected void doDelete(@NotNull Filter.Expression filterExpression) {
        super.doDelete(filterExpression);
    }

    @Override
    public void doAdd(@NotNull List<Document> documents) {
        C014001Resp txEntity;
        if (converter == null) {
            List<float[]> embeddings = this.embeddingModel.embed(documents, EmbeddingOptionsBuilder.builder().build(),
                    this.batchingStrategy);

            txEntity = ragRequestHandle.sendC014001(collectionName, documents);
        } else {
            txEntity = ragRequestHandle.sendC014001(collectionName, converter.requestConvert(documents, embeddingModel));
        }
        List<String> failedInsertList = txEntity.getFailedInsertList();
        if (!failedInsertList.isEmpty()) {
            throw new RuntimeException("批量插入接口失败条数： " + failedInsertList);
        }
    }

    @Override
    public void doDelete(@NotNull List<String> idList) {

    }

    @NotNull
    @Override
    public List<Document> doSimilaritySearch(SearchRequest request) {
        String query = request.getQuery();
        int topK = request.getTopK();
        double similarityThreshold = request.getSimilarityThreshold();

        float[] embedding = embeddingModel.embed(query);
        Filter.Expression filterExpression = request.getFilterExpression();
        Map<String, Object> conditionMap = new HashMap<>();
        extractConditions(filterExpression,conditionMap);

        C014006Req c014006Req = new C014006Req();
        c014006Req.setVector(embedding);
        c014006Req.setConditionMap(conditionMap);
        c014006Req.setSystemNo(sendSysNo);
        c014006Req.setIndexName(collectionName);
        c014006Req.setVectorFieldName(vectorFieldName);
        c014006Req.setTopK(topK);
        c014006Req.setNumCandidates(topK);
        c014006Req.setSize(topK);

        C014006Resp<LinkedHashMap<String, Object>> c014006Resp = ragRequestHandle.sendC014006(c014006Req);
        List<LinkedHashMap<String, Object>> resultMap = c014006Resp.getResultMap();
        List<Document> documents;
        if (converter == null) {
            documents = resultMap.stream().map(map -> {
                BigDecimal score = (BigDecimal) map.get("score");
                String id = (String) map.get("id");
                String text = (String) map.get("text");
                return Document.builder()
                        .id(id)
                        .text(text)
                        .score(score.doubleValue())
                        .metadata(map)
                        .build();
            }).collect(Collectors.toList());
        } else {
            documents = converter.resultConvert(resultMap);
        }
        return documents.stream().filter(document -> {
            Double score = (Double) document.getMetadata().get("score");
            return score >= similarityThreshold;
        }).collect(Collectors.toList());

    }


    private void extractConditions(Filter.Operand operand, Map<String, Object> conditionMap) {
        if (operand instanceof Filter.Expression expression) {
            // 处理NOT表达式
            if (expression.type() == Filter.ExpressionType.NOT) {
                extractConditions(expression.left(), conditionMap);
                return;
            }

            // 处理逻辑组合（AND/OR）- 递归处理左右操作数
            if (expression.type() == Filter.ExpressionType.AND ||
                    expression.type() == Filter.ExpressionType.OR) {
                extractConditions(expression.left(), conditionMap);
                extractConditions(expression.right(), conditionMap);
                return;
            }

            // 处理分组表达式
            if (expression.left() instanceof Filter.Group group) {
                extractConditions(group.content(), conditionMap);
            }
            if (expression.right() instanceof Filter.Group group) {
                extractConditions(group.content(), conditionMap);
            }

            // 递归到叶子结点,取值
            if (expression.left() instanceof Filter.Key key &&
                    expression.right() instanceof Filter.Value value) {

                // 键名
                String keyName = key.key();

                // 值
                Object valueObj = value.value();

                conditionMap.put(keyName , valueObj);
            }
        }
    }

    @NotNull
    @Override
    public VectorStoreObservationContext.Builder createObservationContextBuilder(@NotNull String operationName) {
        return VectorStoreObservationContext.builder(VectorStoreProvider.ELASTICSEARCH.value(), operationName).collectionName(this.collectionName).dimensions(this.embeddingModel.dimensions());
    }

    public static Builder builder(EmbeddingModel embeddingModel) {
        return new Builder(embeddingModel);
    }

    public static class Builder extends AbstractVectorStoreBuilder<MilvusVectorStore.Builder> {

        private String collectionName = "vector_store";

        private String sendSysNo = "";

        private String targetSysNo = "";

        private String vectorFieldName = "embedding";

        private DocumentConverter converter;


        private Builder(EmbeddingModel embeddingModel) {
            super(embeddingModel);

        }

        public Builder collectionName(String collectionName) {
            this.collectionName = collectionName;
            return this;
        }

        public Builder sendSysNo(String sendSysNo) {
            this.sendSysNo = sendSysNo;
            return this;
        }

        public Builder targetSysNo(String targetSysNo) {
            this.targetSysNo = targetSysNo;
            return this;
        }

        public Builder converter(DocumentConverter converter) {
            this.converter = converter;
            return this;
        }

        public Builder vectorFieldName(String vectorFieldName) {
            this.vectorFieldName = vectorFieldName;
            return this;
        }


        @NotNull
        @Override
        public CustomVectorStore build() {
            return new CustomVectorStore(this);
        }

    }

}
