package com.dahuaboke.mpda.core.client.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * @Desc: 输入格式需要自定义, 与本地rerank模型服务端匹配
 * @Author：zhh
 * @Date：2025/7/24 19:03
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RerankerRequest {

    @JsonProperty("query")
    private String query;
    @JsonProperty("model")
    private String model;

    @JsonProperty("texts")
    private List<String> texts;


    public RerankerRequest(@JsonProperty("model") String model, @JsonProperty("query") String query, @JsonProperty("texts") List<String> texts) {
        this.model = model;
        this.query = query;
        this.texts = texts;
    }

    public String getQuery() {
        return query;
    }

    public void setQuery(String query) {
        this.query = query;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<String> getTexts() {
        return texts;
    }

    public void setTexts(List<String> texts) {
        this.texts = texts;
    }
}
