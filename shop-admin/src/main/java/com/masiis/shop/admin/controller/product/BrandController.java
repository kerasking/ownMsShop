package com.masiis.shop.admin.controller.product;

import com.alibaba.druid.support.logging.Log;
import com.alibaba.druid.support.logging.LogFactory;
import com.github.pagehelper.StringUtil;
import com.masiis.shop.admin.service.product.BrandService;
import com.masiis.shop.common.util.OSSObjectUtils;
import com.masiis.shop.common.util.PropertiesUtils;
import com.masiis.shop.dao.po.ComBrand;
import com.masiis.shop.dao.po.PbUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Date;
import java.util.Map;

/**
 * Created by cai_tb on 16/5/17.
 */
@Controller
@RequestMapping("/brand")
public class BrandController {

    private final static Log log = LogFactory.getLog(BrandController.class);

    @Resource
    private BrandService brandService;

    @RequestMapping("/list.shtml")
    public String list(){
        return "brand/list";
    }

    @RequestMapping("/add.shtml")
    public String add(){
        return "brand/add";
    }

    @RequestMapping("/edit.shtml")
    public String edit(HttpServletRequest request, HttpServletResponse response,
                       Model model,
                       Integer brandId){
        ComBrand comBrand = brandService.find(brandId);
        model.addAttribute("brand", comBrand);

        return "brand/edit";
    }

    @RequestMapping("/add.do")
    @ResponseBody
    public String add(HttpServletRequest request, HttpServletResponse response,
                      ComBrand comBrand,
                      @RequestParam("logoName")String logoName,
                      @RequestParam("illustratingPictureName")String illustratingPictureName,
                      @RequestParam("brandPosterName")String brandPosterName
                      ) throws FileNotFoundException{

        String realPath = request.getServletContext().getRealPath("/");
        realPath = realPath.substring(0, realPath.lastIndexOf("/"));

        try {
            PbUser pbUser = (PbUser)request.getSession().getAttribute("pbUser");
            if(pbUser != null) {
                log.info("保存品牌-开始准备comBrand数据");
                comBrand.setCreateTime(new Date());
                comBrand.setLogoUrl(PropertiesUtils.getStringValue("oss.BASE_URL") + "/static/brand/"+logoName);
                comBrand.setIllustratingPictureName(illustratingPictureName);
                comBrand.setBrandPosterName(brandPosterName);
                log.info("保存品牌-comBrand数据[comBrand=" + comBrand + "]");
            }

            brandService.add(comBrand);

            return "success";
        } catch (Exception e) {
            log.error("添加品牌失败![comBrand="+comBrand+"]");
            e.printStackTrace();
        }

        return "error";
    }

    @RequestMapping("/update.do")
    @ResponseBody
    public String update(HttpServletRequest request, HttpServletResponse response,
                      ComBrand comBrand,
                      @RequestParam(value = "logoName", required = false)String logoName,
                      @RequestParam(value = "illustratingPictureName", required = false)String illustratingPictureName,
                      @RequestParam(value = "brandPosterName", required = false)String brandPosterName
    ) throws FileNotFoundException{

        String realPath = request.getServletContext().getRealPath("/");
        realPath = realPath.substring(0, realPath.lastIndexOf("/"));

        try {
            PbUser pbUser = (PbUser)request.getSession().getAttribute("pbUser");
            log.info("保存品牌-开始准备comBrand数据");
            if(pbUser != null && StringUtils.isNotBlank(logoName)) {
                comBrand.setLogoUrl(PropertiesUtils.getStringValue("oss.BASE_URL") + "/static/brand/"+logoName);
            }
            if(pbUser != null && StringUtils.isNotBlank(illustratingPictureName)) {
                comBrand.setLogoUrl(PropertiesUtils.getStringValue("oss.BASE_URL") + "/static/brand/"+illustratingPictureName);
            }
            if(pbUser != null && StringUtils.isNotBlank(brandPosterName)) {
                comBrand.setLogoUrl(PropertiesUtils.getStringValue("oss.BASE_URL") + "/static/brand/"+brandPosterName);
            }
            log.info("保存品牌-comBrand数据[comBrand=" + comBrand + "]");


            if(StringUtil.isEmpty(comBrand.getContent())) comBrand.setContent(null);

            brandService.update(comBrand);

            return "success";
        } catch (Exception e) {
            log.error("添加品牌失败![comBrand="+comBrand+"]");
            e.printStackTrace();
        }

        return "error";
    }

    @RequestMapping("/list.do")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                       ComBrand comBrand,
                       Integer pageNumber,
                       Integer pageSize
    ){
        Map<String, Object> pageMap = brandService.list(pageNumber, pageSize, comBrand);

        return pageMap;
    }
}
