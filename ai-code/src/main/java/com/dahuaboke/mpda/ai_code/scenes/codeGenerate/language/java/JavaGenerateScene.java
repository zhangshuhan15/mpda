package com.dahuaboke.mpda.ai_code.scenes.codeGenerate.language.java;


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
public class JavaGenerateScene implements Scene {

    @Autowired
    private JavaGenerateGraph javaGenerateGraph;

    @Autowired
    private JavaGenerateAgentPrompt javaGenerateAgentPrompt;

    @Override
    public String description() {
        return "java代码生成";
    }

    @Override
    public Graph graph() {
        return javaGenerateGraph;
    }

    @Override
    public AgentPrompt prompt() {
        return javaGenerateAgentPrompt;
    }

    @Override
    public Class<? extends Scene> parent() {
        return CodeGenerateScene.class;
    }
}
