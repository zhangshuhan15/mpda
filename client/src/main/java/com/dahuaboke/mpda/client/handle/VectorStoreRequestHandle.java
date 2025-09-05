package com.dahuaboke.mpda.client.handle;

import com.dahuaboke.mpda.client.CustomClient;
import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.dahuaboke.mpda.client.entity.req.C014001Req;
import com.dahuaboke.mpda.client.entity.req.C014006Req;
import com.dahuaboke.mpda.client.entity.resp.C014001Resp;
import com.dahuaboke.mpda.client.entity.resp.C014006Resp;
import com.dahuaboke.mpda.client.utils.CommonHeaderUtils;

import java.util.List;

/**
 * @Desc: 数据库接口发送
 * @Author：zhh
 * @Date：2025/9/4 17:32
 */
public class VectorStoreRequestHandle {

    private final CustomClient customCommonClient;

    private final CoreClientProperties coreClientProperties;


    public VectorStoreRequestHandle(CustomClient customCommonClient, CoreClientProperties coreClientProperties) {
        this.customCommonClient = customCommonClient;
        this.coreClientProperties = coreClientProperties;
    }

    /**
     * 通用向量检索接口发送
     *
     * @param c014006Req 请求封装对象
     * @return List<Document>
     */
    public C014006Resp sendC014006(C014006Req c014006Req) {
        c014006Req.setSystemNo(coreClientProperties.getSendSysNo());
        CommonReq<C014006Req> bodyReq = new CommonReq<>();
        TxHeaderReq headerReq = CommonHeaderUtils.build(coreClientProperties.getSendSysNo(),
                coreClientProperties.getTargetSysNo(), RagConstant.RAG_V1_C014006);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014006Req> txBodyReq = new TxBodyReq<>();
        txBodyReq.setTxEntity(c014006Req);
        bodyReq.setTxBody(txBodyReq);

        return customCommonClient.execute(RagConstant.RAG_URL + RagConstant.C014006, bodyReq, C014006Resp.class);

    }

    /**
     * 通用数据批量写入接口发送
     *
     * @param indexName       索引名(表名)
     * @param c014001Entities 数据集合
     * @return 返回对象
     */
    public <T> C014001Resp sendC014001(String indexName, List<T> c014001Entities) {
        CommonReq<C014001Req<T>> bodyReq = new CommonReq<>();
        String sendSysNo = coreClientProperties.getSendSysNo();
        TxHeaderReq headerReq = CommonHeaderUtils.build(sendSysNo,
                coreClientProperties.getTargetSysNo(), RagConstant.RAG_V1_C014001);
        bodyReq.setTxHeader(headerReq);

        TxBodyReq<C014001Req<T>> insertReq = new TxBodyReq<>();
        C014001Req<T> c014001Req = new C014001Req<>();
        c014001Req.setDataMapList(c014001Entities);
        c014001Req.setSystemNo(sendSysNo);
        c014001Req.setIndexName(indexName);
        insertReq.setTxEntity(c014001Req);
        bodyReq.setTxBody(insertReq);

        return customCommonClient.execute(RagConstant.RAG_URL + RagConstant.C014001, bodyReq, C014001Resp.class);
    }


}
