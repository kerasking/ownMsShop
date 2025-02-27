package com.masiis.shop.admin.service.user;

import com.masiis.shop.admin.service.product.SkuAgentService;
import com.masiis.shop.admin.service.product.SkuService;
import com.masiis.shop.common.enums.platform.UpGradeStatus;
import com.masiis.shop.common.enums.platform.UpGradeUpStatus;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.OrderMakeUtils;
import com.masiis.shop.dao.beans.order.BOrderUpgradeDetail;
import com.masiis.shop.dao.beans.user.upgrade.UpGradeInfoPo;
import com.masiis.shop.dao.platform.user.PfUserRebateMapper;
import com.masiis.shop.dao.platform.user.PfUserUpgradeNoticeMapper;
import com.masiis.shop.dao.po.*;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * Created by JingHao on 2016/6/15
 * 升级管理service
 */
@Service
@Transactional
public class UpgradeNoticeService {
    private static final Logger logger = Logger.getLogger(UpgradeNoticeService.class);
    @Resource
    private PfUserUpgradeNoticeMapper pfUserUpgradeNoticeMapper;

    @Resource
    private PfUserRebateMapper pfUserRebateMapper;

    @Resource
    private SkuService comSkuService;
    @Resource
    private ComUserService comUserService;
    @Resource
    private ComAgentLevelService comAgentLevelService;
    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private PfUserSkuService pfUserSkuService;
    /**
     * 根据订单id查询通知单
     * @param orderId
     * @return
     */
    public PfUserUpgradeNotice selectByPfBorderId(Long orderId){
        return pfUserUpgradeNoticeMapper.selectByPfBorderId(orderId);
    }

	/**
     * 通过id查询申请单数据
     * @param id            申请单id
     * @return
     * @throws Exception
     */
    public PfUserUpgradeNotice getUpgradeNoticeById(Long id) throws Exception{
        return pfUserUpgradeNoticeMapper.selectByPrimaryKey(id);
    }
    /**
     * jjh
     * 我的升级申请记录
     * @param userId
     * @return
     */
    public List<PfUserUpgradeNotice> getPfUserUpGradeInfoByUserId(Long userId) throws Exception{
        return pfUserUpgradeNoticeMapper.selectByUserId(userId);
    }

    /**
     * jjh
     * 我的下级申请记录
     * @param userId
     * @return
     */
    public List<PfUserUpgradeNotice> getPfUserUpGradeInfoByUserPId(Long userId) throws Exception {
        return pfUserUpgradeNoticeMapper.selectByUserPId(userId);
    }

    /**
     * jjh
     * 一次性返利<获得的返利>
     * @param userId
     * @return
     */
    public List<PfUserRebate> getPfUserRebateByUserId(Long userId) throws Exception{
        return pfUserRebateMapper.selectByUserId(userId);
    }

    /**
     * jjh
     * 一次性返利<支付的返利>
     * @param userId
     * @return
     */
    public List<PfUserRebate> getPfUserRebateByUserPId(Long userId) throws Exception{
        return pfUserRebateMapper.selectByUserPId(userId);
    }

    /**
     * jjh
     * 根据主键获取申请信息
     * @param id
     * @return
     */
    public PfUserUpgradeNotice getPfUserUpGradeInfoByPrimaryKey(Long id) throws Exception{
        return pfUserUpgradeNoticeMapper.selectByPrimaryKey(id);
    }

    /**
     * jjh
     * 组合查询
     * @param UserPId 我作为上级
     * @param skuId 商品
     * @param upStatus 申请状态
     * @return
     * @throws Exception
     */
    public List<PfUserUpgradeNotice> getPfUserUpGradeInfoByParam(Long UserPId, Integer skuId, Integer upStatus) throws Exception {
        return pfUserUpgradeNoticeMapper.selectByParam(UserPId, skuId, upStatus);
    }

    /**
     * jjh
     * 组合查询
     * @param skuId 商品
     * @param userPid 支付返利
     * @param userId 获取返利
     * @return
     */
    public List<PfUserUpgradeNotice> getPfUserUpGradeInfoByRebateAndSkuId(Integer skuId, Long userPid, Long userId) {
        return pfUserUpgradeNoticeMapper.selectBySkuIdAndRebateType(skuId, userPid, userId);
    }

    public String coverCodeByMyUpgrade(Integer upStatus) {
        return UpGradeStatus.statusPickList.get(upStatus);
    }

    public String coverCodeByLowerUpgrade(Integer upStatus) {
        return UpGradeUpStatus.upStatusPickList.get(upStatus);
    }

    /**
     * 根据id查询申请信息
     * @param id 申请信息表id
     * @return
     */
    public UpGradeInfoPo getUpGradeInfo(Long id){
        logger.info("根据id查询申请信息 id=" + id);
        return pfUserUpgradeNoticeMapper.selectUpGradeInfoPoById(id);
    }

