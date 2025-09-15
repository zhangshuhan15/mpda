package com.dahuaboke.mpda.core.rag.config;


import com.dahuaboke.mpda.core.client.RerankerClientManager;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingSearchHandler;
import com.dahuaboke.mpda.core.rag.handler.RerankHandler;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;


/**
 * @Desc: rag配置
 * @Author：zhh
 * @Date：2025/7/18 11:00
 */
@AutoConfiguration
public class RagConfiguration {

    @Bean
    @ConditionalOnBean(VectorStore.class)
    public EmbeddingSearchHandler embeddingSearchHandler(VectorStore vectorStore) {
        return new EmbeddingSearchHandler(vectorStore);
    }

    @Bean
    @ConditionalOnBean(RerankerClientManager.class)
    public RerankHandler rerankHandler(RerankerClientManager rerankerClientManager) {
        return new RerankHandler(rerankerClientManager);
    }


}
