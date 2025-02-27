package com.masiis.shop.web.mall.controller.promotion.gorder;

import com.masiis.shop.common.enums.promotion.ComGiftIsGiftEnum;
import com.masiis.shop.common.enums.promotion.SfTurnTableRuleTypeEnum;
import com.masiis.shop.dao.beans.promotion.UserTurnTableRecordInfo;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.web.mall.controller.base.BaseController;
import com.masiis.shop.web.promotion.cpromotion.service.guser.SfUserTurnTableRecordService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 *  大转盘中奖纪录
 */
@Controller
@RequestMapping("/turnTableGiftRecord")
public class TurnTableGiftRecordController extends BaseController {

    @Resource
    private SfUserTurnTableRecordService userTurnTableRecordService;

    /**
     * 获取用户的中奖纪录
     * @param request
     * @param model
     * @return
     */
    @RequestMapping("/getPromotionGorderPageInfo.html")
    public String getGiftedRecordInfo(HttpServletRequest request,Model model) {
        ComUser comUser =  getComUser(request);
        List<UserTurnTableRecordInfo> records =  userTurnTableRecordService.getRecordInfoByUserId(comUser.getId(),null, ComGiftIsGiftEnum.isGift_true.getCode(), SfTurnTableRuleTypeEnum.C.getCode());
        model.addAttribute("records",records);
        return "promotion/gorder/turnTableGiftRecord";
    }

    @RequestMapping("/getPage.html")
    public String getPage(HttpServletRequest request) {
        return "promotion/gorder/turnTableRecordDemo";
    }
}
