package com.dahuaboke.mpda.bot.scenes.smallTalk;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.context.consts.Constants;
import com.dahuaboke.mpda.core.node.StreamLlmNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/21 09:12
 */
@Component
public class SmallTalkGraph extends AbstractGraph {

    @Autowired
    private StreamLlmNode streamLlmNode;

    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("streamLlm", node_async(streamLlmNode))

                    .addEdge(StateGraph.START, "streamLlm")
                    .addEdge("streamLlm", StateGraph.END);
            return Map.of("default", stateGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public String execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            return getGraph("default").invoke(attribute).get().value(Constants.RESULT, String.class).get();
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            AsyncGenerator<NodeOutput> generator = getGraph("default").stream(attribute,
                    RunnableConfig.builder().threadId(cacheManager.getContext().getSceneId()).build());
            return changeFlux(generator);
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}
