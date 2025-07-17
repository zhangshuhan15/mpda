package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.NodeAction;
import com.alibaba.fastjson.JSON;
import com.dahuaboke.mpda.tools.FileTool;
import com.dahuaboke.mpda.tools.ToolResult;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ToolNode implements NodeAction {

    @Autowired
    private FileTool fileTool;

    @Override
    public Map<String, Object> apply(OverAllState state) {
        AssistantMessage.ToolCall toolCall = state.value("t", AssistantMessage.ToolCall.class).get();
        String name = toolCall.name();
        if ("createFile".equals(name)) {
            String arguments = toolCall.arguments();
            System.out.println(arguments);
            Map map = JSON.parseObject(arguments, Map.class);
            ToolResult toolResult = fileTool.createFile((String) map.get("arg0"), (String) map.get("arg1"));
            return Map.of("q", toolResult);
        }
        return Map.of();
    }
}
