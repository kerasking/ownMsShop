package com.masiis.shop.dao.beans.product;

/**
 * Created by wangbingjian on 2016/4/10.
 */
public class SfShopSkuExtends {

    /**
     * 等级标志
     */
    private String icon;
    /**
     * 代理等级名称
     */
    private String agentName;
    /**
     * sku名称
     */
    private String skuName;

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    public String getSkuName() {
        return skuName;
    }

    public void setSkuName(String skuName) {
        this.skuName = skuName;
    }
}
