package com.dahuaboke.mpda.client;


import com.dahuaboke.mpda.client.constants.RagConstant;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * @Desc: 新核心客户端向量库配置类
 * @Author：zhh
 * @Date：2025/9/5 10:37
 */
@ConfigurationProperties(prefix = RagConstant.VECTOR_STORE)
public class ClientVectorStoreProperties {

    /**
     * 索引名(表名)
     */
    private String collectionName;


    /**
     * 需要向量查询的字段
     */
    private String vectorFieldName;

    public ClientVectorStoreProperties() {
        this.collectionName = "collections";
        this.vectorFieldName = "embedding";
    }

    public String getCollectionName() {
        return collectionName;
    }

    public void setCollectionName(String collectionName) {
        this.collectionName = collectionName;
    }

    public String getVectorFieldName() {
        return vectorFieldName;
    }

    public void setVectorFieldName(String vectorFieldName) {
        this.vectorFieldName = vectorFieldName;
    }
}
