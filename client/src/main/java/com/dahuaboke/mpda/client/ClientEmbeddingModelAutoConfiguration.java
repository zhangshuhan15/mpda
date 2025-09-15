package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.handle.EmbeddingModelRequestHandle;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.ImportAutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;


/**
 * @Desc: 新核心接口向量模型自动配置类
 * @Author：zhh
 * @Date：2025/9/11 15:01
 */
@AutoConfiguration(
        before = {
                OpenAiEmbeddingAutoConfiguration.class
        }
)
@ConditionalOnProperty(prefix = RagConstant.EMBEDDING_MODEL, name = "enabled", havingValue = "true")
@ConditionalOnClass({ChatModel.class, EmbeddingModel.class})
@ImportAutoConfiguration(ClientConfiguration.class)
@EnableConfigurationProperties(ClientProperties.class)
public class ClientEmbeddingModelAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public EmbeddingModelRequestHandle embeddingModelRequestHandle(CustomClient customClient , ClientProperties clientProperties){
        return new EmbeddingModelRequestHandle(customClient, clientProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public CustomEmbeddingModel embeddingModel(EmbeddingModelRequestHandle embeddingModelRequestHandle){
        return new CustomEmbeddingModel(embeddingModelRequestHandle);
    }

}
