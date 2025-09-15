package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.consts.Constants;
import com.dahuaboke.mpda.core.trace.TraceManager;
import reactor.core.publisher.Flux;

import java.util.HashMap;
import java.util.Map;

/**
 * auth: dahua
 * time: 2025/8/21 09:14
 */
public abstract class AbstractChain implements Chain {

    protected final Graph graph;
    protected final AgentPrompt agentPrompt;
    protected final TraceManager traceManager;
    protected Map<String, Object> attribute;

    public AbstractChain(Graph graph, AgentPrompt agentPrompt, TraceManager traceManager) {
        this.graph = graph;
        this.agentPrompt = agentPrompt;
        this.traceManager = traceManager;
        attribute = new HashMap<>() {{
            put(Constants.PROMPT, null);
            put(Constants.QUERY, null);
            put(Constants.RESULT, null);
            put(Constants.TOOLS, null);
            put(Constants.IS_TOOL_QUERY, null);
        }};
    }

    @Override
    public void init() throws MpdaGraphException {
        graph.init(attribute.keySet());
    }

    @Override
    public String slide(String query) throws MpdaRuntimeException {
        prepare(query);
        return executeGraph();
    }

    @Override
    public Flux<String> slideAsync(String query) throws MpdaRuntimeException {
        prepare(query);
        return executeGraphAsync();
    }

    abstract public String executeGraph() throws MpdaRuntimeException;

    abstract public Flux<String> executeGraphAsync() throws MpdaRuntimeException;

    private void prepare(String query) {
        attribute.remove(Constants.TOOLS);
        attribute.put(Constants.PROMPT, agentPrompt.description());
        attribute.put(Constants.QUERY, query);
        attribute.put(Constants.CONVERSATION_ID, traceManager.getConversationId());
        attribute.put(Constants.SCENE_ID, traceManager.getSceneId());
        traceManager.setAttribute(attribute);
    }
}
