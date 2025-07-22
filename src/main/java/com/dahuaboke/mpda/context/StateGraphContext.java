package com.dahuaboke.mpda.context;

import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatResponse;

import java.util.Set;

public class StateGraphContext {

    private String conversationId;
    private volatile boolean end;
    private ChatMemory chatMemory;

    public StateGraphContext(String conversationId, ChatMemory chatMemory) {
        this.conversationId = conversationId;
        this.end = false;
        this.chatMemory = chatMemory;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public boolean isEnd() {
        return end;
    }

    public void setEnd(boolean end) {
        this.end = end;
    }

    public void checkEnd(ChatResponse chatResponse) {
        if (chatResponse.hasFinishReasons(Set.of("mpda_end"))) {
            this.end = true;
            this.chatMemory.clear(this.conversationId);
        }
    }
}
