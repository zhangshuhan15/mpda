package com.dahuaboke.mpda.client.entity;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Desc: 新核心接口通用请求头
 * @Author：zhh
 * @Date：2025/8/29 17:32
 */
public class TxHeaderReq {

    private static final SimpleDateFormat SDF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    /**
     * 发起系统或组件编码  11填调用方系统号
     */
    private String startSysOrCmptNo = "99711240002";

    /**
     * 发送系统或组件编码
     */
    private String sendSysOrCmptNo = "99711240002";

    /**
     * 发起渠道标识码
     */
    private String startChnlFgCd = "01";

    /**
     * 业务发起机构号
     * 总行
     */
    private String busiSendInstNo = "11005293";

    /**
     * 数据中心代码 H-合肥;F-丰台;Y-亦庄
     */
    private String dataCenterCode = "Y";

    /**
     * 交易发起时间 YYYYMMDDHHmmssSSS
     */
    private String txStartTime = SDF.format(new Date());

    /**
     * 交易发送时间
     * YYYYMMDDHHmmssSSS
     */
    private String txSendTime = SDF.format(new Date());

    /**
     * 报文总长度
     */
    private String msgrptTotalLen = "999";

    /**
     * 公共报文头长度
     */
    private String msgrptFmtVerNo = "00001";

    /**
     * 报文协议类型
     */
    private String msgAgrType = "T";

    /**
     * 公共报文头长度
     * N..10
     */
    private String pubMsgHeadLen = "0000000000";

    /**
     * 嵌入报文长度  10
     * N..10
     */
    private String embedMsgrptLen = "0000000000";

    /**
     * 目标系统或组件编码 11
     */
    private String targetSysOrCmptNo = "99900150000";

    /**
     * 服务类型代码
     */
    private String servTpCd = "1";

    /**
     * 服务编码   调用哪个就用哪个 ,例如: rag_v1_C014007
     */
    private String servNo;

    /**
     * 服务版本号
     */
    private String servVerNo = "00001";

    /**
     * 报文鉴别码
     * ANS..32
     */
    private String msgrptMac = "00000000000000000000000000000000";

    /**
     * 全局业务跟踪号
     * 参考《QPSBC 0052—2020 中国邮政储蓄银行信息系统全局业务跟踪号标准》
     * 系统号（11）+时间戳（13）+8位随机数
     * 32
     */
    private String globalBusiTrackNo = "20220817000123456000000000001411";

    /**
     * 子交易序号
     */
    private String subtxNo = "10000000000000000000000000000001";

    /**
     * 子交易序号
     */
    private String reqSysSriNo = "2021077317321202021200123";

    private String resvedInputInfo = "info";

    private String testGroup = "zhm";

    public TxHeaderReq(String startSysOrCmptNo, String sendSysOrCmptNo, String startChnlFgCd, String busiSendInstNo, String dataCenterCode, String txStartTime, String txSendTime, String msgrptTotalLen, String msgrptFmtVerNo, String msgAgrType, String pubMsgHeadLen, String embedMsgrptLen, String targetSysOrCmptNo, String servTpCd, String servNo, String servVerNo, String msgrptMac, String globalBusiTrackNo, String subtxNo, String reqSysSriNo, String resvedInputInfo, String testGroup) {
        this.startSysOrCmptNo = startSysOrCmptNo;
        this.sendSysOrCmptNo = sendSysOrCmptNo;
        this.startChnlFgCd = startChnlFgCd;
        this.busiSendInstNo = busiSendInstNo;
        this.dataCenterCode = dataCenterCode;
        this.txStartTime = txStartTime;
        this.txSendTime = txSendTime;
        this.msgrptTotalLen = msgrptTotalLen;
        this.msgrptFmtVerNo = msgrptFmtVerNo;
        this.msgAgrType = msgAgrType;
        this.pubMsgHeadLen = pubMsgHeadLen;
        this.embedMsgrptLen = embedMsgrptLen;
        this.targetSysOrCmptNo = targetSysOrCmptNo;
        this.servTpCd = servTpCd;
        this.servNo = servNo;
        this.servVerNo = servVerNo;
        this.msgrptMac = msgrptMac;
        this.globalBusiTrackNo = globalBusiTrackNo;
        this.subtxNo = subtxNo;
        this.reqSysSriNo = reqSysSriNo;
        this.resvedInputInfo = resvedInputInfo;
        this.testGroup = testGroup;
    }

