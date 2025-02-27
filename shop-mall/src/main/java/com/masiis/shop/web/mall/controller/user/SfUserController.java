package com.masiis.shop.web.mall.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.web.mall.controller.base.BaseController;
import com.masiis.shop.web.common.service.UserService;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @Date:2016/4/7
 * @auth:lzh
 */
@Controller
@RequestMapping(value = "/sfuser")
public class SfUserController extends BaseController {

    private final Logger logger = Logger.getLogger(SfUserController.class);

    @Autowired
    private UserService userService;

    /**
     * 校验用户是否已经绑定
     * @param userId       //用户id
     * @param request
     * @return
     * @author:wbj
     */
    @RequestMapping(value = "/checkBinding.do",method = RequestMethod.POST)
    @ResponseBody
    public String checkBinding(@RequestParam(value = "userId",required = true) Long userId,
                               HttpServletRequest request) throws Exception{

        ComUser user = getComUser(request);
        logger.info("校验用户是否已经绑定");
        JSONObject jsonobject = new JSONObject();

        if (user == null){
            throw new BusinessException("校验用户是否已经绑定【用户未登录】");
        }
        if (user.getId().longValue() != userId.longValue()){
            throw new BusinessException("校验用户是否已经绑定【用户信息错误】");
        }
        //通过userId查询comuser
        user = userService.getUserById(userId);
        if (user == null){
            throw new BusinessException("校验用户是否已经绑定【用户不存在】");
        }
        int isBinding = user.getIsBinding();
        if (isBinding == 0){
            jsonobject.put("isTrue","false");
            jsonobject.put("message","用户未绑定");
        }else {
            jsonobject.put("isTrue","true");
        }
        logger.info(jsonobject.toJSONString());
        return jsonobject.toJSONString();
    }
}
