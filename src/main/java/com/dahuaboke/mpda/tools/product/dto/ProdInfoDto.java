package com.dahuaboke.mpda.tools.product.dto;

import javax.persistence.Column;
import java.math.BigDecimal;

public class ProdInfoDto {

    /**
     * 基金代码
     */
    private String fundCode;

    /**
     * 基金全称
     */
    private String fundFullName;

    /**
     * 基金简称
     */
    private String fundShortName;

    /**
     * 季报时间
     */
    private String quarterlyReportDate;

    /**
     * 基金经理名字
     */
    private String fundManagerName;

    /**
     * 基金经理任职日期
     */

    private String managerAppointmentDate;

    /**
     * 基金经理证券从业
     * 年限
     */

    private String managerSecuritiesExperience;

    /**
     * 基金经理说明
     */


    private String managerDescription;

    /**
     * 基金经理开始担任
     * 本基金基金经理的日期
     */

    private String managerStartDate;

    /**
     * 基金经理证券从业
     * 日期
     */

    private String managerSecuritiesStartDate;

    /**
     * 报告期末按行业分
     * 类的境内股票投资组合
     */

    private String industryStockPortfolio;

    /**
     * 报告期末按公允价
     * 值占基金资产净值比例大小排序的前十名股票投资明细
     */

    private String topTenStockInvestments;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合
     */

    private String bondPortfolio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中国家债券占基金资产净值比例
     */

    private String nationalBondRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中央行票据占基金资产净值比例
     */

    private String centralBankBillRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中金融债券占基金资产净值比例
     */

    private String financialBondRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中政策性金融债占基金资产净值比例
     */

    private String policyFinancialBondRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中企业债券占基金资产净值比例
     */

    private String corporateBondRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中企业短期融资券占基金资产净值比例
     */

    private String shortTermFinancingBillRatio;

    /**
     * 报告期末按债券品
     * 种分类的债券投资组合中中期票据占基金资产净值比例
     */

    private String mediumTermNoteRatio;

    /**
     * 报告期末按公允价
     * 值占基金资产净值比例大小排序的前五名债券投资明细
     */

    private String topFiveBondInvestments;

    /**
     * 报告期末基金资产
     * 组合情况
     */

    private String assetComposition;

    /**
     * 本报告期基金份额
     * 净值增长率及其与同期业绩比较基准收益率的比较
     */

    private String performanceComparison;

    /**
     * 期末基金资产净值
     */
    private String netAssetValue;


    /**
     * 报告期期末基金份
     * 额总额
     */

    private String totalFundShares;


    /**
     * 基金管理人
     */

    private String fundManagerCompany;


    /**
     * 基金托管人
     */

    private String fundCustodian;


    /**
     * 运作方式
     */

    private String operationMode;


    /**
     * 开放频率
     */

    private String openFrequency;


    /**
     * 基金合同生效日
     */
    private String contractEffectiveDate;


    /**
     * 投资目标
     */

    private String investmentObjective;


    /**
     * 投资范围
     */

    private String investmentScope;


    /**
     * 主要投资策略
     */

    private String investmentStrategy;


    /**
     * 业绩比较基准
     */


    private String performanceBenchmark;

    /**
     * 风险收益特征
     */

    private String riskReturnCharacteristics;


    /**
     * 基金销售相关费用
     * 申购费
     */
    @Column(name = "purchase_fee")
    private String purchaseFee;


    /**
     * 基金销售相关费用
     * 赎回费
     */
    private String redemptionFee;


    /**
     * 基金运作相关费用
     * 管理费
     */
    private String managementFee;


    /**
     * 基金运作相关费用
     * 托管费
     */
    private String custodyFee;


    /**
     * 基金运作相关费用
     * 销售服务费
     */
    private String salesServiceFee;


    /**
     * 基金运作相关费用
     * 审计费用
     */
    private String auditFee;


    /**
     * 基金运作相关费用
     * 信息披露费
     */
    private String disclosureFee;


    /**
     * 基金运作相关费用
     * 其他费用
     */
    private String otherFees;


    /**
     * 基金运作综合费率
     * （年化）
     */
    private String totalExpenseRatio;


    /**
     * 基金分类(代码)
     */
    private String fundClassificationCode;


    /**
     * 基金分类原因(代码
     * )
     */

    private String fundClassificationReasonCode;


    /**
     * 基金分类(模型)
     */

    private String fundClassificationModel;


    /**
     * 基金分类原因(模型
     * )
     */

    private String fundClassificationReasonModel;


    private String fundProdtNo;

    /**
     * 登记机构号||TA登记机构
     */
    private String enrollInstNo;

    /**
     * 所属机构号||产品所属机构
     */
    private String belongInstNo;

    /**
     * 基金状态代码||0-可申购赎回，1-发行4-停止申购赎回5-停止申购，6-停止赎回8-基金终止，9-基金封闭
     */
    private String fundStatusCode;

    /**
     * 产品名称||产品名称
     */
    private String prodtName;

    /**
     * 募集起始日期|募集起始日期
     */
    private String raisBgnDate;

    /**
     * 募集结束日期|募集结束日期
     */
    private String collectEndDt;

    /**
     * 募集天数|募集天数
     */
    private Long collectDaynum;

    /**
     * 收费方式||0-前端收费1-后端收费
     */
    private String ctfeeModeCd;

    /**
     * 基金类型||0-货币型基金1-股票型基金2-债券型基金3-混合型基金4-保本型基金	5-指数型6-短债型7-QDII
     */
    private String fundType;

