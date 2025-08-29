package com.dahuaboke.mpda.scenes.product.comparison;


import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.agent.scene.Scene;
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
    private ComparisonPrompt comparisonPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return comparisonGraph;
    }

    @Override
    public Prompt prompt() {
        return comparisonPrompt;
    }
}
