package com.dahuaboke.mpda.bot.rag.service;

import com.alibaba.fastjson.JSONObject;
import com.dahuaboke.mpda.bot.rag.client.FundEntity;
import com.dahuaboke.mpda.bot.rag.constants.FundConstant;
import com.dahuaboke.mpda.client.entity.resp.C014005Resp;
import com.dahuaboke.mpda.client.handle.VectorStoreRequestHandle;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Desc: 文档删除服务
 * @Author：zhh
 * @Date：2025/9/9 11:12
 */
@Component
public class DocumentDeleteService {
    @Autowired
    private VectorStore vectorStore;

    @Autowired
    private VectorStoreRequestHandle requestHandle;

    public void doAllDel() {
        C014005Resp c014005Resp = requestHandle.sendC014005(FundConstant.INDEX_NAME, Map.of(), Map.of());
        List resultMap = c014005Resp.getResultMap();
        ArrayList<FundEntity> fundEntities = new ArrayList<>();
        for (Object obj : resultMap) {
            String jsonStr = JSONObject.toJSONString(obj);
            FundEntity fundEntity = JSONObject.parseObject(jsonStr, FundEntity.class);
            fundEntities.add(fundEntity);
        }
        List<String> idList = fundEntities.stream().map(FundEntity::getId).toList();
        vectorStore.delete(idList);
    }
}