    /**
     * 基金二级分类||01-普通股票型、02-被动指数型、03-增强指数型、04-偏股混合型、05-平衡混合型、06-偏债混合型、07-灵活配置型、08-中长期纯债型、09-短期纯债型、10-混合债券型一级、11-混合债券型二级、12-被动指数债券型、13-增强指数债券型、14-传统货币型、15-短期理财型、16-浮动净值型货币、17-股票多空型、18-商品型、19-QDII股票型、20-QDII债券型、21-QDII混合型、22-QDII另类型、99-其他
     */
    private String fundLvl2ClsCd;

    /**
     * 理财产品风险等级代码||1-低2-中低3-中4-中高5-高
     */
    private String chremProdtRiskLvlCode;

    /**
     * 默认分红方式||0-红利转股1-现金分红
     */
    private String defauDividMode;

    /**
     * 机构最低认购限额||机构最低认购限额
     */
    private BigDecimal instLowestSubscriLmtAmt;

    /**
     * 机构追加最低认购限额||机构追加最低认购限额
     */
    private BigDecimal instaddtoLowestSubscriLmamt;

    /**
     * 机构最高认购限额||机构最高认购限额
     */
    private BigDecimal instHighestSubscriLmtAmt;

    /**
     * 机构最低申购限额||机构最低申购限额
     */
    private BigDecimal instLowestAplypchsLmtAmt;

    /**
     * 机构追加最低申购限额||机构追加最低申购限额
     */
    private BigDecimal instaddtoLoweAplypchsLmamt;

    /**
     * 机构单笔最高申购限额||机构单笔最高申购限额
     */
    private BigDecimal instPerHighestAplypLmtAmt;

    /**
     * 机构最高申购限额||机构最高申购限额
     */
    private BigDecimal instHighestAplypchsLmtAmt;

    /**
     * 机构最低赎回限额||机构最低赎回限额
     */
    private BigDecimal instLowestRedemLmtAmt;

    /**
     * 机构单笔最高赎回限额||机构单笔最高赎回限额
     */
    private BigDecimal instPerHighestRedemLmtAmt;

    /**
     * 机构最高赎回限额||机构最高赎回限额
     */
    private BigDecimal instHighestRedemLmtAmt;

    /**
     * 机构最低持有限额||机构最低持有限额
     */
    private BigDecimal instLowestHoldHaveLmtAmt;

    /**
     * 基金转换下限金额||基金转换下限金额
     */
    private BigDecimal fundTranfmLowLmtAmt;

    /**
     * 定期定额申购上限金额||定期定额申购上限金额
     */
    private BigDecimal fixQuotaAplypchsUpLmtAmt;

    /**
     * 定期定额申购下限金额||定期定额申购下限金额
     */
    private BigDecimal fixQuotaAplypchsLowLmtAmt;

    /**
     * 拼音名称||拼音名称全拼
     */
    private String enPyName;

    /**
     * 拼音简称||产品拼音首字母
     */
    private String enPySname;

    /**
     * 产品名称||基金产品全称
     */
    private String prodtAname;

    /**
     * 基金托管银行||基金托管银行
     */
    private String fundTrusteeBankName;

    /**
     * 短期理财标志||0-否1-是
     */
    private String chremFlag;

    /**
     * 合同类型代码||0-纸质；1-电子
     */
    private String contractType;

    /**
     * 合同名称||业务合同名称
     */
    private String busiContractName;

    /**
     * 电子确认方代码||0-基金公司确认1-中登确认
     */
    private String eConfParCode;

    /**
     * 是否是lof基金标志||0-非lof基金1-lof基金
     */
    private String lofFundFlag;

    /**
     * 某年持有基金标志|0-否，1-是
     */
    private String partcYrHoldHvFundFlag;

    /**
     * 是否被退出标志||0-正常1-已退出
     */
    private String byquitFlag;

    /**
     * 是否关联方管理产品标志|0-否，1-是
     */
    private String relPtyManageProdtFlag;

    /**
     * 是否集合理财产品标志||0-否1-是
     */
    private String setsChmprdtFlag;

    /**
     * 是否结构化产品标志|0-否，1-是
     */
    private String structurProdtFlag;

    /**
     * 是否开办定期定额业务||0–开办1–不开办
     */
    private String openFixQuotaBusiFlag;

    /**
     * 是否控制额度||0-允许；1-不允许
     */
    private String ctrlLimitFlag;

    /**
     * 是否披露产品资料概要标志||0-否1-是
     */
    private String revealProdtDocOutlFlag;

    /**
     * 是否是专户理财产品||0-否；1-是
     */
    private String spcaccChmprdtFlag;

    /**
     * 是否我行托管标志|0-否，1-是
     */
    private String psbcTrusteeFlag;

    /**
     * 是否香港互认基金标志|0-否，1-是
     */
    private String hkMutRecoFundFlag;

    /**
     * 是否允许分红方式变更||0-允许；1-不允许
     */
    private String dividModeCode;

    /**
     * 是否允许转换标志||1.关闭，2.转入，3.转出，4.转入转出
     */
    private String permitTranfmFlag;

    /**
     * 推荐指数参数||0-无星级、1-3星级、2-4星级、3-5星级、4-特别推荐
     */
    private String recomendExpParaCode;

    /**
     * 养老基金代码||0-目标日期基金,1-目标风险基金
     */
    private String agedFundCd;

    /**
     * 是否养老目标基金||0-否1-是
     */
    private String agedTargetFundFlag;

    /**
     * 养老基金封闭年数||养老基金封闭年数
     */
    private Short agedFundCloseYearNum;

    /**
     * 优选基金标志0-否1-是
     */
    private String optimizFundFlag;

    /**
     * 热销基金标志0-否1-是
     */
    private String hotSaleFundFlag;

    /**
     * 展示风险揭示书标志|0-否，1-是
     */
    private String showRiskReveaBkFlag;

    /**
     * 转托管方式代码||0-一步转托管1-两步转托管2-禁止
     */
    private String tranTrusteeMode;

