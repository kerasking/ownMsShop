package com.masiis.shop.dao.beans.material;

import com.masiis.shop.dao.po.ComSku;
import com.masiis.shop.dao.po.ComSkuMaterialLibrary;

import java.util.List;

/**
 * 素材库数据bean
 * Created by cai_tb on 16/7/1.
 */
public class MaterialLibrary extends ComSkuMaterialLibrary {

    private ComSku comSku;

    private List<MaterialGroup> materialGroups;

    private Integer isSubscript;//是否被订阅

    public ComSku getComSku() {
        return comSku;
    }

    public void setComSku(ComSku comSku) {
        this.comSku = comSku;
    }

    public List<MaterialGroup> getMaterialGroups() {
        return materialGroups;
    }

    public void setMaterialGroups(List<MaterialGroup> materialGroups) {
        this.materialGroups = materialGroups;
    }

    public Integer getIsSubscript() {
        return isSubscript;
    }

    public void setIsSubscript(Integer isSubscript) {
        this.isSubscript = isSubscript;
    }
}
