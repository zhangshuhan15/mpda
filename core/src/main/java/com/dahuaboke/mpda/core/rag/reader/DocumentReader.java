package com.dahuaboke.mpda.core.rag.reader;


import org.springframework.core.io.Resource;

import java.io.IOException;

/**
 * @Desc: 文件资源路径读取接口
 * @Author：zhh
 * @Date：2025/9/1 22:45
 */
public interface  DocumentReader {

    Resource[] read(String path) throws IOException;
}
