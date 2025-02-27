package com.masiis.shop.api.controller.product;

import com.masiis.shop.api.bean.base.BaseBusinessRes;
import com.masiis.shop.api.bean.base.BasePagingReq;
import com.masiis.shop.api.bean.common.CommonReq;
import com.masiis.shop.api.bean.product.*;
import com.masiis.shop.api.constants.SignValid;
import com.masiis.shop.api.constants.SysConstants;
import com.masiis.shop.api.constants.SysResCodeCons;
import com.masiis.shop.api.controller.base.BaseController;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.beans.product.AgentSku;
import com.masiis.shop.dao.mallBeans.SkuInfo;
import com.masiis.shop.web.platform.service.order.BOrderAddService;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.product.ManageShopProductService;
import com.masiis.shop.web.platform.service.product.ProductService;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.common.service.UserAddressService;
import com.masiis.shop.web.platform.service.user.UserCertificateService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import com.masiis.shop.common.enums.platform.BOrderType;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.order.BOrderAdd;
import com.masiis.shop.dao.beans.order.BOrderConfirm;
import com.masiis.shop.dao.beans.product.Product;
import com.masiis.shop.dao.po.*;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by JingHao on 2016/5/19 0019.
 */


@Controller
@RequestMapping("/managePro")
public class ManageProController extends BaseController {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private ProductService productService;
    @Resource
    private UserAddressService userAddressService;
    @Resource
    private SkuService skuService;
    @Resource
    private UserSkuService userSkuService;
    @Resource
    private BOrderAddService bOrderAddService;
    @Resource
    private BOrderService bOrderService;
    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private UserCertificateService userCertificateService;
    @Resource
    private ManageShopProductService manageShopProductService;


