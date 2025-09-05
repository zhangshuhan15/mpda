package com.dahuaboke.mpda.client.convert;

import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.lang.Nullable;

import java.util.LinkedHashMap;
import java.util.List;

/**
 * @Desc: 转换对象接口
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public interface DocumentConverter<R> {

    List<R> requestConvert(@Nullable List<Document> documents, EmbeddingModel embeddingModel);

    List<Document> resultConvert(@Nullable List<LinkedHashMap<String, Object>> result);

}
