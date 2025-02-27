package com.masiis.shop.web.platform.controller.user;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.masiis.shop.common.util.OSSObjectUtils;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.platform.product.*;
import com.masiis.shop.dao.platform.user.ComUserMapper;
import com.masiis.shop.dao.platform.user.PfUserCertificateMapper;
import com.masiis.shop.dao.platform.user.PfUserSkuMapper;
import com.masiis.shop.dao.po.*;
import com.masiis.shop.common.constant.wx.WxConsPF;
import com.masiis.shop.web.platform.controller.base.BaseController;
import com.masiis.shop.web.common.service.SkuService;
import com.masiis.shop.web.platform.service.qrcode.WeiXinPFQRCodeService;
import com.masiis.shop.web.platform.service.shop.JSSDKPFService;
import com.masiis.shop.web.platform.service.user.ComPosterService;
import com.masiis.shop.web.platform.service.user.PfUserShareParamService;
import com.masiis.shop.web.platform.service.user.UserCertificateService;
import com.masiis.shop.web.common.utils.DownloadImage;
import com.masiis.shop.web.platform.utils.image.DrawImageUtil;
import com.masiis.shop.web.platform.utils.image.Element;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import javax.imageio.ImageIO;
import javax.imageio.stream.ImageOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;

/**
 * 发展合伙人
 * Created by cai_tb on 16/3/17.
 */
@Controller
@RequestMapping("/developing")
public class DevelopingController extends BaseController {

    private final static Log log = LogFactory.getLog(DevelopingController.class);

    @Resource
    private ComUserMapper comUserMapper;
    @Resource
    private ComSkuMapper comSkuMapper;
    @Resource
    private PfSkuAgentMapper pfSkuAgentMapper;
    @Resource
    private ComSpuMapper comSpuMapper;
    @Resource
    private SkuService skuService;
    @Resource
    private ComBrandMapper comBrandMapper;
    @Resource
    private PfUserSkuMapper pfUserSkuMapper;
    @Resource
    private ComAgentLevelMapper comAgentLevelMapper;
    @Resource
    private PfUserCertificateMapper pfUserCertificateMapper;
    @Resource
    private UserCertificateService userCertificateService;
    @Resource
    private JSSDKPFService jssdkService;
    @Resource
    private WeiXinPFQRCodeService weiXinPFQRCodeService;
    @Resource
    private PfUserShareParamService pfUserShareParamService;
    @Resource
    private ComPosterService comPosterService;

    @RequestMapping("/ui")
    public ModelAndView ui(HttpServletRequest request, HttpServletResponse response){
        ModelAndView mav = new ModelAndView("platform/user/developing");
        ComUser comUser = null;

        try {
            comUser = getComUser(request);
            PfUserSku userSkuC = new PfUserSku();
            userSkuC.setUserId(comUser.getId());
            List<PfUserSku> pfUserSkus = pfUserSkuMapper.selectByCondition(userSkuC);


            if(pfUserSkus != null && pfUserSkus.size() > 0){

                List<Map<String, Object>> agentMaps = new ArrayList<>();
                for(PfUserSku pus : pfUserSkus){

                    ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(pus.getAgentLevelId());
                    ComSku comSku = comSkuMapper.selectById(pus.getSkuId());
                    ComSpu comSpu = comSpuMapper.selectById(comSku.getSpuId());
                    ComBrand comBrand = comBrandMapper.selectById(comSpu.getBrandId());
                    /* 统计商品代理等级数 */
                    Integer skuAgentLevels = pfSkuAgentMapper.countSkuAgentLevel(pus.getSkuId());

                    Map<String, Object> agentMap = new HashMap<>();
                    agentMap.put("levelId", comAgentLevel.getId());
                    agentMap.put("levelName", comAgentLevel.getName());
                    agentMap.put("skuName", comSku.getName());
                    agentMap.put("skuId", comSku.getId());
                    agentMap.put("brandLogo", comBrand.getLogoUrl());
                    agentMap.put("userSkuId", pus.getId());
                    agentMap.put("canDeveloping", pus.getAgentLevelId()==skuAgentLevels?"no":"yes");

                    agentMaps.add(agentMap);
                }

                mav.addObject("agentMaps", agentMaps);

                return mav;
            }
        } catch (Exception e) {
            log.error("获取我代理的产品失败![comUser="+comUser+"]");
            e.printStackTrace();
        }

        return mav;
    }

