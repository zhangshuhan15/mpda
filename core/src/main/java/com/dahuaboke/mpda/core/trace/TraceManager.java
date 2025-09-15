package com.dahuaboke.mpda.core.trace;


import com.dahuaboke.mpda.core.agent.scene.Scene;
import com.dahuaboke.mpda.core.trace.memory.AssistantMessageWrapper;
import com.dahuaboke.mpda.core.trace.memory.ToolResponseMessageWrapper;
import com.dahuaboke.mpda.core.trace.memory.UserMessageWrapper;
import org.apache.commons.collections4.CollectionUtils;
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

    private final ThreadLocal<Map<String, Object>> attributes = new ThreadLocal<>();

    private final Map<String, List<String>> logManager = new HashMap();

    private final Map<String, Map<String, List<Message>>> memoryManager = new LinkedHashMap();

    private final Map<Class<? extends Scene>, String> sceneMapper = new LinkedHashMap<>();

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
        if (memoryManager.containsKey(conversationId)) {
            return memoryManager.get(conversationId).get(sceneId);
        }
        return List.of();
    }

    public List<Message> getMemory(String conversationId, String sceneId, List<String> sceneMerge) {
        if (memoryManager.containsKey(conversationId)) {
            List<Message> messages = memoryManager.get(conversationId).get(sceneId);
            if (messages == null) {
                messages = new ArrayList<>();
            }
            if (!CollectionUtils.isEmpty(sceneMerge)) {
                List<Message> finalMessages = messages;
                sceneMerge.stream().forEach(merge -> {
                    finalMessages.addAll(getMemory(conversationId, merge));
                });
            }
            return messages.stream().sorted((m1, m2) -> {
                if (m1 instanceof UserMessageWrapper user1 && m2 instanceof UserMessageWrapper user2) {
                    return Long.valueOf(user1.getTime() - user2.getTime()).intValue();
                }
                if (m1 instanceof AssistantMessageWrapper assistant1 && m1 instanceof AssistantMessageWrapper assistant2) {
                    return Long.valueOf(assistant1.getTime() - assistant2.getTime()).intValue();
                }
                if (m1 instanceof ToolResponseMessageWrapper tool1 && m1 instanceof ToolResponseMessageWrapper tool2) {
                    return Long.valueOf(tool1.getTime() - tool2.getTime()).intValue();
                }
                return 0;
            }).toList();
        }
        return List.of();
    }

    public void addSceneMapper(Class<? extends Scene> clz, String sceneId) {
        sceneMapper.putIfAbsent(clz, sceneId);
    }

    public String getSceneId(Class<? extends Scene> clz) {
        return sceneMapper.get(clz);
    }

    public Map<String, Object> getAttribute() {
        try {
            return attributes.get();
        } finally {
            attributes.remove();
        }
    }

    public void setAttribute(Map<String, Object> attribute) {
        attributes.set(attribute);
    }
}
