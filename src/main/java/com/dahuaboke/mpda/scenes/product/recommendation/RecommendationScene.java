package com.dahuaboke.mpda.scenes.product.recommendation;


import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:47
 */
@Component
public class RecommendationScene implements Scene {

    private final String description = "为用户推荐产品";

    @Autowired
    private RecommendationGraph recommendationGraph;

    @Autowired
    private RecommendationPrompt recommendationPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return recommendationGraph;
    }

    @Override
    public Prompt prompt() {
        return recommendationPrompt;
    }
}
