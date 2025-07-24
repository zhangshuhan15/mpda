package com.dahuaboke.mpda.utils;

import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;

import java.util.List;

public class CustomTokenTextSplitter {

    public static List<Document> splitDocuments(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter();
        return splitter.apply(documents);
    }

    /*
        defaultChunkSize：每个文本块的目标大小（以令牌为单位）（默认值：800）。
        minChunkSizeChars：每个文本块的最小大小（以字符为单位）（默认值：350）。
        minChunkLengthToEmbed：要包含的块的最小长度（默认值：5）。
        maxNumChunks：从文本生成的最大块数（默认：10000）。
        keepSeparator：是否在块中保留分隔符（如换行符）（默认值：true）。
     */
    public static List<Document> splitCustomized(List<Document> documents) {
        TokenTextSplitter splitter = new TokenTextSplitter(1000, 400, 10, 5000, true);
        return splitter.apply(documents);
    }

}
