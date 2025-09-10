package com.dahuaboke.mpda.ai_code.scenes.codeGenerate.language.html;


import com.dahuaboke.mpda.ai_code.scenes.codeGenerate.CodeGenerateScene;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.agent.scene.Scene;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/9/10 16:21
 */
@Component
public class HtmlGenerateScene implements Scene {

    @Autowired
    private HtmlGenerateGraph htmlGenerateGraph;

    @Autowired
    private HtmlGenerateAgentPrompt htmlGenerateAgentPrompt;

    @Override
    public String description() {
        return "html代码生成";
    }

    @Override
    public Graph graph() {
        return htmlGenerateGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return htmlGenerateAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return CodeGenerateScene.class;
    }
}
