package com.dahuaboke.mpda.ai_code.scenes.codeGenerate;


import com.alibaba.cloud.ai.graph.KeyStrategyFactory;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.ai_code.scenes.codeGenerate.edge.CodeGenerateDispatcher;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.agent.exception.MpdaRuntimeException;
import com.dahuaboke.mpda.core.agent.graph.AbstractGraph;
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.consts.Constants;
import com.dahuaboke.mpda.core.node.HumanNode;
import com.dahuaboke.mpda.core.node.LlmNode;
import com.dahuaboke.mpda.core.node.ToolNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.List;
import java.util.Map;

import static com.alibaba.cloud.ai.graph.action.AsyncEdgeAction.edge_async;
import static com.alibaba.cloud.ai.graph.action.AsyncNodeAction.node_async;

/**
 * auth: dahua
 * time: 2025/9/10 15:59
 */
@Component
public class CodeGenerateGraph extends AbstractGraph {

    @Autowired
    private CodeGenerateDispatcher codeGenerateDispatcher;

    @Autowired
    private LlmNode llmNode;

    @Autowired
    private HumanNode humanNode;

    @Autowired
    private ToolNode toolNode;

    @Override
    public Map<Object, StateGraph> buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))
                    .addNode("human", node_async(humanNode))
                    .addNode("tool", node_async(toolNode))

                    .addEdge(StateGraph.START, "llm")
                    .addConditionalEdges("llm", edge_async(codeGenerateDispatcher),
                            Map.of("go_human", "human", "go_tool", "tool"))
                    .addEdge("tool", "llm")
                    .addEdge("human", StateGraph.END);
            return Map.of("default", stateGraph);
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public String execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("codeGenerateTool"));
        try {
            LlmResponse llmResponse = getGraph("default").invoke(attribute).get().value(Constants.RESULT, LlmResponse.class).get();
            return llmResponse.chatResponse().getResult().getOutput().getText();
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        attribute.put(Constants.TOOLS, List.of("codeGenerateTool"));
        try {
            AsyncGenerator<NodeOutput> generator = getGraph("default").stream(attribute,
                    RunnableConfig.builder().threadId(traceManager.getSceneId()).build());
            return changeFlux(generator);
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }
}
