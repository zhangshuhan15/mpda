package com.dahuaboke.mpda.tools.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

@TableName(value = "fd_intbank_netvalue")
public class FdIntbankNetvalueKey implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 登记机构号||TA登记机构
     */
    @TableField(value = "enroll_inst_no")
    private String enrollInstNo;
    /**
     * 基金产品代码
     */
    @TableField(value = "fund_prodt_no")
    private String fundProdtNo;
    /**
     * 净值公布日期
     */
    @TableField(value = "net_val_publish_date")
    private String netValPublishDate;

    /**
     * 获取登记机构号||TA登记机构
     *
     * @return enroll_inst_no - 登记机构号||TA登记机构
     */
    public String getEnrollInstNo() {
        return enrollInstNo;
    }

    /**
     * 设置登记机构号||TA登记机构
     *
     * @param enrollInstNo 登记机构号||TA登记机构
     */
    public void setEnrollInstNo(String enrollInstNo) {
        this.enrollInstNo = enrollInstNo == null ? null : enrollInstNo.trim();
    }

    /**
     * 获取基金产品代码
     *
     * @return fund_prodt_no - 基金产品代码
     */
    public String getFundProdtNo() {
        return fundProdtNo;
    }

    /**
     * 设置基金产品代码
     *
     * @param fundProdtNo 基金产品代码
     */
    public void setFundProdtNo(String fundProdtNo) {
        this.fundProdtNo = fundProdtNo == null ? null : fundProdtNo.trim();
    }

    /**
     * 获取净值公布日期
     *
     * @return net_val_publish_date - 净值公布日期
     */
    public String getNetValPublishDate() {
        return netValPublishDate;
    }

    /**
     * 设置净值公布日期
     *
     * @param netValPublishDate 净值公布日期
     */
    public void setNetValPublishDate(String netValPublishDate) {
        this.netValPublishDate = netValPublishDate == null ? null : netValPublishDate.trim();
    }


    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", enrollInstNo=" + enrollInstNo +
                ", fundProdtNo=" + fundProdtNo +
                ", netValPublishDate=" + netValPublishDate +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}