package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.agent.tools.ToolUtil;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.consts.Constants;
import com.dahuaboke.mpda.core.trace.TraceManager;
import com.dahuaboke.mpda.core.trace.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.trace.memory.ToolResponseMessageWrapper;
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
    private TraceManager traceManager;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        AssistantMessage assistantMessage = chatResponse.getResult().getOutput();
        AssistantMessageWrapper assistantMessageWrapper =
                new AssistantMessageWrapper(assistantMessage.getText(), assistantMessage.getMetadata(), assistantMessage.getToolCalls(), assistantMessage.getMedia());
        traceManager.addMemory(conversationId, sceneId, assistantMessageWrapper);
        ToolResponseMessage toolResponseMessage = toolUtil.executeToolCalls(chatResponse);
        ToolResponseMessageWrapper toolResponseMessageWrapper = new ToolResponseMessageWrapper(toolResponseMessage);
        traceManager.addMemory(conversationId, sceneId, toolResponseMessageWrapper);
        return Map.of(Constants.QUERY, toolResponseMessageWrapper, Constants.IS_TOOL_QUERY, true);
    }
}
