package com.dahuaboke.mpda.tools.product.entity;

import javax.persistence.Column;
import javax.persistence.Table;

/**
 * 基金产品信息实体类
 */
@Table(name = "ywjqr_product")
public class YwjqrProduct {
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

    /**
     * 基金代码
     */


    private String fundCode;


    public YwjqrProduct() {
    }

    public YwjqrProduct(String fundFullName, String fundShortName, String quarterlyReportDate, String fundManagerName, String managerAppointmentDate, String managerSecuritiesExperience, String managerDescription, String managerStartDate, String managerSecuritiesStartDate, String industryStockPortfolio, String topTenStockInvestments, String bondPortfolio, String nationalBondRatio, String centralBankBillRatio, String financialBondRatio, String policyFinancialBondRatio, String corporateBondRatio, String shortTermFinancingBillRatio, String mediumTermNoteRatio, String topFiveBondInvestments, String assetComposition, String performanceComparison, String netAssetValue, String totalFundShares, String fundManagerCompany, String fundCustodian, String operationMode, String openFrequency, String contractEffectiveDate, String investmentObjective, String investmentScope, String investmentStrategy, String performanceBenchmark, String riskReturnCharacteristics, String purchaseFee, String redemptionFee, String managementFee, String custodyFee, String salesServiceFee, String auditFee, String disclosureFee, String otherFees, String totalExpenseRatio, String fundClassificationCode, String fundClassificationReasonCode, String fundClassificationModel, String fundClassificationReasonModel, String fundCode) {
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

    public String getFundCode() {
        return fundCode;
    }

    public void setFundCode(String fundCode) {
        this.fundCode = fundCode;
    }
}
