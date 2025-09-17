package com.dahuaboke.mpda.bot.scenes.product.comparison.edge;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.dahuaboke.mpda.bot.scenes.product.comparison.ComparisonAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.context.consts.Constants;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class ComparisonDispatcher implements EdgeAction {

    @Autowired
    private ComparisonAgentPrompt comparisonPrompt;

    @Override
    public String apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        if (chatResponse.hasToolCalls()) {
            comparisonPrompt.changePrompt("tool");
            state.input(Map.of(Constants.PROMPT, comparisonPrompt.description()));
            return "go_tool";
        }
        return "go_human";
    }
}