    @RequestMapping("/isAudit")
    @ResponseBody
    public Object isAudit(HttpServletRequest request, HttpServletResponse response, Integer userSkuId){
        PfUserCertificate pfUserCertificate = userCertificateService.getCertificateBypfuId(userSkuId);
        if(pfUserCertificate != null && pfUserCertificate.getStatus().intValue() == 1){
            return "yes";
        }else{
            return "no";
        }
    }

    /**
     * 可发展等级页面
     * @param request
     * @param skuId
     * @return
     */
    @RequestMapping("/developLevel")
    public ModelAndView developLevel(HttpServletRequest request, Integer skuId){
        ModelAndView mav = new ModelAndView("platform/user/developLevel");

        ComUser comUser = null;
        try {
            comUser = getComUser(request);
            PfUserSku pfUserSku = pfUserSkuMapper.selectByUserIdAndSkuId(comUser.getId(), skuId);
            List<Integer> levelIds = pfSkuAgentMapper.selectLevelIdsBySkuIdAndIsShow(skuId, 1);
            List<PfSkuAgent> skuAgents = pfSkuAgentMapper.selectAllBySkuId(skuId);

        /* 删除高于comUser代理的等级id */
            for(int i=0; i<levelIds.size(); i++){
                if(levelIds.get(i).intValue() < pfUserSku.getAgentLevelId().intValue()){
                    levelIds.remove(i--);
                }
            }

            List<ComAgentLevel> comAgentLevels = comAgentLevelMapper.selectByIds(levelIds);
            mav.addObject("comAgentLevels", comAgentLevels);
            mav.addObject("skuAgents", skuAgents);
            mav.addObject("skuId", skuId);
            mav.addObject("agentLevelId", pfUserSku.getAgentLevelId());
        } catch (Exception e) {
            log.error("获取可发展等级失败![comUser="+comUser+"][skuId="+skuId+"]"+e);
            e.printStackTrace();
        }

        return mav;
    }

