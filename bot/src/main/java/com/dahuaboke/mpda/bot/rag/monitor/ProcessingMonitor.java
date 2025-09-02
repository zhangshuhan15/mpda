package com.dahuaboke.mpda.bot.rag.monitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.Function;

/**
 * @Desc: 基金产品监控类
 * @Author：zhh
 * @Date：2025/9/2 17:20
 */
@Component
public class ProcessingMonitor {

    private static final Logger log = LoggerFactory.getLogger(ProcessingMonitor.class);

    /**
     * 处理一批产品，并监控成功/失败情况
     *
     * @param items          要处理的产品列表
     * @param processor      处理每个产品的函数
     * @param itemNameGetter 获取产品名称的函数
     * @param operationName  操作名称（用于日志）
     * @param <T>            产品类型
     * @return 处理结果报告
     */
    public <T> ProcessingResult<T> processBatch(
            List<T> items,
            Function<T, Boolean> processor,
            Function<T, String> itemNameGetter,
            String operationName) {

        ProcessingResult<T> result = new ProcessingResult<>(operationName);

        log.info("开始{}，共 {} 个产品", operationName, items.size());

        for (int i = 0; i < items.size(); i++) {
            T item = items.get(i);
            String itemName = itemNameGetter.apply(item);

            try {
                log.info("正在处理产品 [{}/{}]: {}", i + 1, items.size(), itemName);
                boolean success = processor.apply(item);

                if (success) {
                    result.recordSuccess(item);
                    log.debug("产品处理成功: {}", itemName);
                } else {
                    result.recordFailure(item, "处理返回失败");
                    log.warn("产品处理返回失败: {}", itemName);
                }
            } catch (Exception e) {
                result.recordFailure(item, e.getMessage());
                log.error("产品处理失败: {} | 原因: {}", itemName, e.getMessage(), e);
            }
        }

        // 生成并输出报告
        result.generateReport();
        return result;
    }
    
    /**
     * 将失败记录写入文件
     *
     * @param failures     失败产品映射
     * @param filePrefix   文件前缀
     */
    public void writeFailuresToFile(Map<?, String> failures, String filePrefix) {
        if (failures.isEmpty()) {
            return;
        }

        String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
        String failureLogPath = filePrefix + "_failures_" + timestamp + ".log";

        try (PrintWriter writer = new PrintWriter(failureLogPath)) {
            failures.forEach((item, reason) -> writer.println(item + " | " + reason));
            log.info("失败记录已写入文件: {}", failureLogPath);
        } catch (Exception e) {
            log.error("无法写入失败日志文件", e);
        }
    }

    /**
     * 处理结果容器类
     */
    public static class ProcessingResult<T> {
        private final String operationName;
        private final List<T> successItems = new ArrayList<>();
        private final Map<T, String> failures = new HashMap<>();

        public ProcessingResult(String operationName) {
            this.operationName = operationName;
        }

        public void recordSuccess(T item) {
            successItems.add(item);
        }

        public void recordFailure(T item, String reason) {
            failures.put(item, reason);
        }

        public void generateReport() {
            log.info("\n===== {} 处理结果 =====", operationName);
            log.info("成功: {} 个产品", successItems.size());
            log.info("失败: {} 个产品", failures.size());

            if (!failures.isEmpty()) {
                log.info("失败产品列表:");
                failures.forEach((item, reason) -> log.info("- {} (原因: {})", item, reason));
            }
        }

        public List<T> getSuccessItems() {
            return successItems;
        }

        public Map<T, String> getFailures() {
            return failures;
        }

        public String getOperationName() {
            return operationName;
        }
    }
}