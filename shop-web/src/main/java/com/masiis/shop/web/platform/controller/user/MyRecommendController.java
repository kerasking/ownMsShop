package com.masiis.shop.web.platform.controller.user;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.enums.platform.BOrderType;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.recommend.MyRecommendPo;
import com.masiis.shop.dao.beans.recommend.RecommenOrder;
import com.masiis.shop.dao.beans.user.CountGroup;
import com.masiis.shop.dao.beans.user.UserRecommend;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.common.constant.platform.SysConstants;
import com.masiis.shop.web.common.service.ComAgentLevelService;
import com.masiis.shop.web.common.service.UserService;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.order.PfBorderRecommenRewardService;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.user.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 我的推荐
 * @author muchaofeng
 * @date 2016/6/15 11:05
 */
@Controller
@RequestMapping("/myRecommend")
public class MyRecommendController extends BaseController{

    private final Log log = LogFactory.getLog(MyRecommendController.class);

    @Resource
    private PfUserStatisticsService pfUserStatisticsService;
    @Resource
    private PfUserRecommendRelationService pfUserRecommendRelationService;
    @Resource
    private PfBorderRecommenRewardService pfBorderRecommenRewardService;
    @Resource
    private SkuService skuService;
    @Resource
    private BOrderService bOrderService;
    @Resource
    private UserService userService;
    @Resource
    private UserCertificateService userCertificateService;
    @Resource
    private ComAgentLevelService comAgentLevelService;
    @Resource
    private PfUserSkuService pfUserSkuService;

    @Resource
    private CountGroupService countGroupService;
//    /**
//     * 我的推荐
//     * @author muchaofeng
//     * @date 2016/6/15 17:44
//     */
//    @RequestMapping("/feeList")
//    public ModelAndView feeList(HttpServletRequest request){
//        try{
//            ModelAndView modelAndView = new ModelAndView();
//            ComUser comUser = getComUser(request);
//            PfUserStatistics pfUserStatistics = pfUserStatisticsService.selectFee(comUser.getId());
//            int numByUserId = pfUserRecommendRelationService.findNumByUserId(comUser.getId());//帮我推荐的人数
//            int numByUserPid = pfUserRecommendRelationService.findNumByUserPid(comUser.getId());//我推荐的人数
//            Integer borders = pfBorderRecommenRewardService.findBorders(comUser.getId());//获得奖励订单数
//            Integer pBorders = pfBorderRecommenRewardService.findPBorders(comUser.getId());//发出奖励订单数
//            modelAndView.addObject("pfUserStatistics",pfUserStatistics);
//            modelAndView.addObject("numByUserId",numByUserId);
//            modelAndView.addObject("numByUserPid",numByUserPid);
//
//            modelAndView.addObject("borders",borders);
//            modelAndView.addObject("pBorders",pBorders);
//            modelAndView.setViewName("platform/user/wodetuijian");
//            return modelAndView;
//        }catch (Exception e){
//            e.printStackTrace();
//            log.error("获取代理产品列表失败!",e);
//        }
//        return null;
//    }

