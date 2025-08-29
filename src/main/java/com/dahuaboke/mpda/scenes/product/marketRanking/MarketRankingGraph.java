package com.dahuaboke.mpda.scenes.product.marketRanking;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.agent.entity.LlmResponse;
import com.dahuaboke.mpda.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.consts.Constants;
import com.dahuaboke.mpda.node.HumanNode;
import com.dahuaboke.mpda.node.LlmNode;
import com.dahuaboke.mpda.node.StreamLlmNode;
import com.dahuaboke.mpda.node.ToolNode;
import com.dahuaboke.mpda.scenes.product.marketRanking.edge.MarketRankingDispatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/8/22 14:27
 */
@Component
public class MarketRankingGraph extends AbstractGraph {

    @Autowired
    private LlmNode llmNode;

    @Autowired
    private StreamLlmNode streamLlmNode;

    @Autowired
    private HumanNode humanNode;

    @Autowired
    private ToolNode toolNode;

    @Autowired
    private MarketRankingDispatcher marketRankingDispatcher;

    @Autowired
    private MarketRankingPrompt marketRankingPrompt;

    @Override
    public StateGraph buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("streamLlm", node_async(streamLlmNode))
                    .addNode("human", node_async(humanNode))
                    .addNode("tool", node_async(toolNode))

                    .addEdge(StateGraph.START, "llm")
                    .addConditionalEdges("llm", edge_async(marketRankingDispatcher),
                            Map.of("go_human", "human", "go_tool", "tool"))
                    .addEdge("tool", "streamLlm")
                    .addEdge("human", StateGraph.END)
                    .addEdge("streamLlm", StateGraph.END);
            return stateGraph;
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public String execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("marketRankingTool"));
        marketRankingPrompt.changePrompt("guide");
        try {
            LlmResponse llmResponse = this.compiledGraph.invoke(attribute).get().value(Constants.RESULT, LlmResponse.class).get();
            return llmResponse.chatResponse().getResult().getOutput().getText();
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("marketRankingTool"));
        marketRankingPrompt.changePrompt("guide");
        try {
            AsyncGenerator<NodeOutput> generator = this.compiledGraph.stream(attribute,
                    RunnableConfig.builder().threadId(traceManager.getSceneId()).build());
            return changeFlux(generator);
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}
