package com.dahuaboke.mpda.agent.config;

import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.tool.ToolExecutionEligibilityPredicate;
import org.springframework.stereotype.Component;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/8/18 17:43
 */
@Component
public class CustomToolExecutionEligibilityPredicate implements ToolExecutionEligibilityPredicate {

    @Override
    public boolean test(ChatOptions promptOptions, ChatResponse chatResponse) {
        return false;
    }
}