/*
 * PfBorder.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-03-23 Created
 */
package com.masiis.shop.dao.po;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

public class PfBorder {

    private Long id;
    /**
     * 创建时间
     */
    private Date createTime;
    /**
     * 创建人
     */
    private Long createMan;
    /**
     * 订单号
     */
    private String orderCode;
    /**
     * 用户id
     */
    private Long userId;
    /**
     * 上一级用户id，平台为0
     */
    private Long userPid;
    /**
     * 用户来源(1世界市场2手机号搜索3扫码)
     */
    private Integer userSource;
    /**
     * 买家留言
     */
    private String userMessage;
    /**
     * 平台供应商id(暂时不用，保留)
     */
    private Integer supplierId;
    /**
     * 修改时间
     */
    private Date modifyTime;
    /**
     * 修改人
     */
    private Long modifyMan;
    /**
     * 应收费用
     */
    private BigDecimal receivableAmount;
    /**
     * 订单费用
     */
    private BigDecimal orderAmount;
    /**
     * 保证金
     */
    private BigDecimal bailAmount;
    /**
     * 商品总费用
     */
    private BigDecimal productAmount;
    /**
     * 推荐奖励金额
     */
    private BigDecimal recommenAmount;
    /**
     * 运费
     */
    private BigDecimal shipAmount;
    /**
     * 支付金额
     */
    private BigDecimal payAmount;
    /**
     * 支付时间
     */
    private Date payTime;
    /**
     * 配送商id
     */
    private Integer shipManId;
    /**
     * 配送商名称
     */
    private String shipManName;
    /**
     * 配送类型
     */
    private Integer shipType;
    /**
     * 发货时间
     */
    private Date shipTime;
    /**
     * 配送备注，只工作日送货、都可送货
     */
    private String shipRemark;
    /**
     * 拿货方式(0未选择1平台代发2自己发货)
     */
    private Integer sendType;
    /**
     * 订单类型(0代理1补货2拿货3升级)
     */
    private Integer orderType;
    /**
     * 订单状态
     */
    private Integer orderStatus;
    /**
     * 物流状态
     */
    private Integer shipStatus;
    /**
     * 支付状态: 0,未付款; 1,已付款
     */
    private Integer payStatus;
    /**
     * 是否结算(0未结算1已结算)
     */
    private Integer isCounting;
    /**
     * 发货标志位(0未发货1已发货)
     */
    private Integer isShip;
    /**
     * 收货标志位(0未收货1已收货)
     */
    private Integer isReceipt;
    /**
     * 确认收货时间
     */
    private Date receiptTime;
    /**
     * 换货订单号
     */
    private Long replaceOrderId;
    /**
     * 备注
     */
    private String remark;
    /**
     * 订单商品状态名称
     */
    private String orderSkuStatus;
    private String imgUrl;//图片地址
    /**
     * 数量
     */
    private Integer totalQuantity = 0;
    private String pidUserName;
    private String payTimes;//线下支付到期时间
    private String orderMoney;
    private String bailMoney;

    private ComUser userName;
    private ComUser userPname;
    /**
     * 订单商品
     */
    private List<PfBorderItem> pfBorderItems;
    private PfBorderConsignee pfBorderConsignee;

    private String beginTime;
    private String endTime;

    private String orderStatusDes;//订单状态描述

    private String orderTypeDes;//订单类型描述

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

    public Long getCreateMan() {
        return createMan;
    }

