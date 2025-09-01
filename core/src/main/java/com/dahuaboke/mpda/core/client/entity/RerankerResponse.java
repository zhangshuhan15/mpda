package com.dahuaboke.mpda.core.client.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @Desc: 输出格式需要自定义, 与本地rerank模型服务端匹配
 * @Author：zhh
 * @Date：2025/7/24 19:24
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RerankerResponse {

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
