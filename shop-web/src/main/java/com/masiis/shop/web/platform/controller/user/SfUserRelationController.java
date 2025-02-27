package com.masiis.shop.web.platform.controller.user;

import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.beans.user.BfSpokesManDetailPo;
import com.masiis.shop.dao.beans.user.SfSpokesAndFansInfo;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.SfShop;
import com.masiis.shop.web.mall.service.shop.SfShopService;
import com.masiis.shop.web.mall.service.user.SfUserRelationService;
import com.masiis.shop.web.platform.controller.base.BaseController;
import org.apache.log4j.Logger;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * b端小铺代言人Controller
 * Created by wangbingjian on 2016/7/7.
 */
@Controller
@RequestMapping(value = "/distribution")
public class SfUserRelationController extends BaseController{
    private static final Logger logger = Logger.getLogger(SfUserRelationController.class);
    /**
     * 每页展示条数
     */
    private static final Integer pageSize = 10;

    @Autowired
    private SfShopService sfShopService;
    @Autowired
    private SfUserRelationService sfUserRelationService;

    /**
     * B端小铺代言人查询首页
     * @param request request
     * @return mv
     * @throws Exception
     */
    @RequestMapping(value = "/spokesManHome.shtml")
    public ModelAndView spokesManHome(HttpServletRequest request) throws Exception{
        logger.info("B端小铺代言人首页");
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户未登陆");
        }
        Long userId = comUser.getId();
        logger.info("userId = " + userId);
        ModelAndView mv = new ModelAndView();
        SfShop sfShop = sfShopService.getSfShopByUserId(userId);
        if (sfShop == null){
            logger.info("该用户："+userId+"，没有对应的小铺信息");
            mv.addObject("totalPage", 0);
            mv.addObject("currentPage", 0);
            mv.addObject("totalCount", 0);
        }else {
            Long shopId = sfShop.getId();
            logger.info("shopId = " + shopId);
//            List<SfSpokesAndFansInfo> infos = sfUserRelationService.getAllSfSpokesManInfos(true, 1, pageSize, shopId);
//            Integer totalCount = sfUserRelationService.getFansOrSpokesMansNum(shopId, false);
            //获取代言人数量
            Integer spokesManNum = sfUserRelationService.getFansOrSpokesMansNum(shopId, true, userId);
            //获取粉丝数量
            Integer fansNum = sfUserRelationService.getFansOrSpokesMansNum(shopId, false, userId);
            if (fansNum == 0 /*|| infos == null || infos.size() == 0*/){
                logger.info("没有对应的代言人数据");
                mv.addObject("totalPage", 0);
                mv.addObject("currentPage", 0);
                mv.addObject("totalCount", 0);
            }else {
                Integer pageNums = fansNum%pageSize == 0 ? fansNum/pageSize : fansNum/pageSize + 1;
//                mv.addObject("infos",infos);
                mv.addObject("totalPage", pageNums);
                mv.addObject("currentPage", 1);
                mv.addObject("fansNum", fansNum);
                mv.addObject("spokesManNum", spokesManNum);
            }
        }
        mv.setViewName("platform/user/spokesManList");
        return mv;
    }

    /**
     * 通过代言人ID查询代言人信息
     * @param request   request
     * @param currentPage 查询第几页
     * @param queryType 查询方式   0：第一页 1：下一页 2：上一页
     * @param pageNums  总页数
     * @param ID    ID
     * @return
     */
    @RequestMapping(value = "/findByID.do")
    @ResponseBody
    public String findSpokesManByID(HttpServletRequest request,
                                    @RequestParam(value = "currentPage",required = true) Integer currentPage,
                                    @RequestParam(value = "queryType",required = true) Integer queryType,
                                    @RequestParam(value = "pageNums",required = true) Integer pageNums,
                                    @RequestParam(value = "ID") String ID)throws Exception{
        logger.info("B端小铺代言人首页");
        ComUser comUser = getComUser(request);
        JSONObject jsonObject = new JSONObject();
        if (comUser == null){
            jsonObject.put("isTrue",false);
            jsonObject.put("msg","用户未登陆");
            logger.info("result:"+jsonObject.toString());
            return jsonObject.toString();
        }
        Long userId = comUser.getId();
        logger.info("userId = " + userId);
        SfShop sfShop = sfShopService.getSfShopByUserId(userId);
        List<SfSpokesAndFansInfo> infos = null;
        if (sfShop == null){
            jsonObject.put("isTrue",false);
            jsonObject.put("msg","没有对应的小铺信息");
            logger.info("result:"+jsonObject.toString());
            return jsonObject.toString();
        }
        Long shopId = sfShop.getId();
        logger.info("shopId = " + shopId);
        if (StringUtils.isEmpty(ID) || ID.equals("NaN")){ID = null;}
        logger.info("ID = " + ID);
        logger.info("currentPage = " + currentPage);
        logger.info("pageNums = " + pageNums);
        switch (queryType.intValue()){
            //查询第一页
            case 0 : {
                logger.info("查询第一页信息");
                logger.info("ID = " + ID);
                Integer totalCount = sfUserRelationService.getSpokesManNumByID(shopId, ID, false, userId);
                logger.info("totalCount = " + totalCount);
                if (totalCount == 0){
                    logger.info("没有对应的代言人信息");
                    jsonObject.put("isTrue",false);
                    jsonObject.put("msg","没有对应的代言人信息");
                    logger.info("result:"+jsonObject.toString());
                    return jsonObject.toString();
                }
                pageNums = totalCount%pageSize == 0 ? totalCount/pageSize : totalCount/pageSize + 1;
                infos = sfUserRelationService.getShopSpokesManByID(true, 1, pageSize, shopId, ID, userId);
                jsonObject.put("totalPage", pageNums);
                jsonObject.put("currentPage", 1);
                jsonObject.put("totalCount", totalCount);
                break;
            }
            //查询下一页
            case 1 : {
                logger.info("查询下一页信息");
                if (currentPage + 1 > pageNums){
                    jsonObject.put("isTrue",false);
                    jsonObject.put("msg","已经是最后一页");
                    logger.info("result:"+jsonObject.toString());
                    return jsonObject.toString();
                }
                infos = sfUserRelationService.getShopSpokesManByID(true, currentPage + 1, pageSize, shopId, ID, userId);
                jsonObject.put("totalPage", pageNums);
                jsonObject.put("currentPage", currentPage + 1);
                break;
            }
            //查询上一页
            case 2 : {
                logger.info("查询上一页信息");
                if (currentPage - 1 < 1){
                    jsonObject.put("isTrue",false);
                    jsonObject.put("msg","已经是第一页");
                    logger.info("result:"+jsonObject.toString());
                    return jsonObject.toString();
                }
                infos = sfUserRelationService.getShopSpokesManByID(true, currentPage - 1, pageSize, shopId, ID, userId);
                jsonObject.put("totalPage", pageNums);
                jsonObject.put("currentPage", currentPage - 1);
                break;
            }
        }
        SfSpokesAndFansInfo info;
        for (int i = 0; i < infos.size(); i++){
            info = infos.get(i);
            info.setFansNum(sfUserRelationService.getFansNumByUserId(info.getUserId(), shopId));
            info.setSpokesManNum(sfUserRelationService.getSpokesManNumByUserId(info.getUserId(), shopId));
            infos.set(i,info);
        }
        jsonObject.put("infos",infos);
        jsonObject.put("isTrue",true);
        logger.info("result:"+jsonObject.toString());
        return jsonObject.toString();
    }

    /**
     * 查看代言人详情
     * @param request   request
     * @param showUserId    查看人id
     * @return  mv
     * @throws Exception
     */
    @RequestMapping(value = "/spokesManDetail.shtml")
    public ModelAndView querySpokesManDetail(HttpServletRequest request,
                                             @RequestParam(value = "showUserId",required = true) Long showUserId)throws Exception{
        logger.info("查看代言人详情");
        ComUser comUser = getComUser(request);
        if (comUser == null){
            throw new BusinessException("用户未登陆");
        }
        Long userId = comUser.getId();
        logger.info("userId = " + userId);
        ModelAndView mv = new ModelAndView();
        SfShop sfShop = sfShopService.getSfShopByUserId(userId);
        logger.info("shopId = " + sfShop.getId());
        if (sfShop == null){
            throw new BusinessException("不存在小铺信息");
        }
        BfSpokesManDetailPo detailPo = null;
        try{
            detailPo = sfUserRelationService.getSpokesManDetail(showUserId, sfShop.getId());
        }catch (Exception e){
            e.printStackTrace();
        }
        mv.addObject("detail",detailPo);
        mv.setViewName("platform/user/spokesManDetail");
        return mv;
    }
}
