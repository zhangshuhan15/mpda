package com.dahuaboke.mpda.core.agent.scene;

import com.dahuaboke.mpda.core.agent.chain.Chain;
import com.dahuaboke.mpda.core.agent.exception.MpdaException;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.apache.commons.collections4.CollectionUtils;
import reactor.core.publisher.Flux;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * auth: dahua
 * time: 2025/08/21 8:50
 */
public class SceneWrapper {

    private final String sceneId = UUID.randomUUID().toString();
    private final TraceManager traceManager;
    private final Chain chain;
    protected Set<SceneWrapper> childrenWrapper;
    private final AgentPrompt prompt;
    private final String description;

    protected SceneWrapper(Chain chain, TraceManager traceManager, AgentPrompt prompt, String description) {
        this.chain = chain;
        this.traceManager = traceManager;
        this.prompt = prompt;
        this.description = description;
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getSceneId() {
        return sceneId;
    }

    public void addChildWrapper(SceneWrapper childWrapper) {
        if (childrenWrapper == null) {
            childrenWrapper = new HashSet<>();
        }
        this.childrenWrapper.add(childWrapper);
    }

    public void init() throws MpdaGraphException {
        if (CollectionUtils.isNotEmpty(childrenWrapper)) {
            prompt.build(childrenWrapper.stream().collect(Collectors.toMap(child -> child.sceneId, child -> child.description)));
        }
        this.chain.init();
    }

    private String execute(String conversationId, String query) throws MpdaException {
        try {
            traceManager.setConversationId(conversationId);
            traceManager.setSceneId(this.sceneId);
            return chain.slide(query);
        } finally {
            traceManager.clearConversationId();
            traceManager.clearSceneId();
        }
    }

    private Flux<String> executeAsync(String conversationId, String query) throws MpdaException {
        try {
            traceManager.setConversationId(conversationId);
            traceManager.setSceneId(this.sceneId);
            return chain.slideAsync(query);
        } finally {
            traceManager.clearConversationId();
            traceManager.clearSceneId();
        }
    }

    public boolean isEnd() {
        return childrenWrapper == null;
    }


    public String apply(String conversationId, String query) throws MpdaException {
        return execute(conversationId, query);
    }

    public Flux<String> applyAsync(String conversationId, String query) throws MpdaException {
        return executeAsync(conversationId, query);
    }


    public SceneWrapper next(String conversationId, String query) throws MpdaException {
        return next(conversationId, query, 0);
    }

    private SceneWrapper next(String conversationId, String query, int retry) throws MpdaException {
        String execute = execute(conversationId, query);
        if (execute.startsWith("<think>")) {
            execute = execute.replaceFirst("(?s)<think>.*?</think>", "");
        }
        String finalExecute = execute.trim();
        Optional<SceneWrapper> match = childrenWrapper.stream().filter(child -> child.sceneId.equals(finalExecute)).findFirst();
        if (match.isPresent()) {
            return match.get();
        }
        retry++;
        if (retry >= 3) {
            return new UnknowWrapper();
        }
        return next(conversationId, query, retry);
    }

    public static final class Builder {

        private Chain chain;
        private TraceManager traceManager;
        private AgentPrompt prompt;
        private String description;

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

        public Builder prompt(AgentPrompt prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder description(String description) {
            this.description = description;
            return this;
        }

        public SceneWrapper build() {
            return new SceneWrapper(chain, traceManager, prompt, description);
        }
    }
}
