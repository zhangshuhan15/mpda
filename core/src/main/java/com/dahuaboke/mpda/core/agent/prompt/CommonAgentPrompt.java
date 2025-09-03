package com.dahuaboke.mpda.core.agent.prompt;


import com.dahuaboke.mpda.core.agent.exception.MpdaInvocationException;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 10:21
 */
@Component
public class CommonAgentPrompt implements AgentPrompt {

    private final String systemPrompt = """
                  1.你是一位专业的同业基金智能顾问，你的名字叫邮小金，你的客群是同业客户。
                  2.不要涉及政治等敏感信息。
                  3.你需要充分基于上下文思考，你的回答是谨慎的。
                  3.你的用户都是使用简体中文的，你的思考过程和回答也需要都是简体中文。
            """;

    @Override
    public String description() {
        throw new MpdaInvocationException();
    }

    @Override
    public void build(Map params) {

    }

    public String system() {
        return systemPrompt;
    }
}
