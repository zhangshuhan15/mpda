package com.dahuaboke.mpda.bot.rag;

import com.dahuaboke.mpda.bot.rag.advisor.QuestionAnswerAdvisor;
import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.bot.rag.utils.FundClassifierUtil;
import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.entity.YwjqrProduct;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingHandler;
import com.dahuaboke.mpda.core.rag.handler.RerankHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @Desc:
 * @Author：zhh
 * @Date：2025/9/1 23:19
 */
@Component
public class FundDocSelectManager {

    @Autowired
    private ChatClient chatClient;

    @Autowired
    private SearchHandler searchHandler;

    @Autowired
    private EmbeddingHandler embeddingHandler;

    @Autowired
    private DocContextHandler docContextHandler;

    @Autowired
    private RerankHandler rerankHandler;

    @Autowired
    private SortHandler sortHandler;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductToolHandler productToolHandler;


    private static final Logger log = LoggerFactory.getLogger(FundDocSelectManager.class);


    /**
     * rag检索,检索结果保存到pqsql TODO 抽离出来采集失败监控代码
     *
     * @param productMap key: fundCode value: fundName
     * @throws IOException
     */
    public void selectDoc(Map<String, String> productMap) throws IOException {

        Set<Map.Entry<String, String>> productEntries = productMap.entrySet();

        //1. 初始化查询
        Map<String, String> queryMap = initAllQuery();
        Set<Map.Entry<String, String>> entries = queryMap.entrySet();

        //2. 状态跟踪
        List<String> successFiles = new ArrayList<>();

        List<String> failedFiles = new ArrayList<>();
        Map<String, String> failureReasons = new HashMap<>();

        List<String> failedQuery = new ArrayList<>();
        Map<String, String> failedQueryReasons = new HashMap<>();

        List<String> emptyQueryAnswer = new ArrayList<>();
        Map<String, String> emptyQueryAnswerReasons = new HashMap<>();

        int i = 0;
        //3. 开始遍历通过模型获取答案
        for (Map.Entry<String, String> productEntry : productEntries) {
            String productCode = productEntry.getKey();
            String productName = productEntry.getValue();
            log.info("正在处理产品 [{}/{}]: {}", i + 1, productEntries.size(), productName);
            try {
                //4. 将答案封装到基金对象
                FundFieldMapper mapper = new FundFieldMapper(FundProduct.class);
                FundProduct fund = new FundProduct();
                for (Map.Entry<String, String> entry : entries) {
                    String question = entry.getKey();
                    String userQuery = "基于" + productCode + "产品代码," + productName + "的" + question;
                    String key = entry.getValue();
                    try {
                        SearchRequest searchRequest = SearchRequest.builder()
                                .topK(15)
                                .query(userQuery)
                                .similarityThreshold(0.25)
                                .build();
                        String content = chatClient.prompt()
                                .advisors(QuestionAnswerAdvisor
                                        .builder()
                                        .keys(List.of(key))
                                        .productName(List.of(productName))
                                        .searchRequest(searchRequest)
                                        .searchHandler(searchHandler)
                                        .embeddingHandler(embeddingHandler)
                                        .docContextHandler(docContextHandler)
                                        .rerankHandler(rerankHandler)
                                        .sortHandler(sortHandler)
                                        .build())
                                .user(userQuery)
                                .call().content();

                        String answer = content.split("『RESULT』")[1].split("『END』")[0].trim();

                        //单个字段设置
                        mapper.setFieldByComment(fund, question, answer);
                        if (answer.isEmpty()) {
                            emptyQueryAnswer.add(userQuery);
                            emptyQueryAnswerReasons.put(userQuery, "没查询到任何数据");
                        }
                    } catch (Exception e) {
                        failedQuery.add(userQuery);
                        failureReasons.put(userQuery, e.getMessage());
                        log.error("产品{} 对应的query{} 处理失败:  | 原因: {}", productName, userQuery, e.getMessage(), e);
                    }
                }
                //获取基金分类
                if (checkFundType(fund.getFundCode())) {
                    Map<String, String> map = FundClassifierUtil.classifyFund(fund);
                    String[] modelResult = callModel(fund);
                    //封装对象
                    fund.setFundClassificationCode(map.get("final_category"));
                    fund.setFundClassificationReasonCode(map.get("reason"));
                    fund.setFundClassificationModel(modelResult[0]);
                    fund.setFundClassificationReasonModel(modelResult[1]);
                }
                //写入数据库
                writeDb(fund);
                successFiles.add(productName);
                log.debug("产品处理成功: {}", productName);
                i++;
            } catch (Exception e) {
                failedFiles.add(productName);
                failureReasons.put(productName, e.getMessage());
                log.error("产品处理失败: {} | 原因: {}", productName, e.getMessage(), e);
            }
        }

        // 输出最终报告
        log.info("\n===== 处理结果 =====");
        log.info("成功: {} 个文件", successFiles.size());
        log.info("失败: {} 个文件", failedFiles.size());
        log.info("失败: {} 个问题", failedQuery.size());

        if (!failedFiles.isEmpty()) {
            log.info("失败文件列表:");
            failedFiles.forEach(f -> log.info("- {} (原因: {})", f, failureReasons.get(f)));

            // 将失败产品记录写入文件
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String failureLogPath = "pdf_processing_failures_" + timestamp + ".log";
            try (PrintWriter writer = new PrintWriter(failureLogPath)) {
                failedFiles.forEach(f -> writer.println(f + " | " + failureReasons.get(f)));
            } catch (FileNotFoundException e) {
                log.error("无法写入失败日志文件", e);
            }
        }

        if (!failedQuery.isEmpty()) {
            log.info("失败问题列表:");
            failedFiles.forEach(f -> log.info("- {} (原因: {})", f, failedQueryReasons.get(f)));

            // 将失败问题记录写入文件
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String failureLogPath = "query_processing_failures_" + timestamp + ".log";
            try (PrintWriter writer = new PrintWriter(failureLogPath)) {
                failedFiles.forEach(f -> writer.println(f + " | " + failedQueryReasons.get(f)));
            } catch (FileNotFoundException e) {
                log.error("无法写入失败日志文件", e);
            }
        }

        if (!emptyQueryAnswer.isEmpty()) {
            log.info("未查询到答案列表:");
            failedFiles.forEach(f -> log.info("- {} (原因: {})", f, emptyQueryAnswerReasons.get(f)));

            // 将失败问题记录写入文件
            String timestamp = new SimpleDateFormat("yyyyMMdd-HHmmss").format(new Date());
            String failureLogPath = "emptyQueryAnswer_processing_failures_" + timestamp + ".log";
            try (PrintWriter writer = new PrintWriter(failureLogPath)) {
                failedFiles.forEach(f -> writer.println(f + " | " + emptyQueryAnswerReasons.get(f)));
            } catch (FileNotFoundException e) {
                log.error("无法写入失败日志文件", e);
            }
        }

    }

