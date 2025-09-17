package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.agent.tools.ToolUtil;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.memory.MemoryManager;
import com.dahuaboke.mpda.core.memory.ToolResponseMessageWrapper;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.messages.ToolResponseMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 10:24
 */
@Component
public class ToolNode implements NodeAction {

    @Autowired
    private ToolUtil toolUtil;

    @Autowired
    private MemoryManager memoryManager;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        ChatResponse chatResponse = chatResponse(state);
        ToolResponseMessage toolResponseMessage = executeTool(chatResponse);
        ToolResponseMessageWrapper toolResponseMessageWrapper = buildToolResponseMessageWrapper(state, toolResponseMessage);
        return Map.of(Constants.QUERY, toolResponseMessageWrapper, Constants.IS_TOOL_QUERY, true);
    }

    protected ChatResponse chatResponse(OverAllState state) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        AssistantMessageWrapper assistantMessageWrapper =
                new AssistantMessageWrapper(assistantMessage.getText(), assistantMessage.getMetadata(), assistantMessage.getToolCalls(), assistantMessage.getMedia());
        memoryManager.addMemory(conversationId, sceneId, assistantMessageWrapper);
        return chatResponse;
    }

    protected ToolResponseMessage executeTool(ChatResponse chatResponse) {
        return toolUtil.executeToolCalls(chatResponse);
    }

    protected ToolResponseMessageWrapper buildToolResponseMessageWrapper(OverAllState state, ToolResponseMessage toolResponseMessage) {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        ToolResponseMessageWrapper toolResponseMessageWrapper = new ToolResponseMessageWrapper(toolResponseMessage);
        memoryManager.addMemory(conversationId, sceneId, toolResponseMessageWrapper);
        return toolResponseMessageWrapper;
    }
}
