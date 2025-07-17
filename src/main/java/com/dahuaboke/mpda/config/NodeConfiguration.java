package com.dahuaboke.mpda.config;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.dahuaboke.mpda.node.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

@Configuration
public class NodeConfiguration {

    @Autowired
    private LLmNode llmNode;

    @Autowired
    private ToolNode toolNode;

    @Autowired
    private HumanNode humanNode;

    @Autowired
    private RecordingNode recordingNode;

    @Autowired
    private NodeDispatcher nodeDispatcher;

    @Bean
    public StateGraph mpdaGraph() throws GraphStateException {
        KeyStrategyFactory keyStrategyFactory = () -> {
            Map<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            keyStrategyHashMap.put("q", new ReplaceStrategy());
            return keyStrategyHashMap;
        };
        StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                .addNode("llm", node_async(llmNode))
                .addNode("tool", node_async(toolNode))
                .addNode("human", node_async(humanNode))
                .addNode("recode", node_async(recordingNode))

                .addEdge(StateGraph.START, "llm")
                .addConditionalEdges("llm", edge_async(nodeDispatcher),
                        Map.of("go_human", "human","go_tool", "tool"))
                .addEdge("tool","llm")
                .addEdge("human","llm")
                .addEdge("recode", StateGraph.END);
        return stateGraph;
    }
}
