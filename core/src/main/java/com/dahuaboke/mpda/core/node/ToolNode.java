package com.dahuaboke.mpda.core.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.core.agent.tools.ToolUtil;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.consts.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
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
    private ObjectMapper objectMapper;

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        ToolResponseMessage toolResponseMessage = toolUtil.executeToolCalls(chatResponse);
        return Map.of(Constants.QUERY, objectMapper.writeValueAsString(toolResponseMessage));
    }
}