    /**
     * 更新升级申请信息
     * @param pfUserUpgradeNotice
     * @return
     */
    public int updateUpgradeNotice(PfUserUpgradeNotice pfUserUpgradeNotice) throws Exception{
        int a = pfUserUpgradeNoticeMapper.updateByPrimaryKey(pfUserUpgradeNotice);
        if (a == 0){
            throw new BusinessException("处理失败");
        }
        return a;
    }

    /**
     * 订单界面获得升级通知信息
     * @param id   通知单id
     */
    public BOrderUpgradeDetail getUpgradeNoticeInfo(Long id){
        //升级订单通知信息
        if (id==null){
            throw new BusinessException("升级通知信息id为null");
        }
        logger.info("获取升级通知信息------start");
        logger.info("获取升级通知信息------id----"+id);
        PfUserUpgradeNotice upgradeNotice = pfUserUpgradeNoticeMapper.selectByPrimaryKey(id);
        BOrderUpgradeDetail upgradeDetail = null;
        if (upgradeNotice!=null){
            //验证条件是否可以进入
            if (true){
                upgradeDetail = new BOrderUpgradeDetail();
                upgradeDetail.setUpgradeNoticeId(id);
                upgradeDetail.setUpgradeOrderCode(upgradeNotice.getCode());
                upgradeDetail.setPfBorderId(upgradeNotice.getPfBorderId());
                logger.info("订单id-------"+upgradeDetail.getPfBorderId());
                upgradeDetail.setUpgradeStatus(upgradeNotice.getStatus());
                logger.info("上级状态--------"+upgradeDetail.getUpStatus());
                upgradeDetail.setUpStatus(upgradeNotice.getUpStatus());
                ComUser oldComUser = comUserService.getUserById(upgradeNotice.getUserPid());
                if (oldComUser!=null){
                    logger.info("旧上级-------"+upgradeNotice.getUserPid());
                    upgradeDetail.setOldPUserId(upgradeNotice.getUserPid());
                    upgradeDetail.setOldPUserName(oldComUser.getRealName());
                    Long newComUserId = getNewComUserId(upgradeDetail.getOldPUserId(),upgradeNotice.getSkuId(),upgradeNotice.getWishAgentLevelId());
                    if (newComUserId!=null){
                        logger.info("新上级id----------"+newComUserId);
                        ComUser newComUser = comUserService.getUserById(newComUserId);
                        if (newComUser!=null){
                            upgradeDetail.setNewPUserId(newComUserId);
                            upgradeDetail.setNewPUserName(newComUser.getRealName());
                        }
                    }
                }
                //商品信息
                ComSku comSku = getComSku(upgradeNotice.getSkuId());
                if (comSku!=null){
                    upgradeDetail.setSkuId(comSku.getId());
                    upgradeDetail.setSpuId(comSku.getSpuId());
                    upgradeDetail.setSkuName(comSku.getName());
                    upgradeDetail.setPriceRetail(comSku.getPriceRetail());
                    logger.info("商品销售价-------"+comSku.getPriceRetail());
                }else{
                    logger.info("商品信息为null-----id--"+upgradeNotice.getSkuId());
                    throw new BusinessException("商品信息为null-----id--"+upgradeNotice.getSkuId());
                }
                //级别
                logger.info("获得商品代理原等级信息----商品skuId----"+comSku.getId()+"-----原始等级id----"+upgradeNotice.getOrgAgentLevelId());
                PfSkuAgent oldSkuAgent = getPfSkuAgent(comSku.getId(),upgradeNotice.getOrgAgentLevelId());
                logger.info("获得商品代理新等级信息----商品skuId----"+comSku.getId()+"-----期望等级id----"+upgradeNotice.getWishAgentLevelId());
                PfSkuAgent newSkuAgent = getPfSkuAgent(comSku.getId(),upgradeNotice.getWishAgentLevelId());
                if (oldSkuAgent!=null){
                    ComAgentLevel oldAgentLevel = getComAgentLeveal(oldSkuAgent.getAgentLevelId());
                    upgradeDetail.setCurrentAgentLevel(upgradeNotice.getOrgAgentLevelId());
                    upgradeDetail.setCurrentAgentLevelName(oldAgentLevel.getName());
                }else{
                    logger.info("原始等级商品代理为null");
                    throw new BusinessException("原始等级商品代理为null");
                }
                if (newSkuAgent!=null){
                    ComAgentLevel newAgentLevel = getComAgentLeveal(newSkuAgent.getAgentLevelId());
                    upgradeDetail.setApplyAgentLevel(newSkuAgent.getAgentLevelId());
                    upgradeDetail.setApplyAgentLevelName(newAgentLevel.getName());
                }else{
                    logger.info("新等级商品代理为null");
                    throw new BusinessException("新等级商品代理为null");
                }
                //拿货数量
                upgradeDetail.setQuantity(newSkuAgent.getQuantity());
                //保证金差额
                upgradeDetail.setBailChange(getBailChange(oldSkuAgent,newSkuAgent));
                logger.info("保证金差额------"+upgradeDetail.getBailChange());
                //总价
                BigDecimal totalPrice = getTotalPrice(oldSkuAgent,newSkuAgent);
                logger.info("总价------"+totalPrice);
                upgradeDetail.setTotalPrice(totalPrice);
            }else{
                logger.info("升级通知信息状态不正确-----");
                throw new BusinessException("升级通知信息状态不正确-----");
            }
        }else{
            logger.info("升级通知信息为null------id----"+id);
        }
        logger.info("获取升级通知信息------end");
        return upgradeDetail;
    }

