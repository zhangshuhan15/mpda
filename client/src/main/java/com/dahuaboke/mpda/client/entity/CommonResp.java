package com.dahuaboke.mpda.client.entity;


/**
 * @Desc: 新核心接口通用返回
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class CommonResp<T> {

    private TxHeaderResp txHeader;

    private T txBody;


    public CommonResp() {
    }

    public CommonResp(TxHeaderResp txHeader, T txBody) {
        this.txHeader = txHeader;
        this.txBody = txBody;
    }

    public TxHeaderResp getTxHeader() {
        return txHeader;
    }

    public void setTxHeader(TxHeaderResp txHeader) {
        this.txHeader = txHeader;
    }

    public T getTxBody() {
        return txBody;
    }

    public void setTxBody(T txBody) {
        this.txBody = txBody;
    }

}
