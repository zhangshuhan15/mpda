package com.dahuaboke.mpda.client.utils;

import com.dahuaboke.mpda.client.entity.TxHeaderReq;

/**
 * @Desc: 通用报文头构建工具类
 * @Author：zhh
 * @Date：2025/9/5 10:43
 */
public class CommonHeaderUtils {
    /**
     * 构建通用报文头
     * @param sendSysNo 发送方系统号
     * @param targetSysNo 接收方系统号
     * @param servNo 接口服务号（如RAG_V1_C014007）
     * @return 构建好的TxHeaderReq
     */
    public static TxHeaderReq build(String sendSysNo, String targetSysNo, String servNo) {
        TxHeaderReq txHeaderReq = new TxHeaderReq();
        txHeaderReq.setStartSysOrCmptNo(sendSysNo);
        txHeaderReq.setSendSysOrCmptNo(sendSysNo);
        txHeaderReq.setTargetSysOrCmptNo(targetSysNo);
        txHeaderReq.setServNo(servNo);
        // TODO 补充全局流水号
        return txHeaderReq;
    }
}
