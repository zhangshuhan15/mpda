package com.dahuaboke.mpda.controller;

import com.alibaba.cloud.ai.graph.CompiledGraph;
import com.alibaba.cloud.ai.graph.StateGraph;
import com.alibaba.cloud.ai.graph.exception.GraphRunnerException;
import com.alibaba.cloud.ai.graph.exception.GraphStateException;
import com.dahuaboke.mpda.tools.CommandTool;
import com.dahuaboke.mpda.tools.DirectoryTool;
import com.dahuaboke.mpda.tools.FileTool;
import com.dahuaboke.mpda.utils.CustomTokenTextSplitter;
import com.dahuaboke.mpda.utils.DocumentReader;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.SimpleLoggerAdvisor;
import org.springframework.ai.chat.client.advisor.api.Advisor;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.model.transformer.KeywordMetadataEnricher;
import org.springframework.ai.model.transformer.SummaryMetadataEnricher;
import org.springframework.ai.rag.advisor.RetrievalAugmentationAdvisor;
import org.springframework.ai.rag.generation.augmentation.ContextualQueryAugmenter;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.rag.retrieval.search.VectorStoreDocumentRetriever;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
public class ChatController {

    @Autowired
    private ObjectProvider<StateGraph> prototypeBeanProvider;
    @Autowired
    private VectorStore vectorStore;
    @Autowired
    private ChatModel chatModel;

    @RequestMapping("chat")
    public String chat(@RequestParam("q") String q) throws GraphRunnerException, GraphStateException {
        CompiledGraph stateGraph = prototypeBeanProvider.getObject().compile();
        return stateGraph.invoke(Map.of("q", q)).get().value("h", String.class).get();
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

    @RequestMapping("/rag/save")
    public void ragSave() {
        List<Document> docsFromMd = DocumentReader.getDocsFromMd("classpath:/华安物联网主题股票型证券投资基金 2025 年第 1 季度报告.md");
        List<Document> documents = CustomTokenTextSplitter.splitDocuments(docsFromMd);
        KeywordMetadataEnricher enricher = new KeywordMetadataEnricher(chatModel, 5);
        List<Document> apply = enricher.apply(documents);
        SummaryMetadataEnricher summaryMetadataEnricher = new SummaryMetadataEnricher(chatModel, List.of(SummaryMetadataEnricher.SummaryType.CURRENT));
        List<Document> apply1 = summaryMetadataEnricher.apply(apply);
        vectorStore.add(apply1);
    }
}
