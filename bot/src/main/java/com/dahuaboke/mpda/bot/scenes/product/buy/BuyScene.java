package com.dahuaboke.mpda.bot.scenes.product.buy;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;

/**
 * auth: dahua
 * time: 2025/8/22 16:41
 */
//@Component
public class BuyScene implements Scene {

    private final String description = "购买产品";

    @Autowired
    private BuyGraph buyGraph;

    @Autowired
    private BuyAgentPrompt buyPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return buyGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return buyPrompt;
    }
}
