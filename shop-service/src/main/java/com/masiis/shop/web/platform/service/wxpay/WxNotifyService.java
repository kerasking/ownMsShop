package com.masiis.shop.web.platform.service.wxpay;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.PfBorderPayment;
import com.masiis.shop.dao.po.PfCorderPayment;
import com.masiis.shop.common.beans.wx.wxpay.CallBackNotifyReq;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.order.COrderService;
import com.masiis.shop.web.platform.service.order.BOrderPayService;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;

/**
 * Created by lzh on 2016/3/16.
 */
@Service("bWxNotifyService")
public class WxNotifyService {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private BOrderService bOrderService;
    @Resource
    private COrderService cOrderService;
    @Resource
    private BOrderPayService payBOrderService;

    /**
     * 处理微信支付订单异步回调业务
     *
     * @param param
     */
    @Transactional
    public void handleWxPayNotify(CallBackNotifyReq param) {
        // 支付流水号
        String paySerialNum = param.getOut_trade_no();
        String orderType = String.valueOf(paySerialNum.charAt(0));
        if("B".equals(orderType)){
            try {
                PfBorderPayment payment = bOrderService.findOrderPaymentBySerialNum(paySerialNum);
                if(payment == null){
                    throw new BusinessException("该支付流水号不存在,pay_serial_num:" + paySerialNum);
                }
                if(payment.getIsEnabled() == 1){
                    // 已经支付
                    log.info("该订单已经支付,支付流水号:" + paySerialNum);
                    return;
                }
                log.info("处理订单开始,类型为B,支付流水号为:" + paySerialNum);
                // 调用borderService的方法处理
                payBOrderService.mainPayBOrder(payment, param.getTransaction_id());
            } catch (Exception e) {
                // 判断异常类型
                if(e instanceof BusinessException && "".equals(e.getMessage())){
                    // 如果是订单已支付且缺货,走次流程
                    String err = "订单已支付且缺货,系统支付流水号:" + paySerialNum + "||" + e.getMessage();
                    log.error(err);
                    throw new BusinessException(err);
                } else {
                    log.error("订单支付成功处理失败,系统支付流水号:" + paySerialNum, e);
                    // 普通支付处理失败流程
                    throw new BusinessException(e.getMessage());
                }
            }
        } else if ("C".equals(orderType)){
            try {
                PfCorderPayment payment = cOrderService.findOrderPaymentBySerialNum(paySerialNum);
                if(payment == null){
                    throw new BusinessException("该支付流水号不存在,pay_serial_num:" + paySerialNum);
                }
                if(payment.getIsEnabled() == 1){
                    // 已经支付
                    log.info("该订单已经支付,支付流水号:" + paySerialNum);
                    return;
                }
                // 调用borderService的方法处理
                log.info("处理订单开始,类型为C,支付流水号为:" + paySerialNum);
                cOrderService.payCOrder(payment, param.getTransaction_id());
            } catch (Exception e) {
                // 判断异常类型
                if(e instanceof BusinessException && "".equals(e.getMessage())){
                    // 如果是订单已支付且缺货,走次流程
                    String err = "订单已支付且缺货,系统支付流水号:" + paySerialNum + "||" + e.getMessage();
                    log.error(err);
                    throw new BusinessException(err);
                } else {
                    log.error("订单支付成功处理失败,系统支付流水号:" + paySerialNum, e);
                    // 普通支付处理失败流程
                    throw new BusinessException(e.getMessage());
                }
            }
        } else {
            throw new BusinessException("支付流水号类型错误!");
        }
    }
}
