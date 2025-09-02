package com.dahuaboke.mpda.bot.rag;

import com.dahuaboke.mpda.core.rag.convert.PdfDocumentConvert;
import com.dahuaboke.mpda.core.rag.enricher.KeywordEnricher;
import com.dahuaboke.mpda.core.rag.enricher.SummaryEnricher;
import com.dahuaboke.mpda.core.rag.reader.DefaultDocumentReader;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Desc: 基金产品文档管理
 * @Author：zhh
 * @Date：2025/9/1 22:25
 */
@Component
public class FundDocInsertManager {

    private final Logger log = LoggerFactory.getLogger(FundDocInsertManager.class);

    @Value("${pdfFilePath}")
    private  String pdfFilePath;

    @Autowired
    private ChatModel chatModel;

    @Autowired
    private VectorStore vectorStore;

    /**
     * 批量将pdf插入向量数据库
     *
     * @param paths 文件资源路径
     * @throws IOException
     */
    public void batchProcessDocs(List<String> paths) throws IOException {

        ArrayList<Resource> allResources = new ArrayList<>();
        for (String path : paths) {
            DefaultDocumentReader defaultDocumentReader = new DefaultDocumentReader();
            Resource[] read = defaultDocumentReader.read(path);
            allResources.addAll(List.of(read));
        }
        batchProcessDocs(allResources.toArray(new Resource[0]));
    }

    /**
     * 批量将pdf插入向量数据库   TODO 抽离出来采集失败监控代码
     *
     * @param resources 文件来源
     */
    public  void batchProcessDocs(Resource[] resources) {
        // 状态跟踪
        List<String> successFiles = new ArrayList<>();
        List<String> failedFiles = new ArrayList<>();
        Map<String, String> failureReasons = new HashMap<>();

        log.info("开始处理PDF文件，共 {} 个文件", resources.length);

        for (int i = 0; i < resources.length; i++) {
            Resource resource = resources[i];
            String filename = resource.getFilename();

            try {
                log.info("正在处理文件 [{}/{}]: {}", i + 1, resources.length, filename);
                PdfDocumentConvert pdfDocumentConvert = new PdfDocumentConvert();
                List<Document> docs = pdfDocumentConvert.readToDocuments(resource);

                String prefix = "『RESULT』";
                String suffix = "『END』";
                KeywordEnricher keywordEnricher = new KeywordEnricher(chatModel, ".基于文档内容,提取5个关键字", prefix, suffix);
                keywordEnricher.setPrompt(getPrompt());
                List<Document> keywordDocs = keywordEnricher.apply(docs);


                String summaryPrompt = "以下是该部分的内容: 总结该部分的关键主题和实体.\n\n摘要:";
                SummaryEnricher summaryMetadataEnricher = new SummaryEnricher(chatModel,
                        List.of(SummaryEnricher.SummaryType.PREVIOUS,
                                SummaryEnricher.SummaryType.CURRENT, SummaryEnricher.SummaryType.NEXT)
                        , summaryPrompt
                );
                summaryMetadataEnricher.setPrompt(summaryPrompt);
                List<Document> summaryDocs = summaryMetadataEnricher.apply(keywordDocs);

                vectorStore.add(summaryDocs);

                successFiles.add(filename);
                log.debug("文件处理成功: {}", filename);
            } catch (Exception e) {
                failedFiles.add(filename);
                failureReasons.put(filename, e.getMessage());
                log.error("文件处理失败: {} | 原因: {}", filename, e.getMessage(), e);
            }
        }

        // 输出最终报告
        log.info("\n===== 处理结果 =====");
        log.info("成功: {} 个文件", successFiles.size());
        log.info("失败: {} 个文件", failedFiles.size());

        if (!failedFiles.isEmpty()) {
            log.info("失败文件列表:");
            failedFiles.forEach(f -> log.info("- {} (原因: {})", f, failureReasons.get(f)));

            // 可以将失败记录写入文件
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String failureLogPath = "pdf_processing_failures_" + timestamp + ".log";
            try (PrintWriter writer = new PrintWriter(failureLogPath)) {
                failedFiles.forEach(f -> writer.println(f + " | " + failureReasons.get(f)));
            } catch (FileNotFoundException e) {
                log.error("无法写入失败日志文件", e);
            }
        }
    }

    private static String getPrompt() {
        List<String> keyWords = RagPrompt.FUND_KEYS;
        return RagPrompt.DEFAULT_PROMPT_TEMPLATE
                .render(Map.of("keyWords", keyWords.toString()));
    }




}
