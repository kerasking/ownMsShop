package com.masiis.shop.common.beans.wx.wxpay;

import com.alibaba.fastjson.annotation.JSONField;
import com.masiis.shop.common.annotation.SignField;
import com.thoughtworks.xstream.annotations.XStreamAlias;

/**
 * Created by lzh on 2016/3/8.
 */
@XStreamAlias("xml")
public class BrandWCPayReq {
    private String appId;
    private String timeStamp;
    private String nonceStr;
    @JSONField(name = "package")
    @XStreamAlias("package")
    private String packages;
    private String signType;
    @SignField
    private String paySign;

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appid) {
        this.appId = appid;
    }

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getNonceStr() {
        return nonceStr;
    }

    public void setNonceStr(String nonceStr) {
        this.nonceStr = nonceStr;
    }

    public String getPackages() {
        return packages;
    }

    public void setPackages(String packages) {
        this.packages = packages;
    }

    public String getSignType() {
        return signType;
    }

    public void setSignType(String signType) {
        this.signType = signType;
    }

    public String getPaySign() {
        return paySign;
    }

    public void setPaySign(String paySign) {
        this.paySign = paySign;
    }
}