    public TxHeaderReq() {
    }

    public String getStartSysOrCmptNo() {
        return startSysOrCmptNo;
    }

    public void setStartSysOrCmptNo(String startSysOrCmptNo) {
        this.startSysOrCmptNo = startSysOrCmptNo;
    }

    public String getSendSysOrCmptNo() {
        return sendSysOrCmptNo;
    }

    public void setSendSysOrCmptNo(String sendSysOrCmptNo) {
        this.sendSysOrCmptNo = sendSysOrCmptNo;
    }

    public String getStartChnlFgCd() {
        return startChnlFgCd;
    }

    public void setStartChnlFgCd(String startChnlFgCd) {
        this.startChnlFgCd = startChnlFgCd;
    }

    public String getBusiSendInstNo() {
        return busiSendInstNo;
    }

    public void setBusiSendInstNo(String busiSendInstNo) {
        this.busiSendInstNo = busiSendInstNo;
    }

    public String getDataCenterCode() {
        return dataCenterCode;
    }

    public void setDataCenterCode(String dataCenterCode) {
        this.dataCenterCode = dataCenterCode;
    }

    public String getTxStartTime() {
        return txStartTime;
    }

    public void setTxStartTime(String txStartTime) {
        this.txStartTime = txStartTime;
    }

    public String getTxSendTime() {
        return txSendTime;
    }

    public void setTxSendTime(String txSendTime) {
        this.txSendTime = txSendTime;
    }

    public String getMsgrptTotalLen() {
        return msgrptTotalLen;
    }

    public void setMsgrptTotalLen(String msgrptTotalLen) {
        this.msgrptTotalLen = msgrptTotalLen;
    }

    public String getMsgrptFmtVerNo() {
        return msgrptFmtVerNo;
    }

    public void setMsgrptFmtVerNo(String msgrptFmtVerNo) {
        this.msgrptFmtVerNo = msgrptFmtVerNo;
    }

    public String getMsgAgrType() {
        return msgAgrType;
    }

    public void setMsgAgrType(String msgAgrType) {
        this.msgAgrType = msgAgrType;
    }

    public String getPubMsgHeadLen() {
        return pubMsgHeadLen;
    }

    public void setPubMsgHeadLen(String pubMsgHeadLen) {
        this.pubMsgHeadLen = pubMsgHeadLen;
    }

    public String getEmbedMsgrptLen() {
        return embedMsgrptLen;
    }

    public void setEmbedMsgrptLen(String embedMsgrptLen) {
        this.embedMsgrptLen = embedMsgrptLen;
    }

    public String getTargetSysOrCmptNo() {
        return targetSysOrCmptNo;
    }

    public void setTargetSysOrCmptNo(String targetSysOrCmptNo) {
        this.targetSysOrCmptNo = targetSysOrCmptNo;
    }

    public String getServTpCd() {
        return servTpCd;
    }

    public void setServTpCd(String servTpCd) {
        this.servTpCd = servTpCd;
    }

    public String getServNo() {
        return servNo;
    }

    public void setServNo(String servNo) {
        this.servNo = servNo;
    }

    public String getServVerNo() {
        return servVerNo;
    }

    public void setServVerNo(String servVerNo) {
        this.servVerNo = servVerNo;
    }

    public String getMsgrptMac() {
        return msgrptMac;
    }

    public void setMsgrptMac(String msgrptMac) {
        this.msgrptMac = msgrptMac;
    }

    public String getGlobalBusiTrackNo() {
        return globalBusiTrackNo;
    }

    public void setGlobalBusiTrackNo(String globalBusiTrackNo) {
        this.globalBusiTrackNo = globalBusiTrackNo;
    }

    public String getSubtxNo() {
        return subtxNo;
    }

    public void setSubtxNo(String subtxNo) {
        this.subtxNo = subtxNo;
    }

    public String getReqSysSriNo() {
        return reqSysSriNo;
    }

    public void setReqSysSriNo(String reqSysSriNo) {
        this.reqSysSriNo = reqSysSriNo;
    }

    public String getResvedInputInfo() {
        return resvedInputInfo;
    }

    public void setResvedInputInfo(String resvedInputInfo) {
        this.resvedInputInfo = resvedInputInfo;
    }

    public String getTestGroup() {
        return testGroup;
    }

    public void setTestGroup(String testGroup) {
        this.testGroup = testGroup;
    }

}
