/*
 * ComUserExtractApply.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-17 Created
 */
package com.masiis.shop.dao.po;

import com.masiis.shop.common.util.DateUtil;

import java.math.BigDecimal;
import java.util.Date;

public class ComUserExtractApply {

    private Long id;
    /**
     * 用户id
     */
    private Long comUserId;
    /**
     * 提现金额
     */
    private BigDecimal extractFee;
    /**
     * 提现方式信息id
     */
    private Long extractwayInfoId;
    /**
     * 提现申请时间
     */
    private Date applyTime;
    /**
     * 提现方式
     */
    private Long extractWay;
    /**
     * 审核通过时间
     */
    private Date extractTime;
    /**
     * 打款日期
     */
    private Date payTime;
    /**
     * 审核状态:0,待审核; 1,已拒绝; 2, 待打款;3,已付款
     */
    private Integer auditType;
    /**
     * 审核原因
     */
    private String auditCause;
    /**
     * 银行卡号
     */
    private String bankCard;
    /**
     * 银行名称
     */
    private String bankName;
    /**
     * 持卡人姓名
     */
    private String cardOwnerName;
    /**
     * 开户行名称
     */
    private String depositBankName;
    private String remark;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Long getComUserId() {
        return comUserId;
    }
    public void setComUserId(Long comUserId) {
        this.comUserId = comUserId;
    }
    public BigDecimal getExtractFee() {
        return extractFee;
    }
    public void setExtractFee(BigDecimal extractFee) {
        this.extractFee = extractFee;
        if(extractFee != null){
            this.extractFeeView = String.format("%.2f", extractFee).toString();
        }
    }
    public Long getExtractwayInfoId() {
        return extractwayInfoId;
    }
    public void setExtractwayInfoId(Long extractwayInfoId) {
        this.extractwayInfoId = extractwayInfoId;
    }
    public Date getApplyTime() {
        return applyTime;
    }
    public void setApplyTime(Date applyTime) {
        this.applyTime = applyTime;
        if(applyTime != null){
            applyTimeView = DateUtil.Date2String(applyTime, "MM-dd");
        }
    }
    public Long getExtractWay() {
        return extractWay;
    }
    public void setExtractWay(Long extractWay) {
        this.extractWay = extractWay;
    }
    public Date getExtractTime() {
        return extractTime;
    }
    public void setExtractTime(Date extractTime) {
        this.extractTime = extractTime;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getAuditType() {
        return auditType;
    }
    public void setAuditType(Integer auditType) {
        this.auditType = auditType;
    }
    public String getAuditCause() {
        return auditCause;
    }
    public void setAuditCause(String auditCause) {
        this.auditCause = auditCause == null ? null : auditCause.trim();
    }
    public String getBankCard() {
        return bankCard;
    }
    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
        if(bankCard != null && bankCard.length() > 8){
            this.bankCardView = bankCard.substring(0, 4)
                    + " **** **** "
                    + bankCard.substring(bankCard.length() - 4, bankCard.length());
        }
    }
    public String getBankName() {
        return bankName;
    }
    public void setBankName(String bankName) {
        this.bankName = bankName == null ? null : bankName.trim();
    }
    public String getCardOwnerName() {
        return cardOwnerName;
    }
    public void setCardOwnerName(String cardOwnerName) {
        this.cardOwnerName = cardOwnerName == null ? null : cardOwnerName.trim();
    }
    public String getDepositBankName() {
        return depositBankName;
    }
    public void setDepositBankName(String depositBankName) {
        this.depositBankName = depositBankName == null ? null : depositBankName.trim();
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    @Override
    public String toString() {
        return "ComUserExtractApply{" +
                "id=" + id +
                ", comUserId=" + comUserId +
                ", extractFee=" + extractFee +
                ", extractwayInfoId=" + extractwayInfoId +
                ", applyTime=" + applyTime +
                ", extractWay=" + extractWay +
                ", extractTime=" + extractTime +
                ", payTime=" + payTime +
                ", auditType=" + auditType +
                ", auditCause='" + auditCause + '\'' +
                ", bankCard='" + bankCard + '\'' +
                ", bankName='" + bankName + '\'' +
                ", cardOwnerName='" + cardOwnerName + '\'' +
                ", depositBankName='" + depositBankName + '\'' +
                ", remark='" + remark + '\'' +
                '}';
    }

    /**************************************以下为视图属性**************************************/
    private String auditTypeView;
    private String bankCardView;
    private String applyTimeView;
    private String extractFeeView;

    public String getAuditTypeView() {
        return auditTypeView;
    }

    public void setAuditTypeView(String auditTypeView) {
        this.auditTypeView = auditTypeView;
    }

    public String getBankCardView() {
        return bankCardView;
    }

    public void setBankCardView(String bankCardView) {
        this.bankCardView = bankCardView;
    }

    public String getApplyTimeView() {
        return applyTimeView;
    }

    public void setApplyTimeView(String applyTimeView) {
        this.applyTimeView = applyTimeView;
    }

    public String getExtractFeeView() {
        return extractFeeView;
    }

    public void setExtractFeeView(String extractFeeView) {
        this.extractFeeView = extractFeeView;
    }
}