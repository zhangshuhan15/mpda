package com.dahuaboke.mpda.ai_code.scenes.resolution;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 14:02
 */
@Component
public class ResolutionAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文和用户的问题，判断用户的聊天意向。
                2.用户的聊天意向分为以下几类：
                    {scenes}
                3.仅返回对应的意向编号，注意不要添加任何其他符号
                4.示例：
                    数据：
                        abcdefg: 问候聊天
                        1234567: 生成html代码
                    返回：abcdefg
            """;
    @Autowired
    private ObjectMapper objectMapper;
    private String description;

    @Override
    public String description() {
        return this.description;
    }

    @Override
    public void build(Map params) {
        try {
            PromptTemplate promptTemplate = new PromptTemplate(prompt);
            promptTemplate.add("scenes", objectMapper.writeValueAsString(params));
            this.description = promptTemplate.create().getContents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();//TODO
        }
    }
}
