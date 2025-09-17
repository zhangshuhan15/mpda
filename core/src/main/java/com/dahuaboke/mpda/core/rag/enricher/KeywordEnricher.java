package com.dahuaboke.mpda.core.rag.enricher;

import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;

import java.util.List;


/**
 * @Desc: 文档关键字提取工具
 * @Author：zhh
 * @Date：2025/7/22 15:03
 */
public class KeywordEnricher implements DocumentTransformer {

    public static final String EXCERPT_KEYWORDS_METADATA_KEY = "excerpt_keywords";
    private final ChatModel chatModel;
    private final String prefix;
    private final String suffix;
    private String keyWordTemplate;

    public KeywordEnricher(ChatModel chatModel, String keyWordTemplate, String prefix, String suffix) {
        this.chatModel = chatModel;
        this.keyWordTemplate = keyWordTemplate;
        this.prefix = prefix;
        this.suffix = suffix;
    }

    public void setPrompt(String prompt) {
        this.keyWordTemplate = prompt;
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        for (Document document : documents) {
            Prompt text = new Prompt(document.getText() + keyWordTemplate);
            String content = chatModel.call(text).getResult().getOutput().getText();
            String keys = content.split(prefix)[1].split(suffix)[0].trim();

            if (keys.isEmpty()) {
                document.getMetadata().put(EXCERPT_KEYWORDS_METADATA_KEY, content);
            }
            document.getMetadata().put(EXCERPT_KEYWORDS_METADATA_KEY, keys);
        }
        return documents;
    }
}
