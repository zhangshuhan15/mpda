package com.dahuaboke.mpda.tools.product.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.dahuaboke.mpda.tools.product.entity.FdIntbankNetvalue;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface FdIntbankNetvalueMapper extends BaseMapper<FdIntbankNetvalue> {
    /**
     * 根据公司、产品查询净值表最新产品净值
     *
     * @param fdIntbankNetvalue
     * @return
     */
    FdIntbankNetvalue selectLatestNetvalue(FdIntbankNetvalue fdIntbankNetvalue);

    /**
     *根据公司代码,产品代码,开始时间和结束时间查询开始产品净值
     */

    /**
     * 根据产品查询净值
     *
     * @param fdFundProdtNoList
     * @return
     */
    List<FdIntbankNetvalue> selectUnitNetVal(@Param("fdFundProdtNoList") List<String> fdFundProdtNoList);

    /**
     * 查询当前日期下最新公布净值
     *
     * @param fdIntbankNetvalue
     * @return
     */
    FdIntbankNetvalue selectTodayNetvalue(FdIntbankNetvalue fdIntbankNetvalue);

    /**
     * int updateOldCurrentFlag(FdIntbankNetvalue fdNetvalueInsert);
     * <p>
     * void updateCurrentFlag(FdIntbankNetvalue netvalue);
     * <p>
     * FdIntbankNetvalue selectNetvalueByNetDate(FdIntbankNetvalue fdIntbankNetvalue);
     * <p>
     * FdIntbankNetvalue selectNetvalue1(@Param(value = "prodno")String prodno, @Param(value = "lastMonthDay")String lastMonthDay);
     * <p>
     * /**
     * 根据公司代码,产品代码,净值公布时间  倒叙排列
     */
    List<FdIntbankNetvalue> selectByNetvalue(FdIntbankNetvalue fdIntbankNetvalue);

    /**
     * 查询当前日期下最新公布净值
     *
     * @param enrollInstNo 基金公司代码
     * @param fundProdtNo  基金产品代码
     * @param netValDate   基金净值日期
     * @return
     */
    FdIntbankNetvalue selectNetvalue(@Param(value = "enrollInstNo") String enrollInstNo,
                                     @Param(value = "fundProdtNo") String fundProdtNo,
                                     @Param(value = "netValDate") String netValDate);

    FdIntbankNetvalue selectNetvalueByFundDate(FdIntbankNetvalue fdIntbankNetvalue);

    FdIntbankNetvalue selectDateNetvalueByTxdate(FdIntbankNetvalue fdIntbankNetvalue);

    List<FdIntbankNetvalue> selectTodayNetvalueLimit1(FdIntbankNetvalue fdIntbankNetvalue);

    List<FdIntbankNetvalue> selectNetvalue2();

    FdIntbankNetvalue selectNetvalueone(@Param(value = "enrollInstNo") String enrollInstNo,
                                        @Param(value = "fundProdtNo") String fundProdtNo,
                                        @Param(value = "netValDate") String netValDate);

    FdIntbankNetvalue selectNetvalueByTradeDate(FdIntbankNetvalue fdIntbankNetvalue);
}
