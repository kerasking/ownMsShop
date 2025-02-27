package com.masiis.shop.api.bean.user;

import com.masiis.shop.api.bean.base.BaseRes;

import java.math.BigDecimal;
import java.util.List;

/**
 * Created by wangbingjian on 2016/5/5.
 */
public class PersonalCenterRes extends BaseRes {
    /**
     * 电话号码,用户唯一标识
     */
    private String mobile;
    /**
     * 用户在平台的昵称,不同于微信昵称
     */
    private String wxNkName;
    /**
     * 微信头像地址
     */
    private String wxHeadImg;
    /**
     * 是否绑定（0否1是）
     */
    private Integer isBinding;
    /**
     * 审核状态(0未审核1已提交审核(审核中)2审核通过3审核不通过)
     */
    private Integer auditStatus;
    /**
     * 审核状态名称
     */
    private String auditStatusName;
    /**
     * 可提现金额
     */
    private String extractableFee;
    /**
     * 等级图标和代理商品名称
     */
    private List<SkuAgentDetail> skuAgentDetails;
    /**
     * 微信号

     */
    private String wxId;

    public String getWxId() { return wxId; }

    public void setWxId(String wxId) { this.wxId = wxId; }
    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getWxHeadImg() {
        return wxHeadImg;
    }

    public void setWxHeadImg(String wxHeadImg) {
        this.wxHeadImg = wxHeadImg;
    }

    public String getWxNkName() {
        return wxNkName;
    }

    public void setWxNkName(String wxNkName) {
        this.wxNkName = wxNkName;
    }

    public Integer getIsBinding() {
        return isBinding;
    }

    public void setIsBinding(Integer isBinding) {
        this.isBinding = isBinding;
    }

    public Integer getAuditStatus() {
        return auditStatus;
    }

    public void setAuditStatus(Integer auditStatus) {
        this.auditStatus = auditStatus;
    }

    public String getAuditStatusName() {
        return auditStatusName;
    }

    public void setAuditStatusName(String auditStatusName) {
        this.auditStatusName = auditStatusName;
    }

    public String getExtractableFee() {
        return extractableFee;
    }

    public void setExtractableFee(String extractableFee) {
        this.extractableFee = extractableFee;
    }

    public List<SkuAgentDetail> getSkuAgentDetails() {
        return skuAgentDetails;
    }

    public void setSkuAgentDetails(List<SkuAgentDetail> skuAgentDetails) {
        this.skuAgentDetails = skuAgentDetails;
    }

}
