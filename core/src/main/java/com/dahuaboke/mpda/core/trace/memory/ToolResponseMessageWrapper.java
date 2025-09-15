package com.dahuaboke.mpda.core.trace.memory;


import com.fasterxml.jackson.annotation.JsonCreator;
import org.springframework.ai.chat.messages.ToolResponseMessage;

/**
 * auth: dahua
 * time: 2025/9/11 13:19
 */
public class ToolResponseMessageWrapper extends ToolResponseMessage {

    private final long time;

    @JsonCreator
    public ToolResponseMessageWrapper(ToolResponseMessage toolResponseMessage) {
        super(toolResponseMessage.getResponses(), toolResponseMessage.getMetadata());
        this.time = System.currentTimeMillis();
    }
}
