package com.dahuaboke.mpda.ai_code.scenes.codeGenerate.language.html;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/10 16:01
 */
@Component
public class HtmlGenerateAgentPrompt implements AgentPrompt {

    @Override
    public String description() {
        return """
                1.用原生css、js、html完成代码生成任务
                2.需要调用以下工具
                    代码生成
                """;
    }

    @Override
    public void build(Map params) {

    }
}
