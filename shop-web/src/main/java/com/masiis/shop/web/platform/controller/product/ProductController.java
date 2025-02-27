package com.masiis.shop.web.platform.controller.product;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.product.Product;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.common.constant.platform.SysConstants;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.order.BOrderAddService;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.product.PfUserAgentApplicationService;
import com.masiis.shop.web.platform.service.product.ProductService;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.user.PfUserRelationService;
import com.masiis.shop.web.common.service.UserAddressService;
import com.masiis.shop.web.common.service.UserService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @autor JingHao
 */
@Controller
@RequestMapping("/product")
public class ProductController extends BaseController {
    private Log log = LogFactory.getLog(this.getClass());
    @Resource
    private ProductService productService;
    @Resource
    private UserSkuService userSkuService;
    @Resource
    private BOrderService bOrderService;
    @Resource
    private SkuService skuService;
    @Resource
    private UserService userService;
    @Resource
    private UserAddressService userAddressService;
    @Resource
    private BOrderAddService bOrderAddService;
    @Resource
    private PfUserRelationService pfUserRelationService;
    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private PfUserAgentApplicationService pfUserAgentApplicationService;
    /**
     * 1 商品详细信息
     * 2 库存信息暂时都显示平台,与上级无关
     * 3 排单只和商品设置有关
     * @author jjh
     * @throws Exception
     */
    @RequestMapping("/skuDetails.shtml")
    public ModelAndView getProductDetails(HttpServletRequest request, HttpServletResponse response,
                                          @RequestParam(value = "skuId", required = true) Integer skuId,
                                          @RequestParam(value = "pUserId", required = false) Long pUserId) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/product/product");
        ComUser user = getComUser(request);
        if (user == null) {
            throw new BusinessException("用户未登录!");
        }
        ComSku sku = skuService.getSkuById(skuId);
        if (sku == null) {
            throw new BusinessException("sku不合法,系统不存在该sku");
        }
        Product productDetails = productService.getSkuDetails(skuId);
        if (user != null && user.getIsAgent() == 1) {
            productDetails.setIsPartner(true);
        }
        PfUserRelation pfUserRelation = pfUserRelationService.getRelation(user.getId(), skuId);//代理关系
        if(pfUserRelation==null){ //过滤非连接进入的用户
            productDetails.setIsUserByLink(1);
        }else{
            productDetails.setIsUserByLink(0);
        }
        productDetails.setMaxDiscount(productService.getMaxDiscount(skuId));
        PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), skuId);
        //订单信息
        PfBorder pfBorder = bOrderService.getPfBorderBySkuAndUserId(skuId,user.getId());
        mav.addObject("pfUserSku", pfUserSku);//是否代理过该商品
        mav.addObject("productDetails", productDetails);
        mav.addObject("pfBorder", pfBorder);//订单信息
        return mav;
    }

    @RequestMapping(value = "/user/{userId}", method = RequestMethod.GET)
    public ModelAndView getProductByUser(HttpServletRequest request, HttpServletResponse response, @PathVariable("userId") Long userId) throws Exception {
        ModelAndView mav;
        ComUser comUser = userService.getUserById(userId);
        if (comUser.getSendType() == 1) { //平台发货
            mav = new ModelAndView("/platform/user/userSkulist");
        } else {
            mav = new ModelAndView("/platform/user/selfSkuList");
        }
        List<Product> userProducts = productService.productListByUser(userId);
        if (userProducts != null) {
            for (Product product : userProducts) {
                product.setUpperStock(productService.getUpperStock(userId, product.getId()));
            }
        }
        mav.addObject("userProducts", userProducts);
        return mav;
    }

    /**
     * jjh
     * 平台发货，申请拿货
     */
    @RequestMapping(value = "/user/applyStock.do")
    @ResponseBody
    public String applyStock(HttpServletRequest request, HttpServletResponse response,
                             @RequestParam(required = true) Integer stock,
                             @RequestParam(required = true) Long id,
                             @RequestParam(required = false) String message,
                             @RequestParam(required = true) Long userAddressId
                             ) {
        JSONObject object = new JSONObject();
        try {
            HttpSession session = request.getSession();
            ComUser comUser = (ComUser) session.getAttribute("comUser");
            PfUserSkuStock product = productService.getStockByUser(id);
            //v1.5.6 减去平台赠送小白的库存
            //Integer currentStock = product.getStock()-product.getFrozenStock();
            log.info("申请拿货---RegisterGiveSkuStock---"+product.getRegisterGiveSkuStock()+"-----");
            Integer currentStock = product.getStock()-product.getFrozenStock()-product.getRegisterGiveSkuStock();
            if (currentStock - stock < 0) {
                throw new BusinessException("拿货数量超出库存!");
            }
            Long orderCode = bOrderAddService.addProductTake(comUser.getId(), product.getSkuId(), stock, message, userAddressId);
            object.put("borderId", orderCode);
            object.put("isError", false);
        } catch (Exception ex) {
            object.put("isError", true);
            object.put("message", ex.getMessage());
        }
        return object.toJSONString();
    }

    /**
     * 拿货信息
     * Jing Hao
     */
    @RequestMapping(value = "/user/applySkuInfo.list")
    @ResponseBody
    public ModelAndView applySkuInfo(HttpServletRequest request,
                                     HttpServletResponse response,
                                     @RequestParam(value = "selectedAddressId", required = false) Long selectedAddressId,
                                     @RequestParam("id") Long id) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/user/nahuo");
        ComUser comUser = getComUser(request);
        ComUserAddress comUserAddress = userAddressService.getOrderAddress(selectedAddressId, comUser.getId());
        if (comUserAddress != null) {
            mav.addObject("addressId", comUserAddress.getId());
            mav.addObject("comUserAddress", comUserAddress);
        }
        mav.addObject("pfUserSkuStockId", id);
        PfUserSkuStock product = productService.getStockByUser(id);
        Integer currentStock = product.getStock()-product.getFrozenStock();
        if (currentStock < 0) {
            product.setStock(0);
        } else {
            //v1.5.6 减去平台赠送小白的库存
            //product.setStock(product.getStock() - product.getFrozenStock());
            product.setStock(currentStock);
        }
        ComSku comSku = skuService.getSkuById(product.getSkuId());
        ComSkuImage comSkuImage = skuService.findComSkuImage(comSku.getId());
        String productImgValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
        PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(product.getUserId(), product.getSkuId());
        Map<String, Object> objectMap = productService.getLowerCount(product.getSkuId(), product.getStock(), pfUserSku.getAgentLevelId());
        PfSkuAgent pfSkuAgent = skuAgentService.getBySkuIdAndLevelId(product.getSkuId(),pfUserSku.getAgentLevelId());
        mav.addObject("productInfo", product);
        mav.addObject("lowerCount", objectMap.get("countLevel"));//下级人数
        mav.addObject("freeAgent", objectMap.get("freeAgent"));//0元代理标志
        mav.addObject("comSku", comSku);
        mav.addObject("comSkuImage", productImgValue + comSkuImage.getImgUrl());
        mav.addObject("levelStock", objectMap.get("levelStock"));
        mav.addObject("priceDiscount", objectMap.get("priceDiscount"));
        return mav;
    }

    /**
     * 补货信息
     * Jing Hao
     */
    @RequestMapping(value = "/user/addSkuInfo.list")
    @ResponseBody
    public ModelAndView addSkuInfo(HttpServletRequest request, HttpServletResponse response,
                                   @RequestParam("id") Long id) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/user/buhuo");
        PfUserSkuStock product = productService.getStockByUser(id);
        ComSku comSku = skuService.getSkuById(product.getSkuId());
        Integer upperStock = productService.getUpperStock(product.getUserId(), product.getSkuId());
        mav.addObject("productInfo", product);
        mav.addObject("upperStock", upperStock);//上级库存
        mav.addObject("comSku", comSku);
        return mav;
    }

    /**
     * 补货验证
     * JJH
     */
    @RequestMapping("/checkStock.do")
    @ResponseBody
    public String checkStock(HttpServletRequest request,
                             HttpServletResponse response,
                                   @RequestParam(value = "skuId",required = true) Integer skuId,
                                   @RequestParam(value = "stock",required = true) Integer stock,
                                   @RequestParam(value = "pUserId",required = true) Long pUserId
                                   ) throws Exception {
        JSONObject object = new JSONObject();
         try{
             int status = skuService.getSkuStockStatus(skuId,stock,pUserId);
             object.put("isError", false);
             object.put("stockStatus", status);
         }catch (Exception ex){
             object.put("isError", true);
             object.put("message", ex.getMessage());
         }
        return object.toJSONString();
    }
    
    /**
      * @Author jjh
      * @Date 2016/5/7 0007 下午 3:23
      * 拿货成功预览
      */
    @RequestMapping(value = "replenishmentSelf.shtml")
    public ModelAndView replenishmentOrder(@RequestParam(value = "bOrderId", required = true) Long bOrderId,
                                                        HttpServletRequest request) throws Exception {
        if (getComUser(request) == null) {
            throw new BusinessException("请重新登录");
        }
        ModelAndView mv = new ModelAndView();
        PfBorder pfBorder = bOrderService.getPfBorderById(bOrderId);
        mv.addObject("pfBorder", pfBorder);
        //sendtype  1:平台代发货  2:自己发货  0:未选择发货类型
        //orderType 1:补货 2:拿货 0:代理
        if (pfBorder.getSendType() == 2 || pfBorder.getOrderType() == 2) {
            PfBorderConsignee pfBorderConsignee = bOrderService.findpfBorderConsignee(pfBorder.getId());
            mv.addObject("pfBorderConsignee", pfBorderConsignee);
        }
        mv.setViewName("platform/user/previewnahuo");
        return mv;
    }

    /**
     * jjh
     * 添加意向
     * @param request
     * @param skuId :商品ID
     * @param applyName：姓名
     * @param wxCode :微信号
     * @param mobile：手机号
     * @return
     * @throws Exception
     */
    @RequestMapping("/addAgent.do")
    @ResponseBody
    public String addAgentApplication(HttpServletRequest request,
                             @RequestParam(value = "applyName",required = true) String applyName,
                             @RequestParam(value = "wxCode",required = true) String wxCode,
                             @RequestParam(value = "mobile",required = true) String mobile,
                             @RequestParam(value = "skuId",required = true) Integer skuId
    ){
        JSONObject object = new JSONObject();
        try{
            ComUser comUser = getComUser(request);
            PfUserAgentApplication pfUserAgentApplication = new PfUserAgentApplication();
            pfUserAgentApplication.setCreateMan(comUser.getId());
            pfUserAgentApplication.setCreateTime(new Date());
            pfUserAgentApplication.setMobile(mobile);
            pfUserAgentApplication.setName(applyName);
            pfUserAgentApplication.setWxId(wxCode);
            pfUserAgentApplication.setSkuId(skuId);
            pfUserAgentApplication.setStatus(0);
            pfUserAgentApplication.setRemark("");
            pfUserAgentApplication.setAgentLevelId(0);
            pfUserAgentApplicationService.addApplicationUser(pfUserAgentApplication);
            object.put("isError", false);
        }catch (Exception ex){
            object.put("isError", true);
            log.info(ex.getMessage());
        }
        return object.toJSONString();
    }

    /**
     * jjh
     * 一个手机号只能发一次意向
     * @param phone
     * @return
     */
    @RequestMapping(value = "/checkBindedPhone.do")
    @ResponseBody
    public String isBindedPhone(@RequestParam(value = "phone", required = true) String phone) {
        JSONObject obj = new JSONObject();
        try {
            List<PfUserAgentApplication> pfUserAgentApplication = pfUserAgentApplicationService.getPfUserAgentApplicationByPhone(phone);
            if (pfUserAgentApplication != null && pfUserAgentApplication.size()>0) {
                obj.put("isError", true);
                obj.put("isBinded", true);
                obj.put("msg", "手机号已经被绑定请更换手机号");
                return obj.toJSONString();
            } else {
                obj.put("isError", false);
                return obj.toJSONString();
            }
        } catch (Exception e) {
            obj.put("isError", true);
            obj.put("isBinded", false);
            obj.put("msg", "手机号查询是否被绑定失败:" + e.getMessage());
            return obj.toJSONString();
        }
    }
}
