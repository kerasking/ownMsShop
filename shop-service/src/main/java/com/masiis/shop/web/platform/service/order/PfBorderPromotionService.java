package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.common.enums.platform.*;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.common.util.PromotionMakeUtils;
import com.masiis.shop.dao.platform.order.PfBorderPromotionMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.product.PfSkuStockService;
import com.masiis.shop.web.platform.service.product.PfUserSkuStockService;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * Created by hzz on 2016/9/6.
 */
@Service
@Transactional
public class PfBorderPromotionService {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfBorderPromotionMapper pfBorderPromotionMapper;
    @Resource
    private PfUserSkuStockService userSkuStockService;
    @Resource
    private PfSkuStockService pfSkuStockService;
    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private SkuService skuService;
    @Resource
    private PfUserSkuStockService pfUserSkuStockService;


    private BigDecimal unitPrice = null;
    private BigDecimal totalPrice = null;
    private String promotionStartDateString = PromotionMakeUtils.promotionStartDateString;
    private String promotionStartEndString = PromotionMakeUtils.promotionStartEndString;
    private Integer giveSkuAgentLevel = PromotionMakeUtils.giveSkuAgentLevel; //赠送商品的等级
    private Integer giveSkuQuantity = PromotionMakeUtils.giveSkuQuantity;//赠送的商品数量
    private static final Integer giveSkuId = PromotionMakeUtils.giveSkuId;


    public int update(PfBorderPromotion pfBorderPromotion) {
        return pfBorderPromotionMapper.updateByPrimaryKey(pfBorderPromotion);
    }

    /**
     * 下单时增加平台冻结库存 并插入到 pf_border_promotion 表中
     *
     * @param orderType
     * @param agentLevelId
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    public void registerGiveSkuInitStock(Long pfBorderId,
                                         Long userId,
                                         Long userPid,
                                         Integer orderType,
                                         Integer agentLevelId) {
        //判断条件是否满足
        ComSku comSku = skuService.getComSkuBySkuId(giveSkuId);
        Integer spuId = comSku.getSpuId();
        Integer skuId = comSku.getId();
        String skuName = comSku.getName();
        if (registerGiveSkuCondition(spuId, skuId, orderType, agentLevelId)) {
            //插入订单促销活动赠品表
            insert(pfBorderId,
                    userId,
                    userPid,
                    spuId,
                    skuId,
                    skuName,
                    giveSkuQuantity,
                    unitPrice,
                    totalPrice,
                    PfBorderPromotionIsSendEnum.NO_GiVE.getCode(),
                    PfBorderPromotionIsTakeEnum.NO_MAY_TAKE.getCode(),
                    "小白注册赠送商品插入");
        }
    }

    /**
     * 判断是否给小白赠送商品
     *
     * @param spuId
     * @param skuId
     * @param orderType
     * @param agentLevelId
     * @return
     */
    private Boolean registerGiveSkuCondition(Integer spuId, Integer skuId, Integer orderType, Integer agentLevelId) {
        //活动是否开启
        if (isOpenPromotion()) {
            //是否是合伙订单
            if (orderType.equals(BOrderType.agent.getCode())) {
                //是否是合伙的是最低等级
                //ComAgentLevel comAgentLevel = comAgentLevelService.getMinLevelByIsOrganizationShow(1);
                //if (orderType.equals(comAgentLevel.getId())){
                if (agentLevelId.equals(giveSkuAgentLevel)) {
                    //平台商品是否足够
                    PfSkuAgent pfSkuAgent = skuAgentService.getBySkuIdAndLevelId(skuId, agentLevelId);
                    if (pfSkuAgent != null) {
                        Map<String, Object> map = pfSkuStockService.isEnoughPlatformSku(pfSkuAgent.getQuantity(), spuId, skuId);
                        Boolean isEnoughPlatformSku = (Boolean) map.get("isEnoughPlatformSku");
                        if (isEnoughPlatformSku) {
                            log.info("满足活动条件");
                            unitPrice = pfSkuAgent.getUnitPrice();
                            totalPrice = pfSkuAgent.getTotalPrice();
                            return true;
                        } else {
                            log.info("-------给小白赠送商品----平台库存不足");
                            return false;
                        }
                    } else {
                        log.info("-----等级商品的代理信息不存在--------");
                    }
                } else {
                    log.info("-----等级不是最低等级------");
                    return false;
                }
            } else {
                log.info("------给小白赠送商品不是代理订单不赠送商品------");
                return false;
            }
        } else {
            log.info("-----活动没有开启不赠送商品------------");
        }
        return false;
    }

