package com.dahuaboke.mpda.client.entity;


/**
 * @Desc: 新核心接口通用请求
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class CommonReq {

    private TxHeaderReq txHeader;

    private TxBodyReq txBody;

    public CommonReq(TxHeaderReq txHeader, TxBodyReq txBody) {
        this.txHeader = txHeader;
        this.txBody = txBody;
    }

    public CommonReq() {
    }

    public TxHeaderReq getTxHeader() {
        return txHeader;
    }

    public void setTxHeader(TxHeaderReq txHeader) {
        this.txHeader = txHeader;
    }

    public TxBodyReq getTxBody() {
        return txBody;
    }

    public void setTxBody(TxBodyReq txBody) {
        this.txBody = txBody;
    }

}

