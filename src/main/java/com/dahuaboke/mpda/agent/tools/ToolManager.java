package com.dahuaboke.mpda.agent.tools;

import org.springframework.ai.tool.ToolCallback;
import org.springframework.ai.tool.function.FunctionToolCallback;
import org.springframework.ai.tool.metadata.ToolMetadata;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/22 17:32
 */
@Component
public class ToolManager implements BeanPostProcessor {

    private final Map<String, ToolCallback> tools = new LinkedHashMap<>();

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof AbstractBaseTool tool) {
            tools.put(tool.getName(), buildTool(tool));
        }
        return bean;
    }

    public ToolCallback getToolByName(String name) {
        return tools.get(name);
    }

    private ToolCallback buildTool(AbstractBaseTool tool) {
        FunctionToolCallback<?, ToolResult> functionToolcallback = FunctionToolCallback
                .builder(tool.getName(), tool)
                .description(tool.getDescription())
                .inputSchema(tool.getParameters())
                .inputType(tool.getInputType())
                .toolMetadata(ToolMetadata.builder().returnDirect(false).build())
                .build();
        return functionToolcallback;
    }
}
