/*
 * Copyright 2023-2025 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dahuaboke.mpda.rag.advisor;

import com.alibaba.cloud.ai.document.DocumentWithScore;
import com.alibaba.cloud.ai.model.RerankRequest;
import com.alibaba.cloud.ai.model.RerankResponse;
import com.dahuaboke.mpda.client.RerankerClientManager;
import org.springframework.ai.chat.client.ChatClientRequest;
import org.springframework.ai.chat.client.ChatClientResponse;
import org.springframework.ai.chat.client.advisor.api.AdvisorChain;
import org.springframework.ai.chat.client.advisor.api.BaseAdvisor;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.milvus.MilvusSearchRequest;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;
import reactor.core.scheduler.Scheduler;
import reactor.core.scheduler.Schedulers;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


/**
 * @Desc: 自定义rag问答增强器
 * @Author：zhh
 * @Date：2025/7/25 1:32
 */
public class CustomQuestionAnswerAdvisor implements BaseAdvisor {

    public static final String RETRIEVED_DOCUMENTS = "qa_retrieved_documents";

    private static final PromptTemplate DEFAULT_PROMPT_TEMPLATE = new PromptTemplate("""
                 请严格根据用户查询问题，从提供的参考内容中提取最精确的答案：
            
             **用户查询（核心焦点，必须优先匹配）** \s
             "{query}"
            
             **参考内容（仅作为检索来源）** \s
             {question_answer_context}
            
             **硬性要求** \s
             1. **答案必须100%来自参考内容**，禁止任何形式的编造、推断或外部知识 \s
             2. **匹配逻辑**： \s
                - 先筛选参考内容中所有与用户查询直接相关的片段 \s
                - 选择其中语义匹配度最高的一段作为最终答案 \s
                - 若存在有多个结果(匹配度未达到绝对唯一),必须完整保留所有相关结果,按语义匹配度从高到底排序,整理输出                   
                - 根据最终答案和用户查询问题,分析结果直接输出原文格式 \s
            
             3. 根据最终答案和用户查询问题,分析结果和整理格式 ,并将最终结果放在『RESULT』和『END』之间：
                  <think>思考过程...</think>
                  『RESULT』
                    制造业占比60% ,金融业是...
                  『END』
                 \s
             4. **无答案情况**：如果参考内容中无任何相关片段，不输出内容"      
            
             5.处理示例
                  输入页面内容："报告期末按行业分类的境内股票投资组合：制造业 (60%)、金融业 (20%)..."
                 \s
                  <think>思考过程...</think>
                  『RESULT』
                     制造业占比60% ,金融业是...
                  『END』 ""\";
            注：用户查询是判断相关性的唯一标准，参考内容仅用于检索，不可影响问题理解。
            """);

    private static final int DEFAULT_ORDER = 0;

    private final VectorStore vectorStore;

    private final PromptTemplate promptTemplate;

    private final SearchRequest searchRequest;

    private final Scheduler scheduler;

    private final RerankerClientManager rerankerClientManager;

    private final int order;

    private final List<String> productName;

    private final List<String> keys;

    public CustomQuestionAnswerAdvisor(VectorStore vectorStore, RerankerClientManager rerankerClientManager, SearchRequest searchRequest, @Nullable PromptTemplate promptTemplate,
                                       @Nullable Scheduler scheduler, int order, List<String> productName, List<String> keys) {
        Assert.notNull(vectorStore, "vectorStore cannot be null");
        Assert.notNull(searchRequest, "searchRequest cannot be null");

        this.vectorStore = vectorStore;
        this.rerankerClientManager = rerankerClientManager;
        this.searchRequest = searchRequest;
        this.promptTemplate = promptTemplate != null ? promptTemplate : DEFAULT_PROMPT_TEMPLATE;
        this.scheduler = scheduler != null ? scheduler : BaseAdvisor.DEFAULT_SCHEDULER;
        this.order = order;
        this.productName = productName;
        this.keys = keys;
    }

