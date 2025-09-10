package com.dahuaboke.mpda.core.agent.prompt;


import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 09:18
 */
public interface AgentPrompt {

    String description();

    void build(Map params);
}
