package com.masiis.shop.web.platform.controller.user;

import com.alibaba.fastjson.JSONObject;
import com.masiis.shop.common.exceptions.BusinessException;
import com.masiis.shop.common.util.OSSObjectUtils;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.beans.certificate.CertificateInfo;
import com.masiis.shop.dao.po.ComSku;
import com.masiis.shop.dao.po.ComUser;
import com.masiis.shop.dao.po.PfUserCertificate;
import com.masiis.shop.dao.po.PfUserSku;
import com.masiis.shop.common.constant.platform.SysConstants;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.user.UserCertificateService;
import com.masiis.shop.web.common.service.UserService;
import com.masiis.shop.web.platform.service.user.UserSkuService;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * UserCertificateController
 *
 * @author ZhaoLiang
 * @date 2016/3/10
 */
@Controller
@RequestMapping("/userCertificate")
public class UserCertificateController extends BaseController {

    private Logger log = Logger.getLogger(this.getClass());

    @Resource
    private UserSkuService userSkuService;
    @Resource
    private UserService userService;
    @Resource
    private SkuService skuService;
    @Resource
    private UserCertificateService userCertificateService;


    @RequestMapping(value = "text.do")
    public String text(HttpServletRequest request,HttpServletResponse response){
        return "platform/user/shimingrenzheng";
    }

    /**
     * @author ZhaoLiang
     * @date 2016/3/10 18:37
     */
    @RequestMapping("/setUserCertificate.shtml")
    public ModelAndView setUserCertificate(HttpServletRequest request,
                                           @RequestParam(value = "userSkuId", required = true) Integer userSkuId
    ) {
        ModelAndView modelAndView = new ModelAndView();
        String url = PropertiesUtils.getStringValue("index_user_idCard_url");
        try {
            PfUserSku pfUserSku = userSkuService.getUserSkuById(userSkuId);
            ComUser comUser = userService.getUserById(pfUserSku.getUserId());
            ComSku comSku = skuService.getSkuById(pfUserSku.getSkuId());
            modelAndView.addObject("userSkuId", pfUserSku.getId());
            modelAndView.addObject("skuName", comSku.getName());
            modelAndView.addObject("name", comUser.getRealName());
            modelAndView.addObject("idCard", comUser.getIdCard());
            modelAndView.addObject("idCardFrontUrl", StringUtils.isBlank(comUser.getIdCardFrontUrl()) ? "" : url + comUser.getIdCardFrontUrl());
            modelAndView.addObject("idCardBackUrl", StringUtils.isBlank(comUser.getIdCardBackUrl()) ? "" : url + comUser.getIdCardBackUrl());
            modelAndView.setViewName("platform/user/tijiaosq");
        } catch (Exception ex) {

        }
        return modelAndView;
    }

    /**
     * 上传身份证正反面
     *
     * @author ZhaoLiang
     * @date 2016/3/11 11:54
     */
    @ResponseBody
    @RequestMapping("/idCardImgUpload.do")
    public String imgUpload(HttpServletRequest request, HttpServletResponse response,
                            @RequestParam(value = "idCardImg", required = true) MultipartFile idCardImg
    ) throws IOException {

        JSONObject object = new JSONObject();
        try {
            String savepath = "http://" + OSSObjectUtils.BUCKET + "." + OSSObjectUtils.ENDPOINT + "/" + OSSObjectUtils.OSS_CERTIFICATE_TEMP;
            String fileName = userCertificateService.uploadCertificateToOss(idCardImg,getComUser(request));
            log.info("上传服务器根路径---------"+savepath);
            log.info("上传服务器文件名----------"+fileName);
            if (StringUtils.isBlank(fileName)) {
                object.put("code", "0");
                object.put("msg", "");
                object.put("imgPath", "");
            } else {
                object.put("code", "1");
                object.put("msg", "上传成功");
                object.put("fileName",fileName);

                log.info("上传身份证路径------------"+savepath + fileName);
                object.put("imgPath", savepath + fileName);// java.net.URLEncoder.encode(savepath + imgPath, "UTF-8")
            }
        } catch (Exception e) {
            object.put("code", "99");
            object.put("msg", e.toString());
            object.put("imgPath", "");
        }
        return object.toJSONString();
    }

