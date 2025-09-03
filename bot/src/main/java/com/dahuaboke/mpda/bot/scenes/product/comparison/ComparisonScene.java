package com.dahuaboke.mpda.bot.scenes.product.comparison;


import com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 09:13
 */
@Component
public class ComparisonScene implements Scene {

    private final String description = "对比两产品";

    @Autowired
    private ComparisonGraph comparisonGraph;

    @Autowired
    private ComparisonAgentPrompt comparisonPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return comparisonGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return comparisonPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return ResolutionScene.class;
    }
}
