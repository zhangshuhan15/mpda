package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
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

import java.util.Map;

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
        Prompt userPrompt = new Prompt(new SystemMessage("对话结束请将finish_reason设置为mpda_end"), new UserMessage(q));
        ChatResponse chatResponse = chatClient.prompt(userPrompt).call().chatResponse();
        String text = chatResponse.getResult().getOutput().getText();
        stateGraphContext.checkEnd(chatResponse);
        return Map.of("l", chatResponse, "r", text);
    }
}
