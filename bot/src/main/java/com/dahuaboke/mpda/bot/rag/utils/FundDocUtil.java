package com.dahuaboke.mpda.bot.rag.utils;

import com.dahuaboke.mpda.bot.rag.FundProduct;
import com.dahuaboke.mpda.bot.rag.advisor.QuestionAnswerAdvisor;
import com.dahuaboke.mpda.bot.rag.handler.DocContextHandler;
import com.dahuaboke.mpda.bot.rag.handler.SearchHandler;
import com.dahuaboke.mpda.bot.rag.handler.SortHandler;
import com.dahuaboke.mpda.bot.tools.ProductToolHandler;
import com.dahuaboke.mpda.bot.tools.entity.YwjqrProduct;
import com.dahuaboke.mpda.core.rag.enricher.KeywordEnricher;
import com.dahuaboke.mpda.core.rag.enricher.SummaryEnricher;
import com.dahuaboke.mpda.core.rag.entity.FundFieldMapper;
import com.dahuaboke.mpda.core.rag.handler.EmbeddingHandler;
import com.dahuaboke.mpda.core.rag.handler.RerankHandler;
import com.dahuaboke.mpda.core.utils.SpringUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.milvus.MilvusVectorStore;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.core.io.support.ResourcePatternResolver;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.text.SimpleDateFormat;
import java.util.*;

public class FundDocUtil {

    private static final Logger log = LoggerFactory.getLogger(FundDocUtil.class);
    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
             .基于文档,所需任务要求:
            
             **关键词检测：检查页面是否包含以下字段 (若存在则提取，否则留空)** \s
             {keyWords}
            
             1. 只提取"关键词检测"中定义的字符串,不要对应的值, 并且不要自动生成关键词：若未检测到上述字段，留空，不要产生不存在"关键词检测"的关键字。
            
             2.请将最终结果放在『RESULT』和『END』之间：
             <think>思考过程...</think>
             『RESULT』
             关键词1,关键词2,关键词3...
             『END』
            
            
             3.处理示例
             输入页面内容：报告期末按行业分类的境内股票投资组合：制造业 (60%)、金融业 (20%)...
            
