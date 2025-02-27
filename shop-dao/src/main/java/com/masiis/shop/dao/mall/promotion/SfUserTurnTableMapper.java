/*
 * SfUserTurnTableMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-07-29 Created
 */
package com.masiis.shop.dao.mall.promotion;

import com.masiis.shop.dao.po.SfUserTurnTable;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface SfUserTurnTableMapper {
    int deleteByPrimaryKey(Long id);

    int insert(SfUserTurnTable record);

    SfUserTurnTable selectByPrimaryKey(Long id);

    SfUserTurnTable getSfUserTurnTable(@Param("userId")Long userId,@Param("turnTableId") Integer turnTableId);

    List<SfUserTurnTable> selectAll();

    int updateByPrimaryKey(SfUserTurnTable record);
}