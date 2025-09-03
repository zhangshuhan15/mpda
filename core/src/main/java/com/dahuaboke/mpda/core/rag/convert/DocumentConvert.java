package com.dahuaboke.mpda.core.rag.convert;

import org.springframework.ai.document.Document;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.util.List;

/**
 * @Desc: 文档转换接口, 将pdf，md，html等转换成spring ai Document
 * @Author：zhh
 * @Date：2025/9/1 22:03
 */
public interface DocumentConvert {

    List<Document> readToDocuments(Resource resource) throws IOException;
}
