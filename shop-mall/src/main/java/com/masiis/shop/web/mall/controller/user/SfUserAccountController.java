package com.masiis.shop.web.mall.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.mall.controller.base.BaseController;
import com.masiis.shop.web.mall.service.order.SfOrderItemDistributionService;
import com.masiis.shop.web.mall.service.order.SfOrderService;
import com.masiis.shop.web.mall.service.user.SfUserAccountService;
import com.masiis.shop.web.mall.service.user.SfUserExtractApplyService;
import com.masiis.shop.web.mall.service.user.SfUserRelationService;
import com.masiis.shop.web.common.service.UserService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Created by wangbingjian on 2016/4/8.
 */
@Controller
@RequestMapping(value = "/sfaccount")
public class SfUserAccountController extends BaseController {

    private final Logger log = Logger.getLogger(SfUserAccountController.class);

    @Autowired
    private SfUserAccountService userAccountService;
    @Autowired
    private SfOrderItemDistributionService sfOrderItemDistributionService;
    @Autowired
    private UserService userService;
    @Autowired
    private SfUserRelationService sfUserRelationService;
    @Autowired
    private SfOrderService sfOrderService;
    @Autowired
    private SfUserExtractApplyService sfUserExtractApplyService;
    /**
     * 我的佣金首页
     * @param request
     * @return
     * @author:wbj
     */
    @RequestMapping(value = "/commissionHome.shtml")
    public ModelAndView userCommission(HttpServletRequest request) throws Exception{
        log.info("进入小铺我的佣金首页");
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户没有登录");
        }
        ModelAndView mv = new ModelAndView();
        Long userId = comUser.getId();
        //查询分销用户账户表
        SfUserAccount userAccount = userAccountService.findAccountByUserId(userId);
        if (userAccount == null){
            userAccount = new SfUserAccount();
            BigDecimal fee = new BigDecimal(0);
            userAccount.setUserId(Long.valueOf(0));
            userAccount.setCountingFee(fee);
            userAccount.setExtractableFee(fee);
            mv.addObject("userAccount",userAccount);
            mv.addObject("totalCount",0);
            mv.addObject("orderItemDistributions",null);
            mv.setViewName("mall/user/sf_commission");
            return mv;
        }
        SfOrderItemDistribution record = new SfOrderItemDistribution();
        record.setUserId(userId);
        record.setIsCounting(1);
        //根据条件查询 小铺订单商品分润 数量
        int totalCount = sfOrderItemDistributionService.findCountByCondition(record);
        log.info("userId:"+userId+"   小铺订单商品分润数量:"+totalCount);
        List<SfOrderItemDistribution> list = null;
        if (totalCount > 0){
            //根据userId查询小铺订单商品分润信息
            list = sfOrderItemDistributionService.findCommissionRecordByUserIdLimitPage(userId,1,20);
        }
        mv.addObject("currentPage",1);
        mv.addObject("pageSize",20);
        mv.addObject("userAccount",userAccount);
        mv.addObject("totalCount",totalCount);
        mv.addObject("orderItemDistributions",list);
        mv.setViewName("mall/user/sf_commissionHome");
        return mv;
    }

    /**
     * ajax 查询更多用户佣金记录
     * @param currentPage
     * @param count
     * @param request
     * @return
     * @author:wbj
     */
    @RequestMapping(value = "/moreCommission.do")
    @ResponseBody
    public String queryMoreCommissionRecord(@RequestParam(value = "currentPage",required = true) int currentPage,
                                            @RequestParam(value = "count",required = true) int count,
                                            HttpServletRequest request) throws Exception{

        ComUser user = getComUser(request);
        log.info("ajax 查询更多用户佣金记录");
        if (user == null){
            log.info("用户未登录");
            throw new BusinessException("用户未登录");
        }
        JSONArray jsonArray = new JSONArray();
        try {
            int pageSize = 10;
            currentPage = currentPage + 1;
            List<SfOrderItemDistribution> list = sfOrderItemDistributionService.findCommissionRecordByUserIdLimitPage(user.getId(),currentPage,pageSize);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            JSONObject jsonObject;
            for(SfOrderItemDistribution itemDistribution : list){
                jsonObject = new JSONObject();
                jsonObject.put("distributionAmount",itemDistribution.getDistributionAmount());
                jsonObject.put("nkName",itemDistribution.getNkName());
                jsonObject.put("skuName",itemDistribution.getSkuName());
                jsonObject.put("skuId",itemDistribution.getSkuId());
                jsonObject.put("orderTime",format.format(itemDistribution.getOrderTime()));
                jsonArray.put(jsonObject);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.info("ajax 查询更多用户佣金记录 查询异常");
            throw new BusinessException("ajax 查询更多用户佣金记录 查询异常");
        }
        return jsonArray.toString();
    }

    /**
     * 我的奖励首页
     * @param request
     * @return
     */
    @RequestMapping(value = "/rewardHome.shtml")
    public ModelAndView myReward(HttpServletRequest request){
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户没有登录");
        }
        Long userId = comUser.getId();
        ModelAndView mv = new ModelAndView();
        log.info("处理可提现的财富、佣金记录begin");
        SfUserAccount userAccount = userAccountService.findAccountByUserId(userId);
        if (userAccount == null){
            userAccount = new SfUserAccount();
            BigDecimal fee = new BigDecimal(0);
            userAccount.setUserId(Long.valueOf(0));
            userAccount.setCountingFee(fee);
            userAccount.setExtractableFee(fee);
            mv.addObject("userAccount",userAccount);
            mv.addObject("totalCount",0);
            mv.addObject("orderItemDistributions",null);
            return mv;
        }
        //根据条件查询 小铺订单商品分润 数量
        int totalCount = sfOrderItemDistributionService.findCommissionRecordCountByUserId(userId);
        log.info("userId:"+userId+"   小铺订单商品分润数量:"+totalCount);
        List<SfOrderItemDistribution> list = null;
        if (totalCount > 0){
            //根据userId查询小铺订单商品分润信息
            list = sfOrderItemDistributionService.findCommissionRecordByUserIdLimitPage(userId,1,10);
        }

        log.info("处理可提现的财富、佣金记录end");

        log.info("处理已付款、未付款订单财富begin");
        BigDecimal isPayDistribution = new BigDecimal(0);
        BigDecimal isNotPayDistribution = new BigDecimal(0);
        List<Map<String,Object>> mapList = sfOrderItemDistributionService.selectSumAmount(userId);
        for (Map<String,Object> map : mapList){
            if ("0".equals(map.get("paystatus").toString())){
                isNotPayDistribution = isNotPayDistribution.add(new BigDecimal(map.get("amount").toString()));
            }
            if ("1".equals(map.get("paystatus").toString())){
                isPayDistribution = isPayDistribution.add(new BigDecimal(map.get("amount").toString()));
            }
        }
        log.info("处理已付款、未付款订单财富end");
        log.info("查询已经提现成功的金额begin");
        Map<String,BigDecimal> map = sfUserExtractApplyService.selectextractFeeByUserId(userId);
        BigDecimal withdraw;
        if (map == null){
            withdraw = new BigDecimal(0);
        }else {
            withdraw = new BigDecimal(map.get("extractFee").toString());
        }
        log.info("查询已经提现成功的金额end");
        List<SfOrder> sfOrders = sfOrderService.findByUserId(userId);
        //是否购买过商品
        if (sfOrders != null && sfOrders.size() > 0){
            mv.addObject("hasOrder",1);
        }else {
            mv.addObject("hasOrder",0);
        }
        mv.addObject("withdraw",withdraw);
        mv.addObject("isPayDistribution",isPayDistribution.subtract(userAccount.getExtractableFee().add(withdraw)));
        mv.addObject("isNotPayDistribution",isNotPayDistribution);
        mv.addObject("currentPage",1);
        mv.addObject("pageSize",10);
        userAccount.setExtractableFee(userAccount.getExtractableFee().subtract(userAccount.getAppliedFee() == null?new BigDecimal(0):userAccount.getAppliedFee()));
        mv.addObject("userAccount",userAccount);
        mv.addObject("totalCount",totalCount);
        mv.addObject("orderItemDistributions",list);
        mv.addObject("isBuy",comUser.getIsBuy());
        mv.setViewName("mall/user/sf_reward");
        return mv;
    }
}
