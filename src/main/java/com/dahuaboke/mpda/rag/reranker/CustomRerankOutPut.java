package com.dahuaboke.mpda.rag.reranker;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Desc: 输出格式需要自定义, 与本地rerank模型服务端匹配
 * @Author：zhh
 * @Date：2025/7/24 19:24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class CustomRerankOutPut {

    @JsonProperty("score")
    private double relevanceScore;

    @JsonProperty("index")
    private int index;


    public double getRelevanceScore() {
        return relevanceScore;
    }

    public int getIndex() {
        return index;
    }
}
