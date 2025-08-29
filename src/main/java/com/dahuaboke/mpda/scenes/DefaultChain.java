package com.dahuaboke.mpda.scenes;


import com.dahuaboke.mpda.agent.chain.AbstractChain;
import com.dahuaboke.mpda.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.agent.graph.Graph;
import com.dahuaboke.mpda.agent.prompt.Prompt;
import com.dahuaboke.mpda.trace.TraceManager;
import reactor.core.publisher.Flux;

/**
 * auth: dahua
 * time: 2025/8/21 14:11
 */
public class DefaultChain extends AbstractChain {

    private DefaultChain(Graph graph, Prompt prompt, TraceManager traceManager) {
        super(graph, prompt, traceManager);
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
        private Prompt prompt;
        private TraceManager traceManager;

        private Builder() {
        }

        public Builder graph(Graph graph) {
            this.graph = graph;
            return this;
        }

        public Builder prompt(Prompt prompt) {
            this.prompt = prompt;
            return this;
        }

        public Builder traceManager(TraceManager traceManager) {
            this.traceManager = traceManager;
            return this;
        }

        public DefaultChain build() {
            return new DefaultChain(graph, prompt, traceManager);
        }
    }
}
