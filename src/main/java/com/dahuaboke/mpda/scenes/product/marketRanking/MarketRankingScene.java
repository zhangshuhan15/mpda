package com.dahuaboke.mpda.scenes.product.marketRanking;


import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingScene implements Scene {

    private final String description = "查询产品市场排名";

    @Autowired
    private MarketRankingGraph marketRankingGraph;

    @Autowired
    private MarketRankingPrompt marketRankingPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return marketRankingGraph;
    }

    @Override
    public Prompt prompt() {
        return marketRankingPrompt;
    }
}
