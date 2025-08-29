package com.dahuaboke.mpda.rag.extractor;


import java.util.List;

/**
 * @Desc: 用户输入内容提取接口
 * @Author：zhh
 * @Date：2025/7/25 11:06
 */
public interface QueryExtractor {
    List<String> extractor();
}