    public static Builder builder(VectorStore vectorStore, RerankerClientManager rerankerClientManager) {
        return new Builder(vectorStore, rerankerClientManager);
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    @Override
    public ChatClientRequest before(ChatClientRequest chatClientRequest, AdvisorChain advisorChain) {
        MilvusSearchRequest milvusSearchRequest = (MilvusSearchRequest) searchRequest;
        //处理请求
        List<Document> topKDocuments = processRequest(milvusSearchRequest);
        //封装返回
        String documentContext = topKDocuments == null ? ""
                : topKDocuments.stream().map(Document::getText).collect(Collectors.joining(System.lineSeparator()));

        UserMessage userMessage = chatClientRequest.prompt().getUserMessage();

        String augmentedUserText = this.promptTemplate
                .render(Map.of("query", userMessage.getText(), "question_answer_context", documentContext));

        Map<String, Object> context = new HashMap<>(chatClientRequest.context());
        context.put(RETRIEVED_DOCUMENTS, documentContext);

        return chatClientRequest.mutate()
                .prompt(chatClientRequest.prompt().augmentUserMessage(augmentedUserText))
                .context(context)
                .build();
    }


    private List<Document> processRequest(MilvusSearchRequest milvusSearchRequest) {
        int topK = milvusSearchRequest.getTopK();
        //1. 通过关键字做精准查询
        List<Document> documents = new ArrayList<>();
        if (productName.isEmpty() && !keys.isEmpty()) {
            documents.addAll(requestKey(milvusSearchRequest));
        } else if (!productName.isEmpty() && !keys.isEmpty()) {
            documents.addAll(requestProductAndKey(milvusSearchRequest));
        }

        //2. 通过关键字并没有匹配到数据,或者匹配到的数据过少, 通过产品名称提取出来
        if (!productName.isEmpty() && (documents.isEmpty() || documents.size() < topK)) {
            List<Document> productDocs = requestProduct(milvusSearchRequest, topK * 2);
            documents.addAll(productDocs);
        }

        //3. 兜底向量查询
        if (documents.isEmpty() || documents.size() < topK) {
            documents.addAll(requestEmbedding(milvusSearchRequest, topK * 2));
        }

        //4. 查询的结果,有可能出现pdf分页情况,获取上下页防止分页场景出现
        List<Document> docContext = new ArrayList<>();
        if (!documents.isEmpty()) {
            docContext = getDocContext(documents, milvusSearchRequest);
        }

        //5. 根据Document ID去重
        Set<String> seenIds = new HashSet<>();
        ArrayList<Document> uniqueDocs = new ArrayList<>();
        Stream.of(documents, docContext)
                .flatMap(List::stream)
                .forEach(doc -> {
                    if (seenIds.add(doc.getId())) {
                        uniqueDocs.add(doc);
                    }
                });

        //5. 重排序
        List<Document> topDocs = reRankDocument(uniqueDocs, topK);

        //6. 按文件,页码整理有序,并返回
        Map<String, List<Document>> groupByFileName = topDocs.stream().collect(Collectors.groupingBy(doc -> (String) doc.getMetadata().get("file_name")));
        HashMap<String, List<Document>> sortedGroups = new HashMap<>();
        groupByFileName.forEach((filename, docs) -> {
            List<Document> sortedDocs = docs.stream().sorted(Comparator.comparingDouble(doc -> (Double) doc.getMetadata().get("page_number")))
                    .collect(Collectors.toList());
            sortedGroups.put(filename, sortedDocs);
        });
        List<Document> finalDocs = sortedGroups.values().stream().flatMap(Collection::stream).collect(Collectors.toList());
        return finalDocs;
    }

    private List<Document> getDocContext(List<Document> documents, MilvusSearchRequest milvusSearchRequest) {
        ArrayList<Document> docContext = new ArrayList<>();
        //文件名称分组
        Map<String, List<Document>> docsByFileName = documents.stream()
                .collect(Collectors.groupingBy(doc -> (String) doc.getMetadata().get("file_name")));

        for (Map.Entry<String, List<Document>> fileEntity : docsByFileName.entrySet()) {
            String fileName = fileEntity.getKey();
            List<Document> fileDocs = fileEntity.getValue();
            List<Double> pageNumbers = fileDocs.stream()
                    .map(doc -> (Double) doc.getMetadata().get("page_number"))
                    .sorted()
                    .collect(Collectors.toList());
            //获取连续段
            ArrayList<List<Double>> continuousSegments = new ArrayList<>();
            if (!pageNumbers.isEmpty()) {
                ArrayList<Double> currentSegment = new ArrayList<>();
                currentSegment.add(pageNumbers.get(0));
                for (int i = 1; i < pageNumbers.size(); i++) {
                    Double prev = pageNumbers.get(i - 1);
                    Double curr = pageNumbers.get(i);
                    if (curr - prev == 1) {
                        currentSegment.add(curr);
                    } else {
                        continuousSegments.add(currentSegment);
                        currentSegment = new ArrayList<>();
                        currentSegment.add(curr);
                    }
                }
                continuousSegments.add(currentSegment);
            }
            //根据连续段查询
            for (List<Double> segment : continuousSegments) {
                Double minPage = segment.get(0);
                Double maxPage = segment.get(segment.size() - 1);
                if (minPage > 1) {
                    List<Document> docPre = requestPage(milvusSearchRequest, fileName, minPage - 1);
                    docContext.addAll(docPre);
                }
                List<Document> docNext = requestPage(milvusSearchRequest, fileName, maxPage + 1);
                docContext.addAll(docNext);
            }
        }
        return docContext;
    }

    private List<Document> requestEmbedding(MilvusSearchRequest milvusSearchRequest, int topK) {
        MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
                .query(milvusSearchRequest.getQuery())
                .topK(topK)
                .similarityThreshold(milvusSearchRequest.getSimilarityThreshold())
                .searchParamsJson(milvusSearchRequest.getSearchParamsJson())
                .build();
        return vectorStore.similaritySearch(request);
    }

    private List<Document> requestProduct(MilvusSearchRequest milvusSearchRequest, int topK) {
        MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
                .query(milvusSearchRequest.getQuery())
                .topK(topK)
                .nativeExpression("metadata[\"file_name\"] like \"%" + productName.get(0) + "%\" ")
                .similarityThreshold(milvusSearchRequest.getSimilarityThreshold())
                .searchParamsJson(milvusSearchRequest.getSearchParamsJson())
                .build();
        return vectorStore.similaritySearch(request);
    }

    private List<Document> requestKey(MilvusSearchRequest milvusSearchRequest) {
        MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
                .query(milvusSearchRequest.getQuery())
                .topK(milvusSearchRequest.getTopK())
                .nativeExpression("metadata[\"excerpt_keywords\"] like \"%" + keys.get(0) + "%\" ")
                .similarityThreshold(milvusSearchRequest.getSimilarityThreshold())
                .searchParamsJson(milvusSearchRequest.getSearchParamsJson())
                .build();
        return vectorStore.similaritySearch(request);
    }

    private List<Document> requestProductAndKey(MilvusSearchRequest milvusSearchRequest) {
        MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
                .query(milvusSearchRequest.getQuery())
                .topK(milvusSearchRequest.getTopK())
                .nativeExpression("metadata[\"file_name\"] like \"%" + productName.get(0) + "%\" and metadata[\"excerpt_keywords\"] like \"%" + keys.get(0) + "%\"")
                .similarityThreshold(milvusSearchRequest.getSimilarityThreshold())
                .searchParamsJson(milvusSearchRequest.getSearchParamsJson())
                .build();
        return vectorStore.similaritySearch(request);
    }

    private List<Document> requestPage(MilvusSearchRequest milvusSearchRequest, String fileName, double pageNumber) {
        MilvusSearchRequest request = MilvusSearchRequest.milvusBuilder()
                .query(milvusSearchRequest.getQuery())
                .topK(milvusSearchRequest.getTopK())
                .nativeExpression("metadata[\"file_name\"] like \"%" + fileName + "%\" and metadata[\"page_number\"] == " + pageNumber + " ")
                .similarityThreshold(milvusSearchRequest.getSimilarityThreshold())
                .searchParamsJson(milvusSearchRequest.getSearchParamsJson())
                .build();
        return vectorStore.similaritySearch(request);
    }

    /**
     * 调用rerank模型对文档重排序
     *
     * @param documents
     * @param topK
     * @return
     */
    private List<Document> reRankDocument(List<Document> documents, int topK) {
        if (documents.isEmpty()) {
            return List.of();
        }
        //重排序
        RerankResponse call = rerankerClientManager.call(new RerankRequest(searchRequest.getQuery(), documents));
        List<DocumentWithScore> results = call.getResults();

        //取topK个
        return results.stream()
                .sorted(Comparator.comparingDouble(DocumentWithScore::getScore).reversed())
                .limit(topK)
                .map(DocumentWithScore::getOutput)
                .toList();

    }

    @Override
    public ChatClientResponse after(ChatClientResponse chatClientResponse, AdvisorChain advisorChain) {
        ChatResponse.Builder chatResponseBuilder;
        if (chatClientResponse.chatResponse() == null) {
            chatResponseBuilder = ChatResponse.builder();
        } else {
            chatResponseBuilder = ChatResponse.builder().from(chatClientResponse.chatResponse());
        }
        chatResponseBuilder.metadata(RETRIEVED_DOCUMENTS, chatClientResponse.context().get(RETRIEVED_DOCUMENTS));
        return ChatClientResponse.builder()
                .chatResponse(chatResponseBuilder.build())
                .context(chatClientResponse.context())
                .build();
    }


    @Override
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    public static final class Builder {

        private final VectorStore vectorStore;

        private final RerankerClientManager rerankerClientManager;

        private SearchRequest searchRequest = SearchRequest.builder().build();

        private PromptTemplate promptTemplate;

        private List<String> productName;

        private List<String> keys;

        private Scheduler scheduler;

        private int order = DEFAULT_ORDER;

        private Builder(VectorStore vectorStore, RerankerClientManager rerankerClientManager) {
            Assert.notNull(vectorStore, "The vectorStore must not be null!");
            this.rerankerClientManager = rerankerClientManager;
            this.vectorStore = vectorStore;
        }

        public Builder promptTemplate(PromptTemplate promptTemplate) {
            Assert.notNull(promptTemplate, "promptTemplate cannot be null");
            this.promptTemplate = promptTemplate;
            return this;
        }

        public Builder searchRequest(SearchRequest searchRequest) {
            Assert.notNull(searchRequest, "The searchRequest must not be null!");
            this.searchRequest = searchRequest;
            return this;
        }

        public Builder productName(List<String> productName) {
            Assert.notNull(productName, "productName cannot be null");
            this.productName = productName;
            return this;
        }

        public Builder keys(List<String> keys) {
            Assert.notNull(keys, "keys cannot be null");
            this.keys = keys;
            return this;
        }

        public Builder protectFromBlocking(boolean protectFromBlocking) {
            this.scheduler = protectFromBlocking ? BaseAdvisor.DEFAULT_SCHEDULER : Schedulers.immediate();
            return this;
        }

        public Builder scheduler(Scheduler scheduler) {
            this.scheduler = scheduler;
            return this;
        }

        public Builder order(int order) {
            this.order = order;
            return this;
        }

        public CustomQuestionAnswerAdvisor build() {
            return new CustomQuestionAnswerAdvisor(this.vectorStore, this.rerankerClientManager, this.searchRequest, this.promptTemplate, this.scheduler,
                    this.order, this.productName, this.keys);
        }

    }

}
