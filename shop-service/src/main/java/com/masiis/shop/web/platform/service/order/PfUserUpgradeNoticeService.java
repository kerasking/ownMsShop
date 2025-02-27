package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.common.enums.platform.BOrderStatus;
import com.masiis.shop.common.enums.platform.UpGradeStatus;
import com.masiis.shop.common.enums.platform.UpGradeUpStatus;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.platform.order.PfBorderMapper;
import com.masiis.shop.dao.platform.product.ComAgentLevelMapper;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.platform.user.PfUserUpgradeNoticeMapper;
import com.masiis.shop.dao.po.ComAgentLevel;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfBorder;
import com.masiis.shop.dao.po.PfUserUpgradeNotice;
import com.masiis.shop.web.common.utils.notice.SysNoticeUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Date;
import java.util.List;

/**
 * 升级通知
 */
@Service
@Transactional
public class PfUserUpgradeNoticeService {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfUserUpgradeNoticeMapper noticeMapper;
    @Resource
    private PfBorderMapper pfBorderMapper;
    @Resource
    private BOrderOperationLogService bOrderOperationLogService;
    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private ComAgentLevelMapper comAgentLevelMapper;


    public PfUserUpgradeNotice selectByPrimaryKey(Long id){
        return noticeMapper.selectByPrimaryKey(id);
    }

    /**
     * 根据订单id查询通知单
     * @param orderId
     * @return
     */
    public PfUserUpgradeNotice selectByPfBorderId(Long orderId){
        return noticeMapper.selectByPfBorderId(orderId);
    }

    public int update(PfUserUpgradeNotice po){
        return noticeMapper.updateByPrimaryKey(po);
    }

    public List<PfUserUpgradeNotice> selectByUserPidAndStatus(Long userPid,Integer status){
        return noticeMapper.selectByUserPidAndStatus(userPid,status);
    }

    public List<PfUserUpgradeNotice> selectByUserPidAndInStatus(Long userPid,List<Integer> statusList){
        return noticeMapper.selectByUserPidAndInStatus(userPid,statusList);
    }

    /**
     * 查询上级未处理状态和创建时间小于指定时间的未取消通知单
     *
     * @param time  指定时间
     * @return  通知单集合
     */
    public List<PfUserUpgradeNotice> findAllUnsolvedNoticesByDate(Date time) {
        return findUncancelNoticesByStatusAndDate(UpGradeUpStatus.UP_STATUS_Untreated, time);
    }

    /**
     * 查询指定上级处理状态和创建时间小于指定时间的未取消通知单
     *
     * @param status    上级处理状态枚举
     * @param time  指定时间
     * @return  通知单集合
     */
    public List<PfUserUpgradeNotice> findUncancelNoticesByStatusAndDate(UpGradeUpStatus status, Date time) {
        return noticeMapper.selectUncalcelByUpStatusAndDate(status.getCode(), time);
    }

    @Transactional
    public void handleUnSolvedUpgradeNotice(PfUserUpgradeNotice notice) {
        try {
            if (notice == null) {
                throw new BusinessException("notice is null");
            }
            if(notice.getStatus().intValue() != UpGradeStatus.STATUS_Untreated.getCode().intValue()){
                String str = "升级单status不是{" + UpGradeStatus.STATUS_Untreated.getMessage() + "}状态";
                log.error(str);
                throw new BusinessException(str);
            }
            if(notice.getUpStatus().intValue() != UpGradeUpStatus.UP_STATUS_Untreated.getCode().intValue()){
                String str = "升级单upStatus不是{" + UpGradeUpStatus.UP_STATUS_Untreated.getMessage() + "}状态";
                log.error(str);
                throw new BusinessException(str);
            }
            // 将自身状态置为待支付状态
            notice.setStatus(UpGradeStatus.STATUS_NoPayment.getCode());
            // 将上级处理状态置为暂不升级
            notice.setUpStatus(UpGradeUpStatus.UP_STATUS_NotUpgrade.getCode());
            notice.setUpdateTime(new Date());
            int res = update(notice);
            if (res != 1) {
                throw new BusinessException("更新升级单失败");
            }
        } catch (Exception e) {
            log.error("处理2天上级未处理升级单失败");
            throw new BusinessException(e);
        }
    }

