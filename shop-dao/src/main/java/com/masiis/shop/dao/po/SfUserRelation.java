/*
 * SfUserRelation.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-07-04 Created
 */
package com.masiis.shop.dao.po;

import java.util.Date;

public class SfUserRelation {

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
     * 上一级用户id
     */
    private Long userPid;
    /**
     * 树结构编码(方便查询)
     */
    private String treeCode;
    /**
     * 树结构等级(方便查询)
     */
    private Integer treeLevel;
    /**
     * 是否在小铺中购买过(0否1是)
     */
    private Integer isBuy;
    /**
     * 是否代言人(0否1是)
     */
    private Integer isSpokesman;
    /**
     * 备注
     */
    private String remark;

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
    public Long getUserPid() {
        return userPid;
    }
    public void setUserPid(Long userPid) {
        this.userPid = userPid;
    }
    public String getTreeCode() {
        return treeCode;
    }
    public void setTreeCode(String treeCode) {
        this.treeCode = treeCode == null ? null : treeCode.trim();
    }
    public Integer getTreeLevel() {
        return treeLevel;
    }
    public void setTreeLevel(Integer treeLevel) {
        this.treeLevel = treeLevel;
    }
    public Integer getIsBuy() {
        return isBuy;
    }
    public void setIsBuy(Integer isBuy) {
        this.isBuy = isBuy;
    }
    public String getRemark() {
        return remark;
    }
    public void setRemark(String remark) {
        this.remark = remark == null ? null : remark.trim();
    }

    public Integer getIsSpokesman() {
        return isSpokesman;
    }

    public void setIsSpokesman(Integer isSpokesman) {
        this.isSpokesman = isSpokesman;
    }
}