    @RequestMapping("/sharelink")
    public ModelAndView shareLink(HttpServletRequest request, HttpServletResponse response,
                                  Integer skuId,
                                  @RequestParam(value = "levelIds", required = false)String[] levelIds,
                                  @RequestParam(value = "fromUserId", required = false)Long fromUserId
    ){

        ModelAndView mav = new ModelAndView("platform/user/sharePage");

        try {
            ComUser comUser = null;
            String curUrl = null;
            String shareLink = null;
            if(fromUserId != null){
                comUser = comUserMapper.selectByPrimaryKey(fromUserId);
                curUrl = request.getRequestURL().toString()+"?skuId="+skuId+"&fromUserId="+comUser.getId();
            }else{
                comUser = getComUser(request);
                curUrl = request.getRequestURL().toString()+"?skuId="+skuId;
            }

            if(request.getParameter("from") != null){
                curUrl += "&from=" + request.getParameter("from");
            }
            if(request.getParameter("isappinstalled") != null){
                curUrl += "&isappinstalled=" + request.getParameter("isappinstalled");
            }

            /** 获取调用JSSDK所需要的数据 **/
            Map<String, String> resultMap = jssdkService.requestJSSDKData(curUrl);
            if(fromUserId != null){
                shareLink = curUrl;
            }else{
                shareLink = curUrl + "&fromUserId="+comUser.getId();
            }

            /**
             * 获取海报
             */
            /* 拼接等级ID  */
            StringBuilder sLevelIds = new StringBuilder();
            List<Integer> agentLevelIds = new ArrayList<>();
            if(levelIds != null && levelIds.length > 0){
                for(String levelId : levelIds){
                    sLevelIds.append(levelId);
                    sLevelIds.append(",");

                    agentLevelIds.add(Integer.valueOf(levelId));
                }
            }


            //获取海报1:如果海报已存在并且未过期,直接返回
            PfUserShareParam userShareParam = new PfUserShareParam();
            userShareParam.setfUserId(comUser.getId());
            userShareParam.setSkuId(skuId);
            userShareParam.setAgentLevelIds(sLevelIds.toString());

            List<PfUserShareParam> pfUserShareParams = pfUserShareParamService.findByCondition(userShareParam);
            Long shareParamId = (pfUserShareParams != null && pfUserShareParams.size() > 0) ? pfUserShareParams.get(0).getId() : -1L;

            ComPoster comPosterParam = new ComPoster();
            comPosterParam.setType(0);
            comPosterParam.setUserId(comUser.getId());
            comPosterParam.setShareParamId(shareParamId);

            ComPoster comPoster = comPosterService.findByCondition(comPosterParam);
            Long curTime = System.currentTimeMillis();
            Long monthTime = 1000L * 60 * 60 * 24 * 28;//28天
            if(comPoster != null && (curTime-comPoster.getCreateTime().getTime()) < monthTime) {
                resultMap.put("poster", PropertiesUtils.getStringValue("index_user_poster_url") + comPoster.getPosterName());
                mav.addObject("shareMap", resultMap);

                return mav;
            }
            //获取海报2:如果海报不存在或已经过期,重新创建海报
            log.info("发展合伙人[comUser="+comUser+"]");
            ComSku comSku = comSkuMapper.selectById(skuId);
            ComSkuExtension comSkuExtension = skuService.findSkuExteBySkuId(skuId);
            ComSpu comSpu = comSpuMapper.selectById(comSku.getSpuId());
            ComBrand comBrand = comBrandMapper.selectById(comSpu.getBrandId());
            String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath() + "/";

            PfUserCertificate puc = new PfUserCertificate();
            puc.setUserId(comUser.getId());
            puc.setSkuId(comSku.getId());
            List<PfUserCertificate> pfUserCertificates = pfUserCertificateMapper.selectByCondition(puc);
            String[] contents = new String[2];
            if(pfUserCertificates != null && pfUserCertificates.size() > 0){
                PfUserCertificate pfUserCertificate = pfUserCertificates.get(0);

                String headImg = "h-"+comUser.getId()+".png";
                String qrcodeName = "develop-qrcode-"+comUser.getId()+"-"+comSku.getId()+".png";
                String bgPoster = "develop-sku-"+comSku.getId()+".png";
                String posterDirPath = request.getServletContext().getRealPath("/")+"static/images/poster";
                File  posterDir = new File(posterDirPath);
                if(!posterDir.exists()){
                    posterDir.mkdirs();
                }
                //先删除旧的图片,再下载新的图片
                new File(posterDirPath+"/"+headImg).delete();
                new File(posterDirPath+"/"+bgPoster).delete();
                new File(posterDirPath+"/"+qrcodeName).delete();

                File headImgFile   = new File(posterDirPath+"/"+headImg);
                File bgImgFile     = new File(posterDirPath+"/"+bgPoster);
                //File qrcodeImgFile = new File(posterDirPath+"/"+qrcodeName);
                if(!headImgFile.exists() && StringUtils.isNotBlank(comUser.getWxHeadImg()))   DownloadImage.download(comUser.getWxHeadImg(), headImg, posterDirPath);
                if(!headImgFile.exists() && StringUtils.isBlank(comUser.getWxHeadImg()))      OSSObjectUtils.downloadFile("static/user/background_poster/h-default.png", headImgFile.getAbsolutePath());
                if(!bgImgFile.exists())     OSSObjectUtils.downloadFile("static/user/background_poster/"+comSkuExtension.getPoster(), posterDirPath+"/"+bgPoster);
                String[] qrcodeResult = weiXinPFQRCodeService.createAgentQRCode(comUser.getId(),skuId, sLevelIds.toString());
                DownloadImage.download(qrcodeResult[1], qrcodeName, posterDirPath);


                //画图
                String fontPath = request.getServletContext().getRealPath("/")+"static/font";
                //Font font1 = Font.createFont(Font.TRUETYPE_FONT, new File(fontPath+"/msyh.ttc"));
                //font1.deriveFont(Font.PLAIN, 32);
                Font font1 = new Font("华文细黑", Font.PLAIN, 30);
                Font font2 = new Font("华文细黑", Font.PLAIN, 20);
                Date curDate = new Date();
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
                String startTime = sdf.format(curDate);
                curDate.setDate(curDate.getDate()+30);
                String endDate = sdf.format(curDate);

                int              random           = (int)((Math.random()*9+1)*100000);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
                Date createTime                   = new Date();
                String posterName                 = simpleDateFormat.format(createTime)+random+".png";

                StringBuilder agentLevelNames = new StringBuilder();
                              agentLevelNames.append("可注册等级:");
                List<ComAgentLevel> comAgentLevels = comAgentLevelMapper.selectByIds(agentLevelIds);
                for(ComAgentLevel agentLevel : comAgentLevels){
                    agentLevelNames.append(agentLevel.getId().intValue()==1?"联合创始人":agentLevel.getName());
                    agentLevelNames.append("、");
                }
                agentLevelNames.deleteCharAt(agentLevelNames.length()-1);

                Element headImgElement = new Element(373, 433, 156, 156, ImageIO.read(new File(posterDirPath+"/"+headImg)));
                Element bgPosterImgElement = new Element(0, 0, 904, 1200, ImageIO.read(new File(posterDirPath+"/"+bgPoster)));
                Element qrcodeImgElement = new Element(124, 833, 243, 243, ImageIO.read(new File(posterDirPath+"/"+qrcodeName)));
                ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(pfUserCertificate.getAgentLevelId().intValue()==1?2:pfUserCertificate.getAgentLevelId());//隐藏联合创始人
                Element text1Element = new Element(98, 651, font1, new Color(51,51,51), "Hi，我是"+comSku.getName()+"的"+comAgentLevel.getName()+comUser.getWxNkName()+"，我在麦链实现了创业梦想！长按识别二维码，加入麦链合伙人，一起赚钱吧");
                Element text2Element = new Element(421, 967, font2, new Color(102,102,102), "该二维码有效期:");
                Element text3Element = new Element(421, 1026, font2, new Color(102,102,102), startTime+"-"+endDate);
                Element text4Element = new Element(421, 1035, font2, new Color(102, 102, 102), agentLevelNames.toString());
                text1Element.setLineCount(22);
                text3Element.setLineStyle(0);
                List<Element> drawElements = new ArrayList<>();
                drawElements.add(headImgElement);
                drawElements.add(bgPosterImgElement);
                drawElements.add(qrcodeImgElement);
                drawElements.add(text1Element);
                drawElements.add(text2Element);
                drawElements.add(text3Element);
                drawElements.add(text4Element);

                DrawImageUtil.drawImage(904, 1200, drawElements, "static/user/poster/"+posterName);

//                if("kangyinli.png".equals(comSkuExtension.getPoster())){
//                    Element headImgElement = new Element(373, 433, 156, 156, ImageIO.read(new File(posterDirPath+"/"+headImg)));
//                    Element bgPosterImgElement = new Element(0, 0, 904, 1200, ImageIO.read(new File(posterDirPath+"/"+bgPoster)));
//                    Element qrcodeImgElement = new Element(124, 833, 243, 243, ImageIO.read(new File(posterDirPath+"/"+qrcodeName)));
//                    ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(pfUserCertificate.getAgentLevelId());
//                    Element text1Element = new Element(98, 651, font1, new Color(51,51,51), "Hi，我是"+comSku.getName()+"的"+comAgentLevel.getName()+comUser.getWxNkName()+"，我在麦链实现了创业梦想！长按识别二维码，加入麦链合伙人，一起赚钱吧");
//                    Element text2Element = new Element(421, 967, font2, new Color(102,102,102), "该二维码有效期:");
//                    Element text3Element = new Element(421, 1026, font2, new Color(102,102,102), startTime+"-"+endDate);
//                    Element text4Element = new Element(421, 1085, font2, new Color(102, 102, 102), agentLevelNames.toString());
//                    text1Element.setLineCount(22);
//                    text3Element.setLineStyle(0);
//                    List<Element> drawElements = new ArrayList<>();
//                    drawElements.add(headImgElement);
//                    drawElements.add(bgPosterImgElement);
//                    drawElements.add(qrcodeImgElement);
//                    drawElements.add(text1Element);
//                    drawElements.add(text2Element);
//                    drawElements.add(text3Element);
//                    drawElements.add(text4Element);
//
//                    DrawImageUtil.drawImage(904, 1200, drawElements, "static/user/poster/"+posterName);
//                }else if("mss.png".equals(comSkuExtension.getPoster())){
//                    Element headImgElement = new Element(36, 878, 132, 132, ImageIO.read(new File(posterDirPath+"/"+headImg)));
//                    Element bgPosterImgElement = new Element(0, 0, 904, 1200, ImageIO.read(new File(posterDirPath+"/"+bgPoster)));
//                    Element qrcodeImgElement = new Element(604, 874, 220, 220, ImageIO.read(new File(posterDirPath+"/"+qrcodeName)));
//                    ComAgentLevel comAgentLevel = comAgentLevelMapper.selectByPrimaryKey(pfUserCertificate.getAgentLevelId());
//                    Element text1Element = new Element(176, 880, font1, new Color(51,51,51), "Hi，我是"+comSku.getName()+"的"+comAgentLevel.getName()+comUser.getWxNkName()+"，我在麦链实现了创业梦想！长按识别二维码，加入麦链合伙人，一起赚钱吧");
//                    Element text2Element = new Element(638, 1086, font2, new Color(102,102,102), "该二维码有效期:");
//                    Element text3Element = new Element(610, 1145, font2, new Color(102,102,102), startTime+"-"+endDate);
//                    text1Element.setLineCount(13);
//                    text3Element.setLineStyle(0);
//                    List<Element> drawElements = new ArrayList<>();
//                    drawElements.add(headImgElement);
//                    drawElements.add(bgPosterImgElement);
//                    drawElements.add(qrcodeImgElement);
//                    drawElements.add(text1Element);
//                    drawElements.add(text2Element);
//                    drawElements.add(text3Element);
//
//                    DrawImageUtil.drawImage(904, 1200, drawElements, "static/user/poster/"+posterName);
//                }


                //保存二维码海报图片地址
                ComPoster newComPoster = new ComPoster();
                newComPoster.setCreateTime(createTime);
                newComPoster.setShareParamId(Long.valueOf(qrcodeResult[0]));
                newComPoster.setType(0);
                newComPoster.setUserId(comUser.getId());
                newComPoster.setPosterName(posterName);

                comPosterService.add(newComPoster);

                resultMap.put("poster", PropertiesUtils.getStringValue("index_user_poster_url") + newComPoster.getPosterName());
            }


            resultMap.put("appId", WxConsPF.APPID);
            resultMap.put("shareTitle", "麦链合伙人邀请");
            resultMap.put("shareDesc", contents[0]);
            resultMap.put("shareLink", shareLink);
            resultMap.put("shareImg", comUser.getWxHeadImg());

            //TODO

            mav.addObject("shareMap", resultMap);

        } catch (Exception e) {
            log.error("获取分享链接失败![skuId="+skuId+"]");
            e.printStackTrace();
        }

        return mav;
    }

