package com.masiis.shop.admin.controller.certificate;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.masiis.shop.admin.service.certificate.CertificateService;
import com.masiis.shop.dao.beans.certificate.CertificateInfo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by JingHao on 2016/3/7 0007.
 */
@Controller
@RequestMapping("/certificate")
public class CertificateController {

    @Resource
    private CertificateService certificateService;


    @RequestMapping("/certificate_list.shtml")
    public String list(HttpServletRequest request, HttpServletResponse response) {
        return "certificate/certificate_list";
    }
    @RequestMapping("list.do")
    @ResponseBody
    public Object list(HttpServletRequest request, HttpServletResponse response,
                       String beginTime,
                       String endTime,
                       String ctName,
                       String mobile,
                       String agentLevel,
                       String sort,
                       String order,
                       Integer offset,
                       Integer limit
    )throws Exception {
        offset = offset==null ? 0 : offset;
        limit  = limit ==null ? 10 : limit;
        Integer pageNo = offset/limit + 1;
        PageHelper.startPage(pageNo, limit);
        Map<String, Object> searchParam = new HashMap<>();//组合搜索
        searchParam.put("beginTime",beginTime);
        searchParam.put("endTime",endTime);
        searchParam.put("ctName",ctName);
        searchParam.put("mobile",mobile);
        searchParam.put("agentLevel",agentLevel);
        List<CertificateInfo> certificateInfoList = certificateService.getCertificates(searchParam);
        PageInfo<CertificateInfo> pageInfo = new PageInfo<>(certificateInfoList);
        Map<String, Object> pageMap = new HashMap<>();
        pageMap.put("total", pageInfo.getTotal());
        pageMap.put("rows", certificateInfoList);
        return pageMap;
     }

    @RequestMapping("/updateUpper.do")
    @ResponseBody
    public String updateUpperById(HttpServletRequest request, HttpServletResponse response, String id, String pid){
        try {
            certificateService.updateUpperPartner(id, pid);
        } catch (Exception e) {
            e.printStackTrace();
            return "更改失败";
        }
        return "更改成功";
    }

    @RequestMapping("findById.do")
    @ResponseBody
    public String findById(HttpServletRequest request, HttpServletResponse response,Integer id){
        return  certificateService.findById(id);
    }
    }
