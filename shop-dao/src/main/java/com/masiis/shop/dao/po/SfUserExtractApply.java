/*
 * SfUserExtractApply.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-10 Created
 */
package com.masiis.shop.dao.po;

import java.math.BigDecimal;
import java.util.Date;

public class SfUserExtractApply {

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
     * 1,微信; 2,支付宝; 3,银行卡
     */
    private Integer extractWay;
    /**
     * 审核通过时间
     */
    private Date auditTime;
    /**
     * 审核状态:0,待审核; 1,已拒绝; 2, 待打款;3,已付款 
     */
    private Integer auditType;
    /**
     * 审核原因
     */
    private String auditCause;
    /**
     * 打款时间
     */
    private Date payTime;
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
    }
    public Integer getExtractWay() {
        return extractWay;
    }
    public void setExtractWay(Integer extractWay) {
        this.extractWay = extractWay;
    }
    public Date getAuditTime() {
        return auditTime;
    }
    public void setAuditTime(Date auditTime) {
        this.auditTime = auditTime;
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
    public Date getPayTime() {
        return payTime;
    }
    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }
    public String getBankCard() {
        return bankCard;
    }
    public void setBankCard(String bankCard) {
        this.bankCard = bankCard == null ? null : bankCard.trim();
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
}