    /**
     * 授权书申请
     *
     * @author ZhaoLiang
     * @date 2016/3/11 13:58
     */
    @ResponseBody
    @RequestMapping("/add.do")
    public String userCertificateAdd(HttpServletRequest request,
                                     @RequestParam(value = "userSkuId", required = true) Integer userSkuId,
                                     @RequestParam(value = "name", required = true) String name,
                                     @RequestParam(value = "idCard", required = true) String idCard,
                                     @RequestParam(value = "idCardFrontUrl", required = true) String idCardFrontUrl,
                                     @RequestParam(value = "idCardBackUrl", required = true) String idCardBackUrl
    ) {
        JSONObject object = new JSONObject();
        try {
            if (StringUtils.isBlank(name)) {
                throw new BusinessException("姓名不能为空");
            }
            if (StringUtils.isBlank(idCard)) {
                throw new BusinessException("身份证不能为空");
            }
            if (StringUtils.isBlank(idCardFrontUrl)) {
                throw new BusinessException("身份证照片不能为空");
            }
            if (StringUtils.isBlank(idCardBackUrl)) {
                throw new BusinessException("身份证照片不能为空");
            }
            PfUserSku pfUserSku = userSkuService.getUserSkuById(userSkuId);
            if (pfUserSku == null) {
                throw new BusinessException("代理信息有误");
            }
            String rootPath = request.getServletContext().getRealPath("/");
            String webappPath = rootPath.substring(0, rootPath.lastIndexOf(File.separator));
            String frontFillFullName = uploadFile(webappPath + SysConstants.ID_CARD_PATH + idCardFrontUrl);
            String backFillFullName = uploadFile(webappPath + SysConstants.ID_CARD_PATH + idCardBackUrl);
            //修改用户数据
            ComUser comUser = userService.getUserById(pfUserSku.getUserId());
            comUser.setIdCard(idCard);
            comUser.setIdCardFrontUrl(frontFillFullName);
            comUser.setIdCardBackUrl(backFillFullName);
            //创建证书申请数据
            PfUserCertificate pfUserCertificate = new PfUserCertificate();
            pfUserCertificate.setPfUserSkuId(pfUserSku.getId());
            pfUserCertificate.setCreateTime(new Date());
            pfUserCertificate.setCode("");
            pfUserCertificate.setUserId(comUser.getId());
            ComSku comSku = skuService.getSkuById(pfUserSku.getSkuId());
            pfUserCertificate.setSpuId(comSku.getSpuId());
            pfUserCertificate.setSkuId(pfUserSku.getSkuId());
            pfUserCertificate.setIdCard(idCard);
            pfUserCertificate.setMobile(comUser.getMobile());
            pfUserCertificate.setWxId(comUser.getWxId());
            pfUserCertificate.setBeginTime(new Date());
            Calendar calendar = Calendar.getInstance();
            Date date = new Date(System.currentTimeMillis());
            calendar.setTime(date);
            calendar.add(Calendar.YEAR, 2);
            date = calendar.getTime();
            pfUserCertificate.setEndTime(date);
            pfUserCertificate.setAgentLevelId(pfUserSku.getAgentLevelId());
            pfUserCertificate.setStatus(0);
            userService.insertUserCertificate(comUser, pfUserCertificate);
            object.put("isError", false);
        } catch (Exception ex) {
            object.put("isError", true);
            object.put("message", ex.getMessage());
        }
        return object.toJSONString();
    }

    /**
     * 上传文件
     *
     * @author ZhaoLiang
     * @date 2016/3/11 15:12
     */
    private String uploadFile(String filePath) throws FileNotFoundException {
        File frontFile = new File(filePath);
        OSSObjectUtils.uploadFile( frontFile, SysConstants.ID_CARD_PATH);
        return frontFile.getName();
    }

    /**
     * @Author 贾晶豪
     * @Date 2016/3/17 0017 下午 5:12
     * 个人的授权书列表
     */
    @RequestMapping(value = "/userList/{userId}")
    public ModelAndView userCertificate(HttpServletRequest request, HttpServletResponse response,
                                        @PathVariable("userId") Long userId) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/user/certificateList");
        List<CertificateInfo> pfUserCertificates = userCertificateService.CertificateByUser(userId);
        mav.addObject("pfUserCertificates", pfUserCertificates);
        return mav;
    }

    /**
     * @Author 贾晶豪
     * @Date 2016/3/17 0017 下午 6:37
     * 个人证书详情
     */
    @RequestMapping(value = "/detail")
    public ModelAndView userCertificateDetail(HttpServletRequest request, HttpServletResponse response,
                                              @RequestParam(value = "pfuId",required = true) Integer pfuId) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/user/cdetail");
        PfUserSku pfUserSku = userSkuService.getUserSkuById(pfuId);
        ComUser comUser = userService.getUserById(pfUserSku.getUserId());
        PfUserCertificate cdetail = userCertificateService.CertificateDetailsByUser(pfuId);
        String ctName = userCertificateService.getCtname(cdetail.getAgentLevelId());
        ComSku comSku = skuService.getSkuById(cdetail.getSkuId());
        mav.addObject("cdetail", cdetail);
        mav.addObject("comUser", comUser);
        mav.addObject("comSku", comSku);
        mav.addObject("ctname", ctName);
        return mav;
    }
    /**
     * @Author jjh
     * @Date 2016/3/18 0018 下午 1:50
     * 上级信息
     */
    @RequestMapping("/userInfo.list")
    public ModelAndView ready(HttpServletRequest request, HttpServletResponse response,
                              @RequestParam(value ="uskId",required = true) Integer uskId) throws Exception {
        ModelAndView mav = new ModelAndView("/platform/user/upperUserInfo");
        PfUserSku pfUserSku = userSkuService.getUserSkuById(uskId);
        ComUser userInfo = userService.getUserById(pfUserSku.getUserId());
        SimpleDateFormat sdf=new SimpleDateFormat("yyyy-MM-dd");
        String sDate=sdf.format(userInfo.getCreateTime());
        ComSku comSku = skuService.getSkuById(pfUserSku.getSkuId());
        PfUserCertificate pfUserCertificate = userCertificateService.getCertificateBypfuId(pfUserSku.getId());
        String ctName = userCertificateService.getCtname(pfUserCertificate.getAgentLevelId());
        String ctValue = PropertiesUtils.getStringValue("index_user_certificate_url");
        pfUserCertificate.setImgUrl(ctValue + pfUserCertificate.getImgUrl());
        mav.addObject("userInfo", userInfo);
        mav.addObject("pfUserSku", pfUserSku);
        mav.addObject("comSku", comSku);
        mav.addObject("pfUserCertificate", pfUserCertificate);
        mav.addObject("sDate", sDate);
        mav.addObject("ctname", ctName);
        return mav;
    }
}
