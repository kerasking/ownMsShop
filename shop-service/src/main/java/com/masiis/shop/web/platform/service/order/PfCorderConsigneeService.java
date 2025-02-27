package com.masiis.shop.web.platform.service.order;

import com.masiis.shop.dao.platform.order.PfCorderConsigneeMapper;
import com.masiis.shop.dao.po.PfCorderConsignee;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by hzz on 2016/3/17.
 */
@Service
@Transactional
public class PfCorderConsigneeService {
    @Resource
    private PfCorderConsigneeMapper pfCorderConsigneeMapper;

    public void insertPfCC(PfCorderConsignee pfCorderConsignee)throws Exception{
        try {
            pfCorderConsigneeMapper.insert(pfCorderConsignee);
        }catch (Exception e){
            throw new Exception("试用申请订单插入收获地址失败"+e);
        }
    }
    /**
     * 根据订单id查询订单地址
     * @author hanzengzhi
     * @date 2016/5/25 15:42
     */
    public PfCorderConsignee getOrdConByOrdId(Long ordId){
        return pfCorderConsigneeMapper.selectByCorderId(ordId);
    }
}
