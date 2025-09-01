package com.dahuaboke.mpda.core.agent.graph;


import com.alibaba.cloud.ai.graph.*;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.state.strategy.ReplaceStrategy;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import com.dahuaboke.mpda.core.agent.exception.MpdaGraphException;
import com.dahuaboke.mpda.core.trace.TraceManager;
import org.springframework.ai.chat.messages.Message;
import org.springframework.beans.factory.annotation.Autowired;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

/**
 * auth: dahua
 * time: 2025/8/21 14:13
 */
public abstract class AbstractGraph implements Graph {

    @Autowired
    protected TraceManager traceManager;
    protected CompiledGraph compiledGraph;

    @Override
    public void init(Set<String> keys) throws MpdaGraphException {
        KeyStrategyFactory keyStrategyFactory = buildKeyStrategyFactory(keys);
        StateGraph stateGraph = buildGraph(keyStrategyFactory);
        try {
            this.compiledGraph = stateGraph.compile();
        } catch (GraphStateException e) {
            throw new MpdaGraphException("Compiled failed.", e);
        }
    }

    protected KeyStrategyFactory buildKeyStrategyFactory(Set<String> keys) {
        return () -> {
            Map<String, KeyStrategy> keyStrategyHashMap = new HashMap<>();
            keys.stream().forEach(key -> keyStrategyHashMap.put(key, new ReplaceStrategy()));
            return keyStrategyHashMap;
        };
    }

    @Override
    public void addMemory(Message message) {
        traceManager.addMemory(message);
    }

    @Override
    public void addMemory(String conversationId, String sceneId, Message message) {
        traceManager.addMemory(conversationId, sceneId, message);
    }

    abstract public StateGraph buildGraph(KeyStrategyFactory keyStrategyFactory) throws MpdaGraphException;

    protected Flux<String> changeFlux(AsyncGenerator<NodeOutput> generator) {
        Sinks.Many<String> sink = Sinks.many().multicast().onBackpressureBuffer();
        CompletableFuture.runAsync(() -> generator.forEachAsync(output -> {
            try {
                if (output instanceof StreamingOutput streamingOutput) {
                    sink.tryEmitNext(streamingOutput.chunk());
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenRun(() -> sink.tryEmitComplete()).exceptionally(ex -> {
            sink.tryEmitError(ex);
            return null;
        }));
        return sink.asFlux() // TODO
                .doOnCancel(() -> System.out.println("Client disconnected from stream"))
                .doOnError(e -> System.err.println("Error occurred during streaming: " + e));
    }
}
