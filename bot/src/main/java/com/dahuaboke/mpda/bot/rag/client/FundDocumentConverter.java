package com.dahuaboke.mpda.bot.rag.client;

import com.dahuaboke.mpda.client.convert.DocumentConverter;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @Desc: document转换成基金对象, 以及基金对象转换成document
 * @Author：zhh
 * @Date：2025/9/04 17:32
 */
public class FundDocumentConverter implements DocumentConverter<FundEntity> {


    @Override
    public List<FundEntity> requestConvert(List<Document> documents, EmbeddingModel embeddingModel) {
        List<FundEntity> fundEntitys = new ArrayList<>();

        for (Document document : documents) {
            FundEntity fundEntity = new FundEntity();
            // TODO embeddingModel 去做查询
            fundEntity.setEmbedding(null);
            fundEntity.setText(document.getText());
            Map<String, Object> metadata = document.getMetadata();

            fundEntity.setFileName((String) metadata.get("file_name"));
            fundEntity.setPageNumber((int) metadata.get("page_number"));
            fundEntity.setExcerptKeyWords((List<String>) metadata.get("excerpt_keywords"));
            fundEntity.setFileNameKeyWords((List<String>) metadata.get("file_name_keywords"));
            fundEntitys.add(fundEntity);
        }
        return fundEntitys;
    }


    @Override
    public List<Document> resultConvert(List<LinkedHashMap<String, Object>> result) {
        return result.stream().map(map -> {
            BigDecimal score = (BigDecimal) map.get("score");

            String id = (String) map.get("id");
            String text = (String) map.get("text");
            return Document.builder()
                    .id(id)
                    .text(text)
                    .score(score.doubleValue())
                    .metadata(map)
                    .build();
        }).collect(Collectors.toList());
    }

}
