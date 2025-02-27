<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<META   HTTP-EQUIV="pragma"   CONTENT="no-cache">
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<%
    //设置缓存为空
    response.setHeader("Pragma","No-cache");
    response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",   0);
%>
<%
    if(request.getProtocol().compareTo("HTTP/1.0")==0)
        response.setHeader("Pragma","no-cache");
    else   if(request.getProtocol().compareTo("HTTP/1.1")==0)
        response.setHeader("Cache-Control","no-cache");
    response.setDateHeader("Expires",0);
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta http-equiv="pragma" content="no-cache">
    <meta http-equiv="cache-control" content="no-cache">
    <meta http-equiv="expires" content="0">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>

    <%@ include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/gerenxinxi.css">
</head>
<body>
<div class="wrap">
    <header class="xq_header">
        <a href="<%=path%>/account/home" ><img src="${path}/static/images/xq_rt.png" alt=""></a>
        <p>个人信息</p>
    </header>
    <main>
        <%--<div class="head_img" style="background: url('${path}/static/images/gerenxinxibeijing.png');background-size: 100% 100%">--%>
            <%--<p><img src="${comUser.wxHeadImg}" alt=""></p>--%>
            <%--<div><h1>${comUser.wxNkName}</h1>--%>
                <%--<h1>--%>
                    <%--<c:if test="${pfSkuAgentDetails!=null}">--%>
                        <%--<c:forEach items="${pfSkuAgentDetails}" var="skuAgentDetail">--%>
                            <%--<span><img src="${agentLevelIConUrl}${skuAgentDetail.pfSkuAgent.icon}" alt=""></span>--%>
                        <%--</c:forEach>--%>
                    <%--</c:if>--%>
                <%--</h1>--%>
            <%--</div>--%>
        <%--</div>--%>
        <div id="bindPhoneId" class="sec1" style="background:white url('${path}/static/images/people.png') no-repeat 10px;  background-size: 13px;margin-top: 10px">
            <p>手机号</p>
            <c:choose>
                <c:when test="${comUser.mobile == null || comUser.mobile == ''}">
                    <p><b>点击绑定</b><img src="${path}/static/images/next.png" style="margin-top: 4px;"/></p>
                </c:when>
                <c:otherwise>
                    <p>${comUser.mobile}</p>
                </c:otherwise>
            </c:choose>
            <input id="mobileId" style="display: none" value="${comUser.mobile}" />
        </div>
        <div class="sec1" style="background:white url('${path}/static/images/weixin.png') no-repeat 10px;  background-size: 20px; margin-bottom: 10px;">
            <p>微信号</p>
            <p>已绑定 ${comUser.wxId}</p>
        </div>
        <div class="sec1" id = "identityAuthId"  style="background:white url('${path}/static/images/name.png') no-repeat 10px;  background-size: 20px;">
            <p>实名认证</p>
            <p><b>${auditStatusName}</b><img src="${path}/static/images/next.png" alt="" style="margin-top:4px"></p>
            <input id="auditStatusId" style="display: none" value="${comUser.auditStatus}" />
        </div>
        <%--<div class="sec1" id="capitalId" style="background:white url('${path}/static/images/qianban.png') no-repeat 10px;  background-size: 20px;margin-bottom: 0;">--%>
            <%--<p>我的资金</p>--%>
            <%--<p><b>可提现 ￥${comUserAccount.extractableFee}</b><img src="${path}/static/images/next.png" alt="" style="    margin-top:4px"></p>--%>
        <%--</div>--%>
        <div class="sec1" id="bankCardId" style="background:white url('${path}/static/images/yinh.png') no-repeat 10px;  background-size: 20px;margin-bottom: 10px;">
            <p>我的银行卡</p>
            <p><img src="${path}/static/images/next.png" alt=""></p>
        </div>
        <div  id="addressManageId" class="sec1" style="background:white url('${path}/static/images/address.png') no-repeat 10px;  background-size: 20px;">
            <p>地址管理</p>
            <p><img  src="${path}/static/images/next.png" alt=""></p>
        </div>

        <div class="back_j" style="display: none">
            <span class="close">×</span>
            <p class="biao">绑定手机号</p>
            <div>
                <p>手机号：<input type="tel" class="phone" id="phoneId"></p>
            </div>
            <div class="d">
                <p>验证码：<input type="tel" id="validateNumberDataId">
                    <button id="validateNumberId">获取验证码</button>
                </p>
            </div>
            <p class="tishi" id="errorMessageId"></p>
            <h1 class="j_qu" id="nextPageId">下一步</h1>
        </div>
        <div class="back"></div>
    </main>
</div>
</body>
<%@ include file="/WEB-INF/pages/common/foot.jsp" %>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${path}/static/js/hideWXShare.js"></script>
<script src="${path}/static/js/validateCode.js"></script>
<script type="text/javascript" src="${path}/static/js/personalInfo.js"></script>
</html>