package com.masiis.shop.admin.service.product;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.masiis.shop.common.enums.platform.UserSkuStockLogType;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.platform.product.PfUserSkuStockLogMapper;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuStockMapper;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfUserSkuStock;
import com.masiis.shop.dao.po.PfUserSkuStockLog;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * @Date 2016/5/13
 * @Auther lzh
 */
@Service
public class PfUserSkuStockService {
    private Logger log = Logger.getLogger(this.getClass());
    @Resource
    private PfUserSkuStockMapper pfUserSkuStockMapper;
    @Resource
    private PfUserSkuStockLogMapper userSkuStockLogMapper;
    @Resource
    private ComUserMapper comUserMapper;


    /**
     * 判断用户是否有足够的库存
     *
     * @param userId 用户id
     * @param skuId  商品id
     * @return
     */
    public Boolean isEnoughStock(Long userId, Integer skuId) {
        log.info("判断用户是否有足够的库存-----userId----" + userId + "-----skuId----" + skuId);
        PfUserSkuStock pfUserSkuStock = selectByUserIdAndSkuId(userId, skuId);
        Boolean isStock = true;
        if (pfUserSkuStock == null) {
            isStock = false;
        }
        if (pfUserSkuStock.getCustomStock() <= 0) {
            isStock = false;
        }
        log.info("判断用户是否有足够的库存----------" + isStock);
        return isStock;
    }

    /**
     * 更新代理平台库存
     *
     * @param change     变化值 例如 1，2，3 无负数，根据业务减库存
     * @param stock      代理库存对象
     * @param billId     变化根据单据号
     * @param handleType 变更类型
     */
    public void updateUserSkuStockWithLog(Integer change, PfUserSkuStock stock, Long billId, UserSkuStockLogType handleType) {
        Integer beforeStock = stock.getStock();
        // 改变stock库存
        getAfterStockByType(change, stock, handleType);
        // 获取改变后的库存
        Integer afterStock = stock.getStock();

        if (updateByIdAndVersions(stock) != 1) {
            throw new BusinessException("修改用户库存失败");
        }

        PfUserSkuStockLog stockLog = createPfUserSkuStockLog(stock, beforeStock, afterStock, handleType, billId);
        userSkuStockLogMapper.insert(stockLog);
    }

    private PfUserSkuStockLog createPfUserSkuStockLog(PfUserSkuStock stock, Integer beforeStock, Integer afterStock, UserSkuStockLogType handleType, Long billId) {
        PfUserSkuStockLog res = new PfUserSkuStockLog();

        res.setBillId(billId);
        res.setCreateTime(new Date());
        res.setPfUserSkuStockId(stock.getId());
        res.setPrevStock(beforeStock);
        res.setNextStock(afterStock);
        res.setSkuId(stock.getSkuId());
        res.setSpuId(stock.getSpuId());
        res.setUserId(stock.getUserId());
        res.setType(handleType.getCode());
        res.setRemark(handleType.getDesc());

        return res;
    }

    /**
     * 根据操作类型和变化值来计算操作后的库存
     *
     * @param change
     * @param before
     * @param handleType
     */
    private void getAfterStockByType(Integer change, PfUserSkuStock before, UserSkuStockLogType handleType) {
        Integer afterStock = before.getStock();
        Integer fronzeStock = before.getFrozenStock();
        Integer registerGiveSkuStock = before.getRegisterGiveSkuStock();
        switch (handleType) {
            case agent:
                afterStock += change;
                break;
            case downAgent:
                afterStock -= change;
                fronzeStock -= change;
                registerGiveSkuStock -= change;
                break;
            case shopOrder:
                afterStock -= change;
                fronzeStock -= change;
                registerGiveSkuStock -= change;
                break;
            case shopReturn:
                afterStock += change;
                break;
            case STORAGECHANGE_BILL_ADD:
                afterStock += change;
                break;
            case PROMOTION_ADD:
                afterStock += change;
                registerGiveSkuStock += change;
                break;
            case PROMOTION_REDUCE:
                afterStock -= change;
                registerGiveSkuStock -= change;
                break;
            case TAKE:
                afterStock -= change;
                fronzeStock -= change;
                break;
            default:
                throw new BusinessException("没有这种出入库类型!");
        }
        //如果锁定库存不够减，那么改为0不能为负数
        if (registerGiveSkuStock < 0) {
            registerGiveSkuStock = 0;
        }
        before.setStock(afterStock);
        before.setFrozenStock(fronzeStock);
        before.setRegisterGiveSkuStock(registerGiveSkuStock);
        if (before.getStock().intValue() < 0) {
            throw new BusinessException("库存变动后小于0,错误");
        }
        if (before.getFrozenStock().intValue() < 0) {
            throw new BusinessException("冻结库存变动后小于0,错误");
        }
    }

