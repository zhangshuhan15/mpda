package com.dahuaboke.mpda.trace;


import org.springframework.ai.chat.messages.Message;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * auth: dahua
 * time: 2025/8/21 09:02
 */
@Component
public class TraceManager {

    //TODO 定时删除

    private final ThreadLocal<String> conversationId = new ThreadLocal<>();

    private final ThreadLocal<String> sceneId = new ThreadLocal<>();

    private final Map<String, List<String>> logManager = new HashMap();

    private final Map<String, Map<String, List<Message>>> memoryManager = new LinkedHashMap();

    public void addLog(String key, String log) {
        List<String> logs = logManager.get(key);
        if (logs == null) {
            logs = new ArrayList<>();
            logManager.put(key, logs);
        }
        logs.add(log);
        System.out.println(log);
    }

    public String getConversationId() {
        return conversationId.get();
    }

    public void setConversationId(String conversationId) {
        this.conversationId.set(conversationId);
    }

    public void clearConversationId() {
        this.conversationId.remove();
    }

    public String getSceneId() {
        return this.sceneId.get();
    }

    public void setSceneId(String sceneId) {
        this.sceneId.set(sceneId);
    }

    public void clearSceneId() {
        this.sceneId.remove();
    }

    public void addMemory(Message message) {
        String conversationId = getConversationId();
        String sceneId = getSceneId();
        addMemory(conversationId, sceneId, message);
    }

    public void addMemory(String conversationId, String sceneId, Message message) {
        if (memoryManager.containsKey(conversationId)) {
            Map<String, List<Message>> sceneMessages = memoryManager.get(conversationId);
            if (sceneMessages.containsKey(sceneId)) {
                sceneMessages.get(sceneId).add(message);
            } else {
                sceneMessages.put(sceneId, new ArrayList() {{
                    add(message);
                }});
            }
        } else {
            memoryManager.put(conversationId, new LinkedHashMap<>() {{
                put(sceneId, new ArrayList() {{
                    add(message);
                }});
            }});
        }
    }

    public List<String> getLogs(String key) {
        return logManager.get(key);
    }

    public List<Message> getMemory() {
        return getMemory(conversationId.get(), sceneId.get());
    }

    public List<Message> getMemory(String conversationId, String sceneId) {
        return memoryManager.get(conversationId).get(sceneId);
    }
}
