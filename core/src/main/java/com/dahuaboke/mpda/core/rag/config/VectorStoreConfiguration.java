package com.dahuaboke.mpda.core.rag.config;


import io.milvus.client.MilvusServiceClient;
import io.milvus.param.ConnectParam;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


/**
 * @Desc: 向量数据库Milvus配置
 * @Author：zhh
 * @Date：2025/7/18 11:00
 */
@Configuration
public class VectorStoreConfiguration { // TODO 抽象向量库接口，满足pg、es、milvus、redis-stack等   rag能力为可关闭的，否则强依赖向量库配置

    @Value("${milvus.host}")
    private String host;

    @Value("${milvus.port}")
    private Integer port;

    @Value("${milvus.collectionName}")
    private String collectionName;

    @Value("${milvus.embeddingDimension}")
    private int embeddingDimension;

    /**
     * 定义一个名为 milvusServiceClient 的Bean，用于创建并返回一个 MilvusServiceClient 实例。
     */
    @Bean
    public MilvusServiceClient milvusServiceClient() {
        return new MilvusServiceClient(
                ConnectParam.newBuilder()
                        .withHost(host)
                        .withPort(port)
                        .build());
    }

    /**
     * 用于创建并返回一个 VectorStore 实例。
     * 使用 MilvusVectorStore.builder 方法构建向量存储对象，并设置以下参数：
     * collectionName：集合名称为 "vector_store_02"。
     * databaseName：数据库名称为 "default"。
     * embeddingDimension：嵌入维度为 1536。
     * indexType：索引类型为 IVF_FLAT，这是一种常见的近似最近邻搜索索引类型。
     * metricType：度量类型为 COSINE，用于计算向量之间的余弦相似度。
     * batchingStrategy：使用 TokenCountBatchingStrategy 策略进行批量处理。
     * initializeSchema：设置为 true，表示在构建时初始化数据库模式。
     */
    @Bean(name = "milvusVectorStore")
    public MilvusVectorStore vectorStore(MilvusServiceClient milvusClient, EmbeddingModel embeddingModel) {
        return MilvusVectorStore.builder(milvusClient, embeddingModel)
                .collectionName(collectionName)
                .databaseName("default") // TODO 可配置
                .embeddingDimension(embeddingDimension)
                //.indexType(IndexType.IVF_FLAT)
                // .metricType(MetricType.COSINE)
                // .batchingStrategy(new TokenCountBatchingStrategy())
                .initializeSchema(true)
                .build();
    }
}
