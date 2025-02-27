package com.masiis.shop.web.platform.controller.message;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.beans.message.PfMessageCenterDetail;
import com.masiis.shop.dao.beans.user.upgrade.UserSkuAgent;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.common.service.UserService;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.message.PfMessageContentService;
import com.masiis.shop.web.platform.service.message.PfMessageSrRelationService;
import com.masiis.shop.web.platform.service.user.PfUserSkuService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Date 2016/7/12
 * @Author lzh
 */
@Controller
@RequestMapping("/message")
public class PFMessageController extends BaseController {
    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private PfMessageContentService pfMessageContentService;
    @Resource
    private PfMessageSrRelationService srRelationService;
    @Resource
    private UserService userService;
    @Resource
    private PfUserSkuService pfUserSkuService;
    @Resource
    private SkuService skuService;

    @RequestMapping("/center.shtml")
    public String toCenter(HttpServletRequest request,
                           Model model){
        ComUser user = getComUser(request);
        if(user == null){
            throw new BusinessException("怎么能没有用户呢");
        }
        PfMessageContent content = pfMessageContentService.queryContentLatestByUserId(user.getId(), 2);
        model.addAttribute("content", content);
        model.addAttribute("myHeadUrl", user.getWxHeadImg());
        model.addAttribute("userName", user.getRealName());
        model.addAttribute("myId", user.getId());
        return "platform/message/pf_message/message_center_platform";
    }