    public PfUserUpgradeNotice findByPfBorderId(Long id) {
        return noticeMapper.selectByPfBorderId(id);
    }

    /**
     * 查询指定时间之外的待支付2天未支付升级单(没有升级订单或者2天未支付非线下支付订单对应的升级申请单)
     *
     * @param time
     * @return
     */
    public List<PfUserUpgradeNotice> findAllUnpayNoticesByDate(Date time) {
        return noticeMapper.selectAllUnpayNoticesByDate(time);
    }

    @Transactional
    public void handleUnpayUpgradeNotice(PfUserUpgradeNotice notice, BOrderStatus bstatus) {
        try {
            // 校验升级通知单状态
            if (notice.getStatus().intValue() != UpGradeStatus.STATUS_NoPayment.getCode().intValue()) {
                throw new BusinessException("该升级通知单不是待支付状态");
            }

            log.info("开始取消升级申请通知单");
            // 升级单置为取消状态
            notice.setStatus(UpGradeStatus.STATUS_Revocation.getCode());
            noticeMapper.updateByPrimaryKey(notice);
            // 检查升级单对应的升级订单
            if(notice.getPfBorderId() != null && notice.getPfBorderId().longValue() > 0l) {
                PfBorder pfBorder = pfBorderMapper.selectByPrimaryKey(notice.getPfBorderId());
                if(pfBorder.getPayStatus().intValue() != 0){
                    throw new BusinessException("该订单非未支付订单");
                }
                if(pfBorder.getOrderType() != 3){
                    throw new BusinessException("该订单非升级订单");
                }

                // 修改订单的状态为已取消状态
                int result = pfBorderMapper.updateOrderCancelByIdAndOStatus(pfBorder.getId(), bstatus.getCode());
                if (result != 1) {
                    pfBorder = pfBorderMapper.selectByPrimaryKey(pfBorder.getId());
                    throw new BusinessException("订单取消失败,订单此时状态为:" + pfBorder.getOrderStatus()
                            + ",支付状态为:" + pfBorder.getPayStatus());
                }
                // 插入订单操作记录
                bOrderOperationLogService.insertBOrderOperationLog(pfBorder,"未支付升级申请单,对应升级订单自动取消");
            }

            // 下级升级申请通知单
            List<PfUserUpgradeNotice> nos = noticeMapper.selectAllSubByStatusAndPid(
                    UpGradeStatus.STATUS_Processing.getCode(), notice.getUserId());
            if(nos != null && nos.size() > 0){
                // 处理下级升级申请通知
                for(PfUserUpgradeNotice n:nos){
                    n.setStatus(UpGradeStatus.STATUS_NoPayment.getCode());
                    n.setUpStatus(UpGradeUpStatus.UP_STATUS_NotUpgrade.getCode());
                    int res = update(n);
                    if (res != 1) {
                        throw new BusinessException("更新升级单失败");
                    }

                    // 发送微信消息
                    try{
                        ComUser user = comUserMapper.selectByPrimaryKey(n.getUserId());
                        ComAgentLevel org = comAgentLevelMapper.selectByPrimaryKey(n.getOrgAgentLevelId());
                        ComAgentLevel wish = comAgentLevelMapper.selectByPrimaryKey(n.getWishAgentLevelId());
                        String[] params = {user.getRealName(),
                                org.getName(),
                                wish.getName(),
                                DateUtil.Date2String(n.getCreateTime(), DateUtil.SQL_TIME_FMT),
                                DateUtil.Date2String(DateUtil.getDateNextdays(n.getCreateTime(), 2), DateUtil.SQL_TIME_FMT)
                        };
                        String url = PropertiesUtils.getStringValue("web.domain.name.address")
                                + "/upgrade/skipOrderPageGetNoticeInfo.html?upgradeNoticeId=" + n.getId();
                        SysNoticeUtils.getInstance().upgradeApplyAuditPassNotice(user, params, url);
                    } catch (Exception e){
                        log.error("发送微信消息失败",e);
                    }
                }
            }
        } catch (Exception e) {
            log.error("取消升级申请单失败", e);
            throw new BusinessException(e);
        }
    }

