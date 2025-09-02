package com.dahuaboke.mpda.core.rag.convert;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

/**
 * @Desc: pdf文档转换器
 * @Author：zhh
 * @Date：2025/9/1 21:51
 */
@Component
public class PdfDocumentConvert implements DocumentConvert {

    private static final int PAGE_MARGIN = 70;
    private static final int BOTTOM_MARGIN = 65;
    private static final int TOP_TEXT_LINES_TO_DELETE = 3;
    private static final int BOTTOM_TEXT_LINES_TO_DELETE = 3;

    protected final Logger log = LoggerFactory.getLogger(PdfDocumentConvert.class);

    @Override
    public List<Document> readToDocuments(Resource resource) throws IOException {
        String filename = resource.getFilename();
        log.debug("开始读取PDF文件: {}", filename);

        // 配置PDF读取规则（边距、文本清理、按页拆分）
        PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                .withPageTopMargin(PAGE_MARGIN)
                .withPageBottomMargin(BOTTOM_MARGIN)
                .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                        .withNumberOfTopTextLinesToDelete(TOP_TEXT_LINES_TO_DELETE)
                        .withNumberOfBottomTextLinesToDelete(BOTTOM_TEXT_LINES_TO_DELETE)
                        .build())
                .withPagesPerDocument(1)
                .build();


        // 自动关闭输入流（try-with-resources）
        try (InputStream is = resource.getInputStream()) {
            PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource, config);
            List<Document> documents = pdfReader.read();
            log.debug("PDF文件{}读取完成，生成{}个Document", filename, documents.size());
            return documents;
        }
    }

}
