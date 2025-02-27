package com.masiis.shop.admin.service.order;

import com.masiis.shop.dao.platform.user.PfUserUpgradeNoticeMapper;
import com.masiis.shop.dao.po.PfUserUpgradeNotice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 * 升级通知
 */
@Service
@Transactional
public class PfUserUpgradeNoticeService {
    @Resource
    private PfUserUpgradeNoticeMapper  noticeMapper;


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

}
