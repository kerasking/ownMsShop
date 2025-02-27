package com.masiis.shop.web.platform.controller.user;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.dao.po.ComBank;
import com.masiis.shop.dao.po.ComDictionary;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.ComUserExtractwayInfo;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.platform.service.system.ComBankService;
import com.masiis.shop.web.platform.service.system.ComDictionaryService;
import com.masiis.shop.web.platform.service.user.UserExtractwayInfoService;
import com.masiis.shop.web.common.service.UserService;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by wbj on 2016/3/18.
 */
@Controller
@RequestMapping(value = "/extractwayinfo")
public class UserExtractwayInfoController extends BaseController {

    private final static Log log = LogFactory.getLog(UserExtractwayInfoController.class);

    @Resource
    private ComDictionaryService comDictionaryService;
    @Resource
    private UserExtractwayInfoService userExtractwayInfoService;
    @Resource
    private UserService userService;
    @Resource
    private ComBankService comBankService;

    private final int ADDTOCHOSE = 0; //新增完银行卡跳转到选择银行卡界面
    private final int ADDTOMANAGE = 1; //新增完银行卡跳转到个人中心的管理银行卡界面

    /**
     * 新增用户提现方式信息
     * @param bankcard          银行卡号
     * @param bankid            银行id
     * @param depositbankname   开户行名称
     * @param cardownername     持卡人姓名
     * @return
     */
    @RequestMapping(value = "/add.do",method = RequestMethod.POST)
    @ResponseBody
    public String userExtractwayInfoAdd(@RequestParam(value = "bankcard",required = true) String bankcard,
                                        @RequestParam(value = "bankid",required = true) String bankid,
                                        @RequestParam(value = "depositbankname",required = true) String depositbankname,
                                        @RequestParam(value = "cardownername",required = true) String cardownername,
                                        @RequestParam(value = "returnJumpType",required = false,defaultValue = "0") int returnJumpType,
                                        HttpServletRequest request){

        log.info("bankcard:"+bankcard);
        log.info("bankid:"+bankid);
        log.info("depositbankname:"+depositbankname);
        log.info("cardownername:"+cardownername);

        ComUser user = getComUser(request);
        JSONObject jsonobject = new JSONObject();
        try{
            if (StringUtils.isBlank(bankcard)){
                jsonobject.put("isTrue","false");
                jsonobject.put("message","新增用户提现方式信息【银行卡号不能为空】");
                log.info(jsonobject.toJSONString());
                return jsonobject.toJSONString();
            }
            if (StringUtils.isBlank(bankid)){
                jsonobject.put("isTrue","false");
                jsonobject.put("message","新增用户提现方式信息【银行名称不能为空】");
                log.info(jsonobject.toJSONString());
                return jsonobject.toJSONString();
            }
            if (StringUtils.isBlank(depositbankname)){
                jsonobject.put("isTrue","false");
                jsonobject.put("message","新增用户提现方式信息【开户行名称不能为空】");
                log.info(jsonobject.toJSONString());
                return jsonobject.toJSONString();
            }
            if (StringUtils.isBlank(cardownername)){
                jsonobject.put("isTrue","false");
                jsonobject.put("message","新增用户提现方式信息【持卡人姓名不能为空】");
                log.info(jsonobject.toJSONString());
                return jsonobject.toJSONString();
            }
            if (user == null){
                jsonobject.put("isTrue","false");
                jsonobject.put("message","新增用户提现方式信息【新增前请登陆】");
                log.info(jsonobject.toJSONString());
                return jsonobject.toJSONString();
            }
            Long userId = user.getId();

            //根据银行id查询银行基础信息表
            ComBank comBank = comBankService.findById(Integer.valueOf(bankid));
            ComUserExtractwayInfo extractway = userExtractwayInfoService.findByBankcardAndCardownername(bankcard,cardownername);
            List<ComUserExtractwayInfo> infos = userExtractwayInfoService.findByUserId(userId);
            if (extractway == null){
                extractway = new ComUserExtractwayInfo();
                extractway.setBankCard(bankcard);
                extractway.setBankName(comBank.getBankName());
                extractway.setDepositBankName(depositbankname);
                extractway.setCardOwnerName(cardownername);
                extractway.setComUserId(Long.valueOf(userId));
                extractway.setExtractWay(Long.valueOf(3));   //默认体现方式为银行卡提现
                extractway.setCardImg(comBank.getBankImg());
                extractway.setIsEnable(0);//新增用户体现方式，是否启用默认为启用
                if (infos == null || infos.size() == 0){
                    extractway.setIsDefault(0);//设置为提现默认银行卡
                }else {
                    extractway.setIsDefault(1);
                }
                extractway.setChangedBy("add");
                extractway.setCreatedTime(new Date());
                extractway.setChangedTime(new Date());
                userExtractwayInfoService.addComUserExtractwayInfo(extractway);
            }else {
                //询字典表数据
                ComDictionary comDictionary = comDictionaryService.findByCodeAndKey("COM_USER_EXTRACT_WAY",extractway.getExtractWay().intValue());
                log.info(String.valueOf(comDictionary.getKey()));
                //存在数据并且为未启用状态
                if (extractway.getIsEnable() > 0) {
                    extractway.setBankCard(bankcard);
                    extractway.setBankName(comBank.getBankName());
                    extractway.setDepositBankName(depositbankname);
                    extractway.setCardOwnerName(cardownername);
                    extractway.setComUserId(Long.valueOf(userId));
                    extractway.setExtractWay(comDictionary.getKey() == null ? 3 : comDictionary.getKey().longValue());
                    extractway.setCardImg(comBank.getBankImg());
                    extractway.setIsEnable(0);//将未启用状态改为启用状态
                    if (infos == null || infos.size() == 0){
                        extractway.setIsDefault(0);//设置为提现默认银行卡
                    }else {
                        extractway.setIsDefault(1);
                    }
                    extractway.setChangedBy("edit");
                    extractway.setChangedTime(new Date());
                    userExtractwayInfoService.updataComUserExtractwayInfo(extractway);
                }else {
                    jsonobject.put("isTrue","false");
                    jsonobject.put("message","银行卡号已经存在");
                    log.info(jsonobject.toJSONString());
                    return jsonobject.toJSONString();
                }
            }
            switch (returnJumpType){
                case ADDTOCHOSE:
                    jsonobject.put("returnJumpType",ADDTOCHOSE);
                    break;
                case ADDTOMANAGE:
                    jsonobject.put("returnJumpType",ADDTOMANAGE);
                    break;
                default:
            }
            jsonobject.put("isTrue","true");
        }catch (Exception e){
            jsonobject.put("isTrue","false");
            jsonobject.put("message","服务忙请稍后。。。");
//            jsonobject.put("message",e.getMessage());
            e.printStackTrace();
        }
        log.info(jsonobject.toJSONString());
        return jsonobject.toJSONString();
    }

