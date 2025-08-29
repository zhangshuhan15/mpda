package com.dahuaboke.mpda.rag.config;

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
public class CustomKeywordEnricher implements DocumentTransformer {

    private static final String EXCERPT_KEYWORDS_METADATA_KEY = "excerpt_keywords";

    private final ChatModel chatModel;

    private String keyWordTemplate;

    public CustomKeywordEnricher(ChatModel chatModel) {
        this(chatModel, ".基于文档内容,提取5个关键字");
    }

    public CustomKeywordEnricher(ChatModel chatModel, String keyWordTemplate) {
        this.chatModel = chatModel;
        this.keyWordTemplate = keyWordTemplate;
    }

    public void setPrompt(String prompt) {
        this.keyWordTemplate = prompt;
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        for (Document document : documents) {
            Prompt text = new Prompt(document.getText() + keyWordTemplate);
            String content = chatModel.call(text).getResult().getOutput().getText();
            String keys = content.split("『RESULT』")[1].split("『END』")[0].trim();
            System.out.println("关键字为:" + keys);  // TODO
            if (keys.isEmpty()) {
                document.getMetadata().put(EXCERPT_KEYWORDS_METADATA_KEY, content);
            }
            document.getMetadata().put(EXCERPT_KEYWORDS_METADATA_KEY, keys);
        }
        return documents;
    }
}