    /**
     * @Author jjh
     * @Date 2016/5/19 0019 下午 5:49
     * 管理商品列表
     */
    @RequestMapping("/user")
    @ResponseBody
    @SignValid(paramType = BasePagingReq.class)
    public ProDetailRes proListForUser(HttpServletRequest request, BasePagingReq req, ComUser user) {
        log.info("库存管理---代理中商品展示");
        ProDetailRes proDetailRes = new ProDetailRes();
        try {
            List<Product> userProducts = productService.productListByUserPage(user.getId(), req.getPageNum());
            if (userProducts != null) {
                for (Product product : userProducts) {
                    product.setUpperStock(productService.getUpperStock(user.getId(), product.getId()));
                    PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), product.getId());
                    product.setUnitPrice(skuAgentService.getBySkuIdAndLevelId(product.getId(), pfUserSku.getAgentLevelId()).getUnitPrice());
                }
            }
            proDetailRes.setProductList(userProducts);
            proDetailRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            proDetailRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            proDetailRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            proDetailRes.setResMsg(SysResCodeCons.RES_CODE_NOT_KNOWN_MSG);
        }
        return proDetailRes;
    }

    /**
     * jjh
     * 仓库中
     */
    @RequestMapping("/notAgent")
    @ResponseBody
    @SignValid(paramType = BasePagingReq.class)
    public ProDetailNotAgentRes proListForUserNotAgent(HttpServletRequest request, BasePagingReq req, ComUser user) {
        ProDetailNotAgentRes proDetailRes = new ProDetailNotAgentRes();
        try {
            List<AgentSku> userProducts = productService.productListByUserNotAgent(user.getId(), req.getPageNum());
            proDetailRes.setAgentSkuList(userProducts);
            proDetailRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            proDetailRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            e.printStackTrace();
            proDetailRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            proDetailRes.setResMsg(SysResCodeCons.RES_CODE_NOT_KNOWN_MSG);
        }
        return proDetailRes;
    }

    /**
     * @Author jjh
     * @Date 2016/5/19 0019 下午 5:49
     * 申请拿货信息展示
     */
    @RequestMapping("/applyInfo")
    @ResponseBody
    @SignValid(paramType = ApplyProReq.class)
    public ApplyProRes applyProInfo(HttpServletRequest request, ApplyProReq req,
                                    ComUser user) {
        ApplyProRes applyProRes = new ApplyProRes();
        try {
            ComUserAddress comUserAddress = userAddressService.getOrderAddress(req.getSelectedAddressId(), user.getId());
            if (comUserAddress != null) {
                applyProRes.setComUserAddress(comUserAddress);
            }
            PfUserSkuStock pfUserSkuStock = productService.getStockByUser(req.getId());
            int userStock = pfUserSkuStock.getStock() - pfUserSkuStock.getFrozenStock();
            if (userStock < 0) {
                pfUserSkuStock.setStock(0);
            } else {
                pfUserSkuStock.setStock(userStock);//冻结库存
            }
            ComSku comSku = skuService.getSkuById(pfUserSkuStock.getSkuId());
            ComSkuImage comSkuImage = skuService.findComSkuImage(comSku.getId());
            String productImgValue = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), pfUserSkuStock.getSkuId());
            Map<String, Object> objectMap = productService.getLowerCount(pfUserSkuStock.getSkuId(), userStock, pfUserSku.getAgentLevelId());
            applyProRes.setComSku(comSku);
            applyProRes.setComSkuImg(productImgValue + comSkuImage.getImgUrl());
            applyProRes.setPfUserSkuStock(pfUserSkuStock);
            applyProRes.setLowerCount(objectMap.get("countLevel").toString());
            applyProRes.setPriceDiscount(objectMap.get("priceDiscount").toString());
            applyProRes.setLevelStock(objectMap.get("levelStock").toString());
            applyProRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            applyProRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            applyProRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            applyProRes.setResMsg(e.getMessage());

        }
        return applyProRes;
    }


    /**
     * @Author jjh
     * @Date 2016/5/23 0023 下午 4:03
     * 申请拿货生成订单
     */
    @RequestMapping("/applyStock.do")
    @ResponseBody
    @SignValid(paramType = ApplyProReq.class)
    public ApplyProRes applySkuStock(HttpServletRequest request, ApplyProReq req,
                                     ComUser user) {
        ApplyProRes applyProRes = new ApplyProRes();
        try {
            PfUserSkuStock product = productService.getStockByUser(req.getId());
            Long orderCode = bOrderAddService.addProductTake(user.getId(), product.getSkuId(), req.getStock(), req.getMessage(), req.getSelectedAddressId());
            PfBorder pfBorder = bOrderService.getPfBorderById(orderCode);
            PfBorderConsignee pfBorderConsignee = null;
            if (pfBorder != null && pfBorder.getOrderType() == 2) {
                pfBorderConsignee = bOrderService.findpfBorderConsignee(pfBorder.getId());
            }
            applyProRes.setOrderCode(orderCode);
            applyProRes.setPfBorderConsignee(pfBorderConsignee);
            applyProRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            applyProRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            applyProRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            applyProRes.setResMsg(e.getMessage());
        }
        return applyProRes;
    }

    /**
     * @Author jjh
     * @Date 2016/5/24 0024 下午 3:15
     * 补货生成订单前预览
     */
    @RequestMapping("/addPro.do")
    @ResponseBody
    @SignValid(paramType = AddProReq.class)
    public AddProRes supplementBOrder(HttpServletRequest request, AddProReq req,
                                      ComUser user) {
        AddProRes addProRes = new AddProRes();
        try {
            Integer sendType = user.getSendType();
            Integer agentLevelId = 0;
            PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), req.getUserSkuId());
            agentLevelId = pfUserSku.getAgentLevelId();
//            PfUserCertificate pfUserCertificate = userCertificateService.getCertificateBypfuId(pfUserSku.getId());
            BOrderConfirm bOrderConfirm = new BOrderConfirm();
            bOrderConfirm.setOrderType(BOrderType.agent.getCode());
            //拿货方式
            bOrderConfirm.setSendType(sendType);
