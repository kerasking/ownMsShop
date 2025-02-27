package com.masiis.shop.web.mall.service.order;

import com.masiis.shop.dao.mall.order.SfOrderPaymentMapper;
import com.masiis.shop.dao.po.*;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * Created by hzz on 2016/4/10.
 */
@Service
public class SfOrderPaymentService {

    @Resource
    private SfOrderPaymentMapper sfOrderPaymentMapper;

    public int updateOrderPayment(SfOrderPayment orderPayment){
       return sfOrderPaymentMapper.updateByPrimaryKey(orderPayment);
    }

    public List<SfOrderPayment> selectBySfOrderId(Long orderId) {
        return sfOrderPaymentMapper.selectBySfOrderId(orderId);
    }
}
