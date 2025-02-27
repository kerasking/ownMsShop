package com.masiis.shop.dao.beans.order;

import java.math.BigDecimal;

/**
 * BOrderAdd
 *
 * @author ZhaoLiang
 * @date 2016/4/17
 */
public class BOrderAdd {
    /**
     * 订单类型(0代理1补货2拿货3升级)
     */
    private Integer orderType = 0;
    /**
     * 用户id
     */
    private Long userId = 0L;
    /**
     * 新上级合伙人id
     */
    private Long pUserId = 0L;
    /**
     * 原上级id
     */
    private Long  oldPUserId;
    /**
     * 用户留言
     */
    private String userMessage = "";
    /**
     * 用户来源(1扫码2手机号搜索3世界市场)
     */
    private Integer userSource = 0;
    /**
     * 拿货方式(0未选择1平台代发2自己发货)
     */
    private Integer sendType = 0;
    /**
     * 合伙skuId
     */
    private Integer skuId = 0;
    /**
     * 运费
     */
    private BigDecimal shipAmount = BigDecimal.ZERO;
    /**
     * 数量
     */
    private Integer quantity = 0;
    /**
     * 合伙微信号
     */
    private String weiXinId = "";
    /**
     * 代理等级id
     */
    private Integer agentLevelId;
    /**
     * 用户收货信息
     */
    private Long userAddressId = 0l;
    /**
     * 通知单id 用于增加通知订单用
     */
    private Long upgradeNoticeId;

    private Integer currentAgentLevel;//当前级别，升级订单时使用

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getpUserId() {
        return pUserId;
    }

    public void setpUserId(Long pUserId) {
        this.pUserId = pUserId;
    }

    public Long getOldPUserId() {
        return oldPUserId;
    }

    public void setOldPUserId(Long oldPUserId) {
        this.oldPUserId = oldPUserId;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getUserSource() {
        return userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public String getWeiXinId() {
        return weiXinId;
    }

    public void setWeiXinId(String weiXinId) {
        this.weiXinId = weiXinId;
    }

    public Integer getAgentLevelId() {
        return agentLevelId;
    }

    public void setAgentLevelId(Integer agentLevelId) {
        this.agentLevelId = agentLevelId;
    }

    public Long getUserAddressId() {
        return userAddressId;
    }

    public void setUserAddressId(Long userAddressId) {
        this.userAddressId = userAddressId;
    }

    public Long getUpgradeNoticeId() {
        return upgradeNoticeId;
    }

    public void setUpgradeNoticeId(Long upgradeNoticeId) {
        this.upgradeNoticeId = upgradeNoticeId;
    }

    public Integer getCurrentAgentLevel() {
        return currentAgentLevel;
    }

    public void setCurrentAgentLevel(Integer currentAgentLevel) {
        this.currentAgentLevel = currentAgentLevel;
    }

}
