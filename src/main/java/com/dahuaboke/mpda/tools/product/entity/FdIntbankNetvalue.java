package com.dahuaboke.mpda.tools.product.entity;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.math.BigDecimal;

//同业基金净值参数表
@TableName(value = "fd_intbank_netvalue")
public class FdIntbankNetvalue extends FdIntbankNetvalueKey implements Serializable {
    private static final long serialVersionUID = 1L;
    /**
     * 净值日期
     */
    @TableField(value = "net_val_date")
    private String netValDate;
    /**
     * 当前净值标识代码||0-历史 1-当前
     */
    @TableField(value = "cur_net_val_flag_code")
    private String curNetValFlagCode;
    /**
     * 单位净值
     */
    @TableField(value = "unit_net_val")
    private BigDecimal unitNetVal;
    /**
     * 万份单位收益||万份单位收益
     */
    @TableField(value = "curr_fund_thou_cop_profit")
    private BigDecimal currFundThouCopProfit;
    /**
     * 近七日年化收益率||近七日年化收益率
     */
    @TableField(value = "ndd7_yearly_profrat")
    private BigDecimal ndd7YearlyProfrat;
    /**
     * 最后修改时间戳||最后修改时间戳
     */
    @TableField(value = "last_mod_stamp")
    private String lastModStamp;

    /**
     * 获取净值日期
     *
     * @return net_val_date - 净值日期
     */
    public String getNetValDate() {
        return netValDate;
    }

    /**
     * 设置净值日期
     *
     * @param netValDate 净值日期
     */
    public void setNetValDate(String netValDate) {
        this.netValDate = netValDate == null ? null : netValDate.trim();
    }

    /**
     * 获取当前净值标识代码||0-历史 1-当前
     *
     * @return cur_net_val_flag_code - 当前净值标识代码||0-历史 1-当前
     */
    public String getCurNetValFlagCode() {
        return curNetValFlagCode;
    }

    /**
     * 设置当前净值标识代码||0-历史 1-当前
     *
     * @param curNetValFlagCode 当前净值标识代码||0-历史 1-当前
     */
    public void setCurNetValFlagCode(String curNetValFlagCode) {
        this.curNetValFlagCode = curNetValFlagCode == null ? null : curNetValFlagCode.trim();
    }

    /**
     * 获取单位净值
     *
     * @return unit_net_val - 单位净值
     */
    public BigDecimal getUnitNetVal() {
        return unitNetVal;
    }

    /**
     * 设置单位净值
     *
     * @param unitNetVal 单位净值
     */
    public void setUnitNetVal(BigDecimal unitNetVal) {
        this.unitNetVal = unitNetVal;
    }

    /**
     * 获取万份单位收益||万份单位收益
     *
     * @return curr_fund_thou_cop_profit - 万份单位收益||万份单位收益
     */
    public BigDecimal getCurrFundThouCopProfit() {
        return currFundThouCopProfit;
    }

    /**
     * 设置万份单位收益||万份单位收益
     *
     * @param currFundThouCopProfit 万份单位收益||万份单位收益
     */
    public void setCurrFundThouCopProfit(BigDecimal currFundThouCopProfit) {
        this.currFundThouCopProfit = currFundThouCopProfit;
    }

    /**
     * 获取近七日年化收益率||近七日年化收益率
     *
     * @return ndd7_yearly_profrat - 近七日年化收益率||近七日年化收益率
     */
    public BigDecimal getNdd7YearlyProfrat() {
        return ndd7YearlyProfrat;
    }

    /**
     * 设置近七日年化收益率||近七日年化收益率
     *
     * @param ndd7YearlyProfrat 近七日年化收益率||近七日年化收益率
     */
    public void setNdd7YearlyProfrat(BigDecimal ndd7YearlyProfrat) {
        this.ndd7YearlyProfrat = ndd7YearlyProfrat;
    }

    /**
     * 获取最后修改时间戳||最后修改时间戳
     *
     * @return last_mod_stamp - 最后修改时间戳||最后修改时间戳
     */
    public String getLastModStamp() {
        return lastModStamp;
    }

    /**
     * 设置最后修改时间戳||最后修改时间戳
     *
     * @param lastModStamp 最后修改时间戳||最后修改时间戳
     */
    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp == null ? null : lastModStamp.trim();
    }


    @Override
    public String toString() {
        String sb = getClass().getSimpleName() +
                " [" +
                "Hash = " + hashCode() +
                ", netValDate=" + netValDate +
                ", curNetValFlagCode=" + curNetValFlagCode +
                ", unitNetVal=" + unitNetVal +
                ", currFundThouCopProfit=" + currFundThouCopProfit +
                ", ndd7YearlyProfrat=" + ndd7YearlyProfrat +
                ", lastModStamp=" + lastModStamp +
                ", serialVersionUID=" + serialVersionUID +
                "]";
        return sb;
    }
}