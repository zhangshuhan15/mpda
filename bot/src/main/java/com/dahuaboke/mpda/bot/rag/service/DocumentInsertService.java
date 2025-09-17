package com.dahuaboke.mpda.bot.rag.service;

import com.dahuaboke.mpda.bot.rag.RagPrompt;
import com.dahuaboke.mpda.bot.rag.monitor.ProcessingMonitor;
import com.dahuaboke.mpda.core.rag.convert.PdfDocumentConvert;
import com.dahuaboke.mpda.core.rag.enricher.KeywordEnricher;
import com.dahuaboke.mpda.core.rag.reader.DefaultDocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Desc: 文档处理服务
 * @Author：zhh
 * @Date：2025/9/2 17:27
 */
@Service
public class DocumentInsertService {

    private static final Logger log = LoggerFactory.getLogger(DocumentInsertService.class);

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private ProcessingMonitor processingMonitor;

    /**
     * 处理单个 PDF 资源
     */
    private boolean processPdfResource(Resource resource) {
        String filename = resource.getFilename();
        PdfDocumentConvert pdfDocumentConvert = new PdfDocumentConvert();
        List<Document> docs;
        try {
            docs = pdfDocumentConvert.readToDocuments(resource);
        } catch (IOException e) {
            log.error("文档解析失败{}", filename);
            return false;
        }

        KeywordEnricher keywordEnricher = new KeywordEnricher(
                chatModel,
                ".基于文档内容,提取5个关键字",
                "『RESULT』",
                "『END』"
        );
        keywordEnricher.setPrompt(RagPrompt.DEFAULT_PROMPT_TEMPLATE
                .render(Map.of("keyWords", RagPrompt.FUND_KEYS)));

        List<Document> keywordDocs = keywordEnricher.apply(docs);
        vectorStore.add(keywordDocs);

        return true;
    }

    /**
     * 观测处理 PDF 资源
     */
    public void processPdfResources(List<Resource> resources) {
        ProcessingMonitor.ProcessingResult<Resource> result = processingMonitor.processBatch(
                resources,
                this::processPdfResource,
                Resource::getFilename,
                "PDF文件处理"
        );

        // 将失败记录写入文件
        processingMonitor.writeFailuresToFile(result.getFailures(), "pdf_processing");
    }

    /**
     * 批量处理 PDF 文件
     */
    public void processPdfFilePath(List<String> paths) throws IOException {
        List<Resource> allResources = new ArrayList<>();
        for (String path : paths) {
            DefaultDocumentReader defaultDocumentReader = new DefaultDocumentReader();
            Resource[] read = defaultDocumentReader.read(path);
            allResources.addAll(List.of(read));
        }
        processPdfResources(allResources);
    }

}