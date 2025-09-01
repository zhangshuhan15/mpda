package com.dahuaboke.mpda.bot.scenes.product.information;


import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/22 14:10
 */
@Component
public class InformationScene implements Scene {

    private final String description = """
                查询产品具体信息
                介绍产品
                了解产品详情
            """;

    @Autowired
    private InformationGraph informationGraph;

    @Autowired
    private InformationAgentPrompt informationPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return informationGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return informationPrompt;
    }
}
