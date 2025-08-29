package com.dahuaboke.mpda.scenes.smallTalk;


import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/21 15:03
 */
@Component
public class SmallTalkScene implements Scene {

    private final String description = "问候聊天";

    @Autowired
    private SmallTalkGraph smallTalkGraph;

    @Autowired
    private SmallTalkPrompt smallTalkPrompt;

    @Override
    public String description() {
        return description;
    }

    @Override
    public Graph graph() {
        return smallTalkGraph;
    }

    @Override
    public Prompt prompt() {
        return smallTalkPrompt;
    }
}
