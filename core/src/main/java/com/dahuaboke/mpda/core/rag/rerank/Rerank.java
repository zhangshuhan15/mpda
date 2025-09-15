package com.dahuaboke.mpda.core.rag.rerank;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;

/**
 * @Desc: 重排序模型接口实现
 * @Author：zhh
 * @Date：2025/9/15 15:36
 */
public interface Rerank {

    /**
     * @param searchRequest 查询对象
     * @param documents 文档
     * @param topK 返回个数
     * @return 筛选结果
     */
    List<Document> handler(SearchRequest searchRequest, List<Document> documents, int topK);
}
