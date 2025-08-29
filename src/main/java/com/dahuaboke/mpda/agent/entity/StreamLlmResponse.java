package com.dahuaboke.mpda.agent.entity;


import com.alibaba.cloud.ai.graph.async.AsyncGenerator;

/**
 * auth: dahua
 * time: 2025/8/21 10:37
 */
public record StreamLlmResponse(AsyncGenerator response) implements AsyncGenerator {

    @Override
    public Data next() {
        return response.next();
    }
}
