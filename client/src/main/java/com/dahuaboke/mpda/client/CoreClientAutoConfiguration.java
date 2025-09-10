package com.dahuaboke.mpda.client;

import com.dahuaboke.mpda.client.handle.EmbeddingModelRequestHandle;
import com.dahuaboke.mpda.client.handle.RerankModelRequestHandle;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import java.util.function.Consumer;
import org.springframework.ai.model.openai.autoconfigure.OpenAiEmbeddingAutoConfiguration;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;
import org.springframework.web.reactive.function.client.WebClient;

@AutoConfiguration(
        before = OpenAiEmbeddingAutoConfiguration.class
)
@EnableConfigurationProperties(ClientProperties.class)
public class CoreClientAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public CustomClient customClient(){
        Consumer<HttpHeaders> finalHeaders = (h) -> {
            h.setContentType(MediaType.APPLICATION_JSON);
        };
        return new CustomClient(RestClient.builder().defaultHeaders(finalHeaders).build(),
                WebClient.builder().defaultHeaders(finalHeaders).build());
    }

    @Bean
    @ConditionalOnMissingBean
    public VectorStoreRequestHandle vectorStoreRequestHandle(CustomClient customClient , ClientProperties clientProperties){
        return new VectorStoreRequestHandle(customClient, clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public RerankModelRequestHandle rerankModelRequestHandle(CustomClient customClient , ClientProperties clientProperties){
        return new RerankModelRequestHandle(customClient, clientProperties);
    }

    @Bean
    @ConditionalOnMissingBean
    public EmbeddingModelRequestHandle embeddingModelRequestHandle(CustomClient customClient , ClientProperties clientProperties){
        return new EmbeddingModelRequestHandle(customClient, clientProperties);
    }


    @Bean
    @ConditionalOnMissingBean
    public CustomEmbeddingModel customEmbeddingModel(EmbeddingModelRequestHandle embeddingModelRequestHandle){
        return new CustomEmbeddingModel(embeddingModelRequestHandle);
    }
}
