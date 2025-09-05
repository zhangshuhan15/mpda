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

import java.util.List;

/**
 * @Desc: 新核心RAG接口发送类
 * @Author：zhh
 * @Date：2025/9/4 17:32
 */
public class RagRequestHandle {

    private final CustomClient customCommonClient;

    private  final String sendSysNo ;

    private  final String targetSysNo ;

    public RagRequestHandle(CustomClient customCommonClient, String sendSysNo, String targetSysNo) {
        this.customCommonClient = customCommonClient;
        this.sendSysNo = sendSysNo;
        this.targetSysNo = targetSysNo;
    }



    /**
     * 通用向量检索接口发送
     *
     * @param c014006Req 请求封装对象
     * @return List<Document>
     */
    public C014006Resp sendC014006(C014006Req c014006Req) {
        CommonReq<C014006Req> bodyReq = new CommonReq<>();
        bodyReq.setTxHeader(commonHeaderBuild(RagConstant.RAG_V1_C014006));

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
    public<T> C014001Resp sendC014001(String indexName, List<T> c014001Entities) {
        CommonReq<C014001Req<T>> bodyReq = new CommonReq<>();
        bodyReq.setTxHeader(commonHeaderBuild(RagConstant.RAG_V1_C014001));

        TxBodyReq<C014001Req<T>> insertReq = new TxBodyReq<>();
        C014001Req<T> c014001Req = new C014001Req<>();
        c014001Req.setDataMapList(c014001Entities);
        c014001Req.setSystemNo(sendSysNo);
        c014001Req.setIndexName(indexName);
        insertReq.setTxEntity(c014001Req);
        bodyReq.setTxBody(insertReq);

        return customCommonClient.execute(RagConstant.RAG_URL + RagConstant.C014001, bodyReq, C014001Resp.class);
    }

    /**
     * 通用头构建
     *
     * @param servNo 接口名
     * @return header
     */
    private TxHeaderReq commonHeaderBuild(String servNo) {
        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo(sendSysNo);
        txHeaderReq.setSendSysOrCmptNo(sendSysNo);
        txHeaderReq.setTargetSysOrCmptNo(targetSysNo);
        txHeaderReq.setServNo(servNo);
        //TODO 还需要全局流水号 ...
        return txHeaderReq;
    }

}
