package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.streaming.StreamingChatGenerator;
import com.dahuaboke.mpda.context.StateGraphContext;
import com.dahuaboke.mpda.tools.CommandTool;
import com.dahuaboke.mpda.tools.DirectoryTool;
import com.dahuaboke.mpda.tools.FileTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.SystemMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import reactor.core.publisher.Flux;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;

public class LLmNode implements NodeAction {

    private ChatClient chatClient;
    private StateGraphContext stateGraphContext;

    public LLmNode(StateGraphContext stateGraphContext, ChatModel chatModel, ChatMemory chatMemory) {
        this.stateGraphContext = stateGraphContext;
        this.chatClient = ChatClient.builder(chatModel).defaultTools(
                        new FileTool(), new CommandTool(), new DirectoryTool())
                .defaultAdvisors(
                        new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).conversationId(stateGraphContext.getConversationId()).build())
                .build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        String q = state.value("q", String.class).get();
        Prompt userPrompt = new Prompt(new UserMessage(q));
        Flux<ChatResponse> chatResponseFlux = chatClient.prompt(userPrompt).stream().chatResponse();
        var r = StreamingChatGenerator.builder()
                .startingNode("llmNode")
                .startingState(state)
                .mapResult(
                        response -> Map.of("r", Objects.requireNonNull(response.getResult().getOutput().getText())))
                .build(chatResponseFlux);
        return Map.of("r", r);
    }
}
