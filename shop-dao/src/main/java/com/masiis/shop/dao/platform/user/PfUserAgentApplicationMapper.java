/*
 * PfUserAgentApplicationMapper.java
 * Copyright(C) 2014-2016 麦士集团
 * All rights reserved.
 * -----------------------------------------------
 * 2016-07-04 Created
 */
package com.masiis.shop.dao.platform.user;

import com.masiis.shop.dao.po.PfUserAgentApplication;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface PfUserAgentApplicationMapper {
    int deleteByPrimaryKey(Long id);

    int insert(PfUserAgentApplication record);

    PfUserAgentApplication selectByPrimaryKey(Long id);

    List<PfUserAgentApplication> selectAll();

    int updateByPrimaryKey(PfUserAgentApplication record);

    List<PfUserAgentApplication> selectByPhone(@Param("mobile") String mobile);

    PfUserAgentApplication selectByPhoneAndSkuId(@Param("mobile") String mobile,@Param("skuId") Integer skuId);

    /**
     * 条件查询
     * @param conditionMap
     * @return
     */
    List<Map<String, Object>> selectByCondition(Map<String, Object> conditionMap);


}