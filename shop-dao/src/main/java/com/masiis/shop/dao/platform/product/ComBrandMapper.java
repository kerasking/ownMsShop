package com.masiis.shop.dao.platform.product;

import com.masiis.shop.dao.po.ComBrand;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by cai_tb on 16/3/5.
 */
@Repository
public interface ComBrandMapper {

    /**
     * 根据id查询一条记录
     * @param id
     * @return
     */
    ComBrand selectById(Integer id);

    /**
     * 根据条件查询记录
     * @param comBrand
     * @return
     */
    List<ComBrand> selectByCondition(@Param("comBrand")ComBrand comBrand);

    /**
     * 添加一条记录
     * @param comBrand
     */
    void insert(ComBrand comBrand);

    /**
     * 根据id更新一条记录
     * @param comBrand
     */
    void updateById(ComBrand comBrand);

    /**
     * 根据id删除一条记录
     * @param id
     */
    void deleteById(@Param("id")Long id);

    List<ComBrand> selectAll();


    /**
     * jjh
     * 查找品牌
     */
    ComBrand checkBrandBySkuId(Integer skuId);

    List<ComBrand> selectAllForWorld();

    List<ComBrand> selectAllForWorldPage();

    List<ComBrand> selectAllForFamily(Long userId);

    /**
     * 根据skuId查品牌
     *
     * @param skuId
     * @return
     */
    ComBrand selectBySkuId(@Param("skuId") Integer skuId);
}
