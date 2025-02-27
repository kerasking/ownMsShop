package com.masiis.shop.dao.beans.order;

import java.math.BigDecimal;
import java.util.Iterator;

/**
 * 升级订单详情信息
 */
public class BOrderUpgradeDetail {

    private String  name;                       //姓名
    private Long    upgradeNoticeId;           //通知单id
    private Integer upgradeStatus;             //通知单状态
    private Long    pfBorderId;                //订单id
    private Integer skuId;
    private Integer spuId;
    private Long    oldPUserId;                //原上级id
    private String  oldPUserName;              //原上级姓名
    private Integer upStatus;                  //上级处理状态
    private Long    newPUserId;                //新上级id
    private String  newPUserName;               //新上级名字
    private String skuName;                   //商品名称
    private BigDecimal priceRetail;           //销售价
    private Integer currentAgentLevel;        //当前级别
    private String currentAgentLevelName;    //当前级别名字
    private Integer applyAgentLevel;         //申请级别
    private String applyAgentLevelName;      //申请级别名字
    private String pAgentName;               //上级代理名
    private Integer quantity;                //代理数量
    private String  upgradeOrderCode;        //升级单号
    private BigDecimal bailChange;           //升级的保证金差额
    private BigDecimal totalPrice;           //总价共支付


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getSkuId() {
        return skuId;
    }

    public void setSkuId(Integer skuId) {
        this.skuId = skuId;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }

    public String getCurrentAgentLevelName() {
        return currentAgentLevelName;
    }

    public void setCurrentAgentLevelName(String currentAgentLevelName) {
        this.currentAgentLevelName = currentAgentLevelName;
    }

    public String getApplyAgentLevelName() {
        return applyAgentLevelName;
    }

    public void setApplyAgentLevelName(String applyAgentLevelName) {
        this.applyAgentLevelName = applyAgentLevelName;
    }

    public String getpAgentName() {
        return pAgentName;
    }

    public void setpAgentName(String pAgentName) {
        this.pAgentName = pAgentName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUpgradeOrderCode() {
        return upgradeOrderCode;
    }

    public void setUpgradeOrderCode(String upgradeOrderCode) {
        this.upgradeOrderCode = upgradeOrderCode;
    }

    public Integer getCurrentAgentLevel() {
        return currentAgentLevel;
    }

    public void setCurrentAgentLevel(Integer currentAgentLevel) {
        this.currentAgentLevel = currentAgentLevel;
    }

    public Integer getApplyAgentLevel() {
        return applyAgentLevel;
    }

    public void setApplyAgentLevel(Integer applyAgentLevel) {
        this.applyAgentLevel = applyAgentLevel;
    }

    public BigDecimal getBailChange() {
        return bailChange;
    }

    public void setBailChange(BigDecimal bailChange) {
        this.bailChange = bailChange;
    }

    public Integer getSpuId() {
        return spuId;
    }

    public void setSpuId(Integer spuId) {
        this.spuId = spuId;
    }

    public BigDecimal getPriceRetail() {
        return priceRetail;
    }

    public void setPriceRetail(BigDecimal priceRetail) {
        this.priceRetail = priceRetail;
    }

    public Long getOldPUserId() {
        return oldPUserId;
    }

    public void setOldPUserId(Long oldPUserId) {
        this.oldPUserId = oldPUserId;
    }

    public Long getNewPUserId() {
        return newPUserId;
    }

    public void setNewPUserId(Long newPUserId) {
        this.newPUserId = newPUserId;
    }

    public String getOldPUserName() {
        return oldPUserName;
    }

    public void setOldPUserName(String oldPUserName) {
        this.oldPUserName = oldPUserName;
    }

    public String getNewPUserName() {
        return newPUserName;
    }

    public void setNewPUserName(String newPUserName) {
        this.newPUserName = newPUserName;
    }

    public Long getUpgradeNoticeId() {
        return upgradeNoticeId;
    }

    public void setUpgradeNoticeId(Long upgradeNoticeId) {
        this.upgradeNoticeId = upgradeNoticeId;
    }

    public Integer getUpgradeStatus() {
        return upgradeStatus;
    }

    public void setUpgradeStatus(Integer upgradeStatus) {
        this.upgradeStatus = upgradeStatus;
    }

    public Long getPfBorderId() {
        return pfBorderId;
    }

    public void setPfBorderId(Long pfBorderId) {
        this.pfBorderId = pfBorderId;
    }

    public Integer getUpStatus() {
        return upStatus;
    }

    public void setUpStatus(Integer upStatus) {
        this.upStatus = upStatus;
    }
}
