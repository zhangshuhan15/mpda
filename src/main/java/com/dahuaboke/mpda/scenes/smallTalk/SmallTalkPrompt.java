package com.dahuaboke.mpda.scenes.smallTalk;


import com.dahuaboke.mpda.agent.prompt.Prompt;
import org.springframework.stereotype.Component;

/**
 * auth: dahua
 * time: 2025/8/21 09:17
 */
@Component
public class SmallTalkPrompt implements Prompt {

    public final String description = """
                1.表明自己的身份与工作职责
                2.友好礼貌的回应客户
                3.引导用户提出以下问题:
                    (1)基金投资相关
                    (2)查询基金产品信息
                    (3)查询产品市场报告
            """;

    @Override
    public String description() {
        return description;
    }
}
