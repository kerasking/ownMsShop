package com.masiis.shop.web.platform.controller.user;

import com.masiis.shop.dao.platform.user.ComUserAccountMapper;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.po.ComSku;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.ComUserAccount;
import com.masiis.shop.dao.po.PfUserCertificate;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.user.MyTeamService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

/**
 * Created by cai_tb on 16/3/16.
 */
@Controller
@RequestMapping("/myteam")
public class MyTeamController extends BaseController {

    private final Log log = LogFactory.getLog(MyTeamController.class);

    @Resource
    private MyTeamService myTeamService;
    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private ComUserAccountMapper comUserAccountMapper;

    @RequestMapping("/teamlist")
    public ModelAndView teamlist(HttpServletRequest request, HttpServletResponse response) {
        try {
            ModelAndView mav = new ModelAndView("platform/user/teamList");

            ComUser comUser = getComUser(request);

            List<Map<String, Object>> agentSkuMaps = myTeamService.listAgentSku(comUser.getId());
            Integer totalChild = 0;
            BigDecimal totalSales = new BigDecimal(0);
            for (Map<String, Object> agentSkuMap : agentSkuMaps) {
                totalChild += Integer.parseInt(agentSkuMap.get("countChild").toString());
                totalSales = totalSales.add((BigDecimal) agentSkuMap.get("countSales"));
            }
            mav.addObject("totalChild", totalChild);
            mav.addObject("totalSales", totalSales);
            mav.addObject("agentSkuMaps", agentSkuMaps);

            return mav;
        } catch (Exception e) {
            log.error("获取代理产品列表失败!");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 团队列表
     *
     * @param request
     * @param response
     * @param userSkuId
     * @return
     */
    @RequestMapping("/teamdetail")
    public ModelAndView teamDetail(HttpServletRequest request, HttpServletResponse response,
                                   Integer userSkuId) {

        try {
            ModelAndView mav = new ModelAndView("platform/user/teamDetail");

            Map<String, Object> teamMap = myTeamService.findTeam(userSkuId);

            mav.addObject("teamMap", teamMap);
            return mav;
        } catch (Exception e) {
            log.error("获取团队成员列表失败!");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 团队成员详细信息
     *
     * @param request
     * @param response
     * @return
     */
    @RequestMapping("/memberinfo")
    public ModelAndView memberInfo(HttpServletRequest request, HttpServletResponse response,
                                   String code) {

        try {
            ModelAndView mav = new ModelAndView("platform/user/memberInfo");

            Map<String, Object> memberMap = myTeamService.viewMember(code);
            mav.addObject("memberMap", memberMap);

            return mav;
        } catch (Exception e) {
            log.error("获取团队成员信息失败!");
            e.printStackTrace();

            return null;
        }
    }

    /**
     * 查看队员指定代理产品的升级记录
     *
     * @param skuId
     * @return
     */
    @RequestMapping("/upgradeRecord")
    public ModelAndView upgradeRecord(Long userId, Integer skuId) {

        ModelAndView mav = new ModelAndView("platform/user/upgradeRecord");
        try {
            List<Map<String, Object>> upgradeRecords = myTeamService.upgradeRecord(userId, skuId);
            mav.addObject("upgradeRecords", upgradeRecords);
        } catch (Exception e) {
            log.error("查看队员升级记录失败![userId=" + userId + "][skuId=" + skuId + "]---:" + e);
            e.printStackTrace();
        }

        return mav;
    }
}
