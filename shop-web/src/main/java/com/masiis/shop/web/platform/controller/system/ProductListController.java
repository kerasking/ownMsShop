package com.masiis.shop.web.platform.controller.system;

import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.system.IndexComSku;
import com.masiis.shop.dao.platform.product.ComSpuMapper;
import com.masiis.shop.dao.po.ComSku;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfUserRelation;
import com.masiis.shop.dao.po.PfUserSku;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.order.BOrderService;
import com.masiis.shop.web.platform.service.product.ProductService;
import com.masiis.shop.web.platform.service.product.SkuAgentService;
import com.masiis.shop.web.platform.service.system.IndexShowService;
import com.masiis.shop.web.platform.service.user.PfUserRelationService;
import com.masiis.shop.web.platform.service.user.PfUserSkuService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by muchaofeng on 2016/3/4.
 */
@Controller
@RequestMapping("/productList")
public class ProductListController extends BaseController {

    @Resource
    private SkuAgentService skuAgentService;
    @Resource
    private IndexShowService indexShowService;

    @Resource
    private ProductService productService;
    @Resource
    private BOrderService bOrderService;
    @Resource
    private PfUserRelationService pfUserRelationService;
    @Resource
    private PfUserSkuService pfUserSkuService;

    @RequestMapping("showProduct")
    public ModelAndView showProductList(HttpServletRequest request) throws Exception{
        HttpSession session = request.getSession();
        //获取用户信息
        ComUser user = getComUser(request);
        if (user == null) {
            user = (ComUser)session.getAttribute("comUser");
        }
        //获取图片地址常量
        String value = PropertiesUtils.getStringValue("index_product_800_800_url");
//        List<IndexComSku> indexComSk = indexShowService.findIndexComSku(user.getId());
        //增加逻辑，判断是否是BOSS
        List<IndexComSku> indexComSk = null;
        List<PfUserRelation> pfUserRelations = pfUserRelationService.getRelationByUserId(user.getId());//临时代理关系
        List<PfUserSku> pfUserSkuList = pfUserSkuService.getPfUserSkuInfoByUserIdNotPid(user.getId());
        if((pfUserRelations==null || pfUserRelations.size()<=0) && pfUserSkuList.size()<=0){//小白
            indexComSk = indexShowService.findIndexComSku(user.getId());
        }else if (pfUserSkuList!=null && pfUserSkuList.size()>0){//BOSS
            indexComSk = indexShowService.findIndexComSku(user.getId());
        }else {
            indexComSk = indexShowService.findIndexComSkuNotBoss(user.getId());
        }
        indexComSk.addAll(indexShowService.findTestListComSku(user.getId()));
        List<IndexComSku> Com =new ArrayList<IndexComSku>();
        for (IndexComSku indexComSku:indexComSk) {
            //获取商品图片地址
            String url = value + indexComSku.getImgUrl();
            //重新封装商品图片地址
            indexComSku.setImgUrl(url);
            PfUserSku pfUserSku = bOrderService.findPfUserSku(user.getId(),indexComSku.getId());
            if (pfUserSku !=null){
                indexComSku.setIsPay(pfUserSku.getIsPay());
            }
//            if (user != null && user.getIsAgent() == 1) {
                //确定代理权限
//                indexComSku.setIsPartner(1);
                //显示优惠区间
                indexComSku.setMaxDiscount(productService.getMaxDiscount(indexComSku.getId()));
                indexComSku.setDiscountLevel("最高利润"+productService.getMaxDiscount(indexComSku.getId())+"%");
                indexComSku.setBailLevel(skuAgentService.getSkuAgentLevel(indexComSku.getId()));

//            } else {
//                indexComSku.setDiscountLevel("成为合伙人可查看");
//            }
            Com.add(indexComSku);
        }
        ModelAndView modelAndView = new ModelAndView();
        //封装展示商品信息集合
        modelAndView.addObject("indexComSkus",Com);
        modelAndView.addObject("ComSize",Com.size());
        modelAndView.setViewName("platform/system/liebiao");
        return modelAndView;
    }
}
