package com.dahuaboke.mpda.core.rag.enricher;


import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.document.Document;
import org.springframework.ai.document.DocumentTransformer;
import org.springframework.ai.document.MetadataMode;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Desc: 文档摘要提取工具
 * @Author：zhh
 * @Date：2025/7/26 15:03
 */
public class SummaryEnricher implements DocumentTransformer {
    private static final String SECTION_SUMMARY_METADATA_KEY = "section_summary";
    private static final String NEXT_SECTION_SUMMARY_METADATA_KEY = "next_section_summary";
    private static final String PREV_SECTION_SUMMARY_METADATA_KEY = "prev_section_summary";
    private final ChatModel chatModel;
    private final List<SummaryType> summaryTypes;
    private final MetadataMode metadataMode;
    private String summaryTemplate;


    public SummaryEnricher(ChatModel chatModel, List<SummaryType> summaryTypes, String summaryTemplate) {
        this(chatModel, summaryTypes, summaryTemplate, MetadataMode.ALL);
    }

    public SummaryEnricher(ChatModel chatModel, List<SummaryType> summaryTypes, String summaryTemplate, MetadataMode metadataMode) {
        Assert.notNull(chatModel, "ChatModel must not be null");
        Assert.hasText(summaryTemplate, "Summary template must not be empty");
        this.chatModel = chatModel;
        this.summaryTypes = CollectionUtils.isEmpty(summaryTypes) ? List.of(SummaryType.CURRENT) : summaryTypes;
        this.metadataMode = metadataMode;
        this.summaryTemplate = summaryTemplate;
    }

    public void setPrompt(String prompt) {
        this.summaryTemplate = prompt;
    }

    @Override
    public List<Document> apply(List<Document> documents) {
        List<String> documentSummaries = new ArrayList();
        for (Document document : documents) {
            String documentContext = document.getFormattedContent(this.metadataMode);
            Prompt text = new Prompt(summaryTemplate + documentContext);
            String content = chatModel.call(text).getResult().getOutput().getText();
            documentSummaries.add(content);
        }
        for (int i = 0; i < documentSummaries.size(); i++) {
            Map<String, Object> summaryMetadata = this.getSummaryMetadata(i, documentSummaries);
            documents.get(i).getMetadata().putAll(summaryMetadata);
        }
        return documents;
    }

    private Map<String, Object> getSummaryMetadata(int i, List<String> documentSummaries) {
        Map<String, Object> summaryMetadata = new HashMap();
        if (i > 0 && this.summaryTypes.contains(SummaryType.PREVIOUS)) {
            summaryMetadata.put(PREV_SECTION_SUMMARY_METADATA_KEY, documentSummaries.get(i - 1));
        }

        if (i < documentSummaries.size() - 1 && this.summaryTypes.contains(SummaryType.NEXT)) {
            summaryMetadata.put(NEXT_SECTION_SUMMARY_METADATA_KEY, documentSummaries.get(i + 1));
        }

        if (this.summaryTypes.contains(SummaryType.CURRENT)) {
            summaryMetadata.put(SECTION_SUMMARY_METADATA_KEY, documentSummaries.get(i));
        }

        return summaryMetadata;
    }

    public enum SummaryType {
        PREVIOUS,
        CURRENT,
        NEXT;

        SummaryType() {
        }
    }
}

