package com.dahuaboke.mpda.client;


import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.AbstractVectorStoreBuilder;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.observation.AbstractObservationVectorStore;
import org.springframework.ai.vectorstore.observation.VectorStoreObservationContext;

import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/1 11:18
 */
public class CustomVectorStore extends AbstractObservationVectorStore {

    public CustomVectorStore(AbstractVectorStoreBuilder<?> builder) {
        super(builder);
    }

    @Override
    public void doAdd(List<Document> documents) {

    }

    @Override
    public void doDelete(List<String> idList) {

    }

    @Override
    public List<Document> doSimilaritySearch(SearchRequest request) {
        return List.of();
    }

    @Override
    public VectorStoreObservationContext.Builder createObservationContextBuilder(String operationName) {
        return null;
    }
}
