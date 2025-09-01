package com.dahuaboke.mpda.client;


import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.AbstractEmbeddingModel;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;
import org.springframework.web.client.RestTemplate;

/**
 * auth: dahua
 * time: 2025/9/1 11:20
 */
public class CustomEmbeddingModel extends AbstractEmbeddingModel {

    private final RestTemplate restTemplate;

    public CustomEmbeddingModel(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        return null;
    }

    @Override
    public float[] embed(Document document) {
        return new float[0];
    }
}
