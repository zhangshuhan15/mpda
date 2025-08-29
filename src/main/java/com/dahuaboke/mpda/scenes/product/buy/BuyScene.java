package com.dahuaboke.mpda.scenes.product.buy;


import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.agent.scene.Scene;
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
    private BuyPrompt buyPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return buyGraph;
    }

    @Override
    public Prompt prompt() {
        return buyPrompt;
    }
}
