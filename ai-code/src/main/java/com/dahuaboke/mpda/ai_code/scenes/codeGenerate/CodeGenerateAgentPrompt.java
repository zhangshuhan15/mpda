package com.dahuaboke.mpda.ai_code.scenes.codeGenerate;


import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/10 16:01
 */
@Component
public class CodeGenerateAgentPrompt implements AgentPrompt {

    private final String prompt = """
                1.根据上下文和用户的问题，判断用户代码生成的编程语言类型。
                2.支持的编程语言类型如下：
                    {codeTypeSupport}
                3.仅返回对应的编程语言编号，注意不要添加任何其他符号
                4.示例：
                    数据：
                        abcdefg: java代码生成
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
            promptTemplate.add("codeTypeSupport", objectMapper.writeValueAsString(params));
            this.description = promptTemplate.create().getContents();
        } catch (JsonProcessingException e) {
            e.printStackTrace();//TODO
        }
    }
}
