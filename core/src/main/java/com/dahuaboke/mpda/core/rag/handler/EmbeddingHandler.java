package com.dahuaboke.mpda.core.rag.handler;


import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/1 10:33
 */
@Component
public class EmbeddingHandler {

    private final VectorStore vectorStore;

    public EmbeddingHandler(VectorStore vectorStore) {
        this.vectorStore = vectorStore;
    }

    public List<Document> handler(SearchRequest searchRequest, int topK) {
        SearchRequest request = SearchRequest.builder()
                .query(searchRequest.getQuery())
                .topK(topK)
                .similarityThreshold(searchRequest.getSimilarityThreshold())
                .build();
        return vectorStore.similaritySearch(request);
    }
}
