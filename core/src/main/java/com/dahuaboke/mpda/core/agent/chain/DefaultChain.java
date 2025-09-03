package com.dahuaboke.mpda.core.agent.chain;


import com.dahuaboke.mpda.core.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.agent.graph.Graph;
import com.dahuaboke.mpda.core.agent.prompt.AgentPrompt;
import com.dahuaboke.mpda.core.trace.TraceManager;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:11
 */
public class DefaultChain extends AbstractChain {

    private DefaultChain(Graph graph, AgentPrompt agentPrompt, TraceManager traceManager) {
        super(graph, agentPrompt, traceManager);
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public String executeGraph() throws MpdaRuntimeException {
        return graph.execute(attribute);
    }

    @Override
    public Flux<String> executeGraphAsync() throws MpdaRuntimeException {
        return graph.executeAsync(attribute);
    }

    public static final class Builder {

        private Graph graph;
        private AgentPrompt agentPrompt;
        private TraceManager traceManager;

        private Builder() {
        }

        public Builder graph(Graph graph) {
            this.graph = graph;
            return this;
        }

        public Builder prompt(AgentPrompt agentPrompt) {
            this.agentPrompt = agentPrompt;
            return this;
        }

        public Builder traceManager(TraceManager traceManager) {
            this.traceManager = traceManager;
            return this;
        }

        public DefaultChain build() {
            return new DefaultChain(graph, agentPrompt, traceManager);
        }
    }
}
