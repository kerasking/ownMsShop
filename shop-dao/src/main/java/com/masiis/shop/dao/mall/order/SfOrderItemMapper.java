/*
 * SfOrderItemMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-08 Created
 */
package com.masiis.shop.dao.mall.order;

import com.masiis.shop.dao.po.SfOrderItem;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SfOrderItemMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SfOrderItem record);

    SfOrderItem selectByPrimaryKey(Long id);

    List<SfOrderItem> selectAll();

    int updateByPrimaryKey(SfOrderItem record);

    List<SfOrderItem> getOrderItemByOrderId(Long sfOrderId);

    List<SfOrderItem> selectBySfOrder(Long id);
}