package com.masiis.shop.api.controller.pay.wxpay;

import com.masiis.shop.api.bean.base.BaseReq;
import com.masiis.shop.api.constants.SignValid;
import com.masiis.shop.api.controller.base.BaseController;
import com.masiis.shop.api.utils.SysSignUtils;
import com.masiis.shop.common.annotation.SignField;
import com.masiis.shop.common.beans.wx.wxpay.CallBackNotifyReq;
import com.masiis.shop.common.beans.wx.wxpay.CallBackNotifyRes;
import com.masiis.shop.common.constant.wx.WxConsAPP;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.web.common.utils.wx.WxPFBeanUtils;
import com.masiis.shop.web.platform.service.wxpay.WxNotifyService;
import com.thoughtworks.xstream.XStream;
import com.thoughtworks.xstream.io.xml.DomDriver;
import com.thoughtworks.xstream.io.xml.XmlFriendlyNameCoder;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @Date 2016/8/12
 * @Author lzh
 */
@Controller
@RequestMapping("/wxntfy")
public class WxNotifyController extends BaseController{
    private Logger log = Logger.getLogger(this.getClass());

    @Resource(name = "bWxNotifyService")
    private WxNotifyService notifyService;

    @RequestMapping("/orderNtfy")
    @SignValid(paramType = BaseReq.class, isSysApi = false)
    public void uniOrderNotify(HttpServletRequest request, HttpServletResponse response){
        XStream xStream = new XStream(new DomDriver("UTF-8", new XmlFriendlyNameCoder("-_", "_")));
        xStream.ignoreUnknownElements();
        CallBackNotifyRes resObj = new CallBackNotifyRes();
        try {
            String requestBody = null;
            try{
                // 获取请求参数
                requestBody = getRequestBody(request);
                log.info("uniOrderNotify:请求参数:" + requestBody);
            } catch (Exception e) {
                String err = "获取请求参数错误:" + e.getMessage();
                log.error(err);
                throw new BusinessException(err);
            }

            CallBackNotifyReq param = null;
            try{
                xStream.processAnnotations(CallBackNotifyReq.class);
                param = (CallBackNotifyReq) xStream.fromXML(requestBody);
                log.info("uniOrderNotify:xml解析通过!");
            } catch (Exception e) {
                String err = "参数xml解析错误:" + e.getMessage();
                log.error(err);
                throw new BusinessException(err);
            }

            // 对param进行签名验证
            try{
                String sign = SysSignUtils.toSignString(param, WxConsAPP.WX_PAY_SIGNKEY);
                if(StringUtils.isBlank(sign)
                        || !sign.equals(param.getSign())){
                    throw new BusinessException("签名错误");
                }
                log.info("uniOrderNotify:签名检测通过!");
            } catch (Exception e) {
                String err = "签名验证错误:" + e.getMessage();
                log.error(err);
                throw new BusinessException(err);
            }

            // 开始进行订单异步回调通知业务,要进行参数有效性校验
            synchronized (this) {
                // 放到service中处理
                notifyService.handleWxPayNotify(param);
            }

            resObj.setReturn_code("SUCCESS");
            resObj.setReturn_msg("OK");
        } catch (Exception e) {
            String err = "公众号支付订单异步回调通知错误:" + e.getMessage();
            log.error(err);
            resObj.setReturn_code("FAIL");
            resObj.setReturn_msg(err);
        }

        xStream.processAnnotations(CallBackNotifyRes.class);
        if(StringUtils.isNotBlank(resObj.getReturn_msg())
                && resObj.getReturn_msg().length() > 128){
            // 返回信息不超过128长度
            resObj.setReturn_msg(resObj.getReturn_msg().substring(0, 128));
        }
        String res = xStream.toXML(resObj);
        System.out.println("response:" + res);
        sendResponseBody(response, res);
    }

}