    /**
     * 我的推荐首页
     * @param request
     * @return
     */
    @RequestMapping("/myRecommen.shtml")
    public ModelAndView myRecommen(HttpServletRequest request){
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户未登录！");
        }
        ModelAndView mv = new ModelAndView();
        try {
            MyRecommendPo myRecommendPo = pfUserRecommendRelationService.myRecommen(comUser.getId(),1);
            mv.addObject("myRecommendPo",myRecommendPo);
        }catch (Exception e){
            e.printStackTrace();
        }
        mv.setViewName("platform/user/wodetuijian");
        return mv;
    }

    /**
     * 帮我推荐的详情列表
     * @author muchaofeng
     * @date 2016/6/15 17:44
     */
    @RequestMapping("/recommendGiveList")
    public ModelAndView recommendGiveList(HttpServletRequest request, @RequestParam(value = "skuId", required = false)Integer skuId){
        try{
            ModelAndView modelAndView = new ModelAndView();
            ComUser comUser = getComUser(request);
            List<UserRecommend> sumByUser = pfUserRecommendRelationService.findGiveSum(comUser.getId(), skuId);//推荐给我的
            for (UserRecommend userRecommend:sumByUser) {
                Integer giveNum = pfUserRecommendRelationService.findGiveNum(userRecommend.getUserId(), userRecommend.getSkuId());
                userRecommend.setNumber(giveNum);
            }

            List<Map<String, Object>> agentSkus = pfUserSkuService.listAgentSku(comUser.getId());
            modelAndView.addObject("agentSkus",agentSkus);
            modelAndView.addObject("skuId",skuId);
            modelAndView.addObject("sumByUser",sumByUser);
            modelAndView.setViewName("platform/user/bangwotuijianderen");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return null;
    }

    /**
     * 条件查询帮我推荐的人详情
     * @author muchaofeng
     * @date 2016/6/19 15:45
     */

    @RequestMapping("/giveRecommendLike.do")
    @ResponseBody
    public List<UserRecommend> giveRecommendLike(HttpServletRequest request,Integer skuId){
        List<UserRecommend> sumByUser =null;
        try{
            ComUser comUser = getComUser(request);

            sumByUser = pfUserRecommendRelationService.findGiveSumByLike(skuId,comUser.getId());//推荐给我的
            for (UserRecommend userRecommend:sumByUser) {
                Integer giveNum = pfUserRecommendRelationService.findGiveNum(userRecommend.getUserId(), userRecommend.getSkuId());
                userRecommend.setNumber(giveNum);
            }
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return sumByUser;
    }

    /**
     * 帮我推荐的人详情
     * @author muchaofeng
     * @date 2016/6/17 14:57
     */
    @RequestMapping("/giveRecommend")
    public ModelAndView giveRecommend(Long userId,Integer skuId){
        try{
            ModelAndView modelAndView = new ModelAndView();
            ComSku skuName = skuService.getSkuName(skuId);
            ComUser userName = userService.getUserById(userId);
            List<PfUserCertificate> certificates=new ArrayList<PfUserCertificate>();
            PfUserCertificate certificate =null;
            List<Long> giveList = pfUserRecommendRelationService.findGiveList(userId, skuId);
            for (Long id:giveList) {
                certificate = userCertificateService.getCertificateByuserskuId(id,skuId);
                ComUser user = userService.getUserById(id);
                certificate.setUserName(user.getRealName());
                ComAgentLevel agentLevel = comAgentLevelService.selectByPrimaryKey(certificate.getAgentLevelId());
                certificate.setAgentName(agentLevel.getName());
                certificates.add(certificate);
            }

            modelAndView.addObject("skuName",skuName.getName());
            modelAndView.addObject("userName",userName.getRealName());
            modelAndView.addObject("certificates",certificates);
            modelAndView.addObject("number",certificates.size());
            modelAndView.setViewName("platform/user/bangwotuijianxinxi");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return null;
    }


    /**
     * 我推荐的详情列表
     * @author muchaofeng
     * @date 2016/6/16 17:27
     */
    @RequestMapping("/myRecommendList")
    public ModelAndView myRecommendList(HttpServletRequest request){
        try{
            ModelAndView modelAndView = new ModelAndView();
            ComUser comUser = getComUser(request);

            List<UserRecommend> sumByUserPid = pfUserRecommendRelationService.findSumByUserPid(comUser.getId());//我推荐的详情列表

            for (UserRecommend userRecommend :sumByUserPid){
                PfUserSku pfUserSku = pfUserSkuService.getPfUserSkuByUserIdAndSkuId(userRecommend.getUserId(),userRecommend.getSkuId());
                userRecommend.setCountGroup(countGroupService.countGroupInfo(pfUserSku.getTreeCode()));
            }
//            List<PfUserSku> pfUserSkuList = pfUserSkuService.getPfUserSkuInfoByUserId(comUser.getId());
            //只显示代理的主商品
            List<PfUserSku> pfUserSkuList = pfUserSkuService.selectPrimarySkuByUserId(comUser.getId());
            List<Integer> list = new ArrayList<>();
            if(pfUserSkuList==null){
                throw new BusinessException("代理商品异常，初始化商品列表失败");
            }else{
                List<ComSku> skuList = new ArrayList();
                for(PfUserSku pfUserSku :pfUserSkuList){
                    list.add(pfUserSku.getAgentLevelId());
                    ComSku comSku = skuService.getSkuById(pfUserSku.getSkuId());
                    skuList.add(comSku);
                }
                modelAndView.addObject("skuList", skuList);
            }
//            List<ComAgentLevel> agentLevels = comAgentLevelService.selectAll();
            Collections.sort(list);
            //等级信息
            List<ComAgentLevel> agentLevels = comAgentLevelService.selectLastAll(list.get(0));
            modelAndView.addObject("agentLevels",agentLevels);
            modelAndView.addObject("sumByUserPid",sumByUserPid);
            modelAndView.setViewName("platform/user/wotuijianderen");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return null;
    }

    /**
     * 条件查询我推荐的详情列表
     * @author muchaofeng
     * @date 2016/6/17 10:31
     */

    @RequestMapping("/myRecommendLike.do")
    @ResponseBody
    public List<UserRecommend> myRecommendLike(HttpServletRequest request,Integer skuId, Integer agentLevelIdLong){
        List<UserRecommend> sumByUserPid =null;
        try{
            ComUser comUser = getComUser(request);

            sumByUserPid = pfUserRecommendRelationService.findSumByLike(skuId,comUser.getId(),agentLevelIdLong);//我推荐的详情列表
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return sumByUserPid;
    }

    /**
     * 我推荐的人详情
     * @author muchaofeng
     * @date 2016/6/16 17:56
     */

    @RequestMapping("/myRecommend")
    public ModelAndView myRecommend(Long userId,Integer skuId){
        try{
            ModelAndView modelAndView = new ModelAndView();

            PfUserCertificate certificate = userCertificateService.getCertificateByuserskuId(userId,skuId);
            String ctValue = PropertiesUtils.getStringValue("index_user_certificate_url");
            certificate.setImgUrl(ctValue + certificate.getImgUrl());
            modelAndView.addObject("certificate",certificate);

            ComSku skuName = skuService.getSkuName(skuId);
            ComUser user = userService.getUserById(userId);
            ComAgentLevel agentLevel = comAgentLevelService.selectByPrimaryKey(certificate.getAgentLevelId());
            //进货信息
            PfUserStatistics pfUserStatistics = pfUserStatisticsService.selectByUserIdAndSkuId(userId, skuId);
            PfUserSku pfUserSku = pfUserSkuService.getPfUserSkuByUserIdAndSkuId(userId, skuId);
            CountGroup countGroup = countGroupService.countRecommendGroup(pfUserSku.getTreeCode());
            modelAndView.addObject("skuName",skuName.getName());
            modelAndView.addObject("userName",user.getRealName());
            modelAndView.addObject("agentLevelName",agentLevel.getName());
            modelAndView.addObject("pfUserStatistics",pfUserStatistics);
            modelAndView.addObject("countGroup",countGroup.getR_count());
            modelAndView.setViewName("platform/user/wotuijianderenxinxi");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取代理产品列表失败!",e);
        }
        return null;
    }

    /**
     * ajax查询订单列表
     * @param request
     * @param currentPage   当前页码
     * @param tab           查询tab  0：收入奖励订单  1：发出奖励订单
     * @return
     * @throws Exception
     */
    @RequestMapping("/myRecommen.do")
    @ResponseBody
    public String ajaxMyRecommen(HttpServletRequest request,
                                 @RequestParam(value = "currentPage", required = true) Integer currentPage,
                                 @RequestParam(value = "tab", required = true) Integer tab) throws Exception{
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户未登录！");
        }
        JSONObject jsonObject = new JSONObject();
        try {
            MyRecommendPo myRecommendPo = pfBorderRecommenRewardService.getRecommenRewardOrder(comUser.getId(), currentPage, tab);
            jsonObject.put("isTrue","true");
            jsonObject.put("pageNum",myRecommendPo.getCurrentPage());
            jsonObject.put("totalCount",myRecommendPo.getTotalCount());
            StringBuffer sb = new StringBuffer("");
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
            switch (tab.intValue()){
                case 0 : {
                    for (RecommenOrder recommenOrder : myRecommendPo.getRecommenOrders()){
                        sb.append("<section class=\"sec1\" onclick=\"toOrderDetail(" + recommenOrder.getOrderId() + ")\">");
                        sb.append("<h2>订单号：<span>" + recommenOrder.getOrderCode() + "(" + recommenOrder.getOrderTypeView() + ")</span><b >购买人：" + recommenOrder.getBuyUserName() + "</b></h2>");
                        sb.append("<div class=\"shangpin\">");
                        sb.append("<div>");
                        sb.append("<h2><span>" + recommenOrder.getSkuName() + "</h2>");
                        sb.append("<h3><span style=\"color: #666\">x" + recommenOrder.getQuantity() + "</span><b>&nbsp;&nbsp;&nbsp;奖励：" + recommenOrder.getTotalPriceView() + "</b></h3>");
                        sb.append("</div></div>");
                        sb.append("<p>时间：" + sdf.format(recommenOrder.getCreateTime()));
                        sb.append("<span class=\"jixu\">发奖励的人：" + recommenOrder.getSendUserName() + "</span></p></section>");
                    }
                    break;
                }
                case 1 : {
                    for (RecommenOrder recommenOrder : myRecommendPo.getRecommenOrders()){
                        sb.append("<section class=\"sec1\" onclick=\"toOrderDetail(" + recommenOrder.getOrderId() + ")\">");
                        sb.append("<h2>订单号：<span>" + recommenOrder.getOrderCode() + "(" + recommenOrder.getOrderTypeView() + ")</span><b >购买人：" + recommenOrder.getBuyUserName() + "</b></h2>");
                        sb.append("<div class=\"shangpin\">");
                        sb.append("<div>");
                        sb.append("<h2><span>" + recommenOrder.getSkuName() + "</h2>");
                        sb.append("<h3><span style=\"color: #666\">x" + recommenOrder.getQuantity() + "</span><b>&nbsp;&nbsp;&nbsp;支出：" + recommenOrder.getTotalPriceView() + "</b></h3>");
                        sb.append("</div></div>");
                        sb.append("<p>时间：" + sdf.format(recommenOrder.getCreateTime()));
                        sb.append("<span class=\"jixu\">获得奖励的人：" + recommenOrder.getReceiveUserName() + "</span></p></section>");
                    }
                    break;
                }
            }
            jsonObject.put("html",sb.toString());
        }catch (Exception e){
            e.printStackTrace();
            jsonObject.put("isTrue","false");
            jsonObject.put("message","查询出错");
        }
        log.info(jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 获得奖励订单
     * @author muchaofeng
     * @date 2016/6/16 10:47
     */
    @RequestMapping("/getRewardBorder")
    public ModelAndView getRewardBorder(HttpServletRequest request, @RequestParam(value = "skuId", required = false)Integer skuId) throws Exception {
        ComUser comUser = getComUser(request);
        try{
            List<PfBorder> pfBorders = bOrderService.getRecommendPfBorder(comUser.getId(), skuId);
            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfUserCertificate certificateByuserskuId =null;
            PfUserCertificate certificateByuserskuId1 = null;
            if (pfBorders != null && pfBorders.size() != 0) {
                for (PfBorder pfBorder : pfBorders) {
                    List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(pfBorder.getId());
                    for (PfBorderItem pfBorderItem : pfBorderItems) {
                        pfBorderItem.setSkuUrl(skuValue + skuService.findComSkuImage(pfBorderItem.getSkuId()).getImgUrl());
                        pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量
                        certificateByuserskuId = userCertificateService.getCertificateByuserskuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
                        certificateByuserskuId1 = userCertificateService.getCertificateByuserskuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                    }
                    ComUser user = userService.getUserById(pfBorder.getUserId());
                    ComUser userName = userService.getUserById(pfBorder.getUserPid());
                    user.setWxId(certificateByuserskuId.getWxId());
                    userName.setWxId(certificateByuserskuId1.getWxId());
                    pfBorder.setUserName(user);
                    pfBorder.setUserPname(userName);
                    pfBorder.setOrderMoney(pfBorder.getOrderAmount().toString());
                    pfBorder.setPfBorderItems(pfBorderItems);
                }
            }
            List<Map<String, Object>> agentSkus = pfUserSkuService.listAgentSku(comUser.getId());

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("pfBorders", pfBorders);
            modelAndView.addObject("agentSkus", agentSkus);
            modelAndView.addObject("skuId", skuId);
            modelAndView.addObject("bOrderTypes", BOrderType.values());
            modelAndView.setViewName("platform/user/huoqujianglidingdan");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取获得奖励订单列表失败!",e);
        }
        return null;
    }

    
    /**
     * 发出奖励订单
     * @author muchaofeng
     * @date 2016/6/16 16:50
     */
    @RequestMapping("/sendRewardBorder")
    public ModelAndView sendRewardBorder(HttpServletRequest request, @RequestParam(value = "skuId", required = false)Integer skuId) throws Exception {
        ComUser comUser = getComUser(request);
        try{
            List<PfBorder> pfBorders = bOrderService.SendRecommendPfBorder(comUser.getId(), skuId);
            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfUserCertificate certificateByuserskuId =null;
            PfUserCertificate certificateByuserskuId1 = null;
            if (pfBorders != null && pfBorders.size() != 0) {
                for (PfBorder pfBorder : pfBorders) {
                    List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(pfBorder.getId());
                    for (PfBorderItem pfBorderItem : pfBorderItems) {
                        pfBorderItem.setSkuUrl(skuValue + skuService.findComSkuImage(pfBorderItem.getSkuId()).getImgUrl());
                        pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量

                        certificateByuserskuId1 = userCertificateService.getCertificateByuserskuId(pfBorder.getUserPid(), pfBorderItem.getSkuId());
                        certificateByuserskuId = userCertificateService.getCertificateByuserskuId(pfBorder.getUserId(), pfBorderItem.getSkuId());
                    }
                    ComUser user = userService.getUserById(pfBorder.getUserId());
                    ComUser userName = userService.getUserById(pfBorder.getUserPid());
                    user.setWxId(certificateByuserskuId.getWxId());
                    userName.setWxId(certificateByuserskuId1.getWxId());
                    pfBorder.setUserName(user);
                    pfBorder.setUserPname(userName);
                    pfBorder.setOrderMoney(pfBorder.getOrderAmount().toString());
                    pfBorder.setPfBorderItems(pfBorderItems);
                }
            }

            List<Map<String, Object>> agentSkus = pfUserSkuService.listAgentSku(comUser.getId());

            ModelAndView modelAndView = new ModelAndView();
            modelAndView.addObject("pfBorders", pfBorders);
            modelAndView.addObject("agentSkus", agentSkus);
            modelAndView.addObject("skuId", skuId);
            modelAndView.addObject("bOrderTypes", BOrderType.values());
            modelAndView.setViewName("platform/user/fachujianglidingdan");
            return modelAndView;
        }catch (Exception e){
            e.printStackTrace();
            log.error("获取发出奖励订单列表失败!",e);
        }
        return null;
    }
}