    /**
     * 转托管状态代码||0-允许所有转托管1-仅允许场外转托管2-仅允许跨市场转托管3-禁止所有转托管
     */
    private String tranTrusteeStatus;

    /**
     * 总额度||总额度
     */

    private BigDecimal totLimt;

    /**
     * 启用日期||启用日期
     */

    private String enableDate;

    /**
     * 确认时限
     */

    private Short cfmTermVal;

    /**
     * 标签编号
     */

    private String labelNo;

    /**
     * 产品描述
     */

    private String prodtDesc;

    /**
     * 最后修改时间戳||最后修改时间戳
     */
    private String lastModStamp;
    /**
     * 产品特色额度||角色兼容说明
     */
    private String roleCompatDesc;

    public ProdInfoDto() {
    }

    public ProdInfoDto(String fundCode, String fundFullName, String fundShortName, String quarterlyReportDate, String fundManagerName, String managerAppointmentDate, String managerSecuritiesExperience, String managerDescription, String managerStartDate, String managerSecuritiesStartDate, String industryStockPortfolio, String topTenStockInvestments, String bondPortfolio, String nationalBondRatio, String centralBankBillRatio, String financialBondRatio, String policyFinancialBondRatio, String corporateBondRatio, String shortTermFinancingBillRatio, String mediumTermNoteRatio, String topFiveBondInvestments, String assetComposition, String performanceComparison, String netAssetValue, String totalFundShares, String fundManagerCompany, String fundCustodian, String operationMode, String openFrequency, String contractEffectiveDate, String investmentObjective, String investmentScope, String investmentStrategy, String performanceBenchmark, String riskReturnCharacteristics, String purchaseFee, String redemptionFee, String managementFee, String custodyFee, String salesServiceFee, String auditFee, String disclosureFee, String otherFees, String totalExpenseRatio, String fundClassificationCode, String fundClassificationReasonCode, String fundClassificationModel, String fundClassificationReasonModel, String fundProdtNo, String enrollInstNo, String belongInstNo, String fundStatusCode, String prodtName, String raisBgnDate, String collectEndDt, Long collectDaynum, String ctfeeModeCd, String fundType, String fundLvl2ClsCd, String chremProdtRiskLvlCode, String defauDividMode, BigDecimal instLowestSubscriLmtAmt, BigDecimal instaddtoLowestSubscriLmamt, BigDecimal instHighestSubscriLmtAmt, BigDecimal instLowestAplypchsLmtAmt, BigDecimal instaddtoLoweAplypchsLmamt, BigDecimal instPerHighestAplypLmtAmt, BigDecimal instHighestAplypchsLmtAmt, BigDecimal instLowestRedemLmtAmt, BigDecimal instPerHighestRedemLmtAmt, BigDecimal instHighestRedemLmtAmt, BigDecimal instLowestHoldHaveLmtAmt, BigDecimal fundTranfmLowLmtAmt, BigDecimal fixQuotaAplypchsUpLmtAmt, BigDecimal fixQuotaAplypchsLowLmtAmt, String enPyName, String enPySname, String prodtAname, String fundTrusteeBankName, String chremFlag, String contractType, String busiContractName, String eConfParCode, String lofFundFlag, String partcYrHoldHvFundFlag, String byquitFlag, String relPtyManageProdtFlag, String setsChmprdtFlag, String structurProdtFlag, String openFixQuotaBusiFlag, String ctrlLimitFlag, String revealProdtDocOutlFlag, String spcaccChmprdtFlag, String psbcTrusteeFlag, String hkMutRecoFundFlag, String dividModeCode, String permitTranfmFlag, String recomendExpParaCode, String agedFundCd, String agedTargetFundFlag, Short agedFundCloseYearNum, String optimizFundFlag, String hotSaleFundFlag, String showRiskReveaBkFlag, String tranTrusteeMode, String tranTrusteeStatus, BigDecimal totLimt, String enableDate, Short cfmTermVal, String labelNo, String prodtDesc, String lastModStamp, String roleCompatDesc) {
        this.fundCode = fundCode;
        this.fundFullName = fundFullName;
        this.fundShortName = fundShortName;
        this.quarterlyReportDate = quarterlyReportDate;
        this.fundManagerName = fundManagerName;
        this.managerAppointmentDate = managerAppointmentDate;
        this.managerSecuritiesExperience = managerSecuritiesExperience;
        this.managerDescription = managerDescription;
        this.managerStartDate = managerStartDate;
        this.managerSecuritiesStartDate = managerSecuritiesStartDate;
        this.industryStockPortfolio = industryStockPortfolio;
        this.topTenStockInvestments = topTenStockInvestments;
        this.bondPortfolio = bondPortfolio;
        this.nationalBondRatio = nationalBondRatio;
        this.centralBankBillRatio = centralBankBillRatio;
        this.financialBondRatio = financialBondRatio;
        this.policyFinancialBondRatio = policyFinancialBondRatio;
        this.corporateBondRatio = corporateBondRatio;
        this.shortTermFinancingBillRatio = shortTermFinancingBillRatio;
        this.mediumTermNoteRatio = mediumTermNoteRatio;
        this.topFiveBondInvestments = topFiveBondInvestments;
        this.assetComposition = assetComposition;
        this.performanceComparison = performanceComparison;
        this.netAssetValue = netAssetValue;
        this.totalFundShares = totalFundShares;
        this.fundManagerCompany = fundManagerCompany;
        this.fundCustodian = fundCustodian;
        this.operationMode = operationMode;
        this.openFrequency = openFrequency;
        this.contractEffectiveDate = contractEffectiveDate;
        this.investmentObjective = investmentObjective;
        this.investmentScope = investmentScope;
        this.investmentStrategy = investmentStrategy;
        this.performanceBenchmark = performanceBenchmark;
        this.riskReturnCharacteristics = riskReturnCharacteristics;
        this.purchaseFee = purchaseFee;
        this.redemptionFee = redemptionFee;
        this.managementFee = managementFee;
        this.custodyFee = custodyFee;
        this.salesServiceFee = salesServiceFee;
        this.auditFee = auditFee;
        this.disclosureFee = disclosureFee;
        this.otherFees = otherFees;
        this.totalExpenseRatio = totalExpenseRatio;
        this.fundClassificationCode = fundClassificationCode;
        this.fundClassificationReasonCode = fundClassificationReasonCode;
        this.fundClassificationModel = fundClassificationModel;
        this.fundClassificationReasonModel = fundClassificationReasonModel;
        this.fundProdtNo = fundProdtNo;
        this.enrollInstNo = enrollInstNo;
        this.belongInstNo = belongInstNo;
        this.fundStatusCode = fundStatusCode;
        this.prodtName = prodtName;
        this.raisBgnDate = raisBgnDate;
        this.collectEndDt = collectEndDt;
        this.collectDaynum = collectDaynum;
        this.ctfeeModeCd = ctfeeModeCd;
        this.fundType = fundType;
        this.fundLvl2ClsCd = fundLvl2ClsCd;
        this.chremProdtRiskLvlCode = chremProdtRiskLvlCode;
        this.defauDividMode = defauDividMode;
        this.instLowestSubscriLmtAmt = instLowestSubscriLmtAmt;
        this.instaddtoLowestSubscriLmamt = instaddtoLowestSubscriLmamt;
        this.instHighestSubscriLmtAmt = instHighestSubscriLmtAmt;
        this.instLowestAplypchsLmtAmt = instLowestAplypchsLmtAmt;
        this.instaddtoLoweAplypchsLmamt = instaddtoLoweAplypchsLmamt;
        this.instPerHighestAplypLmtAmt = instPerHighestAplypLmtAmt;
        this.instHighestAplypchsLmtAmt = instHighestAplypchsLmtAmt;
        this.instLowestRedemLmtAmt = instLowestRedemLmtAmt;
        this.instPerHighestRedemLmtAmt = instPerHighestRedemLmtAmt;
        this.instHighestRedemLmtAmt = instHighestRedemLmtAmt;
        this.instLowestHoldHaveLmtAmt = instLowestHoldHaveLmtAmt;
        this.fundTranfmLowLmtAmt = fundTranfmLowLmtAmt;
        this.fixQuotaAplypchsUpLmtAmt = fixQuotaAplypchsUpLmtAmt;
        this.fixQuotaAplypchsLowLmtAmt = fixQuotaAplypchsLowLmtAmt;
        this.enPyName = enPyName;
        this.enPySname = enPySname;
        this.prodtAname = prodtAname;
        this.fundTrusteeBankName = fundTrusteeBankName;
        this.chremFlag = chremFlag;
        this.contractType = contractType;
        this.busiContractName = busiContractName;
        this.eConfParCode = eConfParCode;
        this.lofFundFlag = lofFundFlag;
        this.partcYrHoldHvFundFlag = partcYrHoldHvFundFlag;
        this.byquitFlag = byquitFlag;
        this.relPtyManageProdtFlag = relPtyManageProdtFlag;
        this.setsChmprdtFlag = setsChmprdtFlag;
        this.structurProdtFlag = structurProdtFlag;
        this.openFixQuotaBusiFlag = openFixQuotaBusiFlag;
        this.ctrlLimitFlag = ctrlLimitFlag;
        this.revealProdtDocOutlFlag = revealProdtDocOutlFlag;
        this.spcaccChmprdtFlag = spcaccChmprdtFlag;
        this.psbcTrusteeFlag = psbcTrusteeFlag;
        this.hkMutRecoFundFlag = hkMutRecoFundFlag;
        this.dividModeCode = dividModeCode;
        this.permitTranfmFlag = permitTranfmFlag;
        this.recomendExpParaCode = recomendExpParaCode;
        this.agedFundCd = agedFundCd;
        this.agedTargetFundFlag = agedTargetFundFlag;
        this.agedFundCloseYearNum = agedFundCloseYearNum;
        this.optimizFundFlag = optimizFundFlag;
        this.hotSaleFundFlag = hotSaleFundFlag;
        this.showRiskReveaBkFlag = showRiskReveaBkFlag;
        this.tranTrusteeMode = tranTrusteeMode;
        this.tranTrusteeStatus = tranTrusteeStatus;
        this.totLimt = totLimt;
        this.enableDate = enableDate;
        this.cfmTermVal = cfmTermVal;
        this.labelNo = labelNo;
        this.prodtDesc = prodtDesc;
        this.lastModStamp = lastModStamp;
        this.roleCompatDesc = roleCompatDesc;
    }

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }

    public String getFundFullName() {
        return fundFullName;
    }

    public void setFundFullName(String fundFullName) {
        this.fundFullName = fundFullName;
    }

    public String getFundShortName() {
        return fundShortName;
    }

    public void setFundShortName(String fundShortName) {
        this.fundShortName = fundShortName;
    }

    public String getQuarterlyReportDate() {
        return quarterlyReportDate;
    }

    public void setQuarterlyReportDate(String quarterlyReportDate) {
        this.quarterlyReportDate = quarterlyReportDate;
    }

    public String getFundManagerName() {
        return fundManagerName;
    }

    public void setFundManagerName(String fundManagerName) {
        this.fundManagerName = fundManagerName;
    }

    public String getManagerAppointmentDate() {
        return managerAppointmentDate;
    }

    public void setManagerAppointmentDate(String managerAppointmentDate) {
        this.managerAppointmentDate = managerAppointmentDate;
    }

    public String getManagerSecuritiesExperience() {
        return managerSecuritiesExperience;
    }

    public void setManagerSecuritiesExperience(String managerSecuritiesExperience) {
        this.managerSecuritiesExperience = managerSecuritiesExperience;
    }

    public String getManagerDescription() {
        return managerDescription;
    }

    public void setManagerDescription(String managerDescription) {
        this.managerDescription = managerDescription;
    }

    public String getManagerStartDate() {
        return managerStartDate;
    }

    public void setManagerStartDate(String managerStartDate) {
        this.managerStartDate = managerStartDate;
    }

    public String getManagerSecuritiesStartDate() {
        return managerSecuritiesStartDate;
    }

    public void setManagerSecuritiesStartDate(String managerSecuritiesStartDate) {
        this.managerSecuritiesStartDate = managerSecuritiesStartDate;
    }

    public String getIndustryStockPortfolio() {
        return industryStockPortfolio;
    }

    public void setIndustryStockPortfolio(String industryStockPortfolio) {
        this.industryStockPortfolio = industryStockPortfolio;
    }

    public String getTopTenStockInvestments() {
        return topTenStockInvestments;
    }

    public void setTopTenStockInvestments(String topTenStockInvestments) {
        this.topTenStockInvestments = topTenStockInvestments;
    }

    public String getBondPortfolio() {
        return bondPortfolio;
    }

    public void setBondPortfolio(String bondPortfolio) {
        this.bondPortfolio = bondPortfolio;
    }

    public String getNationalBondRatio() {
        return nationalBondRatio;
    }

    public void setNationalBondRatio(String nationalBondRatio) {
        this.nationalBondRatio = nationalBondRatio;
    }

    public String getCentralBankBillRatio() {
        return centralBankBillRatio;
    }

    public void setCentralBankBillRatio(String centralBankBillRatio) {
        this.centralBankBillRatio = centralBankBillRatio;
    }

    public String getFinancialBondRatio() {
        return financialBondRatio;
    }

    public void setFinancialBondRatio(String financialBondRatio) {
        this.financialBondRatio = financialBondRatio;
    }

    public String getPolicyFinancialBondRatio() {
        return policyFinancialBondRatio;
    }

    public void setPolicyFinancialBondRatio(String policyFinancialBondRatio) {
        this.policyFinancialBondRatio = policyFinancialBondRatio;
    }

    public String getCorporateBondRatio() {
        return corporateBondRatio;
    }

    public void setCorporateBondRatio(String corporateBondRatio) {
        this.corporateBondRatio = corporateBondRatio;
    }

    public String getShortTermFinancingBillRatio() {
        return shortTermFinancingBillRatio;
    }

    public void setShortTermFinancingBillRatio(String shortTermFinancingBillRatio) {
        this.shortTermFinancingBillRatio = shortTermFinancingBillRatio;
    }

    public String getMediumTermNoteRatio() {
        return mediumTermNoteRatio;
    }

    public void setMediumTermNoteRatio(String mediumTermNoteRatio) {
        this.mediumTermNoteRatio = mediumTermNoteRatio;
    }

    public String getTopFiveBondInvestments() {
        return topFiveBondInvestments;
    }

    public void setTopFiveBondInvestments(String topFiveBondInvestments) {
        this.topFiveBondInvestments = topFiveBondInvestments;
    }

    public String getAssetComposition() {
        return assetComposition;
    }

    public void setAssetComposition(String assetComposition) {
        this.assetComposition = assetComposition;
    }

    public String getPerformanceComparison() {
        return performanceComparison;
    }

    public void setPerformanceComparison(String performanceComparison) {
        this.performanceComparison = performanceComparison;
    }

    public String getNetAssetValue() {
        return netAssetValue;
    }

    public void setNetAssetValue(String netAssetValue) {
        this.netAssetValue = netAssetValue;
    }

    public String getTotalFundShares() {
        return totalFundShares;
    }

    public void setTotalFundShares(String totalFundShares) {
        this.totalFundShares = totalFundShares;
    }

    public String getFundManagerCompany() {
        return fundManagerCompany;
    }

    public void setFundManagerCompany(String fundManagerCompany) {
        this.fundManagerCompany = fundManagerCompany;
    }

    public String getFundCustodian() {
        return fundCustodian;
    }

    public void setFundCustodian(String fundCustodian) {
        this.fundCustodian = fundCustodian;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getOpenFrequency() {
        return openFrequency;
    }

    public void setOpenFrequency(String openFrequency) {
        this.openFrequency = openFrequency;
    }

    public String getContractEffectiveDate() {
        return contractEffectiveDate;
    }

    public void setContractEffectiveDate(String contractEffectiveDate) {
        this.contractEffectiveDate = contractEffectiveDate;
    }

    public String getInvestmentObjective() {
        return investmentObjective;
    }

    public void setInvestmentObjective(String investmentObjective) {
        this.investmentObjective = investmentObjective;
    }

    public String getInvestmentScope() {
        return investmentScope;
    }

    public void setInvestmentScope(String investmentScope) {
        this.investmentScope = investmentScope;
    }

    public String getInvestmentStrategy() {
        return investmentStrategy;
    }

    public void setInvestmentStrategy(String investmentStrategy) {
        this.investmentStrategy = investmentStrategy;
    }

    public String getPerformanceBenchmark() {
        return performanceBenchmark;
    }

    public void setPerformanceBenchmark(String performanceBenchmark) {
        this.performanceBenchmark = performanceBenchmark;
    }

    public String getRiskReturnCharacteristics() {
        return riskReturnCharacteristics;
    }

    public void setRiskReturnCharacteristics(String riskReturnCharacteristics) {
        this.riskReturnCharacteristics = riskReturnCharacteristics;
    }

    public String getPurchaseFee() {
        return purchaseFee;
    }

    public void setPurchaseFee(String purchaseFee) {
        this.purchaseFee = purchaseFee;
    }

    public String getRedemptionFee() {
        return redemptionFee;
    }

    public void setRedemptionFee(String redemptionFee) {
        this.redemptionFee = redemptionFee;
    }

    public String getManagementFee() {
        return managementFee;
    }

    public void setManagementFee(String managementFee) {
        this.managementFee = managementFee;
    }

    public String getCustodyFee() {
        return custodyFee;
    }

    public void setCustodyFee(String custodyFee) {
        this.custodyFee = custodyFee;
    }

    public String getSalesServiceFee() {
        return salesServiceFee;
    }

    public void setSalesServiceFee(String salesServiceFee) {
        this.salesServiceFee = salesServiceFee;
    }

    public String getAuditFee() {
        return auditFee;
    }

    public void setAuditFee(String auditFee) {
        this.auditFee = auditFee;
    }

    public String getDisclosureFee() {
        return disclosureFee;
    }

    public void setDisclosureFee(String disclosureFee) {
        this.disclosureFee = disclosureFee;
    }

    public String getOtherFees() {
        return otherFees;
    }

    public void setOtherFees(String otherFees) {
        this.otherFees = otherFees;
    }

    public String getTotalExpenseRatio() {
        return totalExpenseRatio;
    }

    public void setTotalExpenseRatio(String totalExpenseRatio) {
        this.totalExpenseRatio = totalExpenseRatio;
    }

    public String getFundClassificationCode() {
        return fundClassificationCode;
    }

    public void setFundClassificationCode(String fundClassificationCode) {
        this.fundClassificationCode = fundClassificationCode;
    }

    public String getFundClassificationReasonCode() {
        return fundClassificationReasonCode;
    }

    public void setFundClassificationReasonCode(String fundClassificationReasonCode) {
        this.fundClassificationReasonCode = fundClassificationReasonCode;
    }

    public String getFundClassificationModel() {
        return fundClassificationModel;
    }

    public void setFundClassificationModel(String fundClassificationModel) {
        this.fundClassificationModel = fundClassificationModel;
    }

    public String getFundClassificationReasonModel() {
        return fundClassificationReasonModel;
    }

    public void setFundClassificationReasonModel(String fundClassificationReasonModel) {
        this.fundClassificationReasonModel = fundClassificationReasonModel;
    }

    public String getFundProdtNo() {
        return fundProdtNo;
    }

    public void setFundProdtNo(String fundProdtNo) {
        this.fundProdtNo = fundProdtNo;
    }

    public String getEnrollInstNo() {
        return enrollInstNo;
    }

    public void setEnrollInstNo(String enrollInstNo) {
        this.enrollInstNo = enrollInstNo;
    }

    public String getBelongInstNo() {
        return belongInstNo;
    }

    public void setBelongInstNo(String belongInstNo) {
        this.belongInstNo = belongInstNo;
    }

    public String getFundStatusCode() {
        return fundStatusCode;
    }

    public void setFundStatusCode(String fundStatusCode) {
        this.fundStatusCode = fundStatusCode;
    }

    public String getProdtName() {
        return prodtName;
    }

    public void setProdtName(String prodtName) {
        this.prodtName = prodtName;
    }

    public String getRaisBgnDate() {
        return raisBgnDate;
    }

    public void setRaisBgnDate(String raisBgnDate) {
        this.raisBgnDate = raisBgnDate;
    }

    public String getCollectEndDt() {
        return collectEndDt;
    }

    public void setCollectEndDt(String collectEndDt) {
        this.collectEndDt = collectEndDt;
    }

    public Long getCollectDaynum() {
        return collectDaynum;
    }

    public void setCollectDaynum(Long collectDaynum) {
        this.collectDaynum = collectDaynum;
    }

    public String getCtfeeModeCd() {
        return ctfeeModeCd;
    }

    public void setCtfeeModeCd(String ctfeeModeCd) {
        this.ctfeeModeCd = ctfeeModeCd;
    }

    public String getFundType() {
        return fundType;
    }

    public void setFundType(String fundType) {
        this.fundType = fundType;
    }

    public String getFundLvl2ClsCd() {
        return fundLvl2ClsCd;
    }

    public void setFundLvl2ClsCd(String fundLvl2ClsCd) {
        this.fundLvl2ClsCd = fundLvl2ClsCd;
    }

    public String getChremProdtRiskLvlCode() {
        return chremProdtRiskLvlCode;
    }

    public void setChremProdtRiskLvlCode(String chremProdtRiskLvlCode) {
        this.chremProdtRiskLvlCode = chremProdtRiskLvlCode;
    }

    public String getDefauDividMode() {
        return defauDividMode;
    }

    public void setDefauDividMode(String defauDividMode) {
        this.defauDividMode = defauDividMode;
    }

    public BigDecimal getInstLowestSubscriLmtAmt() {
        return instLowestSubscriLmtAmt;
    }

    public void setInstLowestSubscriLmtAmt(BigDecimal instLowestSubscriLmtAmt) {
        this.instLowestSubscriLmtAmt = instLowestSubscriLmtAmt;
    }

    public BigDecimal getInstaddtoLowestSubscriLmamt() {
        return instaddtoLowestSubscriLmamt;
    }

    public void setInstaddtoLowestSubscriLmamt(BigDecimal instaddtoLowestSubscriLmamt) {
        this.instaddtoLowestSubscriLmamt = instaddtoLowestSubscriLmamt;
    }

    public BigDecimal getInstHighestSubscriLmtAmt() {
        return instHighestSubscriLmtAmt;
    }

    public void setInstHighestSubscriLmtAmt(BigDecimal instHighestSubscriLmtAmt) {
        this.instHighestSubscriLmtAmt = instHighestSubscriLmtAmt;
    }

    public BigDecimal getInstLowestAplypchsLmtAmt() {
        return instLowestAplypchsLmtAmt;
    }

    public void setInstLowestAplypchsLmtAmt(BigDecimal instLowestAplypchsLmtAmt) {
        this.instLowestAplypchsLmtAmt = instLowestAplypchsLmtAmt;
    }

    public BigDecimal getInstaddtoLoweAplypchsLmamt() {
        return instaddtoLoweAplypchsLmamt;
    }

    public void setInstaddtoLoweAplypchsLmamt(BigDecimal instaddtoLoweAplypchsLmamt) {
        this.instaddtoLoweAplypchsLmamt = instaddtoLoweAplypchsLmamt;
    }

    public BigDecimal getInstPerHighestAplypLmtAmt() {
        return instPerHighestAplypLmtAmt;
    }

    public void setInstPerHighestAplypLmtAmt(BigDecimal instPerHighestAplypLmtAmt) {
        this.instPerHighestAplypLmtAmt = instPerHighestAplypLmtAmt;
    }

    public BigDecimal getInstHighestAplypchsLmtAmt() {
        return instHighestAplypchsLmtAmt;
    }

    public void setInstHighestAplypchsLmtAmt(BigDecimal instHighestAplypchsLmtAmt) {
        this.instHighestAplypchsLmtAmt = instHighestAplypchsLmtAmt;
    }

    public BigDecimal getInstLowestRedemLmtAmt() {
        return instLowestRedemLmtAmt;
    }

    public void setInstLowestRedemLmtAmt(BigDecimal instLowestRedemLmtAmt) {
        this.instLowestRedemLmtAmt = instLowestRedemLmtAmt;
    }

    public BigDecimal getInstPerHighestRedemLmtAmt() {
        return instPerHighestRedemLmtAmt;
    }

    public void setInstPerHighestRedemLmtAmt(BigDecimal instPerHighestRedemLmtAmt) {
        this.instPerHighestRedemLmtAmt = instPerHighestRedemLmtAmt;
    }

    public BigDecimal getInstHighestRedemLmtAmt() {
        return instHighestRedemLmtAmt;
    }

    public void setInstHighestRedemLmtAmt(BigDecimal instHighestRedemLmtAmt) {
        this.instHighestRedemLmtAmt = instHighestRedemLmtAmt;
    }

    public BigDecimal getInstLowestHoldHaveLmtAmt() {
        return instLowestHoldHaveLmtAmt;
    }

    public void setInstLowestHoldHaveLmtAmt(BigDecimal instLowestHoldHaveLmtAmt) {
        this.instLowestHoldHaveLmtAmt = instLowestHoldHaveLmtAmt;
    }

    public BigDecimal getFundTranfmLowLmtAmt() {
        return fundTranfmLowLmtAmt;
    }

    public void setFundTranfmLowLmtAmt(BigDecimal fundTranfmLowLmtAmt) {
        this.fundTranfmLowLmtAmt = fundTranfmLowLmtAmt;
    }

    public BigDecimal getFixQuotaAplypchsUpLmtAmt() {
        return fixQuotaAplypchsUpLmtAmt;
    }

    public void setFixQuotaAplypchsUpLmtAmt(BigDecimal fixQuotaAplypchsUpLmtAmt) {
        this.fixQuotaAplypchsUpLmtAmt = fixQuotaAplypchsUpLmtAmt;
    }

    public BigDecimal getFixQuotaAplypchsLowLmtAmt() {
        return fixQuotaAplypchsLowLmtAmt;
    }

    public void setFixQuotaAplypchsLowLmtAmt(BigDecimal fixQuotaAplypchsLowLmtAmt) {
        this.fixQuotaAplypchsLowLmtAmt = fixQuotaAplypchsLowLmtAmt;
    }

    public String getEnPyName() {
        return enPyName;
    }

    public void setEnPyName(String enPyName) {
        this.enPyName = enPyName;
    }

    public String getEnPySname() {
        return enPySname;
    }

    public void setEnPySname(String enPySname) {
        this.enPySname = enPySname;
    }

    public String getProdtAname() {
        return prodtAname;
    }

    public void setProdtAname(String prodtAname) {
        this.prodtAname = prodtAname;
    }

    public String getFundTrusteeBankName() {
        return fundTrusteeBankName;
    }

    public void setFundTrusteeBankName(String fundTrusteeBankName) {
        this.fundTrusteeBankName = fundTrusteeBankName;
    }

    public String getChremFlag() {
        return chremFlag;
    }

    public void setChremFlag(String chremFlag) {
        this.chremFlag = chremFlag;
    }

    public String getContractType() {
        return contractType;
    }

    public void setContractType(String contractType) {
        this.contractType = contractType;
    }

    public String getBusiContractName() {
        return busiContractName;
    }

    public void setBusiContractName(String busiContractName) {
        this.busiContractName = busiContractName;
    }

    public String geteConfParCode() {
        return eConfParCode;
    }

    public void seteConfParCode(String eConfParCode) {
        this.eConfParCode = eConfParCode;
    }

    public String getLofFundFlag() {
        return lofFundFlag;
    }

    public void setLofFundFlag(String lofFundFlag) {
        this.lofFundFlag = lofFundFlag;
    }

    public String getPartcYrHoldHvFundFlag() {
        return partcYrHoldHvFundFlag;
    }

    public void setPartcYrHoldHvFundFlag(String partcYrHoldHvFundFlag) {
        this.partcYrHoldHvFundFlag = partcYrHoldHvFundFlag;
    }

    public String getByquitFlag() {
        return byquitFlag;
    }

    public void setByquitFlag(String byquitFlag) {
        this.byquitFlag = byquitFlag;
    }

    public String getRelPtyManageProdtFlag() {
        return relPtyManageProdtFlag;
    }

    public void setRelPtyManageProdtFlag(String relPtyManageProdtFlag) {
        this.relPtyManageProdtFlag = relPtyManageProdtFlag;
    }

    public String getSetsChmprdtFlag() {
        return setsChmprdtFlag;
    }

    public void setSetsChmprdtFlag(String setsChmprdtFlag) {
        this.setsChmprdtFlag = setsChmprdtFlag;
    }

    public String getStructurProdtFlag() {
        return structurProdtFlag;
    }

    public void setStructurProdtFlag(String structurProdtFlag) {
        this.structurProdtFlag = structurProdtFlag;
    }

    public String getOpenFixQuotaBusiFlag() {
        return openFixQuotaBusiFlag;
    }

    public void setOpenFixQuotaBusiFlag(String openFixQuotaBusiFlag) {
        this.openFixQuotaBusiFlag = openFixQuotaBusiFlag;
    }

    public String getCtrlLimitFlag() {
        return ctrlLimitFlag;
    }

    public void setCtrlLimitFlag(String ctrlLimitFlag) {
        this.ctrlLimitFlag = ctrlLimitFlag;
    }

    public String getRevealProdtDocOutlFlag() {
        return revealProdtDocOutlFlag;
    }

    public void setRevealProdtDocOutlFlag(String revealProdtDocOutlFlag) {
        this.revealProdtDocOutlFlag = revealProdtDocOutlFlag;
    }

    public String getSpcaccChmprdtFlag() {
        return spcaccChmprdtFlag;
    }

    public void setSpcaccChmprdtFlag(String spcaccChmprdtFlag) {
        this.spcaccChmprdtFlag = spcaccChmprdtFlag;
    }

    public String getPsbcTrusteeFlag() {
        return psbcTrusteeFlag;
    }

    public void setPsbcTrusteeFlag(String psbcTrusteeFlag) {
        this.psbcTrusteeFlag = psbcTrusteeFlag;
    }

    public String getHkMutRecoFundFlag() {
        return hkMutRecoFundFlag;
    }

    public void setHkMutRecoFundFlag(String hkMutRecoFundFlag) {
        this.hkMutRecoFundFlag = hkMutRecoFundFlag;
    }

    public String getDividModeCode() {
        return dividModeCode;
    }

    public void setDividModeCode(String dividModeCode) {
        this.dividModeCode = dividModeCode;
    }

    public String getPermitTranfmFlag() {
        return permitTranfmFlag;
    }

    public void setPermitTranfmFlag(String permitTranfmFlag) {
        this.permitTranfmFlag = permitTranfmFlag;
    }

    public String getRecomendExpParaCode() {
        return recomendExpParaCode;
    }

    public void setRecomendExpParaCode(String recomendExpParaCode) {
        this.recomendExpParaCode = recomendExpParaCode;
    }

    public String getAgedFundCd() {
        return agedFundCd;
    }

    public void setAgedFundCd(String agedFundCd) {
        this.agedFundCd = agedFundCd;
    }

    public String getAgedTargetFundFlag() {
        return agedTargetFundFlag;
    }

    public void setAgedTargetFundFlag(String agedTargetFundFlag) {
        this.agedTargetFundFlag = agedTargetFundFlag;
    }

    public Short getAgedFundCloseYearNum() {
        return agedFundCloseYearNum;
    }

    public void setAgedFundCloseYearNum(Short agedFundCloseYearNum) {
        this.agedFundCloseYearNum = agedFundCloseYearNum;
    }

    public String getOptimizFundFlag() {
        return optimizFundFlag;
    }

    public void setOptimizFundFlag(String optimizFundFlag) {
        this.optimizFundFlag = optimizFundFlag;
    }

    public String getHotSaleFundFlag() {
        return hotSaleFundFlag;
    }

    public void setHotSaleFundFlag(String hotSaleFundFlag) {
        this.hotSaleFundFlag = hotSaleFundFlag;
    }

    public String getShowRiskReveaBkFlag() {
        return showRiskReveaBkFlag;
    }

    public void setShowRiskReveaBkFlag(String showRiskReveaBkFlag) {
        this.showRiskReveaBkFlag = showRiskReveaBkFlag;
    }

    public String getTranTrusteeMode() {
        return tranTrusteeMode;
    }

    public void setTranTrusteeMode(String tranTrusteeMode) {
        this.tranTrusteeMode = tranTrusteeMode;
    }

    public String getTranTrusteeStatus() {
        return tranTrusteeStatus;
    }

    public void setTranTrusteeStatus(String tranTrusteeStatus) {
        this.tranTrusteeStatus = tranTrusteeStatus;
    }

    public BigDecimal getTotLimt() {
        return totLimt;
    }

    public void setTotLimt(BigDecimal totLimt) {
        this.totLimt = totLimt;
    }

    public String getEnableDate() {
        return enableDate;
    }

    public void setEnableDate(String enableDate) {
        this.enableDate = enableDate;
    }

    public Short getCfmTermVal() {
        return cfmTermVal;
    }

    public void setCfmTermVal(Short cfmTermVal) {
        this.cfmTermVal = cfmTermVal;
    }

    public String getLabelNo() {
        return labelNo;
    }

    public void setLabelNo(String labelNo) {
        this.labelNo = labelNo;
    }

    public String getProdtDesc() {
        return prodtDesc;
    }

    public void setProdtDesc(String prodtDesc) {
        this.prodtDesc = prodtDesc;
    }

    public String getLastModStamp() {
        return lastModStamp;
    }

    public void setLastModStamp(String lastModStamp) {
        this.lastModStamp = lastModStamp;
    }

    public String getRoleCompatDesc() {
        return roleCompatDesc;
    }

    public void setRoleCompatDesc(String roleCompatDesc) {
        this.roleCompatDesc = roleCompatDesc;
    }
}
