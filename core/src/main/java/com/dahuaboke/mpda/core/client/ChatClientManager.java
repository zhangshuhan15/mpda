package com.dahuaboke.mpda.core.client;


import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.streaming.StreamingChatGenerator;
import com.dahuaboke.mpda.core.agent.prompt.CommonAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.client.entity.StreamLlmResponse;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.tool.ToolCallback;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * auth: dahua
 * time: 2025/8/21 10:20
 */
public class ChatClientManager {

    private final ChatClient chatClient;
    private final TraceManager traceManager;

    public ChatClientManager(ChatModel chatModel, CommonAgentPrompt commonPrompt, TraceManager traceManager) {
        this.traceManager = traceManager;
        this.chatClient = ChatClient.builder(chatModel)
                .defaultSystem(commonPrompt.system())
                .defaultAdvisors(new SimpleLoggerAdvisor())
                .build();
    }

    public LlmResponse call(String conversationId, String sceneId, String prompt, String query, List<ToolCallback> tools, List<String> sceneMerge) {
        ChatClient.ChatClientRequestSpec spec = buildChatClientRequestSpec(conversationId, sceneId, prompt, query, tools, sceneMerge);
        ChatResponse chatResponse = spec.call().chatResponse();
        return new LlmResponse(chatResponse);
    }

    public StreamLlmResponse stream(String conversationId, String sceneId, String prompt, String query, String key
            , OverAllState state, String nodeName, List<String> sceneMerge) {
        ChatClient.ChatClientRequestSpec spec = buildChatClientRequestSpec(conversationId, sceneId, prompt, query, null, sceneMerge);
        Flux<ChatResponse> chatResponseFlux = spec.stream().chatResponse();
        AsyncGenerator<? extends NodeOutput> output = StreamingChatGenerator.builder()
                .startingNode(nodeName)
                .startingState(state)
                .mapResult(response -> Map.of(key
                        , Objects.requireNonNull(response.getResult().getOutput().getText())))
                .build(chatResponseFlux);
        return new StreamLlmResponse(output);
    }

    private ChatClient.ChatClientRequestSpec buildChatClientRequestSpec(String conversationId, String sceneId, String prompt
            , String query, List<ToolCallback> tools, List<String> sceneMerge) {
        ChatClient.ChatClientRequestSpec spec = chatClient.prompt(prompt).user(query);
        List<Message> memory = traceManager.getMemory(conversationId, sceneId, sceneMerge);
        if (!CollectionUtils.isEmpty(memory)) {
            spec.messages(memory);
        }
        if (!CollectionUtils.isEmpty(tools)) {
            spec.toolCallbacks(tools);
        }
        return spec;
    }
}
