package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.tools.CommandTool;
import com.dahuaboke.mpda.tools.DirectoryTool;
import com.dahuaboke.mpda.tools.FileTool;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public class LLmNode implements NodeAction {

    private ChatClient chatClient;

    public LLmNode(ChatModel chatModel, ChatMemory chatMemory) {
        this.chatClient = ChatClient.builder(chatModel).defaultTools(new FileTool(), new CommandTool(), new DirectoryTool())
                .defaultAdvisors(new SimpleLoggerAdvisor(), MessageChatMemoryAdvisor.builder(chatMemory).build())
                .build();
    }

    @Override
    public Map<String, Object> apply(OverAllState state) {
        String q = state.value("q").get().toString();
        Prompt userPrompt = new Prompt(new UserMessage(q));
        ChatResponse response = chatClient.prompt(userPrompt).call().chatResponse();
        AssistantMessage output = response.getResult().getOutput();
        List<AssistantMessage.ToolCall> toolCalls = output.getToolCalls();
        String responseByLLm = output.getText();
        System.out.println(toolCalls);
        System.out.println(responseByLLm);
        return Map.of("agent", "agent");
    }
}
