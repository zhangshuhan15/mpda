package com.dahuaboke.mpda.tools.product.dto;

import java.util.List;

public class FilterProdInfoReq {

    //年化利率
    private String yearRita;
    //基金类型
    private List<String> fundType;
    //债券基金类型
    private String fundClassificationCode;
    //一个月最大回车率
    private String withDrawal;

    public FilterProdInfoReq() {
    }

    public FilterProdInfoReq(String yearRita, List<String> fundType, String fundClassificationCode, String withDrawal) {
        this.yearRita = yearRita;
        this.fundType = fundType;
        this.fundClassificationCode = fundClassificationCode;
        this.withDrawal = withDrawal;
    }

    public String getYearRita() {
        return yearRita;
    }

    public void setYearRita(String yearRita) {
        this.yearRita = yearRita;
    }

    public List<String> getFundType() {
        return fundType;
    }

    public void setFundType(List<String> fundType) {
        this.fundType = fundType;
    }

    public String getFundClassificationCode() {
        return fundClassificationCode;
    }

    public void setFundClassificationCode(String fundClassificationCode) {
        this.fundClassificationCode = fundClassificationCode;
    }

    public String getWithDrawal() {
        return withDrawal;
    }

    public void setWithDrawal(String withDrawal) {
        this.withDrawal = withDrawal;
    }
}