    /**
     * 根据2天未支付升级申请单默认不升级,发送微信通知
     *
     * @param notice
     */
    public void sendWxNoticeByTwoDayUnpayUpgradeNotice(PfUserUpgradeNotice notice) {
        // 发给本人的
        ComUser user = comUserMapper.selectByPrimaryKey(notice.getUserId());
        String[] params = {
                DateUtil.Date2String(new Date(), DateUtil.SQL_TIME_FMT)
        };
        String url = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/myApplyUpgrade.shtml?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().upgradeApplyTwoDayNotPayNotice(user, params, url);

        // 发给上级的
        ComUser pUser = comUserMapper.selectByPrimaryKey(notice.getUserPid());
        ComAgentLevel org = comAgentLevelMapper.selectByPrimaryKey(notice.getOrgAgentLevelId());
        ComAgentLevel wish = comAgentLevelMapper.selectByPrimaryKey(notice.getWishAgentLevelId());
        String[] pParams = {user.getRealName(),
                org.getName(),
                wish.getName(),
                DateUtil.Date2String(notice.getCreateTime(), DateUtil.SQL_TIME_FMT)
        };
        String pUrl = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/upgradeInfo.shtml?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().subLineUpgradeApplyCancelNotice(pUser, pParams, pUrl);
    }

    public List<PfUserUpgradeNotice> findAllSevenDayUnpayNoticesByDate(Date time) {
        return noticeMapper.selectAllUnpayOfflineNoticesByDate(time);
    }

    /**
     * 发送线下支付升级申请单7天未支付微信通知
     *
     * @param notice
     */
    public void sendWxNoticeBySevenDayUnpayUpgradeNotice(PfUserUpgradeNotice notice) {
        ComUser user = comUserMapper.selectByPrimaryKey(notice.getUserId());
        String[] params = {
                DateUtil.Date2String(new Date(), DateUtil.SQL_TIME_FMT)
        };
        String url = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/myApplyUpgrade.shtml?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().upgradeApplySevenDayNotPayNotice(user, params, url);

        // 发给上级的
        ComUser pUser = comUserMapper.selectByPrimaryKey(notice.getUserPid());
        ComAgentLevel org = comAgentLevelMapper.selectByPrimaryKey(notice.getOrgAgentLevelId());
        ComAgentLevel wish = comAgentLevelMapper.selectByPrimaryKey(notice.getWishAgentLevelId());
        String[] pParams = {user.getRealName(),
                org.getName(),
                wish.getName(),
                DateUtil.Date2String(notice.getCreateTime(), DateUtil.SQL_TIME_FMT)
        };
        String pUrl = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/upgradeInfo.shtml?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().subLineUpgradeApplyCancelNotice(pUser, pParams, pUrl);
    }

    public void sendWxNoticeByUnsolvedUpgradeNotice(PfUserUpgradeNotice notice) {
        // 给上级通知
        ComUser pUser = comUserMapper.selectByPrimaryKey(notice.getUserPid());
        String[] pParams = {
                DateUtil.Date2String(new Date(), DateUtil.SQL_TIME_FMT)
        };
        String pUrl = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/upgradeInfo.shtml?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().upgradeApplyNotHandleNotice(pUser, pParams, pUrl);

        // 给下级通知
        ComUser user = comUserMapper.selectByPrimaryKey(notice.getUserId());
        ComAgentLevel org = comAgentLevelMapper.selectByPrimaryKey(notice.getOrgAgentLevelId());
        ComAgentLevel wish = comAgentLevelMapper.selectByPrimaryKey(notice.getWishAgentLevelId());
        String[] params = {user.getRealName(),
                org.getName(),
                wish.getName(),
                DateUtil.Date2String(notice.getCreateTime(), DateUtil.SQL_TIME_FMT),
                DateUtil.Date2String(DateUtil.getDateNextdays(notice.getCreateTime(), 2), DateUtil.SQL_TIME_FMT)
        };
        String url = PropertiesUtils.getStringValue("web.domain.name.address")
                + "/upgrade/skipOrderPageGetNoticeInfo.html?upgradeId=" + notice.getId();
        SysNoticeUtils.getInstance().upgradeApplyAuditPassNotice(user, params, url);

    }

}