    private static boolean checkFundType(String fundCode) {
        //TODO 合并书涵编写的基金类型查询代码
        return true;
    }

    private  Map<String, String> initAllQuery() {
        FundFieldMapper fundFieldMapper = new FundFieldMapper(FundProduct.class);
        return fundFieldMapper.getQuestionKeyWordMap();
    }

    private  void writeTxt(FundProduct data, String fileName) throws IOException {
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of(fileName, data));
        Files.write(Paths.get("D:/jsonDir06/" + fileName + ".json"), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    private  void writeDb(FundProduct data) throws Exception {
        YwjqrProduct ywjqrProduct = objectMapper.convertValue(data, YwjqrProduct.class);
        productToolHandler.fundProduct(ywjqrProduct);
    }

    private  String[] callModel(FundProduct fundData) {
        log.info("基金规模是{}", FundClassifierUtil.findDouble(fundData.getNetAssetValue()));
        String content = chatClient.prompt()
                .user("基金简称是" + fundData.getFundShortName()
                        + "，基金规模是" + FundClassifierUtil.findDouble(fundData.getNetAssetValue())
                        + "，基金投资策略是" + fundData.getInvestmentStrategy()
                        + "，基金季度报告中提取的基金债券投资组合表格是" + fundData.getBondPortfolio()
                        + "\n"
                        + RagPrompt.FUND_CLASSIFIER
                )
                .call().content();
        String trim = content.split("『RESULT』")[1].split("『END』")[0].trim();
        log.info("模型返回结果是:{}", trim);
        return trim.split("\\|\\|");
    }


}
