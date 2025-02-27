package com.masiis.shop.web.mall.service.order;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.masiis.shop.dao.mall.shop.SfShopCartMapper;
import com.masiis.shop.dao.mall.shop.SfShopMapper;
import com.masiis.shop.dao.mall.shop.SfShopSkuMapper;
import com.masiis.shop.dao.platform.product.ComSkuMapper;
import com.masiis.shop.dao.po.ComSku;
import com.masiis.shop.dao.po.SfShop;
import com.masiis.shop.dao.po.SfShopCart;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * Created by hzz on 2016/4/9.
 */
@Service
public class SfShopCartService {

    private Log log = LogFactory.getLog(this.getClass());

    @Resource
    private SfShopCartMapper sfShopCartMapper;

    @Resource
    private SfShopSkuMapper sfShopSkuMapper;

    @Resource
    private SfShopMapper sfShopMapper;

    @Resource
    private ComSkuMapper comSkuMapper;

    /**
     * 获得用户购物车中的信息
     * @author hanzengzhi
     * @date 2016/4/9 10:38
     */
    public List<SfShopCart> getShopCartInfoByUserIdAndShopId(Long userId,Long shopId,Integer isCheck){
        return sfShopCartMapper.getShopCartInfoByUserIdAndShopId(userId,shopId,isCheck);
    }
    /**
     * 批量删除购物车中的信息
     * @author hanzengzhi
     * @date 2016/4/11 17:05
     */
    public int deleteByIds(String ids){
        return sfShopCartMapper.deleteByIds(ids);
    }

    /**
     * jjh
     * 添加商品到购物车
     */
    public void addProductToCart(Long shopId,Long userId,Integer skuId,Integer quantity,Long isOwnShip)throws Exception{
        SfShopCart ShopCart = new SfShopCart();
        //购物车功能，暂不使用
//        SfShopCart sfShopCart = sfShopCartMapper.getProductInfoByUserIdAndShipIdAndSkuId(userId,shopId,skuId);
//        if(sfShopCart!=null){
//            sfShopCartMapper.deleteByPrimaryKey(sfShopCart.getId());
//            log.info("----删除当前商品----");
//        }

        //单个商品的操作，清空购物车
        List<SfShopCart> sfShopCartList = sfShopCartMapper.getProductInfoByUserId(userId);
        if(sfShopCartList!=null && sfShopCartList.size()>0){
            for(SfShopCart sfShopCart :sfShopCartList){
                sfShopCartMapper.deleteByPrimaryKey(sfShopCart.getId());
                log.info("用户"+userId+"的购物车删除了一条记录"+sfShopCart.getId()+",当前店铺为"+sfShopCart.getSfShopId()+"商品为"+sfShopCart.getSkuId()+"");
            }
        }
        SfShop sfShop = sfShopMapper.selectByPrimaryKey(shopId);
        ComSku comSku = comSkuMapper.selectByPrimaryKey(skuId);
        ShopCart.setCreateTime(new Date());
        ShopCart.setSfShopId(shopId);
        ShopCart.setUserId(userId);
        ShopCart.setSkuId(skuId);
        ShopCart.setSpuId(comSku.getSpuId());
        ShopCart.setSfShopUserId(sfShop.getUserId());
        ShopCart.setQuantity(quantity);
        ShopCart.setIsCheck(1);
        ShopCart.setSort(0);
        if(isOwnShip==0){
            ShopCart.setSendMan(isOwnShip);//发货人信息 0平台
        }else {
            ShopCart.setSendMan(sfShop.getUserId());//发货人userId
        }
        sfShopCartMapper.insert(ShopCart);
        log.info("----加入cart 成功----");
    }

}
