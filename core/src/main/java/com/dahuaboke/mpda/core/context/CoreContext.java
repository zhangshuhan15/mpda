package com.dahuaboke.mpda.core.context;


/**
 * auth: dahua
 * time: 2025/9/17 16:42
 */
public class CoreContext {

    private String query;
    private String conversationId;
    private String sceneId;

    public CoreContext(String query, String conversationId) {
        this.query = query;
        this.conversationId = conversationId;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getConversationId() {
        return conversationId;
    }

    public void setConversationId(String conversationId) {
        this.conversationId = conversationId;
    }

    public String getSceneId() {
        return sceneId;
    }

    public void setSceneId(String sceneId) {
        this.sceneId = sceneId;
    }
}
