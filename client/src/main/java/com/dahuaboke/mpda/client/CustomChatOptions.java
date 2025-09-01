package com.dahuaboke.mpda.client;


import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.model.tool.ToolCallingChatOptions;
import org.springframework.ai.tool.ToolCallback;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * auth: dahua
 * time: 2025/9/1 11:50
 */
public class CustomChatOptions implements ToolCallingChatOptions {

    private List<ToolCallback> toolCallbacks;
    private Set<String> toolNames;
    private Boolean internalToolExecutionEnabled = false;
    private Map<String, Object> toolContext;
    private String model;
    private Map<String, String> httpHeaders = new HashMap<>();

    @Override
    public List<ToolCallback> getToolCallbacks() {
        return toolCallbacks;
    }

    @Override
    public void setToolCallbacks(List<ToolCallback> toolCallbacks) {
        this.toolCallbacks = toolCallbacks;
    }

    @Override
    public Set<String> getToolNames() {
        return toolNames;
    }

    @Override
    public void setToolNames(Set<String> toolNames) {
        this.toolNames = toolNames;
    }

    @Override
    public Boolean getInternalToolExecutionEnabled() {
        return internalToolExecutionEnabled;
    }

    @Override
    public void setInternalToolExecutionEnabled(Boolean internalToolExecutionEnabled) {
        this.internalToolExecutionEnabled = internalToolExecutionEnabled;
    }

    @Override
    public Map<String, Object> getToolContext() {
        return toolContext;
    }

    @Override
    public void setToolContext(Map<String, Object> toolContext) {
        this.toolContext = toolContext;
    }

    @Override
    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    @Override
    public Double getFrequencyPenalty() {
        return 0.0;
    }

    @Override
    public Integer getMaxTokens() {
        return Integer.MAX_VALUE;
    }

    @Override
    public Double getPresencePenalty() {
        return 0.0;
    }

    @Override
    public List<String> getStopSequences() {
        return List.of();
    }

    @Override
    public Double getTemperature() {
        return 0.0;
    }

    @Override
    public Integer getTopK() {
        return 0;
    }

    @Override
    public Double getTopP() {
        return 0.0;
    }

    @Override
    public <T extends ChatOptions> T copy() {
        return null;
    }

    public Map<String, String> getHttpHeaders() {
        return httpHeaders;
    }

    public void setHttpHeaders(Map<String, String> httpHeaders) {
        this.httpHeaders = httpHeaders;
    }
}
