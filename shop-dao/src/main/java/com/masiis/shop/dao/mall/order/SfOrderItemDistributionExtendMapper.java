/*
 * SfOrderItemDistributionMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-04-08 Created
 */
package com.masiis.shop.dao.mall.order;

import com.masiis.shop.dao.po.SfOrderItemDistribution;
import org.apache.ibatis.annotations.Param;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface SfOrderItemDistributionExtendMapper {

    List<SfOrderItemDistribution> selectCommissionRecordByUserId(Long userId);

    Integer selectCommissionRecordCountByUserId(@Param("userId") Long userId);

    List<Map<String,Object>> selectSumAmount(@Param("userId") Long userId);
    Map<String,BigDecimal> selectUserAmount(@Param("userId") Long userId);
}