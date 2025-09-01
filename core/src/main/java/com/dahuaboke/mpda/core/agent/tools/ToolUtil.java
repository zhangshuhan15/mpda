package com.dahuaboke.mpda.core.agent.tools;

import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.model.tool.ToolCallingManager;
import org.springframework.ai.model.tool.ToolExecutionResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/8/21 11:32
 */
@Component
public class ToolUtil {

    @Autowired
    private ToolCallingManager toolCallingManager;

    public ToolResponseMessage executeToolCalls(ChatResponse chatResponse) {
        ToolExecutionResult toolExecutionResult = toolCallingManager.executeToolCalls(new Prompt(), chatResponse);
        List<Message> conversationHistory = toolExecutionResult.conversationHistory();
        ToolResponseMessage toolResponse = null;
        if (conversationHistory
                .get(conversationHistory.size() - 1) instanceof ToolResponseMessage toolResponseMessage) {
            toolResponse = toolResponseMessage;
        }
        return toolResponse;
    }
}