//            //获得地址
//            ComUserAddress comUserAddress = userAddressService.getOrderAddress(req.getUserAddressId(), user.getId());
//            bOrderConfirm.setComUserAddress(comUserAddress);
            //获取sku
            ComSku comSku = skuService.getSkuById(req.getUserSkuId());
            bOrderConfirm.setSkuId(req.getUserSkuId());
            bOrderConfirm.setSkuName(comSku.getName());
            //获取sku主图片
            ComSkuImage comSkuImage = skuService.findComSkuImage(req.getUserSkuId());
            String skuImg = PropertiesUtils.getStringValue(SysConstants.INDEX_PRODUCT_IMAGE_MIN);
            bOrderConfirm.setSkuImg(skuImg + comSkuImage.getImgUrl());
            //货币类型格式化
//        NumberFormat rmb = NumberFormat.getCurrencyInstance(Locale.CHINA);
            //获取用户代理等级
            bOrderConfirm.setAgentLevelId(agentLevelId);
            PfSkuAgent pfSkuAgent = skuAgentService.getBySkuIdAndLevelId(req.getUserSkuId(), agentLevelId);
            bOrderConfirm.setSkuQuantity(req.getQuantity());
            BigDecimal unitPrice = pfSkuAgent.getUnitPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(bOrderConfirm.getSkuQuantity()));
            bOrderConfirm.setProductTotalPrice(totalPrice.toString());
            BigDecimal highProfit = (comSku.getPriceRetail().subtract(unitPrice))
                    .multiply(BigDecimal.valueOf(bOrderConfirm.getSkuQuantity()))
                    .setScale(2, BigDecimal.ROUND_DOWN);//最高利润
            bOrderConfirm.setHighProfit(highProfit.toString());
            Integer lowerAgentLevelId = agentLevelId + 1;
            //获取下级代理信息
            PfSkuAgent lowerSkuAgent = skuAgentService.getBySkuIdAndLevelId(req.getUserSkuId(), lowerAgentLevelId);
            BigDecimal lowProfit = BigDecimal.ZERO;
            if (lowerSkuAgent == null) {
                lowProfit = highProfit;
            } else {
                lowProfit = (lowerSkuAgent.getUnitPrice().subtract(pfSkuAgent.getUnitPrice()))
                        .multiply(BigDecimal.valueOf(bOrderConfirm.getSkuQuantity()))
                        .setScale(2, BigDecimal.ROUND_DOWN);//最低利润
            }
            bOrderConfirm.setLowProfit(lowProfit.toString());
            bOrderConfirm.setOrderTotalPrice(totalPrice.toString());
            //获取排单信息
            boolean isQueuing = false;
            Integer count = 0;
            int status = skuService.getSkuStockStatus(req.getUserSkuId(), req.getQuantity(), pfUserSku.getUserPid());
            if (status == 1) {
                isQueuing = true;
                count = bOrderService.selectQueuingOrderCount(req.getUserSkuId());
            }
            addProRes.setIsQueuing(isQueuing);
            addProRes.setCount(count);
            addProRes.setbOrderConfirm(bOrderConfirm);
            addProRes.setUnitPrice(pfSkuAgent.getUnitPrice());
            addProRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            addProRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            addProRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            addProRes.setResMsg(e.getMessage());
        }
        return addProRes;
    }


    /**
     * @Author jjh
     * @Date 2016/5/25 0025 上午 10:06
     * 校验库存
     */
    @RequestMapping("/checkStock.do")
    @ResponseBody
    @SignValid(paramType = AddProReq.class)
    public StockStatusRes checkStock(HttpServletRequest request, AddProReq req,
                                     ComUser user) {
        StockStatusRes stockStatusRes = new StockStatusRes();
        try {
            PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), req.getUserSkuId());
            int status = skuService.getSkuStockStatus(req.getUserSkuId(), req.getQuantity(), pfUserSku.getUserPid());
            stockStatusRes.setStatus(status);
            stockStatusRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            stockStatusRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            stockStatusRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            stockStatusRes.setResMsg(e.getMessage());
        }
        return stockStatusRes;
    }

    /**
     * @Author jjh
     * @Date 2016/5/25 0025 上午 10:57
     * 1 提交补货订单
     * 2 跳转到收银台
     */
    @RequestMapping("/supplementBOrder/add.do")
    @ResponseBody
    @SignValid(paramType = AddProReq.class)
    public AddProRes supplementBOrderAdd(HttpServletRequest request, AddProReq req,
                                         ComUser user) {
        AddProRes addProRes = new AddProRes();
        try {
            Integer sendType = 0;
            if (user.getSendType() <= 0) {
                addProRes.setResCode(SysResCodeCons.RES_CODE_NO_SEND_TYPE);
                addProRes.setResMsg(SysResCodeCons.RES_CODE_NO_SEND_TYPE_MSG);
                return addProRes;
            }
            sendType = user.getSendType();
            if (req.getUserSkuId() == null || req.getUserSkuId() <= 0) {
                addProRes.setResCode(SysResCodeCons.RES_CODE_UPAPPLY_SKU_INVALID);
                addProRes.setResMsg(SysResCodeCons.RES_CODE_UPAPPLY_SKU_INVALID_MSG);
                return addProRes;
            }
            if (StringUtils.isBlank(req.getUserMessage())) {
                req.setUserMessage("");
            }
            PfUserSku pfUserSku = userSkuService.getUserSkuByUserIdAndSkuId(user.getId(), req.getUserSkuId());
            BOrderAdd bOrderAdd = new BOrderAdd();
            bOrderAdd.setOrderType(BOrderType.Supplement.getCode());
            bOrderAdd.setpUserId(pfUserSku.getUserPid());
            bOrderAdd.setUserId(user.getId());
            bOrderAdd.setSendType(sendType);
            bOrderAdd.setSkuId(req.getUserSkuId());
            bOrderAdd.setAgentLevelId(pfUserSku.getAgentLevelId());
            bOrderAdd.setWeiXinId(user.getWxId());
            bOrderAdd.setUserMessage(req.getUserMessage());
            bOrderAdd.setQuantity(req.getQuantity());
            bOrderAdd.setUserSource(0);
            Long bOrderId = bOrderAddService.addBOrder(bOrderAdd);
//            PfBorder pfBorder = bOrderService.getPfBorderById(bOrderId);
            addProRes.setPfBorderId(bOrderId);
            addProRes.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            addProRes.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception ex) {
            addProRes.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            addProRes.setResMsg(ex.getMessage());
        }
        return addProRes;
    }

    /**
     * 零售 商品管理 列表（代理中（自己发货 平台发货）筛选，仓库中）
     *
     * @param request
     * @param req
     * @param comUser
     * @return
     */
    @RequestMapping("/retailProductList.do")
    @ResponseBody
    @SignValid(paramType = RetailProReq.class)
    public RetailProRes retailProductList(HttpServletRequest request, RetailProReq req, ComUser comUser) {
        RetailProRes res = new RetailProRes();
        int pageSize = 20;
        try {
            List<SkuInfo> skuInfoList = manageShopProductService.getShopProductsList(req.getShopId(), req.getIsSale(), comUser.getId(), 0, req.getPageNum() + 1, pageSize);
            res.setSkuInfoList(skuInfoList);

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("加载零售商品管理列表失败![sfShopId=" + req.getShopId() + "]");
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 零售 商品管理 上下架
     *
     * @param request
     * @param req
     * @param comUser
     * @return
     */
    @RequestMapping("/updateSale.do")
    @ResponseBody
    @SignValid(paramType = RetailProReq.class)
    public RetailProRes updateSale(HttpServletRequest request, RetailProReq req, ComUser comUser) {
        RetailProRes res = new RetailProRes();
        try {
            log.info("零售商品上下架![ShopSkuId=" + req.getShopSkuId() + "]");
            manageShopProductService.updateSale(req.getShopSkuId(), req.getIsSale());

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("零售商品上下架失败![ShopSkuId=" + req.getShopSkuId() + "userId=" + comUser.getId() + "]");
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    /**
     * 零售 商品管理 库存设置
     *
     * @param request
     * @param req
     * @param comUser
     * @return
     */
    @RequestMapping("/updateProductStock.do")
    @ResponseBody
    @SignValid(paramType = RetailProReq.class)
    public RetailProRes updateProductStock(HttpServletRequest request, RetailProReq req, ComUser comUser) {
        RetailProRes res = new RetailProRes();
        try {
            if (req.getStock() < 0) {
                throw new BusinessException("库存设置数量不能小于0啊");
            }
            log.info("零售商品管理库存![skuId=" + req.getSkuId() + "userId=" + comUser.getId() + "stock=" + req.getStock() + "]");
            productService.updateStock(req.getStock(), req.getSkuId(), comUser.getId());

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("零售商品管理库存失败![skuId=" + req.getSkuId() + "userId=" + comUser.getId() + "stock=" + req.getStock() + "]");
        }
        return res;
    }

    /**
     * 零售 商品管理 我要自己发货
     *
     * @param request
     * @param req
     * @param comUser
     * @return
     */
    @RequestMapping("/addSelfDelivery.do")
    @ResponseBody
    @SignValid(paramType = RetailProReq.class)
    public RetailProRes addSelfDelivery(HttpServletRequest request, RetailProReq req, ComUser comUser) {
        RetailProRes res = new RetailProRes();
        try {
            log.info("生成我要自己发货![ShopSkuId=" + req.getShopSkuId() + "userId=" + comUser.getId() + "]");
            manageShopProductService.addSelfDeliverySku(req.getShopSkuId());

            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error("生成我要自己发货失败![ShopSkuId=" + req.getShopSkuId() + "userId=" + comUser.getId() + "]");
            res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
            res.setResMsg(e.getMessage());
        }
        return res;
    }

    @RequestMapping("/supplementsuccess")
    @ResponseBody
    @SignValid(paramType = SupplementSuccessReq.class)
    public SupplementSuccessRes supplementSuccess(HttpServletRequest request, SupplementSuccessReq req, ComUser user) {
        SupplementSuccessRes res = new SupplementSuccessRes();
        try {
            if (req.getOrderId() == null || req.getOrderId().longValue() <= 0l) {
                res.setResCode(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR);
                res.setResMsg(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR_MSG);
                throw new BusinessException(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR_MSG);
            }
            PfBorder order = bOrderService.getPfBorderById(req.getOrderId());
            if (order == null) {
                res.setResCode(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR);
                res.setResMsg(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR_MSG);
                throw new BusinessException(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERID_ERROR_MSG);
            }
            if (order.getOrderType().intValue() != BOrderType.Supplement.getCode().intValue()) {
                res.setResCode(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERTYPE_ERROR);
                res.setResMsg(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERTYPE_ERROR_MSG);
                throw new BusinessException(SysResCodeCons.RES_CODE_SUPPLEMENT_ORDERTYPE_ERROR_MSG);
            }
            PfBorderItem item = bOrderService.getPfBorderItemByOrderId(order.getId()).get(0);

            res.setIncreaseStock(item.getQuantity());
            res.setPayAmount(order.getPayAmount().add(order.getReceivableAmount()));
            res.setResCode(SysResCodeCons.RES_CODE_SUCCESS);
            res.setResMsg(SysResCodeCons.RES_CODE_SUCCESS_MSG);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if (StringUtils.isBlank(res.getResCode())) {
                res.setResCode(SysResCodeCons.RES_CODE_NOT_KNOWN);
                res.setResMsg(SysResCodeCons.RES_CODE_NOT_KNOWN_MSG);
            }
        }
        return res;
    }
}
