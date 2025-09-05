package com.dahuaboke.mpda.client.handle;

import com.dahuaboke.mpda.client.CustomClient;
import com.dahuaboke.mpda.client.constants.RagConstant;
import com.dahuaboke.mpda.client.entity.CommonReq;
import com.dahuaboke.mpda.client.entity.TxBodyReq;
import com.dahuaboke.mpda.client.entity.TxHeaderReq;
import com.dahuaboke.mpda.client.entity.req.C014007Req;
import com.dahuaboke.mpda.client.entity.resp.C014007Resp;

/**
 * @Desc: 向量接口发送
 * @Author：zhh
 * @Date：2025/9/5 9:39
 */
public class EmbeddingRequestHandle {

    private  final CustomClient customCommonClient;

    private  final String sendSysNo ;

    private  final String targetSysNo ;

    public EmbeddingRequestHandle(CustomClient customCommonClient, String sendSysNo, String targetSysNo) {
        this.customCommonClient = customCommonClient;
        this.sendSysNo = sendSysNo;
        this.targetSysNo = targetSysNo;
    }

    /**
     * 文本转向量接口发送
     *
     * @param text 文本内容
     * @return 向量
     */
    public float[] sendC014007(String text) {
        CommonReq<C014007Req> bodyReq = new CommonReq<>();
        bodyReq.setTxHeader(commonHeaderBuild());

        TxBodyReq<C014007Req> txBodyReq = new TxBodyReq<>();
        C014007Req c014007Req = new C014007Req();
        c014007Req.setText(text);
        txBodyReq.setTxEntity(c014007Req);
        bodyReq.setTxBody(txBodyReq);

        String url = RagConstant.RAG_URL + RagConstant.C014007;
        C014007Resp resp = customCommonClient.execute(url, bodyReq, C014007Resp.class);

        return resp.getVector();
    }

    /**
     * 通用头构建
     *
     * @return header
     */
    private TxHeaderReq commonHeaderBuild() {
        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo(sendSysNo);
        txHeaderReq.setSendSysOrCmptNo(sendSysNo);
        txHeaderReq.setTargetSysOrCmptNo(targetSysNo);
        txHeaderReq.setServNo(RagConstant.RAG_V1_C014007);
        //TODO 还需要全局流水号 ...
        return txHeaderReq;
    }
}