    /**
     * 根据用户id和skuid获取代理库存对象
     *
     * @param userId
     * @param skuId
     * @return
     */
    public PfUserSkuStock selectByUserIdAndSkuId(Long userId, Integer skuId) {
        return pfUserSkuStockMapper.selectByUserIdAndSkuId(userId, skuId);
    }

    public PfUserSkuStock selectByUserIdAndSkuIdAndSpuId(Long userId, Integer skuId, Integer spuId) {
        return pfUserSkuStockMapper.selectByUserIdAndSkuIdAndSpuId(userId, skuId, spuId);
    }


    /**
     * 更新库存 带乐观锁
     *
     * @param pfUserSkuStock
     * @return
     */
    public int updateByIdAndVersions(PfUserSkuStock pfUserSkuStock) {
        return pfUserSkuStockMapper.updateByIdAndVersion(pfUserSkuStock);
    }


    public PfUserSkuStock initPfUserSkuStock(Long userId,Integer skuId,Integer spuId){
        PfUserSkuStock pfUserSkuStock = new PfUserSkuStock();
        pfUserSkuStock = new PfUserSkuStock();
        pfUserSkuStock.setCreateTime(new Date());
        pfUserSkuStock.setUserId(userId);
        pfUserSkuStock.setSpuId(spuId);
        pfUserSkuStock.setSkuId(skuId);
        pfUserSkuStock.setStock(0);
        pfUserSkuStock.setFrozenStock(0);
        pfUserSkuStock.setRegisterGiveSkuStock(0);
        pfUserSkuStock.setFrozenCustomStock(0);
        pfUserSkuStock.setCustomStock(0);
        pfUserSkuStock.setVersion(0);
        int i = insert(pfUserSkuStock);
        if (i!=1){
            throw new BusinessException("初始化用户库存失败----userId---"+userId+"----skuId---"+skuId+"---spuId---"+spuId);
        }
        return pfUserSkuStock;
    }

    public int insert(PfUserSkuStock pfUserSkuStock) {
        return pfUserSkuStockMapper.insert(pfUserSkuStock);
    }

    public int deleteByPrimaryKey(Long id) {
        return pfUserSkuStockMapper.deleteByPrimaryKey(id);
    }

    public PfUserSkuStock selectByPrimaryKey(Long id) {
        return pfUserSkuStockMapper.selectByPrimaryKey(id);
    }

    public List<PfUserSkuStock> selectAll() {
        return pfUserSkuStockMapper.selectAll();
    }

    public List<PfUserSkuStock> selectByUserId(Long userId) {
        return pfUserSkuStockMapper.selectByUserId(userId);
    }

    public List<Map<String, Object>> selectReplenishStock() {
        return pfUserSkuStockMapper.selectReplenishStock();
    }

    /**
     * 查找商品库存
     *
     * @param userId
     * @param skuId
     * @return
     */
    public PfUserSkuStock findByUserIdAndSkuId(Long userId, Integer skuId) {
        return pfUserSkuStockMapper.selectByUserIdAndSkuId(userId, skuId);
    }

    public List<Map<String, Object>> selectByCondition(Integer pageNumber, Integer pageSize,
                                                       String realName, String mobile, String skuName) {

        Map<String, Object> conditionMap = new HashMap<>();
        if (StringUtils.isNotBlank(realName)) {
            conditionMap.put("realName", realName);
        }
        if (StringUtils.isNotBlank(mobile)) {
            conditionMap.put("mobile", mobile);
        }
        if (StringUtils.isNotBlank(skuName)) {
            conditionMap.put("skuName", skuName);
        }

        PageHelper.startPage(pageNumber, pageSize, null);
        List<Map<String, Object>> userSkuStockList = pfUserSkuStockMapper.selectByCondition(conditionMap);

        return userSkuStockList;
    }

}