    private Long getNewComUserId(Long oldPUserId,Integer skuId,Integer applyAgentLevel){
        PfUserSku _parentPfUserSku = pfUserSkuService.getPfUserSkuByUserIdAndSkuId(oldPUserId, skuId);
        PfSkuAgent pfSkuAgent = skuAgentService.getBySkuIdAndLevelId(skuId, applyAgentLevel);
        Long newPUserId = null;
        if (pfSkuAgent != null) {
            if (pfSkuAgent.getIsUpgrade() == 1) {
                Integer pUserAgentLevel = null;
                if (_parentPfUserSku != null) {
                    pUserAgentLevel = _parentPfUserSku.getAgentLevelId();
                        /*if (_parentPfUserSku.getUserPid()!=0){
                            pUserAgentLevel = _parentPfUserSku.getAgentLevelId();
                        }else{
                            logger.info("联合创始人不能升级到boss------当前用户id----"+bOrderAdd.getpUserId()+"----skuId---"+bOrderAdd.getSkuId()+"----上级用户----");
                            throw new BusinessException("联合创始人不能升级到boss");
                        }*/
                } else {
                    logger.info("查询父级userSku为null---");
                    throw new BusinessException("查询父级userSku为null------");
                }
                if (pUserAgentLevel - applyAgentLevel < 0) {
                    //直接升级  上级的等级大于用户要升级的等级
                    newPUserId = _parentPfUserSku.getUserId();
                } else if (pUserAgentLevel - applyAgentLevel == 0) {
                    //平级升级   上级的等级=用户要升级的等级
                    newPUserId = _parentPfUserSku.getUserPid();
                } else {
                    logger.info("申请等级超过了上级的等级-----skuId---" + skuId);
                    logger.info("----升级等级----" + applyAgentLevel);
                    throw new BusinessException("申请登记超过了上级的等级");
                }
            } else {
                logger.info("此等级商品不支持升级------skuId----" + skuId + "-----等级Id----" + applyAgentLevel);
                throw new BusinessException("此等级商品不支持升级------skuId----" + skuId + "-----等级Id----" + applyAgentLevel);
            }
        }
        return newPUserId;
    }

    /**
     * 商品信息
     * @param skuId
     * @return
     */
    private ComSku getComSku (Integer skuId){
        return comSkuService.getSkuById(skuId);
    }

    /**
     * 获取保证金的差额
     * @param oldSkuAgent  旧代理商品
     * @param newSkuAgent  新代理商品
     * @return
     */
    private BigDecimal getBailChange(PfSkuAgent oldSkuAgent,PfSkuAgent newSkuAgent){
        if (oldSkuAgent!=null&&newSkuAgent!=null){
            logger.info("保证金差额-------"+newSkuAgent.getBail().subtract(oldSkuAgent.getBail()).intValue());
            return newSkuAgent.getBail().subtract(oldSkuAgent.getBail());
        }else{
            logger.info("获取保证金差额失败");
            throw new BusinessException("获取保证金差额失败");
        }
    }

    /**
     * 获取订单的总价  = 新代理产品总价（数量*单价） + 保证金差额
     * @param oldSkuAgent
     * @param newSkuAgent
     * @return
     */
    private BigDecimal getTotalPrice(PfSkuAgent oldSkuAgent,PfSkuAgent newSkuAgent){
        BigDecimal totalPice = null;
        if (oldSkuAgent!=null&&newSkuAgent!=null){
            BigDecimal bailChange = getBailChange(oldSkuAgent,newSkuAgent);
            totalPice = newSkuAgent.getTotalPrice().add(bailChange);
        }else{
            totalPice = BigDecimal.ZERO ;
        }
        return totalPice;
    }

    /**
     * 获得商品代理等级信息
     * @param skuId
     * @param levelId
     * @return
     */
    private PfSkuAgent getPfSkuAgent(Integer skuId,Integer levelId){
        return skuAgentService.getBySkuIdAndLevelId(skuId,levelId);
    }
    /**
     * 获得等级信息
     * @param levelId
     * @return
     */
    private ComAgentLevel getComAgentLeveal(Integer levelId){
        logger.info("获得等级信息----等级id-----"+levelId);
        return comAgentLevelService.selectByPrimaryKey(levelId);
    }

}