    /**
     * 通过userId查询已绑定卡片信息
     * @return
     */
    @RequestMapping(value = "/findExtractwayInfo.shtml")
    public ModelAndView findByUserId(HttpServletRequest request) throws Exception{
        log.info("通过userId查询已绑定卡片信息");

        ComUser user = getComUser(request);
        if (user == null){
            throw new BusinessException("该用户未登录");
        }
        ModelAndView mv = new ModelAndView();
        Long userId = user.getId();
        List<ComUserExtractwayInfo> list;
        List<ComUserExtractwayInfo> userExtractwayInfos = new ArrayList<>();
        try{
            list = userExtractwayInfoService.findByUserId(userId);

            String cardCode;
            for (ComUserExtractwayInfo info : list){
                cardCode = info.getBankCard();
                info.setBankCard(cardCode.substring(0,4)+"*********"+cardCode.substring(cardCode.length()-4,cardCode.length()));
                userExtractwayInfos.add(info);
            }
            mv.addObject("userId",userId);
            mv.addObject("extractwayList",userExtractwayInfos);
        }catch (Exception e){
            e.printStackTrace();
        }
        mv.setViewName("platform/user/bankcardSelect");
        return mv;
    }

    /**
     * 跳转至新增银行卡页面
     * @return
     */
    @RequestMapping(value = "/toCreateBankcard.shtml")
    public ModelAndView toBankCardCreate(HttpServletRequest request,
                                         @RequestParam(value = "returnJumpType",required = false,defaultValue = "0") int returnJumpType) throws Exception{

        log.info("准备跳转至新增银行卡页面");
        ComUser user = getComUser(request);
        if (user == null){
            throw new BusinessException("该用户未登录");
        }
        Long userId = user.getId();
        log.info("userId="+userId);
        ModelAndView mv = new ModelAndView();
        List<ComBank> list = null;
        try{
            list = comBankService.findAll();
        }catch (Exception e){
            e.printStackTrace();
        }
        mv.addObject("userId",userId);
        mv.addObject("bankList",list);
        mv.addObject("returnJumpType",returnJumpType);
        mv.setViewName("platform/user/bankcardCreate");
        return mv;
    }

    /**
     * 设置银行卡为支付默认卡号
     * @param id
     * @return
     */
    @RequestMapping(value = "/setbankdefault.do")
    @ResponseBody
    public String setBankDefault(@RequestParam(value = "id",required = true) String id,
                                 HttpServletRequest request){

        ComUser user = getComUser(request);
        log.info("id:"+id);
        JSONObject jsonobject = new JSONObject();
        try{
            if (user == null){
                throw new BusinessException("该用户未登录");
            }
            Long userId = user.getId();
            log.info("userId:"+userId);
            List<ComUserExtractwayInfo> list = userExtractwayInfoService.findByUserId(userId);
            for (ComUserExtractwayInfo info:list){
                if (info.getId().longValue()==Long.valueOf(id).longValue()){
                    if (info.getIsDefault() != 0){
                        info.setIsDefault(0);
                        userExtractwayInfoService.updataComUserExtractwayInfo(info);
                    }
                }else {
                    if (info.getIsDefault() != 1){
                        info.setIsDefault(1);
                        userExtractwayInfoService.updataComUserExtractwayInfo(info);
                    }
                }
            }
            jsonobject.put("isTrue","true");
        }catch (Exception e){
            e.printStackTrace();
            jsonobject.put("isTrue","false");
            jsonobject.put("message","服务忙请稍后。。。");
        }
        return jsonobject.toJSONString();
    }
}

