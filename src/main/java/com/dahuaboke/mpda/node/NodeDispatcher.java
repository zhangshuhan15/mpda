package com.dahuaboke.mpda.node;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import org.springframework.ai.chat.messages.AssistantMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class NodeDispatcher implements EdgeAction {

    private int a = 0;

    @Override
    public String apply(OverAllState state) {
        if (state.containStrategy("h")) {
            return "go_return";
        }
        ChatResponse response = state.value("r", ChatResponse.class).get();
        AssistantMessage output = response.getResult().getOutput();
        System.out.println(output);
        if (output.hasToolCalls()) {
            List<AssistantMessage.ToolCall> toolCalls = output.getToolCalls();
            System.out.println("tool size >>> " + toolCalls.size());
            AssistantMessage.ToolCall toolCall = toolCalls.get(0);
            state.input(new HashMap<>() {{
                put("t", toolCall);
            }});
            return "go_tool";
        } else {
            String responseByLLm = output.getText();
            state.input(new HashMap<>() {{
                put("l", responseByLLm);
            }});
            return "go_human";
        }
    }
}
