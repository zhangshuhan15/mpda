package com.dahuaboke.mpda.core.config;

import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
import com.dahuaboke.mpda.core.client.ChatClientManager;
import com.dahuaboke.mpda.core.client.RerankerClientManager;
import com.dahuaboke.mpda.core.rag.config.RagConfiguration;
import com.dahuaboke.mpda.core.trace.TraceManager;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Import;

@AutoConfiguration
@Import(RagConfiguration.class)
@ComponentScan("com.dahuaboke.mpda.core")
public class CoreAutoConfiguration {

    @Bean
    @ConditionalOnMissingBean
    public ChatClientManager chatClientManager(ChatModel chatModel, CommonAgentPrompt commonPrompt, TraceManager traceManager, ObjectMapper objectMapper) {
        return new ChatClientManager(chatModel, commonPrompt, traceManager, objectMapper);
    }

    @Bean
    @ConditionalOnMissingBean
    @ConditionalOnProperty(name = "rerank.enabled", havingValue = "true")
    public RerankerClientManager rerankerClientManager() {
        return new RerankerClientManager();
    }
}
