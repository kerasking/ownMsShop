package com.masiis.shop.web.common.utils.wx;

import com.alibaba.fastjson.annotation.JSONField;
import com.masiis.shop.common.annotation.SignField;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.MD5Utils;
import com.masiis.shop.common.beans.wx.wxpay.BrandWCPayReq;
import com.masiis.shop.common.beans.wx.wxpay.WxPaySysParamReq;
import com.masiis.shop.common.constant.wx.WxConsSF;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Created by lzh on 2016/3/8.
 */
public class WxSFBeanUtils {
    private static Logger log = Logger.getLogger(WxSFBeanUtils.class);

    private final static String[] charArrs = {"A", "B", "C", "D", "E", "F", "G", "H", "I", "J", "K", "L", "M", "N",
    "O", "P", "Q", "R", "S", "T", "U", "V", "W", "X", "Y", "Z", "1", "2", "3", "4", "5", "6", "7", "8", "9", "0"};

    /**
     * 生成32位随机字符串
     *
     * @return
     */
    public static String createGenerateStr(){
        int len = 32;
        StringBuilder res = new StringBuilder();
        for(int i = 0; i < len; i++){
            res.append(charArrs[(int)(Math.random() * charArrs.length)]);
        }
        return res.toString();
    }

    public static void main(String[] args) throws IllegalAccessException {
        BrandWCPayReq br = new BrandWCPayReq();
        br.setAppId(WxConsSF.APPID);
        String nonce = "sddfs22dsdf5ssdfa53wq3";
        System.out.println("nonce:" + nonce);
        br.setNonceStr(nonce);
        br.setSignType("MD5");
        br.setTimeStamp("1457513229909");
        br.setPackages("prepay_id=wx2016030916391321529474ea0694275688");
        System.out.println(toSignString(br));
    }

    /**
     * 根据反射来组织bean对象的签名字符串
     *
     * @param obj
     * @return
     */
    public static String toSignString(Object obj) {
        if(obj == null){
            throw new BusinessException("parameter obj is null");
        }
        Class clazz = obj.getClass();
        Map<String, Object> map = new HashMap<String, Object>();
        List<String> list = new ArrayList<String>();
        try {
            for(Field f:clazz.getDeclaredFields()) {
                String key = f.getName();
                f.setAccessible(true);
                JSONField aJF = f.getAnnotation(JSONField.class);
                if (aJF != null) {
                    key = aJF.name();
                }
                SignField sf = f.getAnnotation(SignField.class);
                if(sf != null){
                    continue;
                }
                String value = (String) f.get(obj);
                if (StringUtils.isNotBlank(value)) {
                    list.add(key + "=" + f.get(obj) + "&");
                }
            }
        } catch (IllegalAccessException e) {
            log.info(e.getMessage());
        }
        int size = list.size();
        String [] arrayToSort = list.toArray(new String[size]);
        Arrays.sort(arrayToSort, String.CASE_INSENSITIVE_ORDER);
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < size; i ++) {
            sb.append(arrayToSort[i]);
        }
        String result = sb.toString();
        result += "key=" + WxConsSF.WX_PAY_SIGN_KEY;
        result = MD5Utils.encrypt(result).toUpperCase();
        return result;
    }

    public static String createWxPayParam(WxPaySysParamReq param){
        if(param == null){
            throw new BusinessException("param is null");
        }

        String res = toSignString(param);
        return res;
    }

}
