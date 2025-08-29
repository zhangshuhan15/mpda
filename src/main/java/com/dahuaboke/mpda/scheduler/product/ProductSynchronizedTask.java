package com.dahuaboke.mpda.scheduler.product;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dahuaboke.mpda.tools.product.dao.YwjqrIntbankKindparaMapper;
import com.dahuaboke.mpda.tools.product.entity.YwjqrIntbankKindpara;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * auth: liguangdong
 * time: 2025/8/27 09:59
 */
@Component
public class ProductSynchronizedTask {

    @Autowired
    @Lazy
    private YwjqrIntbankKindparaMapper ywjqrIntbankKindparaMapper;

    @Scheduled(fixedRate = 500000) // TODO 改成配置文件读取
    public void scheduledTask() {
        System.out.println("定时任务执行：每隔 5 秒执行一次"); // TODO
        ywjqrIntbankKindparaMapper.deleteAll();
        List<YwjqrIntbankKindpara> data = ywjqrIntbankKindparaMapper.selectList(new LambdaQueryWrapper<>());
        System.out.println("清理后当前条数为 " + data.size()); // TODO
        List<YwjqrIntbankKindpara> list = task();
        for (YwjqrIntbankKindpara ywjqrIntbankKindpara : list) {
            ywjqrIntbankKindparaMapper.insert(ywjqrIntbankKindpara);
        }
        data = ywjqrIntbankKindparaMapper.selectList(new LambdaQueryWrapper<>());
        System.out.println("新增条数 " + data.size()); // TODO
    }

