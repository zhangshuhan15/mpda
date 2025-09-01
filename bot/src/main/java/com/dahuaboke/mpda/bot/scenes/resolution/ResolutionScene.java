package com.dahuaboke.mpda.bot.scenes.resolution;


import com.dahuaboke.mpda.core.agent.exception.MpdaInvocationException;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.dahuaboke.mpda.bot.scenes.resolution.ResolutionScene.BEAN_NAME;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
@Component(BEAN_NAME)
public class ResolutionScene implements Scene {

    public static final String BEAN_NAME = "resolutionScene";

    @Autowired
    private ResolutionGraph resolutionGraph;

    @Autowired
    private ResolutionAgentPrompt resolutionPrompt;

    @Override
    public String description() {
        throw new MpdaInvocationException();
    }

    @Override
    public Graph graph() {
        return resolutionGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return resolutionPrompt;
    }
}