    @RequestMapping("/centerlist.do")
    @ResponseBody
    public String centerList(HttpServletRequest request,
                             @RequestParam(required = true) Integer cur){
        JSONObject res = new JSONObject();
        List<PfMessageCenterDetail> resData = null;
        int pageSize = 10;
        try{
            ComUser user = getComUser(request);
            if(user == null){
                throw new BusinessException("怎么能没用户呢！");
            }
            if(cur == null || cur < 0){
                throw new BusinessException("当前页码不正确");
            }
            int totalNums = srRelationService.queryNumsToUser(user, 2);
            if(totalNums == 0){
                res.put("hasData", false);
                res.put("resCode", "success");
                res.put("resMsg", "暂无数据");
                return res.toJSONString();
            }
            // 第一页页码是0
            int pageNums = totalNums/pageSize + (totalNums%pageSize == 0 ? 0 : 1);
            // 检测页码是否超出总页数
            if(cur + 1 > pageNums){
                throw new BusinessException("下一页码不正确");
            }
            // 检测是否是最后一页
            if(cur + 1 == pageNums){
                // 最后一页
                res.put("isLast", true);
            }else{
                // 非最后一页
                res.put("isLast", false);
            }
            res.put("resCode", "success");
            res.put("cur", cur);
            res.put("hasData", true);
            res.put("pageSize", pageSize);
            res.put("totalPage",pageNums);
            int start = cur * pageSize;
            // 查询要展现的消息数据
            resData = srRelationService.queryFromUsersByToUserWithPaging(user, 2, start, pageSize);
            res.put("data", resData);
        } catch (Exception e) {
            String resMsg = res.getString("resMsg");
            if(StringUtils.isBlank(resMsg)){
                res.put("resMsg", "网络错误");
            }
            res.put("resCode", "fail");
        }

        return JSONObject.toJSONStringWithDateFormat(res, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping("/detail.shtml")
    public String detail(HttpServletRequest request,
                         @RequestParam(required = true) Long userId,
                         Model model){
        ComUser user = getComUser(request);
        if(user == null){
            throw new BusinessException("用户怎么能没有呢");
        }
        ComUser fUser = userService.getUserById(userId);
        if(fUser == null){
            throw new BusinessException("消息来源用户为空");
        }
        srRelationService.updateRelationIsSeeByFromUserAndToUser(fUser.getId(), user.getId());
        model.addAttribute("userId", userId);
        return "platform/message/pf_message/message_detail";
    }

    @RequestMapping("/detaillist.do")
    @ResponseBody
    public String detailList(HttpServletRequest request,
                             @RequestParam(required = true) Integer cur,
                             @RequestParam(required = true) Long uid){
        JSONObject res = new JSONObject();
        List<PfMessageContent> resData = null;
        int pageSize = 10;
        String fromUserName = "";
        Long tUserId = null;
        Long fUserId = null;
        try{
            ComUser user = getComUser(request);
            if(user == null){
                throw new BusinessException("怎么能没用户呢！");
            }
            if(cur == null || cur < 0){
                throw new BusinessException("当前页码不正确");
            }
            if(uid == null){
                log.error("消息来源用户为空");
                res.put("resMsg", "消息来源用户为空");
                throw new BusinessException("消息来源用户为空");
            }
            ComUser fUser = userService.getUserById(uid);
            if(fUser == null){
                log.error("消息来源用户找不到");
                res.put("resMsg", "消息来源用户找不到");
                throw new BusinessException("消息来源用户找不到");
            }
            fUserId = uid;
            if(uid.longValue() == user.getId().longValue()){
                // 查看自己发出的消息
                fromUserName = "我";
                tUserId = null;
            } else {
                fromUserName = fUser.getRealName();
                tUserId = user.getId();
            }
            int totalNums = 0;
            if(tUserId == null){
                totalNums = pfMessageContentService.queryNumsByUserIdAndType(fUserId, 2);
            } else {
                totalNums = srRelationService.queryNumsFromUserAndToUser(tUserId, fUserId, 2);
            }
            if(totalNums == 0){
                res.put("hasData", false);
                res.put("resCode", "success");
                res.put("resMsg", "暂无数据");
                return res.toJSONString();
            }
            // 第一页页码是0
            int pageNums = totalNums/pageSize + (totalNums%pageSize == 0 ? 0 : 1);
            // 检测页码是否超出总页数
            if(cur + 1 > pageNums){
                throw new BusinessException("下一页码不正确");
            }
            // 检测是否是最后一页
            if(cur + 1 == pageNums){
                // 最后一页
                res.put("isLast", true);
            }else{
                // 非最后一页
                res.put("isLast", false);
            }
            res.put("resCode", "success");
            res.put("cur", cur);
            res.put("hasData", true);
            res.put("pageSize", pageSize);
            res.put("totalPage",pageNums);
            res.put("fromUserName", fromUserName);
            int start = cur * pageSize;
            // 查询要展现的消息数据
            if(tUserId == null){
                resData = pfMessageContentService.queryContentByUserIdAndTypeWithPaging(fUserId, 2, start, pageSize);
            } else {
                resData = srRelationService.queryDetailByFromUserAndToUserWithPaging(tUserId, fUserId, 2, start, pageSize);
            }
            res.put("data", resData);
        } catch (Exception e) {
            String resMsg = res.getString("resMsg");
            if(StringUtils.isBlank(resMsg)){
                res.put("resMsg", "网络错误");
            }
            res.put("resCode", "fail");
        }

        return JSON.toJSONStringWithDateFormat(res, "yyyy-MM-dd HH:mm:ss");
    }

    @RequestMapping("/tonew.shtml")
    public String toNewPage(){
        return "platform/message/pf_message/message_create";
    }

    @RequestMapping("/tonew.do")
    @ResponseBody
    public String toNewPageData(HttpServletRequest request){
        JSONObject res = new JSONObject();
        try{
            ComUser user = getComUser(request);
            if(user == null){
                throw new BusinessException("怎么能没用户呢");
            }
            if(user.getIsAgent().intValue() == 0){
                // 还不是代理商
                res.put("resCode", "fail");
                res.put("resMsg", "该用户还不是代理商");
                return res.toJSONString();
            }

            Integer childAgentNum = pfUserSkuService.getNumsByUserPid(user.getId());
            List<UserSkuAgent> skus = pfUserSkuService.getCurrentAgentLevel(user.getId());

            res.put("childAgentNum", childAgentNum);
            res.put("skus", skus);
            res.put("resCode", "success");
        } catch (Exception e) {
            res.put("resCode", "fail");
            res.put("resMsg", "网络错误");
        }

        return res.toJSONString();
    }

    @RequestMapping("/newmessage.do")
    @ResponseBody
    public String newMessage(HttpServletRequest request,
                             @RequestParam(required = true) String message,
                             @RequestParam(required = true) Integer urlType){
        JSONObject res = new JSONObject();
        try{
            if(StringUtils.isBlank(message)){
                res.put("resMsg", "消息内容为空");
                throw new BusinessException("消息内容为空");
            }

            String url = null;
            ComSku sku = null;
            ComUser user = getComUser(request);
            if(urlType.intValue() != -1) {
                sku = skuService.getSkuById(urlType);
                if(sku == null){
                    res.put("resMsg", "链接地址不正确");
                    throw new BusinessException("链接地址不正确");
                }
                url = "product/skuDetails.shtml" + "?skuId=" + sku.getId();
            }

            List<Long> relations = pfUserSkuService.getChildUserIdByUserPid(user.getId());
            if(relations == null || relations.size() <= 0){
                res.put("resMsg", "暂无直接下级");
                throw new BusinessException("暂无直接下级");
            }

            Integer messageType = 2;
            String remark = "直接下级群发";

            PfMessageContent content = pfMessageContentService.createMessageByType(user.getId(), message, messageType, remark, url);
            pfMessageContentService.insert(content);

            for(Long toUser:relations){
                PfMessageSrRelation srRelation = srRelationService.createRelationByContent(content, toUser);
                srRelationService.insert(srRelation);
            }

            res.put("resCode", "success");

        } catch (Exception e) {
            log.error(e.getMessage(), e);
            if(StringUtils.isBlank(res.getString("resMsg"))){
                res.put("resMsg", "网络错误");
            }
            res.put("resCode", "fail");
        }

        return res.toJSONString();
    }

    @RequestMapping("/success.shtml")
    public String messageCreateSuccess(){
        return "platform/message/pf_message/message_create_success";
    }
}
