package com.masiis.shop.admin.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.admin.beans.user.User;
import com.masiis.shop.admin.controller.base.BaseController;
import com.masiis.shop.admin.service.user.ComUserService;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.ComUser;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 会员管理
 * Created by cai_tb on 16/3/30.
 */
@Controller
@RequestMapping("/comuser")
public class ComUserController extends BaseController{

    private final static Log log = LogFactory.getLog(ComUserController.class);

    @Resource
    private ComUserService comUserService;

    @RequestMapping("/list.shtml")
    public String list(){
        return "user/comuser/list";
    }

    @RequestMapping("/auditlist.shtml")
    public String auditList(){
        return "user/comuser/auditList";
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                       Integer pageNumber,
                       Integer pageSize,
                       String sortOrder,
                       ComUser comUser){
        Map<String,Object> conMap = new HashMap<>();
        try {
            comUser.setAuditStatus(2);
            if(StringUtils.isNotBlank(request.getParameter("realName")))  conMap.put("realName", request.getParameter("realName"));
            if(StringUtils.isNotBlank(request.getParameter("mobile1")))    conMap.put("mobile", request.getParameter("mobile1"));
            if(StringUtils.isNotBlank(request.getParameter("idCard"))) conMap.put("idCard", request.getParameter("idCard"));
            Map<String, Object> pageMap = comUserService.listByCondition(pageNumber, pageSize, comUser,conMap);

            return pageMap;
        } catch (Exception e) {
            log.error("获取会员列表信息失败![comUser="+comUser+"]");
            e.printStackTrace();
        }

        return "error";
    }

    @RequestMapping("/auditlist.do")
    @ResponseBody
    public Object auditList(HttpServletRequest request, HttpServletResponse response,
                            Integer pageNumber,
                            Integer pageSize,
                            String sortOrder,
                            ComUser comUser){

        Map<String,Object> conMap = new HashMap<>();
        try {

            if(StringUtils.isNotBlank(request.getParameter("realName")))  conMap.put("realName", request.getParameter("realName"));
            if(StringUtils.isNotBlank(request.getParameter("mobile1")))    conMap.put("mobile", request.getParameter("mobile1"));
            if(StringUtils.isNotBlank(request.getParameter("idCard"))) conMap.put("idCard", request.getParameter("idCard"));
            Map<String, Object> pageMap = comUserService.auditListByCondition(pageNumber, pageSize, comUser,conMap);

            return pageMap;
        } catch (Exception e) {
            log.error("获取待审核会员列表失败![comUser="+comUser+"]");
            e.printStackTrace();
        }

        return "error";
    }

    @RequestMapping("/detail.shtml")
    public ModelAndView detail(HttpServletRequest request, HttpServletResponse response, Long id){
        try {
            User user = comUserService.detail(id);

            ModelAndView mav = new ModelAndView("user/comuser/detail");
            mav.addObject("user", user);

            return mav;
        } catch (Exception e) {
            log.error("获取会员信息详情失败![id="+id+"]");
            e.printStackTrace();
        }

        return new ModelAndView("error");
    }

    /**
     * 获取被审核人信息
     * @param request
     * @param response
     * @param id
     * @return
     */
    @RequestMapping("toaudit.do")
    public ModelAndView toAudit(HttpServletRequest request, HttpServletResponse response, Long id){
        ModelAndView mav = new ModelAndView("user/comuser/modalAudit");

        try {
            Map<String, Object> auditMap = comUserService.toAudit(id);
            mav.addObject("auditMap", auditMap);

        } catch (Exception e) {
            log.error("获取待审核人信息失败![id="+id+"]");
            e.printStackTrace();
        }

        return mav;
    }

    @RequestMapping("/audit.do")
    @ResponseBody
    public Object audit(HttpServletRequest request, HttpServletResponse response, ComUser comUser){
        try {
            comUserService.audit(comUser,getPbUser(request));

            return "success";
        } catch (Exception e) {
            log.error("审核会员失败![comUser="+comUser+"]");
            e.printStackTrace();
        }

        return "error";
    }


}
