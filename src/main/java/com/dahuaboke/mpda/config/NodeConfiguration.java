package com.dahuaboke.mpda.config;

import com.alibaba.cloud.ai.graph.KeyStrategy;
import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.dahuaboke.mpda.context.StateGraphContext;
import com.dahuaboke.mpda.node.HumanNode;
import com.dahuaboke.mpda.node.LLmNode;
import com.dahuaboke.mpda.node.NodeDispatcher;
import com.dahuaboke.mpda.node.ReturnNode;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

@Configuration
public class NodeConfiguration {

    @Bean
    @Scope("prototype")
    public StateGraph mpdaGraph(ChatModel chatModel, ChatMemory chatMemory) throws GraphStateException {
        StateGraphContext stateGraphContext = new StateGraphContext(UUID.randomUUID().toString(), chatMemory);
        KeyStrategyFactory keyStrategyFactory = () -> {
            Map<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            keyStrategyHashMap.put("q", new ReplaceStrategy());
            keyStrategyHashMap.put("r", new ReplaceStrategy());
            keyStrategyHashMap.put("l", new ReplaceStrategy());
            return keyStrategyHashMap;
        };

        StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                .addNode("llm", node_async(new LLmNode(stateGraphContext, chatModel, chatMemory)))
                .addNode("human", node_async(new HumanNode()))
                .addNode("return", node_async(new ReturnNode()))

                .addEdge(StateGraph.START, "llm")
                .addConditionalEdges("llm", edge_async(new NodeDispatcher(stateGraphContext)),
                        Map.of("go_human", "human",
                                "go_return", "return"))
                .addEdge("human", "llm")
                .addEdge("return", StateGraph.END);
        return stateGraph;
    }
}
