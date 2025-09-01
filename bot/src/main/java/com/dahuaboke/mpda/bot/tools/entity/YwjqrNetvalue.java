package com.dahuaboke.mpda.bot.tools.entity;

import com.baomidou.mybatisplus.annotation.TableField;

import javax.persistence.Table;

/**
 * 基金产品信息实体类
 */
@Table(name = "ywjqr_netvalue")
public class YwjqrNetvalue {

    @TableField(value = "prodt_code")
    private String prodtCode;

    @TableField(value = "net_val_date")
    private String netValDate;

    @TableField(value = "unit_net_val")
    private String unitNetVal;

    public YwjqrNetvalue() {
    }

    public YwjqrNetvalue(String prodtCode, String netValDate, String unitNetVal) {
        this.prodtCode = prodtCode;
        this.netValDate = netValDate;
        this.unitNetVal = unitNetVal;
    }

    public String getProdtCode() {
        return prodtCode;
    }

    public void setProdtCode(String prodtCode) {
        this.prodtCode = prodtCode;
    }

    public String getNetValDate() {
        return netValDate;
    }

    public void setNetValDate(String netValDate) {
        this.netValDate = netValDate;
    }

    public String getUnitNetVal() {
        return unitNetVal;
    }

    public void setUnitNetVal(String unitNetVal) {
        this.unitNetVal = unitNetVal;
    }
}
