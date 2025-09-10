package com.dahuaboke.mpda.core.rag.handler;


import com.alibaba.cloud.ai.document.DocumentWithScore;
import com.alibaba.cloud.ai.model.RerankRequest;
import com.alibaba.cloud.ai.model.RerankResponse;
import com.dahuaboke.mpda.core.client.RerankerClientManager;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;

/**
 * auth: dahua
 * time: 2025/9/1 16:29
 */
public class RerankHandler {

    private final RerankerClientManager rerankerClientManager;

    public RerankHandler(RerankerClientManager rerankerClientManager) {
        this.rerankerClientManager = rerankerClientManager;
    }

    /**
     * 调用rerank模型对文档重排序
     *
     * @param documents
     * @param topK
     * @return
     */
    public List<Document> handler(SearchRequest searchRequest, List<Document> documents, int topK) {
        if (documents.isEmpty()) {
            return List.of();
        }
        //重排序
        RerankResponse call = rerankerClientManager.call(new RerankRequest(searchRequest.getQuery(), documents));
        List<DocumentWithScore> results = call.getResults();

        //取topK个
        return results.stream()
                .sorted(Comparator.comparingDouble(DocumentWithScore::getScore).reversed())
                .limit(topK)
                .map(DocumentWithScore::getOutput)
                .toList();

    }
}
