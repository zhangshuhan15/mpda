package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import com.dahuaboke.mpda.client.vector.CustomVectorStore;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;

/**
 * @Desc: 新核心接口向量数据库自动配置类
 * @Author：zhh
 * @Date：2025/9/11 15:16
 */
@AutoConfiguration(
        after = {
                ClientEmbeddingModelAutoConfiguration.class
        }
)
@ConditionalOnClass({VectorStore.class, EmbeddingModel.class})
@ConditionalOnProperty(prefix = RagConstant.VECTOR_STORE, name = "enabled", havingValue = "true")
@ImportAutoConfiguration(ClientConfiguration.class)
@EnableConfigurationProperties({ClientProperties.class,ClientVectorStoreProperties.class})
public class ClientVectorStoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public VectorStoreRequestHandle vectorStoreRequestHandle(CustomClient customClient , ClientProperties clientProperties){
        return new VectorStoreRequestHandle(customClient, clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorStore customVectorStore(EmbeddingModel embeddingModel, ClientVectorStoreProperties clientVectorStoreProperties,VectorStoreRequestHandle vectorStoreRequestHandle) {
        return CustomVectorStore.builder(embeddingModel, vectorStoreRequestHandle)
                .vectorFieldName(clientVectorStoreProperties.getVectorFieldName())
                .collectionName(clientVectorStoreProperties.getCollectionName())
                .build();
    }
}
