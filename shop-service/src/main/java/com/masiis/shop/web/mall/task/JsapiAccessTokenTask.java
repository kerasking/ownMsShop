package com.masiis.shop.web.mall.task;

import com.alibaba.druid.support.json.JSONParser;
import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.masiis.shop.common.util.HttpClientUtils;
import com.masiis.shop.common.constant.wx.WxConsSF;
import com.masiis.shop.web.common.utils.SpringRedisUtil;

import java.util.Map;

/**
 * 获取jsapi的令牌
 * Created by cai_tb on 16/3/17.
 */
public class JsapiAccessTokenTask {

    private final Log log = LogFactory.getLog(JsapiAccessTokenTask.class);

    public String requestToken(){
        String accessTokenUrl = null;
        String jsonResult = null;

        try {
            accessTokenUrl = WxConsSF.URL_JSSDK_ACCESS_TOKEN
                                   + "?grant_type=" + WxConsSF.JSSDK_GRANT_TYPE
                                   + "&appid=" + WxConsSF.APPID
                                   + "&secret=" + WxConsSF.APPSECRET;
            log.info("开始请求获取jssapi令牌中.....");
            jsonResult     = HttpClientUtils.httpGet(accessTokenUrl);
            log.info("请求获取jssapi令牌返回的[jsonResult="+jsonResult+"]");

            Map<String, Object> resultMap = new JSONParser(jsonResult).parseMap();

            if (resultMap.get("access_token") != null){
                SpringRedisUtil.saveEx("mall_jsapi_access_tocken", resultMap.get("access_token"), Long.valueOf(resultMap.get("expires_in").toString()));//7200=2小时
            }else{
                throw new RuntimeException("mall获取jsapi的令牌出错!");
            }

            return (String)resultMap.get("access_token");
        } catch (Exception e){
            e.printStackTrace();
            log.error("mall获取jsapi的令牌出错:[accessTokenUrl=" + accessTokenUrl + "][jsonResult=" + jsonResult + "]");
            return jsonResult;
        }
    }

    public static void main(String[] args){
        JsapiAccessTokenTask j = new JsapiAccessTokenTask();
        String res = j.requestToken();
        System.out.println("result: " + res);
    }
}
