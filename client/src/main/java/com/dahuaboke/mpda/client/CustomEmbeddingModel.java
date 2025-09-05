package com.dahuaboke.mpda.client;


import com.dahuaboke.mpda.client.handle.EmbeddingRequestHandle;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.AbstractEmbeddingModel;
import org.springframework.ai.embedding.Embedding;
import org.springframework.ai.embedding.EmbeddingRequest;
import org.springframework.ai.embedding.EmbeddingResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * @Desc: 新核心向量模型调用实现
 * @Author：zhh
 * @Date：2025/9/05 9:50
 */
public class CustomEmbeddingModel extends AbstractEmbeddingModel {

    private final EmbeddingRequestHandle embeddingRequestHandle;

    public CustomEmbeddingModel(EmbeddingRequestHandle embeddingRequestHandle) {
        this.embeddingRequestHandle = embeddingRequestHandle;
    }

    @Override
    public EmbeddingResponse call(EmbeddingRequest request) {
        List<String> instructions = request.getInstructions();
        ArrayList<Embedding> embeddings = new ArrayList<>();
        for (int i = 0; i < instructions.size(); i++) {
            float[] floats = embeddingRequestHandle.sendC014007(instructions.get(i));
            embeddings.add(new Embedding(floats,i));
        }
        return new EmbeddingResponse(embeddings);
    }

    @Override
    public float[] embed(Document document) {
        String text = document.getText();
        return embeddingRequestHandle.sendC014007(text);
    }
}
