package com.dahuaboke.mpda.tools.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName(value = "fd_intbank_kindpara")
public class FdIntbankKindparaKey implements Serializable {


    private static final long serialVersionUID = 1L;
    /**
     * 基金产品编码	||基金产品编码
     */
    @TableId
    @TableField(value = "fund_prodt_no")
    private String fundProdtNo;

    public FdIntbankKindparaKey() {
    }

    public FdIntbankKindparaKey(String fundProdtNo) {
        this.fundProdtNo = fundProdtNo;
    }

    /**
     * 获取基金产品编码	||基金产品编码
     *
     * @return fund_prodt_no - 基金产品编码	||基金产品编码
     */
    public String getFundProdtNo() {
        return fundProdtNo;
    }

    /**
     * 设置基金产品编码	||基金产品编码
     *
     * @param fundProdtNo 基金产品编码	||基金产品编码
     */
    public void setFundProdtNo(String fundProdtNo) {
        this.fundProdtNo = fundProdtNo == null ? null : fundProdtNo.trim();
    }


    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", fundProdtNo=" + fundProdtNo +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }


}