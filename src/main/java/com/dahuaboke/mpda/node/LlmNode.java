package com.dahuaboke.mpda.node;


import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.dahuaboke.mpda.agent.tools.ToolManager;
import com.dahuaboke.mpda.client.ChatClientManager;
import com.dahuaboke.mpda.consts.Constants;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 10:16
 */
@Component
public class LlmNode implements NodeAction {

    private final ChatClientManager chatClientManager;
    private final ToolManager toolManager;

    public LlmNode(ChatClientManager chatClientManager, ToolManager toolManager) {
        this.chatClientManager = chatClientManager;
        this.toolManager = toolManager;
    }

    @Override
    public Map<String, Object> apply(OverAllState state) throws Exception {
        String prompt = state.value(Constants.PROMPT, String.class).get();
        String query = state.value(Constants.QUERY, String.class).get();
        List<String> toolNames = state.value(Constants.TOOLS, List.class).orElse(List.of());
        String conversationId = state.value(Constants.CONVERSATION_ID, String.class).get();
        String sceneId = state.value(Constants.SCENE_ID, String.class).get();
        return Map.of(Constants.RESULT, chatClientManager.call(conversationId, sceneId, prompt, query, toolNames.stream().map(toolName ->
                toolManager.getToolByName(toolName)
        ).toList()));
    }
}
