package com.dahuaboke.mpda.tools.product.dto;

public class NetValReq {

    private String prodtCode;

    private String begDate;

    private String endDate;

    public NetValReq() {
    }

    public NetValReq(String prodtCode) {
        this.prodtCode = prodtCode;
    }

    public NetValReq(String prodtCode, String begDate, String endDate) {
        this.prodtCode = prodtCode;
        this.begDate = begDate;
        this.endDate = endDate;
    }

    public String getProdtCode() {
        return prodtCode;
    }

    public void setProdtCode(String prodtCode) {
        this.prodtCode = prodtCode;
    }

    public String getBegDate() {
        return begDate;
    }

    public void setBegDate(String begDate) {
        this.begDate = begDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }
}
