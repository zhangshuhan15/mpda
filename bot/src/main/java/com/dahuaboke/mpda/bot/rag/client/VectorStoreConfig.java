package com.dahuaboke.mpda.bot.rag.client;

import com.dahuaboke.mpda.bot.rag.constants.FundConstant;
import com.dahuaboke.mpda.client.CustomVectorStore;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

/**
 * @Desc: 引入新核心接口的向量数据库对象
 * @Author：zhh
 * @Date：2025/9/4 20:16
 */
@Component
public class VectorStoreConfig {

    @Bean
    public CustomVectorStore customVectorStore(EmbeddingModel embeddingModel, VectorStoreRequestHandle vectorStoreRequestHandle){
        return CustomVectorStore.builder(embeddingModel,vectorStoreRequestHandle)
                .collectionName(FundConstant.INDEX_NAME)
                .vectorFieldName(FundConstant.EMBEDDING)
                .sendSysNo(FundConstant.LC_SYSTEM)
                .targetSysNo(FundConstant.LC_SYSTEM)
                .converter(new FundDocumentConverter())
                .build();
    }

}
