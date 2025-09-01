package com.dahuaboke.mpda.bot.scenes.product.marketRanking.edge;

import com.alibaba.cloud.ai.graph.OverAllState;
import com.alibaba.cloud.ai.graph.action.EdgeAction;
import com.dahuaboke.mpda.bot.scenes.product.marketRanking.MarketRankingAgentPrompt;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.consts.Constants;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
public class MarketRankingDispatcher implements EdgeAction {

    @Autowired
    private MarketRankingAgentPrompt marketRankingPrompt;

    @Override
    public String apply(OverAllState state) throws Exception {
        LlmResponse llmResponse = state.value(Constants.RESULT, LlmResponse.class).get();
        ChatResponse chatResponse = llmResponse.chatResponse();
        if (chatResponse.hasToolCalls()) {
            marketRankingPrompt.changePrompt("tool");
            state.input(Map.of(Constants.PROMPT, marketRankingPrompt.description()));
            return "go_tool";
        }
        return "go_human";
    }
}
