package com.masiis.shop.web.platform.service.user;

import com.masiis.shop.dao.platform.user.PfUserRelationMapper;
import com.masiis.shop.dao.po.PfUserRelation;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * PfUserRelationService
 *
 * @author ZhaoLiang
 * @date 2016/4/18
 */
@Service
public class PfUserRelationService {
    @Resource
    private PfUserRelationMapper pfUserRelationMapper;

    public void insert(PfUserRelation pfUserRelation) {
        pfUserRelationMapper.insert(pfUserRelation);
    }

    /**
     * 获取用户上级代理关系
     * @param userId
     * @param skuId
     * @return
     */
    public Long getPUserId(Long userId, Integer skuId) {
        PfUserRelation pfUserRelation = pfUserRelationMapper.selectEnableByUserId(userId, skuId);
        if (pfUserRelation == null) {
            return 0l;
        } else {
            return pfUserRelation.getUserPid();
        }
    }

    /**
     * 获取当前用户的代理关系
     * @param userId
     * @param skuId
     */
    public PfUserRelation getRelation(Long userId, Integer skuId) {
        return pfUserRelationMapper.selectEnableByUserId(userId, skuId);
    }

    public PfUserRelation getRelationByUserIdAndSkuIdAndPUserId(Long userId, Integer skuId, Long pUserId) {
        return pfUserRelationMapper.selectRelationByUserIdAndSkuIdAndPUserId(userId, skuId, pUserId);
    }

    public int update(PfUserRelation existRelation) {
        return pfUserRelationMapper.updateByPrimaryKey(existRelation);
    }

    public int updateAllToUnableByUserIdAndSkuId(Long userId, Integer skuId) {
        return pfUserRelationMapper.updateAllToUnableByUserIdAndSkuId(userId, skuId);
    }

    public List<PfUserRelation> getRelationByUserId(Long userId) {
        return pfUserRelationMapper.selectRelationByUserId(userId);
    }
}