    /**
     * 判断活动是否开启
     *
     * @return
     */
    private Boolean isOpenPromotion() {
        try {
            log.info("判断活动时间是否开启-----start");
            SimpleDateFormat sdf = new SimpleDateFormat(DateUtil.YYYYMMDDFMT);
            Date startDate = sdf.parse(promotionStartDateString);
            Date endDate = sdf.parse(promotionStartEndString);
            Date currentDate = new Date();
            //如果时间1等于时间2，返回0，如果时间1小于时间2，返回负值，如果时间1大于时间2，返回正值
            if (DateUtil.compare(currentDate, startDate) > 0) {
                if (DateUtil.compare(currentDate, endDate) < 0) {
                    return true;
                }
            }
            log.info("判断活动时间是否开启-----end");
            return false;
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }


    /**
     * 插入订单促销活动赠品表
     *
     * @param pfBorderId
     * @param userId
     * @param spuId
     * @param skuId
     * @param skuName
     * @param quantity
     * @param unitPrice
     * @param totalPrice
     * @param isSend
     * @param isTake
     * @param remark
     */
    @Transactional(propagation = Propagation.REQUIRED, readOnly = false)
    private void insert(Long pfBorderId,
                        Long userId,
                        Long userPid,
                        Integer spuId,
                        Integer skuId,
                        String skuName,
                        Integer quantity,
                        BigDecimal unitPrice,
                        BigDecimal totalPrice,
                        Integer isSend,
                        Integer isTake,
                        String remark) {
        PfBorderPromotion pfBorderPromotion = new PfBorderPromotion();
        pfBorderPromotion.setCreateTime(new Date());
        pfBorderPromotion.setPfBorderId(pfBorderId);
        pfBorderPromotion.setPfPromotionId(0);
        pfBorderPromotion.setUserId(userId);
        pfBorderPromotion.setUserPid(userPid);
        pfBorderPromotion.setSpuId(spuId);
        pfBorderPromotion.setSkuId(skuId);
        pfBorderPromotion.setSkuName(skuName);
        pfBorderPromotion.setQuantity(quantity);
        pfBorderPromotion.setUnitPrice(unitPrice);
        pfBorderPromotion.setTotalPrice(totalPrice);
        pfBorderPromotion.setIsSend(isSend);
        pfBorderPromotion.setIsTake(isTake);
        pfBorderPromotion.setRemark(remark);
        int i = pfBorderPromotionMapper.insert(pfBorderPromotion);
        if (i != 1) {
            throw new BusinessException("小白注册赠送商品插入订单促销活动赠品表失败");
        }
    }

    public void doPromotion(PfBorder pfBorder) {
        log.info(" 更新平台赠送给小白的商品库存----start");
        try {
            List<PfBorderPromotion> pfBorderPromotions = pfBorderPromotionMapper.selectByPfBorderId(pfBorder.getId());
            if (pfBorderPromotions != null && pfBorderPromotions.size() > 0) {
                for (PfBorderPromotion pfBorderPromotion : pfBorderPromotions) {
                    if (pfBorderPromotion.getQuantity() > 0 && pfBorderPromotion.getIsSend().intValue() == 0) {
                        log.info("---------------------减少平台库存--------------------");
                        //增加平台冻结库存
                        PfSkuStock pfSkuStock = pfSkuStockService.selectBySkuId(pfBorderPromotion.getSkuId());
                        if (pfSkuStock.getStock() < pfBorderPromotion.getQuantity()) {
                            throw new BusinessException("库存不足，操作失败");
                        }
                        if (pfSkuStock.getFrozenStock() < pfBorderPromotion.getQuantity()) {
                            throw new BusinessException("库存冻结不足，操作失败");
                        }
                        pfSkuStockService.updateFrozenStock(pfBorderPromotion.getQuantity(), "小白注册赠送商品，下单时增加平台冻结库存", pfSkuStock);
                        //减少平台库存
                        pfSkuStock = pfSkuStockService.selectBySkuId(pfBorderPromotion.getSkuId());
                        pfSkuStockService.updateSkuStockWithLog(pfBorderPromotion.getQuantity(), pfSkuStock, pfBorder.getId(), SkuStockLogType.registerGiveSku);
                        log.info("---------------------增加代理用户库存--------------------");
                        PfUserSkuStock pfUserSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(pfBorder.getUserId(), pfBorderPromotion.getSkuId());
                        if (pfUserSkuStock == null) {
                            pfUserSkuStock = pfUserSkuStockService.initPfUserSkuStock(pfBorder.getUserId(), pfBorderPromotion.getSkuId(), pfBorderPromotion.getSpuId());
                        }
                        pfUserSkuStockService.updateUserSkuStockWithLog(pfBorderPromotion.getQuantity(), pfUserSkuStock, pfBorder.getId(), UserSkuStockLogType.PROMOTION_ADD);
                        //修改发送状态
                        updateIsSend(pfBorderPromotion);
                    }
                }
            }
        } catch (Exception e) {
            throw new BusinessException("更新平台赠送给小白的商品库存失败-----" + e.getMessage());
        }
        log.info(" 更新平台赠送给小白的商品库存----end");
    }


    /**
     * 更新订单促销活动赠品表中是否已发放商品
     *
     * @param pfBorderPromotion
     */
    private void updateIsSend(PfBorderPromotion pfBorderPromotion) {
        if (pfBorderPromotion == null) {
            throw new BusinessException("------数据异常!!!------");
//            pfBorderPromotion = getBorderPromotionsByBorderIdAndIsSend(pfBorderId, PfBorderPromotionIsSendEnum.NO_GiVE.getCode());
        }
        pfBorderPromotion.setIsSend(PfBorderPromotionIsSendEnum.GiVED.getCode());
        int i = update(pfBorderPromotion);
        if (i != 1) {
            throw new BusinessException("------更新订单促销活动赠品表中是否已发放商品失败------");
        }
    }

    /**
     * 回收到期的平台送的商品
     */
    public void recoverySkuStock(PfBorderPromotion pfBorderPromotion) {
        try {
            log.info("回收平台赠送的到期的商品-----start");
            log.info("回收到期的平台送的商品----订单活动id-----" + pfBorderPromotion.getId());
            PfUserSkuStock userSkuStock = userSkuStockService.selectByUserIdAndSkuIdAndSpuId(pfBorderPromotion.getUserId(), pfBorderPromotion.getSkuId(), pfBorderPromotion.getSpuId());
            if (userSkuStock != null && userSkuStock.getRegisterGiveSkuStock() > 0) {
                //更新自己的库存
                log.info("-----更新自己的库存----start");
                Integer registerGiveSkuStock = userSkuStock.getRegisterGiveSkuStock();
                if (registerGiveSkuStock > 0) {
                    userSkuStock.setRemark("时间到期回收小白没有卖出去的库存");
                    pfUserSkuStockService.updateUserSkuStockWithLog(pfBorderPromotion.getQuantity(), userSkuStock, pfBorderPromotion.getPfBorderId(), UserSkuStockLogType.PROMOTION_REDUCE);
                    log.info("-----更新自己的库存----end");
                    //更新平台库存
                    log.info("-----更新平台库存----start");
                    PfSkuStock pfSkuStock = pfSkuStockService.selectBySkuId(pfBorderPromotion.getSkuId());
                    pfSkuStockService.updateSkuStockWithLog(pfBorderPromotion.getQuantity(), pfSkuStock, pfBorderPromotion.getPfBorderId(), SkuStockLogType.recoveryGiveSku);
                    log.info("-----更新平台库存----end");
                } else {
                    log.info("赠送商品小于等于0----订单活动id-----" + pfBorderPromotion.getId());
                }
            }
            log.info("回收平台赠送的到期的商品-----end");
        } catch (Exception e) {
            throw new BusinessException("回收平台赠送的到期的商品失败----" + e.getMessage());
        }
    }
}