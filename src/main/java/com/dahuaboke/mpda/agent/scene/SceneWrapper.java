package com.dahuaboke.mpda.agent.scene;

import com.dahuaboke.mpda.agent.chain.Chain;
import com.dahuaboke.mpda.agent.exception.MpdaException;
import com.dahuaboke.mpda.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.trace.TraceManager;
import reactor.core.publisher.Flux;

import java.util.UUID;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public class SceneWrapper {

    private final String sceneId = UUID.randomUUID().toString();
    private final TraceManager traceManager;
    private final Chain chain;

    private SceneWrapper(Chain chain, TraceManager traceManager) {
        this.chain = chain;
        this.traceManager = traceManager;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSceneId() {
        return sceneId;
    }

    public void init() throws MpdaGraphException {
        this.chain.init();
    }

    public String execute(String conversationId, String query) throws MpdaException {
        try {
            traceManager.setConversationId(conversationId);
            traceManager.setSceneId(this.sceneId);
            return chain.slide(query);
        } finally {
            traceManager.clearConversationId();
            traceManager.clearSceneId();
        }
    }

    public Flux<String> executeAsync(String conversationId, String query) throws MpdaException {
        try {
            traceManager.setConversationId(conversationId);
            traceManager.setSceneId(this.sceneId);
            return chain.slideAsync(query);
        } finally {
            traceManager.clearConversationId();
            traceManager.clearSceneId();
        }
    }

    public static final class Builder {

        private Chain chain;
        private TraceManager traceManager;

        private Builder() {
        }

        public Builder chain(Chain chain) {
            this.chain = chain;
            return this;
        }

        public Builder traceManager(TraceManager traceManager) {
            this.traceManager = traceManager;
            return this;
        }

        public SceneWrapper build() {
            return new SceneWrapper(chain, traceManager);
        }
    }
}
