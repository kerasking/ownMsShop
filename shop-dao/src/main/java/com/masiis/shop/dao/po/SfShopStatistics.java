/*
 * SfShopStatistics.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-06-06 Created
 */
package com.masiis.shop.dao.po;

import java.math.BigDecimal;
import java.util.Date;

public class SfShopStatistics {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 小铺id
     */
    private Long shopId;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 总销售额
     */
    private BigDecimal incomeFee;
    /**
     * 总利润
     */
    private BigDecimal profitFee;
    /**
     * 店铺总订单
     */
    private Integer orderCount;
    /**
     * 店铺总销量
     */
    private Integer productCount;
    /**
     * 店铺浏览量
     */
    private Integer pageviewsCount;
    /**
     * 店铺分享数
     */
    private Integer shareCount;
    /**
     * 店铺总退单数
     */
    private Integer returnOrderCount;

    private String remark;
    private Integer version;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public Date getCreateTime() {
        return createTime;
    }
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
    public Long getShopId() {
        return shopId;
    }
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }
    public Long getUserId() {
        return userId;
    }
    public void setUserId(Long userId) {
        this.userId = userId;
    }
    public BigDecimal getIncomeFee() {
        return incomeFee;
    }
    public void setIncomeFee(BigDecimal incomeFee) {
        this.incomeFee = incomeFee;
    }
    public BigDecimal getProfitFee() {
        return profitFee;
    }
    public void setProfitFee(BigDecimal profitFee) {
        this.profitFee = profitFee;
    }
    public Integer getOrderCount() {
        return orderCount;
    }
    public void setOrderCount(Integer orderCount) {
        this.orderCount = orderCount;
    }
    public Integer getProductCount() {
        return productCount;
    }
    public void setProductCount(Integer productCount) {
        this.productCount = productCount;
    }
    public Integer getPageviewsCount() {
        return pageviewsCount;
    }
    public void setPageviewsCount(Integer pageviewsCount) {
        this.pageviewsCount = pageviewsCount;
    }
    public Integer getShareCount() {
        return shareCount;
    }
    public void setShareCount(Integer shareCount) {
        this.shareCount = shareCount;
    }
    public Integer getReturnOrderCount() {
        return returnOrderCount;
    }
    public void setReturnOrderCount(Integer returnOrderCount) {
        this.returnOrderCount = returnOrderCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Integer getVersion() {
        return version;
    }

    public void setVersion(Integer version) {
        this.version = version;
    }
}