             <think>思考过程...</think>
             『RESULT』
             报告期末按行业分类的境内股票投资组合
             『END』
            """);
    private static final MilvusVectorStore vectorStore;
    private static final ChatModel chatModel;
    private static final ChatClient chatClient;
    private static final String pdfFilePath;
    private static final ProductToolHandler productToolHandler;
    private static final ObjectMapper objectMapper;
    private static final SearchHandler searchHandler;
    private static final EmbeddingHandler embeddingHandler;
    private static final SortHandler sortHandler;
    private static final DocContextHandler docContextHandler;
    private static final RerankHandler rerankHandler;

    static {
        vectorStore = SpringUtil.getBean(MilvusVectorStore.class);
        chatModel = SpringUtil.getBean(ChatModel.class);
        chatClient = SpringUtil.getBean(ChatClient.class);
        pdfFilePath = SpringUtil.getProperty("pdfFilePath");
        productToolHandler = SpringUtil.getBean(ProductToolHandler.class);
        objectMapper = SpringUtil.getBean(ObjectMapper.class);
        searchHandler = SpringUtil.getBean(SearchHandler.class);
        embeddingHandler = SpringUtil.getBean(EmbeddingHandler.class);
        sortHandler = SpringUtil.getBean(SortHandler.class);
        docContextHandler = SpringUtil.getBean(DocContextHandler.class);
        rerankHandler = SpringUtil.getBean(RerankHandler.class);
    }

    private static String getPrompt() {

        List<String> keyWords = getKeyWords();
        return DEFAULT_PROMPT_TEMPLATE
                .render(Map.of("keyWords", keyWords.toString()));
    }

    /**
     * 批量将pdf插入向量数据库
     *
     * @param paths 文件路径
     * @throws IOException
     */
    public static void batchProcessDocs(List<String> paths) throws IOException {
        ArrayList<Resource> allResources = new ArrayList<>();
        for (String path : paths) {
            if (path.startsWith("http://") || path.startsWith("https://")) {
                allResources.add(new UrlResource(path));
            } else {
                //相对路径转为file:前缀
                if (!path.startsWith("file:") && !path.startsWith("/")) {
                    path = "file:" + path;
                }
                ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
                Resource[] resources = resolver.getResources(path);
                allResources.addAll(Arrays.asList(resources));
            }
        }

        batchProcessDocs(allResources.toArray(new Resource[0]));
    }

    /**
     * 批量将pdf插入向量数据库
     *
     * @param resources 文件来源
     */
    public static void batchProcessDocs(Resource[] resources) {
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

                // 使用try-with-resources确保PDF资源释放
                try (InputStream is = resource.getInputStream()) {
                    PagePdfDocumentReader pdfReader = new PagePdfDocumentReader(resource,
                            PdfDocumentReaderConfig.builder()
                                    .withPageTopMargin(70)
                                    .withPageBottomMargin(70)
                                    .withPageExtractedTextFormatter(ExtractedTextFormatter.builder()
                                            .withNumberOfTopTextLinesToDelete(3)
                                            .withNumberOfBottomTextLinesToDelete(3)
                                            .build())
                                    .withPagesPerDocument(1)
                                    .build());

                    List<Document> docs = pdfReader.read();
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
                }
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

    /**
     * 获得基金产品
     *
     * @return
     */
    public static Map<String, String> getProductMap() {
        Map<String, String> mapRelation = new HashMap<>();
        ResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        Resource[] resources = null;
        try {
            resources = resolver.getResources(pdfFilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        for (Resource resource : resources) {
            String filename = resource.getFilename();
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
        }
        return mapRelation;
    }

    /**
     * 获得关键字
     *
     * @return
     */
    public static List<String> getKeyWords() {

        return Arrays.asList(
                "基金代码", "基金全称", "季报时间", "基金经理名字", "基金经理任职日期",
                "基金经理证券从业年限", "基金经理说明",
                "报告期末按行业分类的境内股票投资组合",
                "报告期末按公允价值占基金资产净值比例大小排序的前十名股票投资明细",
                "报告期末按债券品种分类的债券投资组合",
                "报告期末按公允价值占基金资产净值比例大小排序的前五名债券投资明细",
                "报告期末基金资产组合情况",
                "本报告期基金份额净值增长率及其与同期业绩比较基准收益率的比较",
                "期末基金资产净值", "报告期期末基金份额总额", "基金管理人",
                "基金托管人", "运作方式", "开放频率", "基金合同生效日",
                "基金经理开始担任本基金基金经理的日期", "基金经理证券从业日期",
                "投资目标", "投资范围", "主要投资策略", "业绩比较基准", "风险收益特征",
                "基金销售相关费用申购费", "基金销售相关费用赎回费", "基金运作相关费用管理费",
                "基金运作相关费用托管费", "基金运作相关费用销售服务费",
                "基金运作相关费用审计费用", "基金运作相关费用信息披露费",
                "基金运作相关费用其他费用", "基金运作综合费率（年化）"
        );
    }


    /**
     * rag检索,检索结果保存到pqsql
     *
     * @param productMap key: fundCode value: fundName
     * @throws IOException
     */
    public static void selectDoc(Map<String, String> productMap) throws IOException {

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
        return true;
        /*GetFundTypeReq getFundTypeReq = new GetFundTypeReq();
        getFundTypeReq.setFundCode(fundCode);
        GetFundTypeResp getFundTypeResp = HttpUtil.fundTypeRequest(getFundTypeReq);
        String fundType = getFundTypeResp.getFundType();
        log.info(fundCode + "查询出来的基金类型是" + fundType);
        if("".equals(fundType) || "2".equals(fundType) || "5".equals(fundType) || "6".equals(fundType)){
            return true;
        }else {
            return false;
        }*/
    }


    private static Map<String, String> initAllQuery() {
        FundFieldMapper fundFieldMapper = new FundFieldMapper(FundProduct.class);
        return fundFieldMapper.getQuestionKeyWordMap();
    }

    private static void writeTxt(FundProduct data, String fileName) throws IOException {
        String json = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(Map.of(fileName, data));
        Files.write(Paths.get("D:/jsonDir06/" + fileName + ".json"), json.getBytes(StandardCharsets.UTF_8), StandardOpenOption.CREATE);
    }

    private static void writeDb(FundProduct data) throws Exception {
        YwjqrProduct ywjqrProduct = objectMapper.convertValue(data, YwjqrProduct.class);
        productToolHandler.fundProduct(ywjqrProduct);
    }

    private static String[] callModel(FundProduct fundData) {
        String prompt = """
                .以上是基金简称，基金规模，基金投资策略，基金季度报告中提取的基金债券投资组合表格
                1. 总共有五个判断条件,你需要严格依赖顺序,分析基金分类,规则需依次校验,前一个判断成功(满足),才执行下一个,若前一个失败(不满足),则直接中止后续判断,计算基金分类规则如下,:
                    （1）第一步: 基础筛选(不满足则直接过滤,不进入后续判断)
                         筛选基金规模大于等于10亿元的产品:
                         - 若满足(规模>=10亿): 进入第二步判断;
                         - 若不满足(规模<10亿): 直接返回无,终止分类流程
                    （2）第二步: 信用债基金/利率债基金判定(第一步满足才执行,不满足则终止)
                        判断基金为信用债基金或利率债基金,需依据基金季度报告中提取的基金债券投资组合表格(格式如下),计算关键占比：
                
