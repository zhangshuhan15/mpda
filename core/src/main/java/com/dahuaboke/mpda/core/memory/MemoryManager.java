package com.dahuaboke.mpda.core.memory;


import com.dahuaboke.mpda.core.context.CacheManager;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/9/17 17:29
 */
@Component
public class MemoryManager {

    //TODO 定时删除

    @Autowired
    private CacheManager cacheManager;

    public void addMemory(Message message) {
        String conversationId = cacheManager.getContext().getConversationId();
        String sceneId = cacheManager.getContext().getSceneId();
        addMemory(conversationId, sceneId, message);
    }

    public void addMemory(String conversationId, String sceneId, Message message) {
        Map<String, Map<String, List<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            Map<String, List<Message>> sceneMessages = memories.get(conversationId);
            if (sceneMessages.containsKey(sceneId)) {
                sceneMessages.get(sceneId).add(message);
            } else {
                sceneMessages.put(sceneId, new ArrayList() {{
                    add(message);
                }});
            }
        } else {
            memories.put(conversationId, new LinkedHashMap<>() {{
                put(sceneId, new ArrayList() {{
                    add(message);
                }});
            }});
        }
    }

    public List<Message> getMemory() {
        return getMemory(cacheManager.getContext().getConversationId(), cacheManager.getContext().getSceneId());
    }

    public List<Message> getMemory(String conversationId, String sceneId) {
        Map<String, Map<String, List<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            return memories.get(conversationId).get(sceneId);
        }
        return List.of();
    }

    public List<Message> getMemory(String conversationId, String sceneId, List<String> sceneMerge) {
        Map<String, Map<String, List<Message>>> memories = cacheManager.getMemories();
        if (memories.containsKey(conversationId)) {
            List<Message> messages = memories.get(conversationId).get(sceneId);
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
}
