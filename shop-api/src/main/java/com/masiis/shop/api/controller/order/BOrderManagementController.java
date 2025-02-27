package com.masiis.shop.api.controller.order;

import com.masiis.shop.api.bean.order.*;
import com.masiis.shop.api.constants.SignValid;
import com.masiis.shop.api.constants.SysConstants;
import com.masiis.shop.api.constants.SysResCodeCons;
import com.masiis.shop.api.controller.base.BaseController;
import com.masiis.shop.common.enums.platform.BOrderStatus;
import com.masiis.shop.common.util.DateUtil;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.order.BorderDetail;
import com.masiis.shop.dao.platform.order.PfBorderPaymentMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.common.service.UserService;
import com.masiis.shop.web.mall.service.order.*;
import com.masiis.shop.web.mall.service.product.SkuImageService;
import com.masiis.shop.web.platform.service.order.*;
import com.masiis.shop.web.platform.service.product.PfUserSkuStockService;
import com.masiis.shop.web.platform.service.system.ComDictionaryService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @Date 2016/5/5
 * @Auther lzh
 */
@Controller
@RequestMapping("/om")
public class BOrderManagementController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private BOrderService bOrderService;
    @Resource
    private SfOrderService sfOrderService;
    @Resource
    private SkuService skuService;
    @Resource
    private ComShipManService comShipManService;
    @Resource
    private UserService userService;
    @Resource
    private ComDictionaryService comDictionaryService;
    @Resource
    private PfBorderPaymentMapper pfBorderPaymentMapper;
    @Resource
    private PfSupplierBankService pfSupplierBankService;
    @Resource
    private PfUserSkuStockService pfUserSkuStockService;
    @Resource
    private SfOrderConsigneeService sfOrderConsigneeService;
    @Resource
    private SfOrderItemService sfOrderItemService;
    @Resource
    private SfOrderPaymentService sfOrderPaymentService;
    @Resource
    private SfOrderItemDistributionService sfOrderItemDistributionService;
    @Resource
    private UserSkuService userSkuService;
    @Resource
    private SkuImageService skuImageService;
    @Resource
    private BOrderPayService payBOrderService;
    @Resource
    private BOrderShipService bOrderShipService;
    @Resource
    private PfBorderConsigneeService pfBorderConsigneeService;

    /**
     * 订单管理
     * @author muchaofeng
     * @date 2016/5/24 16:10
     */
    @RequestMapping("/index")
    @ResponseBody
    @SignValid(paramType = OManagementIndexReq.class)
    public OManagementIndexRes bOrderManagement(HttpServletRequest request, OManagementIndexReq req, ComUser user){
        OManagementIndexRes res = new OManagementIndexRes();
        try{
            List<PfBorder> pfBorders = bOrderService.findByUserId(user.getId(), null, null);
            List<PfBorder> pfBorderps = bOrderService.findByUserPid(user.getId(), null, null);
            List<PfBorder> pfBorders0 = new ArrayList<>();
            List<PfBorder> pfBorders7 = new ArrayList<>();//代发货
            List<PfBorder> pfBorders8 = new ArrayList<>();//待收货
            List<PfBorder> pfBorders6 = new ArrayList<>();//排单中
            for (PfBorder pfBord : pfBorders) {
                if (pfBord.getOrderStatus() == 0) {
                    pfBorders0.add(pfBord);//待付款
                } else if (pfBord.getOrderStatus() == 7 ) {
                    pfBorders7.add(pfBord);//代发货
                } else if (pfBord.getOrderStatus() == 8 ) {
                    pfBorders8.add(pfBord);//待收货
                }  else if (pfBord.getOrderStatus() == 6) {
                    pfBorders6.add(pfBord);//排单中
                }
            }
            List<PfBorder> pfBorderp0 = new ArrayList<>();
            List<PfBorder> pfBorderp7 = new ArrayList<>();//代发货
            List<PfBorder> pfBorderp8 = new ArrayList<>();//待收货
            List<PfBorder> pfBorderp6 = new ArrayList<>();//排单中
            for (PfBorder pfBord : pfBorderps) {
                if (pfBord.getOrderStatus() == 0) {
                    pfBorderp0.add(pfBord);//待付款
                } else if (pfBord.getOrderStatus() == 8 ) {
                    pfBorderp8.add(pfBord);//待收货
                }  else if (pfBord.getOrderStatus() == 6) {
                    pfBorderp6.add(pfBord);//排单中
                }else if (pfBord.getOrderStatus() == 7 ) {
                    pfBorderp7.add(pfBord);//代发货
                }
            }
            res.setInWaitShipNum(pfBorders7.size());
            res.setInMPSNum(pfBorders6.size());
            res.setInPayingNum(pfBorders0.size());
            res.setInReceiveNum(pfBorders8.size());
            res.setOutShipNum(pfBorderp7.size());
            res.setOutMPSNum(pfBorderp6.size());
            res.setOutWaitPayNum(pfBorderp0.size());
            res.setOutWaitReceiveNum(pfBorderp8.size());
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            e.printStackTrace();
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 分段查询进货订单
     * @author muchaofeng
     * @date 2016/5/24 16:51
     */

    @RequestMapping("/stockBorder")
    @ResponseBody
    @SignValid(paramType = OrderListPagingReq.class)
    public OrderListPagingRes stockBorder(HttpServletRequest request, OrderListPagingReq req, ComUser comUser) throws Exception {
        OrderListPagingRes res = new OrderListPagingRes();
        try{
            Integer sendType = req.getSendType();
            Integer orderStatus = req.getOrderStatus();

            if (request.getSession().getAttribute("defaultBank") == null || request.getSession().getAttribute("defaultBank") == "") {
                PfSupplierBank defaultBank = pfSupplierBankService.getDefaultBank();
                request.getSession().setAttribute("defaultBank", defaultBank);
            }
            List<PfBorder> pfBorders = bOrderService.findByUserId(comUser.getId(), orderStatus, sendType);
            if (orderStatus != null) {
                if (orderStatus == 0) {
                    List<PfBorder> byUserId = bOrderService.findByUserId(comUser.getId(), 9, sendType);
                    for (PfBorder pfBorder : byUserId) {
                        pfBorders.add(pfBorder);
                    }
                }
            }
            String index = null;
            if (orderStatus == null && sendType == null) {
                index = "0";//全部
            } else if (orderStatus == 7) {
                index = "2";//代发货
            } else if (orderStatus == 8) {
                index = "3";//待收货
            } else if (orderStatus == 6) {
                index = "5";//排单中
                Iterator<PfBorder> chk_itw = pfBorders.iterator();
                while (chk_itw.hasNext()) {
                    PfBorder pfBorder = chk_itw.next();
                    if (pfBorder.getSendType() == 2 && pfBorder.getOrderStatus() == 6) {//排单订单
                        chk_itw.remove();
                    }
                }
            } else if (orderStatus == 0 || orderStatus == 9) {
                index = "1";//待付款//线下支付待付款
            } else if (orderStatus == 3) {
                index = "4";//已完成
            }
            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            if (pfBorders != null && pfBorders.size() != 0) {
                for (PfBorder pfBorder : pfBorders) {
                    List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(pfBorder.getId());
                    for (PfBorderItem pfBorderItem : pfBorderItems) {
                        pfBorderItem.setSkuUrl(skuValue + skuService.findComSkuImage(pfBorderItem.getSkuId()).getImgUrl());
                        pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量
                    }
                    pfBorder.setPidUserName("平台");
                    pfBorder.setOrderMoney(pfBorder.getOrderAmount().toString());
                    pfBorder.setPfBorderItems(pfBorderItems);
                    String insertDay = DateUtil.insertDay(pfBorder.getCreateTime());
                    pfBorder.setPayTimes(insertDay);
                }
            }
            res.setPfBorders(pfBorders);
            res.setIndex(index);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception ex){
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("网络错误");
            }
        }
        return res;
    }

    /**
     * 异步查询进货订单
     * @author muchaofeng
     * @date 2016/5/24 17:23
     */
    @RequestMapping("/clickType.do")
    @ResponseBody
    @SignValid(paramType = OrderListPagingReq.class)
    public OrderListPagingRes clickType(HttpServletRequest request,OrderListPagingReq req,ComUser user) {
        OrderListPagingRes res = new OrderListPagingRes();
        Integer index = req.getIndex();
        List<PfBorder> pfBorder = null;
        try {
            if (request.getSession().getAttribute("defaultBank") == null || request.getSession().getAttribute("defaultBank") == "") {
                PfSupplierBank defaultBank = pfSupplierBankService.getDefaultBank();
                request.getSession().setAttribute("defaultBank", defaultBank);
            }

            if (index == 0) {
                pfBorder = bOrderService.findPfBorder(user.getId(), null, null);
            } else if (index == 1) {
                pfBorder = bOrderService.findPfBorder(user.getId(), 0, null);
                List<PfBorder> pfBorder1 = bOrderService.findPfBorder(user.getId(), 9, null);
                for (PfBorder pfBorder11 : pfBorder1) {
                    pfBorder.add(pfBorder11);
                }
            } else if (index == 2) {
                pfBorder = bOrderService.findPfBorder(user.getId(), 7, null);
            } else if (index == 3) {
                pfBorder = bOrderService.findPfBorder(user.getId(), 8, null);
            }  else if (index == 5) {
                pfBorder = bOrderService.findPfBorder(user.getId(), 6, null);
                Iterator<PfBorder> chk_itw = pfBorder.iterator();
                while (chk_itw.hasNext()) {
                    PfBorder pfBorders = chk_itw.next();
                    if (pfBorders.getSendType() == 2 && pfBorders.getOrderStatus() == 6) {//排单订单
                        chk_itw.remove();
                    }
                }
            }else if (index == 4) {
                pfBorder = bOrderService.findPfBorder(user.getId(), 3, null);
            }res.setPfBorders(pfBorder);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception ex) {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("网络错误");
            }
        }
        return res;
    }

    /**
     * 确认收货
     * @author muchaofeng
     * @date 2016/5/24 17:39
     */
    @RequestMapping("/deliver.do")
    @ResponseBody
    @SignValid(paramType = BorderDeliverReq.class)
    public OrderListPagingRes closeDeal(HttpServletRequest request, BorderDeliverReq req) {
        OrderListPagingRes res = new OrderListPagingRes();
        Long orderId = req.getOrderId();
        try {
            PfBorder border = bOrderService.getPfBorderById(orderId);
            bOrderShipService.receiptBOrder(border);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception ex) {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("网络错误");
            }
        }
        return res;
    }

    /**
     * 进货订单详情
     * @author muchaofeng
     * @date 2016/5/25 16:19
     */

    @RequestMapping("/borderDetail.html")
    @ResponseBody
    @SignValid(paramType = BorderDetailReq.class)
    public BorderDetailRes borderDetils(HttpServletRequest request, BorderDetailReq req,ComUser user) throws Exception {
        BorderDetailRes res = new BorderDetailRes();
        Long id = req.getId();
        BorderDetail borderDetail = new BorderDetail();
        try{
            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfBorder pfBorder = bOrderService.getPfBorderById(id);
            List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(id);
            PfUserSkuStock pfUserSkuStock = null;
            Integer stockNum = 0;
            StringBuffer stringBuffer = new StringBuffer();
            for (PfBorderItem pfBorderItem : pfBorderItems) {
                ComSkuImage comSkuImage = skuService.findComSkuImage(pfBorderItem.getSkuId());
                pfBorderItem.setSkuUrl(skuValue + comSkuImage.getImgUrl());
                pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量
                pfUserSkuStock = pfUserSkuStockService.selectByUserIdAndSkuId(user.getId(), pfBorderItem.getSkuId());
                if (pfUserSkuStock == null) {
                    stockNum = stockNum + 0;
                } else {
                    stockNum = stockNum + pfUserSkuStock.getStock();
                }
            }
            //快递公司信息
            List<PfBorderFreight> pfBorderFreights = bOrderService.findByPfBorderFreightOrderId(id);
            if (pfBorderFreights.size() != 0 && pfBorderFreights != null) {
                for (PfBorderFreight pfBorderFreight : pfBorderFreights) {
                    stringBuffer.append("<p>承运公司：<span>" + pfBorderFreight.getShipManName() + "</span></p>");
                    stringBuffer.append("<p>运单编号：<span>" + pfBorderFreight.getFreight() + "</span></p>");
                }
            } else {
                stringBuffer.append("<p>承运公司：<span></span></p>");
                stringBuffer.append("<p>运单编号：<span></span></p>");
            }

            //收货人
            PfBorderConsignee pfBorderConsignee = bOrderService.findpfBorderConsignee(id);
            borderDetail.setPfBorder(pfBorder);
            borderDetail.setPfBorderItems(pfBorderItems);
            borderDetail.setPfBorderFreights(pfBorderFreights);
            borderDetail.setPfBorderConsignee(pfBorderConsignee);
            res.setStockNum(stockNum);
            res.setStringBuffer(stringBuffer.toString());
            res.setBorderDetail(borderDetail);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception ex){
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("平台进货订单详情");
            }
        }
        return res;
    }

    /**
     * 分页查询出货订单
     *
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/qSaleOrder")
    @ResponseBody
    @SignValid(paramType = OrderListPagingReq.class)
    public OrderListPagingRes querySaleOrder(HttpServletRequest request, OrderListPagingReq req, ComUser user){
        OrderListPagingRes res = new OrderListPagingRes();
        try{
            Integer sendType = req.getSendType();
            Integer orderStatus = req.getOrderStatus();
            List<PfBorder> pfBorders = bOrderService.findByUserPid(user.getId(), orderStatus, sendType);
            String index = null;
            Integer borderNum = 0;
            if(orderStatus == null && sendType == null){
                index = "0";//全部
            } else if (orderStatus == 0) {
                index = "1";//待付款
            } else if (orderStatus == 7) {
                index = "2";//代发货
                borderNum = pfBorders.size();
            } else if (orderStatus == 8) {
                index = "3";//待收货
            } else if (orderStatus == 3) {
                index = "4";//已完成
            } else if(orderStatus == 6) {
                index = "5";//排单中
                Iterator<PfBorder> chk_itw = pfBorders.iterator();
                while (chk_itw.hasNext()) {
                    PfBorder pfBorder = chk_itw.next();
                    if (pfBorder.getSendType() == 2 && pfBorder.getOrderStatus() == 6 ) {//排单订单
                        chk_itw.remove();
                    }
                }
            }
//        Integer waitShipNum = bOrderService.queryOrderNumsByUpidAndStatus(user.getId(), BOrderStatus.WaitShip.getCode());

            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            if (pfBorders != null && pfBorders.size() != 0) {
                for (PfBorder pfBorder : pfBorders) {
                    List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(pfBorder.getId());
                    PfBorderConsignee pfBorderConsignee = bOrderService.findpfBorderConsignee(pfBorder.getId());
                    for (PfBorderItem pfBorderItem : pfBorderItems) {
                        pfBorderItem.setSkuUrl(skuValue + skuService.findComSkuImage(pfBorderItem.getSkuId()).getImgUrl());
                        pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量
                    }
                    pfBorder.setPfBorderConsignee(pfBorderConsignee);//收货人信息
                    pfBorder.setPfBorderItems(pfBorderItems);
                }
            }
            if(request.getSession().getAttribute("comShipMans")==null || request.getSession().getAttribute("comShipMans")==""){
                List<ComShipMan> comShipMans = comShipManService.list();
                request.getSession().setAttribute("comShipMans", comShipMans);
            }
            res.setPfBorders(pfBorders);
            res.setWaitShipNum(borderNum);
            res.setIndex(index);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch(Exception ex){
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("分页查询出货订单");
            }
        }
        return res;
    }

    /**
     * 异步查询出货订单
     *
     * @author muchaofeng
     * @date 2016/4/7 16:18
     */

    @RequestMapping("/clickDeliverType.do")
    @ResponseBody
    @SignValid(paramType = OrderListPagingReq.class)
    public OrderListPagingRes clickDeliverType(HttpServletRequest request, OrderListPagingReq req, ComUser user) {
        OrderListPagingRes res = new OrderListPagingRes();
        List<PfBorder> pfBorder = null;
        try {
            Integer index= req.getIndex();
            if (index== 0) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), null, null);
            } else if (index == 1) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), 0, null);
                List<PfBorder> pfBorder1 = bOrderService.findPfpBorder(user.getId(), 9, null);
                for (PfBorder pfBorder11 : pfBorder1) {
                    pfBorder.add(pfBorder11);
                }
            } else if (index == 2) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), 7, null);
            }  else if (index == 4) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), 3, null);
            } else if (index == 5) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), 6, null);
                Iterator<PfBorder> chk_itw = pfBorder.iterator();
                while (chk_itw.hasNext()) {
                    PfBorder pfBorders = chk_itw.next();
                    if (pfBorders.getSendType() == 2 && pfBorders.getOrderStatus() == 6) {//排单订单
                        chk_itw.remove();
                    }
                }
            }else if (index == 3) {
                pfBorder = bOrderService.findPfpBorder(user.getId(), 8, null);
            }
            res.setPfBorders(pfBorder);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception ex) {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("异步查询出货订单");
            }
        }
        return res;
    }

    /**
     * 确认发货
     *
     * @author muchaofeng
     * @date 2016/3/20 13:40
     */
    @RequestMapping("/appDeliver.do")
    @ResponseBody
    @SignValid(paramType = BorderDeliverReq.class)
    public OrderListPagingRes appDeliver(HttpServletRequest request, BorderDeliverReq req, ComUser user) {
        OrderListPagingRes res = new OrderListPagingRes();
        try {
            String shipManName=req.getShipManName();
            Long orderId=req.getOrderId();
            String freight=req.getFreight();
            String shipManId= req.getShipManId();
            bOrderService.deliver(shipManName, orderId, freight, shipManId, user);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception ex) {
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("网络错误");
            }
        }
        return res;
    }
    /**
     * 出货订单详情
     *
     * @author muchaofeng
     * @date 2016/3/16 15:00
     */
    @RequestMapping("/deliverBorderDetail.html")
    @ResponseBody
    @SignValid(paramType = BorderDetailReq.class)
    public BorderDetailRes deliveryBorderDetils(HttpServletRequest request, BorderDetailReq req) throws Exception {
        BorderDetail borderDetail = new BorderDetail();
        BorderDetailRes res = new BorderDetailRes();
        Long id = req.getId();
        try{
            String skuValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfBorder pfBorder = bOrderService.getPfBorderById(id);
            ComUser user = userService.getUserById(pfBorder.getUserId());
            List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(id);
            for (PfBorderItem pfBorderItem : pfBorderItems) {
                ComSkuImage comSkuImage = skuService.findComSkuImage(pfBorderItem.getSkuId());
                pfBorderItem.setSkuUrl(skuValue + comSkuImage.getImgUrl());
                pfBorder.setTotalQuantity(pfBorder.getTotalQuantity() + pfBorderItem.getQuantity());//订单商品总量
            }
            ComDictionary comDictionary = comDictionaryService.findComDictionary(pfBorder.getOrderStatus());
            pfBorder.setOrderSkuStatus(comDictionary.getValue());
            //快递公司信息
            List<PfBorderFreight> pfBorderFreights = bOrderService.findByPfBorderFreightOrderId(id);
            //收货人
            PfBorderConsignee pfBorderConsignee = bOrderService.findpfBorderConsignee(id);
            //支付方式
            List<PfBorderPayment> pfBorderPayments = pfBorderPaymentMapper.selectByBorderId(id);
            borderDetail.setBuyerName(user.getWxNkName());
            borderDetail.setPfBorderPayments(pfBorderPayments);
            borderDetail.setPfBorder(pfBorder);
            borderDetail.setPfBorderItems(pfBorderItems);
            borderDetail.setPfBorderFreights(pfBorderFreights);
            borderDetail.setPfBorderConsignee(pfBorderConsignee);
            List<ComShipMan> comShipMans = comShipManService.list();

            res.setComShipMans(comShipMans);
            res.setBorderDetail(borderDetail);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception ex){
            if (StringUtils.isNotBlank(ex.getMessage())) {
                res.setResMsg(ex.getMessage());
            } else {
                res.setResMsg("网络错误");
            }
        }
        return res;
    }

    /**
     * 零售 店铺订单 列表带分页（店主发货，平台代发，状态(待发货，待付款，已发货，已完成，全部)筛选）
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/shopOrderList.do")
    @ResponseBody
    @SignValid(paramType = OrderListReq.class)
    public OrderListRes shopOrderList(HttpServletRequest request, OrderListReq req, ComUser user){
        OrderListRes res = new OrderListRes();
        int pageSize = 20;
        try{
            List<Map<String, Object>> orderList = sfOrderService.getShopOrderList(user.getId(), req.getOrderStatus(), req.getSendType(), req.getPageNum()+1, pageSize);
            res.setImgUrlPrefix(PropertiesUtils.getStringValue("index_product_220_220_url"));
            res.setOrderList(orderList);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询店铺订单列表失败！userId=" + user.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 零售 店铺订单 收货人信息
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/getConsigneeInfo.do")
    @ResponseBody
    @SignValid(paramType = OrderConsigneeReq.class)
    public OrderConsigneeRes getConsigneeInfo(HttpServletRequest request, OrderConsigneeReq req, ComUser user){
        OrderConsigneeRes res = new OrderConsigneeRes();
        try{
            SfOrderConsignee conignee = sfOrderConsigneeService.getOrdConByOrdId(req.getOrderId());
            res.setConsignee(conignee);//收货人信息
            String buyerName = sfOrderService.getBuyerNameByOrderId(req.getOrderId());
            res.setWxNkName(buyerName);//购买人 订单所属人
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询收货人信息失败！userId=" + user.getId() +",orderId=" + req.getOrderId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 获取所有快递公司
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/getAllShipMans.do")
    @ResponseBody
    @SignValid(paramType = ShipMapReq.class)
    public ShipManRes getAllShipMans(HttpServletRequest request, ShipMapReq req, ComUser user){
        ShipManRes res = new ShipManRes();
        try {
            List<ComShipMan> comShipMans = comShipManService.list();
            res.setComShipMans(comShipMans);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("获取所有快递公司失败！userId= " + user.getId());
        }
        return res;
    }

    /**
     * 零售 店铺订单 订单详情
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/shopOrderDetail.do")
    @ResponseBody
    @SignValid(paramType = OrderDetailReq.class)
    public OrderDetailRes shopOrderDetail(HttpServletRequest request, OrderDetailReq req, ComUser user){
        OrderDetailRes res = new OrderDetailRes();
        try{
            String imgUrlPrefix = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            SfOrder sfOrder = sfOrderService.getOrderById(req.getOrderId());
            ComDictionary comDictionary = comDictionaryService.findComDictionary(sfOrder.getOrderStatus());
            sfOrder.setOrderSkuStatus(comDictionary.getValue());
            SfOrderConsignee conignee = sfOrderConsigneeService.getOrdConByOrdId(req.getOrderId());
            List<SfOrderItem> orderItems = sfOrderItemService.getOrderItemByOrderId(req.getOrderId());
            List<SfOrderFreight> orderFreights = sfOrderService.getFreightByOrderId(req.getOrderId());
            List<SfOrderPayment> orderPayments = sfOrderPaymentService.selectBySfOrderId(req.getOrderId());
            List<SfOrderItemDistribution> distribution = sfOrderItemDistributionService.selectBySfOrderId(req.getOrderId());
            ComUser buyerUser = userService.getUserById(sfOrder.getUserId());
            String skuImgUrl = sfOrderService.getSkuDefaultImgUrlBySkuId(orderItems.get(0).getSkuId());

            res.setSfOrder(sfOrder);//订单
            res.setConsignee(conignee);//收货人信息
            res.setBuyerName(buyerUser.getRealName());//购买人
            res.setOrderItems(orderItems);//订单子项
            res.setOrderFreights(orderFreights);//快递公司信息
            res.setPayments(orderPayments);//支付方式
            res.setDistribution(distribution);//三级分佣
            res.setSkuImgUrl(imgUrlPrefix + skuImgUrl);//商品图片

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch(Exception e){
            log.error("查询店铺订单详情失败！userId=" + user.getId() +",orderId=" + req.getOrderId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 家族 我的订单 列表带分页 状态(待发货，排单中，已发货，待付款，已完成，线下支付未付款，全部)筛选
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/myOrderList.do")
    @ResponseBody
    @SignValid(paramType = MyOrderListReq.class)
    public MyOrderListRes myOrderList(HttpServletRequest request, MyOrderListReq req, ComUser user){
        MyOrderListRes res = new MyOrderListRes();
        int pageSize = 20;
        try{
            Map<String, Object> pfBorderMap = bOrderService.findMyOrderList(user.getId(), req.getOrderStatus(), req.getPageNum()+1, pageSize);
            res.setPfBorders((List<PfBorder>)pfBorderMap.get("pfBorderList"));
            res.setTotalPage((Integer)pfBorderMap.get("totalPage"));
            String imgUrlPrefix = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            res.setImgUrlPrefix(imgUrlPrefix);

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询家族我的订单列表失败！userId=" + user.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

   /*
     * 家族 我的订单 线下支付 支付信息（弃用）
     * @param request
     * @param req
     * @param user
     * @return
     *//*
    @RequestMapping(value = "getOffinePaymentDeatil.do")
    @ResponseBody
    @SignValid(paramType = OffinePaymentReq.class)
    public OffinePaymentRes getOffinePaymentDeatil(HttpServletRequest request, OffinePaymentReq req, ComUser user) {
        OffinePaymentRes res = new OffinePaymentRes();
        try{
            Map<String, Object> map = bOrderService.getOffinePaymentDeatil(req.getOrderId());
            if (map != null) {
                res.setSupplierBank((PfSupplierBank)map.get("supplierBank"));
                res.setLatestTime((String)map.get("latestTime"));
                res.setOrderItem((PfBorderItem)map.get("orderItem"));
                res.setPfBorder((PfBorder)map.get("border"));

                res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
                res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
            }
        }catch (Exception e){
            log.error("查询我的订单支付信息失败！userId=" + user.getId() + "，orderId=" + req.getOrderId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }*/

    /**
     * 家族 我的订单 继续支付、更改支付方式 (去收银台)
     */
    @RequestMapping(value = "/toPfBorderCashierdesk.do")
    @ResponseBody
    @SignValid(paramType = PfBorderCashierdeskReq.class)
    public PfBorderCashierdeskRes toPfBorderCashierdesk(HttpServletRequest request, PfBorderCashierdeskReq req, ComUser user){
        PfBorderCashierdeskRes res = new PfBorderCashierdeskRes();
        try{
            PfBorder pfBorder = bOrderService.getPfBorderById(req.getOrderId());
            List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemDetail(req.getOrderId());
            Map<String, Object> agentInfo = bOrderService.getAgentInfo(user.getId(), pfBorder.getId());
            res.setPfBorder(pfBorder);
            res.setPfBorderItems(pfBorderItems);
            res.setAgentInfo(agentInfo);//代理信息

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询我的订单去收银台信息失败！userId=" + user.getId() + "，orderId=" + req.getOrderId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 家族 我的订单 订单详情
     */
    @RequestMapping("/pfBorderDetail.do")
    @ResponseBody
    @SignValid(paramType = BorderDetailReq.class)
    public BorderDetailRes pfBorderDetail(HttpServletRequest request, BorderDetailReq req, ComUser user) {
        BorderDetailRes res = new BorderDetailRes();
        Long id = req.getId();
        BorderDetail borderDetail = new BorderDetail();
        try{
            String imgUrlPrefix = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfBorder pfBorder = bOrderService.getPfBorderById(id);
            pfBorder.setOrderStatusDes(BOrderStatus.getByCode(pfBorder.getOrderStatus()).getDesc());
            List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(id);
            //支付方式
            String paymentDesc = bOrderService.getPfBorderPaymentsByOrderId(id);
            //推荐人信息
            Map<String, Object> rewordInfo = bOrderService.getRewordInfoByOrderId(id);
            //商品图片
            String skuImgUrl = bOrderService.getSkuDefaultImgUrlBySkuId(pfBorderItems.get(0).getSkuId());
            //收货人信息
            PfBorderConsignee consignee = pfBorderConsigneeService.getByBOrderId(id);

            borderDetail.setPfBorder(pfBorder);
            borderDetail.setPfBorderItems(pfBorderItems);
            borderDetail.setPfBorderConsignee(consignee);

            res.setBorderDetail(borderDetail);
            res.setImgUrlPrefix(imgUrlPrefix);
            res.setPaymentDesc(paymentDesc);
            res.setRewordInfo(rewordInfo);
            res.setSkuImgUrl(skuImgUrl);

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询我的订单 订单详情 失败！userId=" + user.getId() + "，orderId=" + req.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 获取下级合伙人（id name） 列表
     */
    @RequestMapping("/getLowerLevelPartnerListByUserPid.do")
    @ResponseBody
    @SignValid(paramType = LowerLevelPartnerReq.class)
    public LowerLevelPartnerRes getLowerLevelPartnerListByUserPid(HttpServletRequest request, LowerLevelPartnerReq req, ComUser user){
        LowerLevelPartnerRes res = new LowerLevelPartnerRes();
        try{
            List<Map<String, Object>> lowerLevelPartnerList = userSkuService.getLowerLevelPartnerListByUserPid(user.getId());
            res.setLowerLevelPartnerList(lowerLevelPartnerList);

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询下级合伙人失败！userId=" + user.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }


    /**
     * 家族 下级订单列表 带分页
     */
    @RequestMapping("/lowerLevelOrderList.do")
    @ResponseBody
    @SignValid(paramType = LowerLevelOrderReq.class)
    public LowerLevelOrderRes lowerLevelOrderList(HttpServletRequest request, LowerLevelOrderReq req, ComUser user){
        LowerLevelOrderRes res = new LowerLevelOrderRes();
        int pageSize = 20;
        try{
            Map<String, Object> pfBorderMap = bOrderService.getLowerLevelOrderList(user.getId(), req.getLowerId(), req.getOrderStatus(), req.getPageNum()+1, pageSize);
            res.setPfBorders((List<PfBorder>)pfBorderMap.get("pfBorderList"));
            res.setTotalPage((Integer)pfBorderMap.get("totalPage"));
            String imgUrlPrefix = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            res.setImgUrlPrefix(imgUrlPrefix);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch(Exception e){
            log.error("查询下级订单列表失败！userId=" + user.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 家族 下级订单 订单详情
     */
    @RequestMapping("/lowerLevelBorderDetail.do")
    @ResponseBody
    @SignValid(paramType = BorderDetailReq.class)
    public BorderDetailRes lowerLevelBorderDetail(HttpServletRequest request, BorderDetailReq req, ComUser user) {
        BorderDetailRes res = new BorderDetailRes();
        Long id = req.getId();
        BorderDetail borderDetail = new BorderDetail();
        try{
            String imgUrlPrefix = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfBorder pfBorder = bOrderService.getPfBorderById(id);
            List<PfBorderItem> pfBorderItems = bOrderService.getPfBorderItemByOrderId(id);
            //支付方式
            String paymentDesc = bOrderService.getPfBorderPaymentsByOrderId(id);
            //推荐人信息
            Map<String, Object> rewordInfo = bOrderService.getRewordInfoByOrderId(id);
            //商品默认图片
            ComSkuImage defaultImg = skuImageService.defaultImg(pfBorderItems.get(0).getSkuId());

            borderDetail.setPfBorder(pfBorder);
            borderDetail.setPfBorderItems(pfBorderItems);
            ComUser buyerUser = userService.getUserById(pfBorder.getUserId());
            String buyerName = buyerUser.getRealName();
            if(buyerName==null || buyerName.length()==0){
                buyerName = buyerUser.getWxNkName();
            }
            borderDetail.setBuyerName(buyerName);//购买人

            res.setBorderDetail(borderDetail);
            res.setImgUrlPrefix(imgUrlPrefix);
            res.setPaymentDesc(paymentDesc);
            res.setRewordInfo(rewordInfo);
            res.setDefaultImg(defaultImg);

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        }catch (Exception e){
            log.error("查询下级订单 订单详情 失败！userId=" + user.getId() + "，orderId=" + req.getId());
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 线下支付 修改状态
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/offinePayment.do")
    @ResponseBody
    @SignValid(paramType = BorderOffineReq.class)
    public BorderOffineRes offinePayment(HttpServletRequest request, BorderOffineReq req, ComUser user) {
        Boolean bl = false;
        BorderOffineRes res = new BorderOffineRes();
        try {
            if (req==null||req.getbOrderId()==null){
                res.setResCode(SysResCodeCons.RES_CODE_REQ_STRUCT_INVALID);
                res.setResMsg(SysResCodeCons.RES_CODE_REQ_STRUCT_INVALID_MSG);
                return res;
            }else{
                bl = payBOrderService.offinePayment(user, req.getbOrderId());
            }
            res.setIsOffinSuccess(bl);
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
         }
        return res;
    }


    /**
     *  获取线下支付详情
     * @param request
     * @param req
     * @param user
     * @return
     */
    @RequestMapping("/getOffinePaymentDeatil.do")
    @ResponseBody
    @SignValid(paramType = BorderOffineReq.class)
    public BorderOffineRes getOffinePaymentDeatil(HttpServletRequest request, BorderOffineReq req, ComUser user) {
        BorderOffineRes res = new BorderOffineRes();
        try {
            if (req==null||req.getbOrderId()==null){
                res.setResCode(SysResCodeCons.RES_CODE_REQ_STRUCT_INVALID);
                res.setResMsg(SysResCodeCons.RES_CODE_REQ_STRUCT_INVALID_MSG);
                return res;
            }
            Map<String, Object> map = payBOrderService.getOffinePaymentDeatil(req.getbOrderId());
            if (map != null) {
                PfBorder pfBorder = (PfBorder)map.get("border");
                if (pfBorder!=null){
                    res.setOrderCode(pfBorder.getOrderCode());
                }
                PfBorderItem pfBorderItem = (PfBorderItem)map.get("orderItem");
                if (pfBorderItem!=null){
                    res.setSkuName(pfBorderItem.getSkuName());
                    res.setTotalPrice(pfBorderItem.getTotalPrice());
                }
                res.setLatestTime((String) map.get("latestTime"));
                res.setPfSupplierBank((PfSupplierBank) map.get("supplierBank"));
                res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
                res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
            }
        } catch (Exception e) {
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }
}