                            序号	债券品种	金额	占基金资产净值比例（%）
                            1	国家债券	/	a
                            2	央行票据	/	b
                            3	金融债券	/	c
                                其中：政策性金融债	/	d
                            4	企业债券	/	e
                            5	企业短期融资券	/	f
                            6	中期票据	/	g
                            7	可转债（可交换债）	/	h
                            8	同业存单	/	i
                            9	其他	/	j
                            10	合计	/	k
                        关键占比公式:
                            - 利率债持仓占比=国家债券(a)+央行票据(b)+  金融债券中政策性金融债(d) -> 既 a+b+d
                            - 信用债持仓占比=企业债券(e)+企业短期融资券(f)+中期票据(g)+金融债券中非政策性金融债（金融债券(c)-政策性金融债(d)) -> 既 e+f+g+c-d
                        判定逻辑:    
                            - 若利率债持仓占比≥80%,则为利率债基金,进入第三步判断；
                            - 若信用债持仓占比≥80%,则为信用债基金,进入第四步判断；
                            - 若两者占比<80%，直接返回无,终止分类流程。
                    （3）第三步: 利率债基金投资风格筛选(第二步满足利率债基金才执行,不满足则终止)
                        对筛选出的利率债基金,分析基金投资策略，判定逻辑：
                        - 必须包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。 返回利率债/利率债主动-开放式,终止分类流程.
                        - 必须不包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。必须包含关键词：“复制”、“复刻”、“跟踪”、“成份券” 其中之一时。则投资风格为被动型,进入第五步判断；
                        - 必须不包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。必须不包含关键词：“复制”、“复刻”、“跟踪”、“成份券” 其中之一时。返回利率债,终止分类流程.
                    （4）第四步: 信用债基金投资风格筛选(第二步满足信用债基金才执行,不满足则终止)
                        对筛选出的信用债基金,分析基金投资策略，判定逻辑：
                        - 必须包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。 返回信用债/信用债主动-开放式,终止分类流程.
                        - 必须不包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。必须包含关键词：“复制”、“复刻”、“跟踪”、“成份券” 其中之一时。返回信用债/信用债-指数型,终止分类流程；
                        - 必须不包含关键词：“灵活”、“超额收益”、“动态”、“高于基准”、“积极”、“主动”、“增强” 其中之一时。必须不包含关键词：“复制”、“复刻”、“跟踪”、“成份券” 其中之一时。返回信用债,终止分类流程. 
                     (5）第五步: 被动型利率债期限分类(第三步满足投资风格被动性才执行,不满足则中止)
                        对于投资风格为被动型的利率债基金，分析基金简称，按以下规则细分：
                        - 基金简称中包含“1-3”时，分类为“利率债指数1-3年”,返回利率债/利率债-被动式/利率债指数1-3年，终止流程
                        - 基金简称中包含“3-5”时，分类为“利率债指数3-5年”,返回利率债/利率债-被动式/利率债指数3-5年，终止流程
                        - 基金简称中包含“1-5”时，分类为“利率债指数1-5年”,返回利率债/利率债-被动式/利率债指数1-5年，终止流程
                        - 若均不包含,返回利率债/利率债-被动式,终止流程
                2. 计算后的结果只有10种,分别为利率债,利率债/利率债主动-开放式,利率债/利率债-被动式,利率债/利率债-被动式/利率债指数1-5年,利率债/利率债-被动式/利率债指数3-5年,利率债/利率债-被动式/利率债指数1-3年,信用债,信用债/信用债主动-开放式,信用债/信用债-指数型,无 .必须从这十种选一个作为结果,以及选择的原因               
                3. 根据最终答案和用户查询问题,分析结果和整理格式 ,并将最终结果放在『RESULT』和『END』之间：
                	  <think>思考过程...</think>
                	  『RESULT』
                		 结果 || 原因
                	  『END』
                
                4. 处理示例
                      输入页面内容："基金全称是货币基金A,期末基金资产净值是123023.12,基金投资策略是主动形,基金季度报告中提取的基金债券投资组合表格是..."
                     \s
                      <think>思考过程...</think>
                      『RESULT』
                         利率债指数1-5年  || 原因是...
                      『END』 ""\";\s
                """;
        log.info("基金规模是" + FundClassifierUtil.findDouble(fundData.getNetAssetValue()));
        String content = chatClient.prompt()
                .user("基金简称是" + fundData.getFundShortName()
                        + "，基金规模是" + FundClassifierUtil.findDouble(fundData.getNetAssetValue())
                        + "，基金投资策略是" + fundData.getInvestmentStrategy()
                        + "，基金季度报告中提取的基金债券投资组合表格是" + fundData.getBondPortfolio()
                        + "\n"
                        + prompt
                )
                .call().content();
        String trim = content.split("『RESULT』")[1].split("『END』")[0].trim();
        log.info("模型返回结果是:" + trim);
        return trim.split("\\|\\|");
    }
}
