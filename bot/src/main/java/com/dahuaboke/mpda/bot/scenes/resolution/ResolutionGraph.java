package com.dahuaboke.mpda.bot.scenes.resolution;


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
import com.dahuaboke.mpda.core.client.entity.LlmResponse;
import com.dahuaboke.mpda.core.consts.Constants;
import com.dahuaboke.mpda.core.node.LlmNode;
import org.springframework.ai.chat.messages.Message;
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
public class ResolutionGraph extends AbstractGraph {

    @Autowired
    private LlmNode llmNode;

    public StateGraph buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException {
        try {
            StateGraph stateGraph = new StateGraph(keyStrategyFactory)
                    .addNode("llm", node_async(llmNode))

                    .addEdge(StateGraph.START, "llm")
                    .addEdge("llm", StateGraph.END);
            return stateGraph;
        } catch (GraphStateException e) {
            throw new MpdaGraphException(e);
        }
    }

    @Override
    public String execute(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            LlmResponse llmResponse = this.compiledGraph.invoke(attribute).get().value(Constants.RESULT, LlmResponse.class).get();
            return llmResponse.chatResponse().getResult().getOutput().getText();
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public Flux<String> executeAsync(Map<String, Object> attribute) throws MpdaRuntimeException {
        try {
            AsyncGenerator<NodeOutput> generator = this.compiledGraph.stream(attribute,
                    RunnableConfig.builder().threadId(traceManager.getSceneId()).build());
            return changeFlux(generator);
        } catch (GraphRunnerException e) {
            throw new MpdaRuntimeException(e);
        }
    }

    @Override
    public void addMemory(Message message) {
    }

    @Override
    public void addMemory(String conversationId, String sceneId, Message message) {
    }
}