    public void setCreateMan(Long createMan) {
        this.createMan = createMan;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
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

    public Integer getUserSource() {
        return userSource;
    }

    public void setUserSource(Integer userSource) {
        this.userSource = userSource;
    }

    public String getUserMessage() {
        return userMessage;
    }

    public void setUserMessage(String userMessage) {
        this.userMessage = userMessage;
    }

    public Integer getSupplierId() {
        return supplierId;
    }

    public void setSupplierId(Integer supplierId) {
        this.supplierId = supplierId;
    }

    public Date getModifyTime() {
        return modifyTime;
    }

    public void setModifyTime(Date modifyTime) {
        this.modifyTime = modifyTime;
    }

    public Long getModifyMan() {
        return modifyMan;
    }

    public void setModifyMan(Long modifyMan) {
        this.modifyMan = modifyMan;
    }

    public BigDecimal getReceivableAmount() {
        return receivableAmount;
    }

    public void setReceivableAmount(BigDecimal receivableAmount) {
        this.receivableAmount = receivableAmount;
    }

    public BigDecimal getOrderAmount() {
        return orderAmount;
    }

    public void setOrderAmount(BigDecimal orderAmount) {
        this.orderAmount = orderAmount;
    }

    public BigDecimal getBailAmount() {
        return bailAmount;
    }

    public void setBailAmount(BigDecimal bailAmount) {
        this.bailAmount = bailAmount;
    }

    public BigDecimal getProductAmount() {
        return productAmount;
    }

    public void setProductAmount(BigDecimal productAmount) {
        this.productAmount = productAmount;
    }

    public BigDecimal getRecommenAmount() {
        return recommenAmount;
    }

    public void setRecommenAmount(BigDecimal recommenAmount) {
        this.recommenAmount = recommenAmount;
    }

    public BigDecimal getShipAmount() {
        return shipAmount;
    }

    public void setShipAmount(BigDecimal shipAmount) {
        this.shipAmount = shipAmount;
    }

    public BigDecimal getPayAmount() {
        return payAmount;
    }

    public void setPayAmount(BigDecimal payAmount) {
        this.payAmount = payAmount;
    }

    public Date getPayTime() {
        return payTime;
    }

    public void setPayTime(Date payTime) {
        this.payTime = payTime;
    }

    public Integer getShipManId() {
        return shipManId;
    }

    public void setShipManId(Integer shipManId) {
        this.shipManId = shipManId;
    }

    public String getShipManName() {
        return shipManName;
    }

    public void setShipManName(String shipManName) {
        this.shipManName = shipManName;
    }

    public Integer getShipType() {
        return shipType;
    }

    public void setShipType(Integer shipType) {
        this.shipType = shipType;
    }

    public Date getShipTime() {
        return shipTime;
    }

    public void setShipTime(Date shipTime) {
        this.shipTime = shipTime;
    }

    public String getShipRemark() {
        return shipRemark;
    }

    public void setShipRemark(String shipRemark) {
        this.shipRemark = shipRemark;
    }

    public Integer getSendType() {
        return sendType;
    }

    public void setSendType(Integer sendType) {
        this.sendType = sendType;
    }

    public Integer getOrderType() {
        return orderType;
    }

    public void setOrderType(Integer orderType) {
        this.orderType = orderType;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public Integer getShipStatus() {
        return shipStatus;
    }

    public void setShipStatus(Integer shipStatus) {
        this.shipStatus = shipStatus;
    }

    public Integer getPayStatus() {
        return payStatus;
    }

    public void setPayStatus(Integer payStatus) {
        this.payStatus = payStatus;
    }

    public Integer getIsCounting() {
        return isCounting;
    }

    public void setIsCounting(Integer isCounting) {
        this.isCounting = isCounting;
    }

    public Integer getIsShip() {
        return isShip;
    }

    public void setIsShip(Integer isShip) {
        this.isShip = isShip;
    }

    public Integer getIsReceipt() {
        return isReceipt;
    }

    public void setIsReceipt(Integer isReceipt) {
        this.isReceipt = isReceipt;
    }

    public Date getReceiptTime() {
        return receiptTime;
    }

    public void setReceiptTime(Date receiptTime) {
        this.receiptTime = receiptTime;
    }

    public Long getReplaceOrderId() {
        return replaceOrderId;
    }

    public void setReplaceOrderId(Long replaceOrderId) {
        this.replaceOrderId = replaceOrderId;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getOrderSkuStatus() {
        return orderSkuStatus;
    }

    public void setOrderSkuStatus(String orderSkuStatus) {
        this.orderSkuStatus = orderSkuStatus;
    }

    public String getImgUrl() {
        return imgUrl;
    }

    public void setImgUrl(String imgUrl) {
        this.imgUrl = imgUrl;
    }

    public Integer getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(Integer totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public String getPidUserName() {
        return pidUserName;
    }

    public void setPidUserName(String pidUserName) {
        this.pidUserName = pidUserName;
    }

    public String getPayTimes() {
        return payTimes;
    }

    public void setPayTimes(String payTimes) {
        this.payTimes = payTimes;
    }

    public String getOrderMoney() {
        return orderMoney;
    }

    public void setOrderMoney(String orderMoney) {
        this.orderMoney = orderMoney;
    }

    public String getBailMoney() {
        return bailMoney;
    }

    public void setBailMoney(String bailMoney) {
        this.bailMoney = bailMoney;
    }

    public ComUser getUserName() {
        return userName;
    }

    public void setUserName(ComUser userName) {
        this.userName = userName;
    }

    public ComUser getUserPname() {
        return userPname;
    }

    public void setUserPname(ComUser userPname) {
        this.userPname = userPname;
    }

    public List<PfBorderItem> getPfBorderItems() {
        return pfBorderItems;
    }

    public void setPfBorderItems(List<PfBorderItem> pfBorderItems) {
        this.pfBorderItems = pfBorderItems;
    }

    public PfBorderConsignee getPfBorderConsignee() {
        return pfBorderConsignee;
    }

    public void setPfBorderConsignee(PfBorderConsignee pfBorderConsignee) {
        this.pfBorderConsignee = pfBorderConsignee;
    }

    public String getBeginTime() {
        return beginTime;
    }

    public void setBeginTime(String beginTime) {
        this.beginTime = beginTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getOrderStatusDes() {
        return orderStatusDes;
    }

    public void setOrderStatusDes(String orderStatusDes) {
        this.orderStatusDes = orderStatusDes;
    }

    public String getOrderTypeDes() {
        return orderTypeDes;
    }

    public void setOrderTypeDes(String orderTypeDes) {
        this.orderTypeDes = orderTypeDes;
    }

    @Override
    public String toString() {
        return "PfBorder{" +
                "id=" + id +
                ", createTime=" + createTime +
                ", createMan=" + createMan +
                ", orderCode='" + orderCode + '\'' +
                ", userId=" + userId +
                ", userPid=" + userPid +
                ", userSource=" + userSource +
                ", userMessage='" + userMessage + '\'' +
                ", supplierId=" + supplierId +
                ", modifyTime=" + modifyTime +
                ", modifyMan=" + modifyMan +
                ", receivableAmount=" + receivableAmount +
                ", orderAmount=" + orderAmount +
                ", bailAmount=" + bailAmount +
                ", productAmount=" + productAmount +
                ", recommenAmount=" + recommenAmount +
                ", shipAmount=" + shipAmount +
                ", payAmount=" + payAmount +
                ", payTime=" + payTime +
                ", shipManId=" + shipManId +
                ", shipManName='" + shipManName + '\'' +
                ", shipType=" + shipType +
                ", shipTime=" + shipTime +
                ", shipRemark='" + shipRemark + '\'' +
                ", sendType=" + sendType +
                ", orderType=" + orderType +
                ", orderStatus=" + orderStatus +
                ", shipStatus=" + shipStatus +
                ", payStatus=" + payStatus +
                ", isCounting=" + isCounting +
                ", isShip=" + isShip +
                ", isReceipt=" + isReceipt +
                ", receiptTime=" + receiptTime +
                ", replaceOrderId=" + replaceOrderId +
                ", remark='" + remark + '\'' +
                ", orderSkuStatus='" + orderSkuStatus + '\'' +
                ", imgUrl='" + imgUrl + '\'' +
                ", totalQuantity=" + totalQuantity +
                ", pidUserName='" + pidUserName + '\'' +
                ", payTimes='" + payTimes + '\'' +
                ", orderMoney='" + orderMoney + '\'' +
                ", bailMoney='" + bailMoney + '\'' +
                ", userName=" + userName +
                ", userPname=" + userPname +
                ", pfBorderItems=" + pfBorderItems +
                ", pfBorderConsignee=" + pfBorderConsignee +
                ", beginTime='" + beginTime + '\'' +
                ", endTime='" + endTime + '\'' +
                ", orderStatusDes='" + orderStatusDes + '\'' +
                ", orderTypeDes='" + orderTypeDes + '\'' +
                '}';
    }
}