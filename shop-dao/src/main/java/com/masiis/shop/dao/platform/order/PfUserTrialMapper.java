package com.masiis.shop.dao.platform.order;


import com.masiis.shop.dao.po.PfUserSku;
import com.masiis.shop.dao.po.PfUserTrial;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by 49134 on 2016/3/3.
 */
@Repository
public interface PfUserTrialMapper {

    /**
     * 根据id查询一条记录
     * @param id
     * @return
     */
    PfUserTrial selectById(@Param("id")Long id);


    /**
     * 根据条件查询记录
     * @param pfUserTrial
     * @return
     */
    List<PfUserTrial> selectByCondition(@Param("pfUserTrial")PfUserTrial pfUserTrial);

    /**
     * 
     * @author  hanzengzhi
     * @date  2016/3/9 11:31 
     */
    List<PfUserTrial> isApplyTrial(@Param("pfUserTrial")PfUserTrial pfUserTrial);

    /**
     * 添加一条记录
     * @param pfUserTrial
     */
    void insert(PfUserTrial pfUserTrial);

    /**
     * 根据id更新一条记录
     * @param pfUserTrial
     */
    void updateById(@Param("pfUserTrial")PfUserTrial pfUserTrial);

    /**
     * 根据id删除一条记录
     * @param id
     */
    void deleteById(@Param("id")Long id);

    void reason(@Param("pfUserTrial") PfUserTrial pfUserTrial);

    String selectReasonById(Long id);
}