    public Font loadFont(File file){
        Font font = new Font("华文细黑", Font.PLAIN, 20);
        try {
            FileInputStream fi = new FileInputStream(file);
            BufferedInputStream fb = new BufferedInputStream(fi);
            font = Font.createFont(Font.TRUETYPE_FONT, fb);
        }
        catch (Exception e) {
            log.error("读取本地字体文件出错了"+e);
            e.printStackTrace();
        }

        return font;
    }

    public static void main(String[] args) {
        // 创建日期对象
        Date d = new Date();
        // 给定模式
        int random = (int)((Math.random()*9+1)*100000);
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        // public final String format(Date date)
        String s = sdf.format(d);
        System.out.println(s);

    }

    /**
     * 画海报
     * @param bPath        海报背景图路径
     * @param qrcodePath   二维码背景图路径
     * @param saveFileName 保存名字
     */
    public void drawPost(String bPath, String qrcodePath, String headImgPath, String saveFileName, String[] contents) {
        ImageIcon bImgIcon = new ImageIcon(bPath);
        ImageIcon qrcodeImgIcon = new ImageIcon(qrcodePath);
        ImageIcon headImgIcon = new ImageIcon(headImgPath);
        Image bImage = bImgIcon.getImage();
        Image qrcodeImage = qrcodeImgIcon.getImage();
        Image headImage = headImgIcon.getImage();

        int width = bImage.getWidth(null) == -1 ? 904 : bImage.getWidth(null);
        int height = bImage.getHeight(null) == -1 ? 1200 : bImage.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.drawImage(headImage, 88, 619, 132, 132, null);
        g.drawImage(bImage, 0, 0, null);
        g.drawImage(qrcodeImage, 566, 776, 220, 220, null);

        g.setFont(new Font("华文细黑", Font.PLAIN, 32));
        g.setColor(new Color(51,51,51));
        //g.drawString(contents[0], 92, 785);

        for(int i=0; i<contents[0].length(); i++){
            int l = i%11;
            int t = i/11;
            g.drawString(contents[0].substring(i, i+1), 92+32*l, 785+40*t);
        }

        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String startTime = sdf.format(curDate);
        curDate.setDate(curDate.getDate()+30);
        String endDate = sdf.format(curDate);

        g.setFont(new Font("华文细黑", Font.PLAIN, 20));
        g.setColor(new Color(51, 51, 51));
        g.drawString("该二维码有效期为", 598, 1016);
        g.drawString(startTime+"-"+endDate, 575, 1046);
        g.dispose();

        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "png", imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());
            OSSObjectUtils.uploadFile( "static/user/poster/" + saveFileName, is);
        } catch (Exception e) {
            log.error("画海报出错了!");
            e.printStackTrace();
            return;
        }
    }

    /**
     * 画海报
     * @param bPath        海报背景图路径
     * @param qrcodePath   二维码背景图路径
     * @param saveFileName 保存名字
     */
    public void drawPost2(String bPath, String qrcodePath, String headImgPath, String saveFileName, String[] contents) {
        ImageIcon bImgIcon = new ImageIcon(bPath);
        ImageIcon qrcodeImgIcon = new ImageIcon(qrcodePath);
        ImageIcon headImgIcon = new ImageIcon(headImgPath);
        Image bImage = bImgIcon.getImage();
        Image qrcodeImage = qrcodeImgIcon.getImage();
        Image headImage = headImgIcon.getImage();

        int width = bImage.getWidth(null) == -1 ? 904 : bImage.getWidth(null);
        int height = bImage.getHeight(null) == -1 ? 1200 : bImage.getHeight(null);
        BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics2D g = bufferedImage.createGraphics();

        g.drawImage(headImage, 36, 880, 132, 132, null);
        g.drawImage(bImage, 0, 0, null);
        g.drawImage(qrcodeImage, 604, 874, 220, 220, null);

        g.setFont(new Font("华文细黑", Font.PLAIN, 28));
        g.setColor(new Color(51,51,51));
        //g.drawString(contents[0], 176, 905);

        for(int i=0; i<contents[0].length(); i++){
            int l = i%13;
            int t = i/13;
            g.drawString(contents[0].substring(i, i+1), 176+28*l, 905+40*t);
        }

        Date curDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy.MM.dd");
        String startTime = sdf.format(curDate);
        curDate.setDate(curDate.getDate()+30);
        String endDate = sdf.format(curDate);

        g.setFont(new Font("华文细黑", Font.PLAIN, 20));
        g.setColor(new Color(51, 51, 51));
        g.drawString("该二维码有效期为", 635, 1125);
        g.drawString(startTime+"-"+endDate, 610, 1165);
        g.dispose();

        try {
            ByteArrayOutputStream bs = new ByteArrayOutputStream();
            ImageOutputStream imOut = ImageIO.createImageOutputStream(bs);
            ImageIO.write(bufferedImage, "png", imOut);
            InputStream is = new ByteArrayInputStream(bs.toByteArray());
            OSSObjectUtils.uploadFile( "static/user/poster/" + saveFileName, is);
        } catch (Exception e) {
            log.error("画海报出错了!");
            e.printStackTrace();
            return;
        }
    }

}