    public List<YwjqrIntbankKindpara> task() {
        // 数据库连接信息 // TODO 读取配置文件
        String url = "jdbc:postgresql://20.200.34.135:5433/mdbfinver1";
        String username = "test";
        String password = "cG9zdGdyZXN0ZXN0Cg==";
        List<YwjqrIntbankKindpara> list = new ArrayList<>();
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException e) {
            // TODO
        }
        try (Connection conn = DriverManager.getConnection(url, username, password);
             Statement stmt = conn.createStatement()) {
            String sql = "SELECT * FROM fd_intbank_kindpara"; // TODO 可以改成读取配置文件
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                YwjqrIntbankKindpara ywjqrIntbankKindpara = new YwjqrIntbankKindpara();
                ywjqrIntbankKindpara.setFundProdtNo(rs.getString("fund_prodt_no"));
                ywjqrIntbankKindpara.setEnrollInstNo(rs.getString("enroll_inst_no"));
                ywjqrIntbankKindpara.setBelongInstNo(rs.getString("belong_inst_no"));
                ywjqrIntbankKindpara.setFundStatusCode(rs.getString("fund_status_code"));
                ywjqrIntbankKindpara.setProdtName(rs.getString("prodt_name"));
                ywjqrIntbankKindpara.setRaisBgnDate(rs.getString("rais_bgn_date"));
                ywjqrIntbankKindpara.setCollectEndDt(rs.getString("collect_end_dt"));
                ywjqrIntbankKindpara.setCollectDaynum(rs.getLong("collect_daynum"));
                ywjqrIntbankKindpara.setCtfeeModeCd(rs.getString("ctfee_mode_cd"));
                ywjqrIntbankKindpara.setFundType(rs.getString("fund_type"));
                ywjqrIntbankKindpara.setFundLvl2ClsCd(rs.getString("fund_lvl2_cls_cd"));
                ywjqrIntbankKindpara.setChremProdtRiskLvlCode(rs.getString("chrem_prodt_risk_lvl_code"));
                ywjqrIntbankKindpara.setDefauDividMode(rs.getString("defau_divid_mode"));
                ywjqrIntbankKindpara.setInstLowestSubscriLmtAmt(rs.getBigDecimal("inst_lowest_subscri_lmt_amt"));
                ywjqrIntbankKindpara.setInstaddtoLowestSubscriLmamt(rs.getBigDecimal("instaddto_lowest_subscri_lmamt"));
                ywjqrIntbankKindpara.setInstHighestSubscriLmtAmt(rs.getBigDecimal("inst_highest_subscri_lmt_amt"));
                ywjqrIntbankKindpara.setInstLowestAplypchsLmtAmt(rs.getBigDecimal("inst_lowest_aplypchs_lmt_amt"));
                ywjqrIntbankKindpara.setInstaddtoLoweAplypchsLmamt(rs.getBigDecimal("instaddto_lowe_aplypchs_lmamt"));
                ywjqrIntbankKindpara.setInstPerHighestAplypLmtAmt(rs.getBigDecimal("inst_per_highest_aplyp_lmt_amt"));
                ywjqrIntbankKindpara.setInstHighestAplypchsLmtAmt(rs.getBigDecimal("inst_highest_aplypchs_lmt_amt"));
                ywjqrIntbankKindpara.setInstLowestRedemLmtAmt(rs.getBigDecimal("inst_lowest_redem_lmt_amt"));
                ywjqrIntbankKindpara.setInstPerHighestRedemLmtAmt(rs.getBigDecimal("inst_per_highest_redem_lmt_amt"));
                ywjqrIntbankKindpara.setInstHighestRedemLmtAmt(rs.getBigDecimal("inst_highest_redem_lmt_amt"));
                ywjqrIntbankKindpara.setInstLowestHoldHaveLmtAmt(rs.getBigDecimal("inst_lowest_hold_have_lmt_amt"));
                ywjqrIntbankKindpara.setFundTranfmLowLmtAmt(rs.getBigDecimal("fund_tranfm_low_lmt_amt"));
                ywjqrIntbankKindpara.setFixQuotaAplypchsUpLmtAmt(rs.getBigDecimal("fix_quota_aplypchs_up_lmt_amt"));
                ywjqrIntbankKindpara.setFixQuotaAplypchsLowLmtAmt(rs.getBigDecimal("fix_quota_aplypchs_low_lmt_amt"));
                ywjqrIntbankKindpara.setEnPyName(rs.getString("en_py_name"));
                ywjqrIntbankKindpara.setEnPySname(rs.getString("en_py_sname"));
                ywjqrIntbankKindpara.setProdtAname(rs.getString("prodt_aname"));
                ywjqrIntbankKindpara.setFundTrusteeBankName(rs.getString("fund_trustee_bank_name"));
                ywjqrIntbankKindpara.setChremFlag(rs.getString("chrem_flag"));
                ywjqrIntbankKindpara.setContractType(rs.getString("contract_type"));
                ywjqrIntbankKindpara.setBusiContractName(rs.getString("busi_contract_name"));
                ywjqrIntbankKindpara.seteConfParCode(rs.getString("e_conf_par_code"));
                ywjqrIntbankKindpara.setLofFundFlag(rs.getString("lof_fund_flag"));
                ywjqrIntbankKindpara.setPartcYrHoldHvFundFlag(rs.getString("partc_yr_hold_hv_fund_flag"));
                ywjqrIntbankKindpara.setByquitFlag(rs.getString("byquit_flag"));
                ywjqrIntbankKindpara.setRelPtyManageProdtFlag(rs.getString("rel_pty_manage_prodt_flag"));
                ywjqrIntbankKindpara.setSetsChmprdtFlag(rs.getString("sets_chmprdt_flag"));
                ywjqrIntbankKindpara.setStructurProdtFlag(rs.getString("structur_prodt_flag"));
                ywjqrIntbankKindpara.setOpenFixQuotaBusiFlag(rs.getString("open_fix_quota_busi_flag"));
                ywjqrIntbankKindpara.setCtrlLimitFlag(rs.getString("ctrl_limit_flag"));
                ywjqrIntbankKindpara.setRevealProdtDocOutlFlag(rs.getString("reveal_prodt_doc_outl_flag"));
                ywjqrIntbankKindpara.setSpcaccChmprdtFlag(rs.getString("spcacc_chmprdt_flag"));
                ywjqrIntbankKindpara.setPsbcTrusteeFlag(rs.getString("psbc_trustee_flag"));
                ywjqrIntbankKindpara.setHkMutRecoFundFlag(rs.getString("hk_mut_reco_fund_flag"));
                ywjqrIntbankKindpara.setDividModeCode(rs.getString("divid_mode_code"));
                ywjqrIntbankKindpara.setPermitTranfmFlag(rs.getString("permit_tranfm_flag"));
                ywjqrIntbankKindpara.setRecomendExpParaCode(rs.getString("recomend_exp_para_code"));
                ywjqrIntbankKindpara.setAgedFundCd(rs.getString("aged_fund_cd"));
                ywjqrIntbankKindpara.setAgedTargetFundFlag(rs.getString("aged_target_fund_flag"));
                ywjqrIntbankKindpara.setAgedFundCloseYearNum(rs.getShort("aged_fund_close_year_num"));
                ywjqrIntbankKindpara.setOptimizFundFlag(rs.getString("optimiz_fund_flag"));
                ywjqrIntbankKindpara.setHotSaleFundFlag(rs.getString("hot_sale_fund_flag"));
                ywjqrIntbankKindpara.setShowRiskReveaBkFlag(rs.getString("show_risk_revea_bk_flag"));
                ywjqrIntbankKindpara.setTranTrusteeMode(rs.getString("tran_trustee_mode"));
                ywjqrIntbankKindpara.setTranTrusteeStatus(rs.getString("tran_trustee_status"));
                ywjqrIntbankKindpara.setTotLimt(rs.getBigDecimal("tot_limt"));
                ywjqrIntbankKindpara.setEnableDate(rs.getString("enable_date"));
                ywjqrIntbankKindpara.setCfmTermVal(rs.getShort("cfm_term_val"));
                ywjqrIntbankKindpara.setLabelNo(rs.getString("label_no"));
                ywjqrIntbankKindpara.setProdtDesc(rs.getString("prodt_desc"));
                ywjqrIntbankKindpara.setLastModStamp(rs.getString("last_mod_stamp"));
                ywjqrIntbankKindpara.setRoleCompatDesc(rs.getString("role_compat_desc"));
                list.add(ywjqrIntbankKindpara);
            }
            System.out.println("总条数:" + list.size());  // TODO
            rs.close();
        } catch (SQLException e) {
            // TODO
            System.out.println("数据库操作失败！");
            e.printStackTrace();
        }
        return list;
    }
}
