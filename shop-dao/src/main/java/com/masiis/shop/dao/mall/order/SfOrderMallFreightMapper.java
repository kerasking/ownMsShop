/*
 * SfOrderFreightMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-08 Created
 */
package com.masiis.shop.dao.mall.order;

import com.masiis.shop.dao.po.SfOrderFreight;

import java.util.List;

public interface SfOrderMallFreightMapper {
    List<SfOrderFreight> selectByOrderId(Long OrderId);
}