package com.dahuaboke.mpda.ai_code.scenes.codeGenerate.language.java;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/10 16:01
 */
@Component
public class JavaGenerateAgentPrompt implements AgentPrompt {

    @Override
    public String description() {
        return """
                1.用原生java体系完成代码生成任务
                2.需要调用以下工具
                    代码生成工具
                """;
    }

    @Override
    public void build(Map params) {

    }
}
