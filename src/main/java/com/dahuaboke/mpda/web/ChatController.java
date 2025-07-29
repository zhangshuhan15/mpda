package com.dahuaboke.mpda.web;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.NodeOutput;
import com.alibaba.cloud.ai.graph.RunnableConfig;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.async.AsyncGenerator;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.alibaba.cloud.ai.graph.streaming.StreamingOutput;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Sinks;

import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;

@CrossOrigin
@RestController
public class ChatController {

    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private ChatModel chatModel;
    private CompiledGraph stateGraph;

    public ChatController(ObjectProvider<StateGraph> prototypeBeanProvider) throws GraphStateException {
        this.stateGraph = prototypeBeanProvider.getObject().compile();
    }

    @RequestMapping("/stream")
    public Flux<String> chat(@RequestHeader("Conversation-Id") String conversationId, @RequestBody String q) throws GraphRunnerException {
        Sinks.Many<String> sink = Sinks.many().unicast().onBackpressureBuffer();
        AsyncGenerator<NodeOutput> generator = stateGraph.stream(Map.of("q", q, "conversationId", conversationId),
                RunnableConfig.builder().threadId(conversationId).build());
        CompletableFuture.runAsync(() -> generator.forEachAsync(output -> {
            System.out.println("Received output: " + output.hashCode() + "result: " + output);
            try {
                if (output instanceof StreamingOutput) {
                    StreamingOutput streamingOutput = (StreamingOutput) output;
                    sink.tryEmitNext(streamingOutput.chunk());
                }
            } catch (Exception e) {
                throw new CompletionException(e);
            }
        }).thenRun(() -> sink.tryEmitComplete()).exceptionally(ex -> {
            sink.tryEmitError(ex);
            return null;
        }));

        return sink.asFlux()
                .doOnCancel(() -> System.out.println("Client disconnected from stream"))
                .doOnError(e -> System.err.println("Error occurred during streaming: " + e));
    }

    public String ragSelect() {
        // 复杂RAG 拦截器
        Advisor retrievalAugmentationAdvisor = RetrievalAugmentationAdvisor.builder()
                .queryTransformers(RewriteQueryTransformer.builder()
                        // 查询重写的逻辑，其核心是通过 RewriteQueryTransformer 对原始用户查询进行优化，以提高检索的准确性
                        .chatClientBuilder(ChatClient.builder(chatModel))
                        .build())
                // 文档检索
                .documentRetriever(VectorStoreDocumentRetriever.builder()
                        .similarityThreshold(0.50)
                        .topK(5)
                        //.filterExpression(b.eq("name","qwp").build())
                        .vectorStore(vectorStore)
                        .build())
                // 查询增强
                .queryAugmenter(ContextualQueryAugmenter.builder()
                        .allowEmptyContext(true)
                        .build())
                .build();
        ChatClient chatClient = ChatClient.builder(chatModel)
                .defaultAdvisors(
                        retrievalAugmentationAdvisor, new SimpleLoggerAdvisor())
                .build();
        String content = chatClient.prompt()
                .user("Please answer my question XYZ")
                .call()
                .content();
        return content;
    }
}
