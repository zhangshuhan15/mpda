package com.dahuaboke.mpda.bot.rag.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * @Desc: 基金pdf文件工具类
 * @Author：zhh
 * @Date：2025/9/1 22:41
 */
public class FundDocUtil {
    /**
     * 通过文件名称,提取基金代码和基金名称
     * @return
     */
    public static Map<String, String> getProductMap(String filename) {
        Map<String, String> mapRelation = new HashMap<>();
        // 1. 去掉 ".pdf" 后缀
        String filenameWithoutExt = filename.replace(".pdf", "");

        // 2. 提取基金代码
        String fundCode = filenameWithoutExt.replaceAll("\\D.*", "");

        // 3. 剩余字符串
        String remaining = filenameWithoutExt.substring(fundCode.length() + 1);

        // 4. 清理后缀
        int lastSeparator = Math.max(remaining.lastIndexOf("_"), remaining.lastIndexOf("-"));
        String fundName = remaining.substring(0, lastSeparator);

        mapRelation.put(fundCode, fundName);
        return mapRelation;
    }
}
