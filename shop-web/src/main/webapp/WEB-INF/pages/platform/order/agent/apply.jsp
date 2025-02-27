<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <%@ include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/shenqing.css">
</head>
<body>
<div class="fakeloader"></div>
<div class="wrap">
    <header class="xq_header">
        <a href="${basePath}product/skuDetails.shtml?skuId=${skuId}">
            <img src="${path}/static/images/xq_rt.png" alt=""></a>
        <p>合伙人申请</p>
    </header>
    <c:if test="${isQueuing==true}">
        <p class="row">本次订单将进入排单期，在您前面有<span>${count}</span>人排单。</p>
    </c:if>
    <main>
        <div class="he">
            <h1><img src="${path}/static/images/shenqing_2.png" alt="">成为合伙人后，您将获得以下特权</h1>
        </div>
        <section class="sec1">
            <img src="${path}/static/images/shenqing_3.png" alt="">
            <div>
                <h2>独立店铺</h2>
                <p>拥有自己的独立店铺进行推广、销售产品</p>
            </div>
            <%--<img src="${path}/static/images/down.png" alt="" class="down">--%>
        </section>
        <%--<div>--%>
        <%--<p></p>--%>
        <%--<h1>--%>
        <%--<span>还没有数据</span>--%>
        <%--<span>还没有数据</span>--%>
        <%--</h1>--%>
        <%--</div>--%>
        <section class="sec1">
            <img src="${path}/static/images/shenqing_4.png" alt="">
            <div>
                <h2>寻找合伙人</h2>
                <p>寻找您的下级合伙人，赚取差价</p>
            </div>
        </section>
        <section class="sec1">
            <img src="${path}/static/images/shenqing_5.png" alt="">
            <div>
                <h2>推广渠道</h2>
                <p>平台提供多样的推广渠道编辑推广自己商品</p>
            </div>
        </section>
        <section class="sec1">
            <img src="${path}/static/images/shenqing_6.png" alt="">
            <div>
                <h2>团队管理</h2>
                <p>提供完善的售后和团队管理工具</p>
            </div>
        </section>
        <%--<section class="sec1">--%>
        <%--<img src="${path}/static/images/shenqing_7.png" alt="">--%>
        <%--<div>--%>
        <%--<h2>平台补助</h2>--%>
        <%--<p style="margin-right:10px;">消费者分享商品可获得佣金，佣金来自平台补助</p>--%>
        <%--</div>--%>
        <%--</section>--%>
        <section class="sec1">
            <img src="${path}/static/images/shouquan.png" alt="">
            <div>
                <h2>授权证书</h2>
                <p style="margin-right:10px;">官方的授权证书证明你的身份</p>
            </div>
        </section>
        <input id="skuId" value="${skuId}" style="display: none"/>
        <%--<input id="skipPageId" value="register" style="display: none"/>--%>
        <input id="type" value="${type}" style="display: none"/>
    </main>
    <div class="biao">
        <h1><img src="${path}/static/images/shenqing_list.png" alt=""><b>申请条件</b></h1>
        <table>
            <tr>
                <td>申请条件</td>
                <td>是否完成</td>
                <td>任务入口</td>
            </tr>
            <tr>
                <c:choose>
                    <c:when test="${user.isBinding==1}">
                        <td>绑定手机号</td>
                        <td><img src="${path}/static/images/dui.png" alt=""></td>
                        <td>已完成</td>
                    </c:when>
                    <c:otherwise>
                        <td>绑定手机号</td>
                        <td><img src="${path}/static/images/cuo.png" alt=""></td>
                        <td><a href="javascript:void(0);" onclick="validateCodeJS.applyTrial('agent')"
                               style="color: #FF5200;text-decoration: underline">去完成</a></td>
                    </c:otherwise>
                </c:choose>
            </tr>
            <tr>
                <td>实名认证</td>
                <c:choose>
                    <c:when test="${user.auditStatus == 2}">
                        <td><img src="${path}/static/images/dui.png" alt=""></td>
                        <td>已完成</td>
                    </c:when>
                    <c:otherwise>
                        <td><img src="${path}/static/images/cuo.png" alt=""></td>
                        <c:if test="${user.auditStatus == 1}">
                            <td><span style="color: #FF5200">审核中</span></td>
                        </c:if>
                        <c:if test="${user.auditStatus == 3}">
                            <td><a href="javascript:void(0);" onclick="reSubmitIdentityAuth();"
                                   style="color: #FF5200;text-decoration: underline">重新提交</a></td>
                        </c:if>
                        <c:if test="${user.auditStatus == 0}">
                            <td><a href="javascript:void(0);" onclick="goVerified();"
                                   style="color: #FF5200;text-decoration: underline">去完成</a></td>
                        </c:if>
                    </c:otherwise>
                </c:choose>
            </tr>
            <%--<tr>--%>
            <%--<td>关注公众号</td>--%>
            <%--<c:choose>--%>
            <%--<c:when test="${isUserForcus==true}">--%>
            <%--<td><img src="${path}/static/images/dui.png" alt=""></td>--%>
            <%--<td>已完成</td>--%>
            <%--</c:when>--%>
            <%--<c:otherwise>--%>
            <%--<td><img src="${path}/static/images/cuo.png" alt=""></td>--%>
            <%--<td><a href="javascript:void(0);" onclick="goGuanZhu();"--%>
            <%--style="color: #FF5200;text-decoration: underline">去完成</a>--%>
            <%--</td>--%>
            <%--</c:otherwise>--%>
            <%--</c:choose>--%>
            <%--</tr>--%>
        </table>
    </div>
    <section class="sec2">
        <p><a id="goToNext" href="javascript:void(0);">继续</a></p>
    </section>
</div>
<div class="paidanqi">
    <div class="back_q">
        <h1>什么是排单期？</h1>
        <p>
            由于商品过于火爆，导致库存量不足。申请合伙人或补货我们将记录付款的先后顺序，待产能提升，麦链商城将按照付款顺序发货
        </p>
        <button class="kNow">我知道了</button>
    </div>
    <div class="Modal"></div>
</div>
<div class="back"></div>
<div class="back_j">
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
<div class="back_f">
    <span class="close">×</span>
    <img src="${path}/static/images/b.png" alt="">
</div>
<div id="realNameVerifyDiv" class="back_login" style="display:none;">
    <p>您的账户还未通过实名认证,无法继续申请合伙人,请去认证!</p>
    <h1><span id="quxiao">取消</span><span id="goMark" onclick="goVerified(this);">去认证</span></h1>
</div>
</body>
<%--<%@ include file="/WEB-INF/pages/common/foot.jsp" %>--%>
<script src="${path}/static/js/jquery-1.8.3.min.js"></script>
<script src="${path}/static/js/definedAlertWindow.js"></script>
<%--<script src="${path}/static/js/commonAjax.js"></script>--%>
<script src="${path}/static/js/iscroll.js"></script>
<script src="${path}/static/js/validateCode.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${path}/static/js/hideWXShare.js"></script>
<script>
    $(document).ready(function () {
        validateCodeJS.initPage();
    });
    function goVerified() {
        if (${user.isBinding!=1}) {
            alert("请先绑定手机号");
            return;
        }
        window.location.href = "${path}/identityAuth/toInentityAuthPage.html?skuId=${skuId}&returnPageIdentity=1&auditStatus=0";
    }
    function reSubmitIdentityAuth() {
        if (${user.isBinding!=1}) {
            alert("请先绑定手机号");
            return false;
        }
        window.location.href = "${path}/identityAuth/toInentityAuthPage.html?skuId=${skuId}&returnPageIdentity=1&auditStatus=3";
    }

    function goGuanZhu() {
        if (${user.isBinding!=1}) {
            alert("请先绑定手机号");
            return;
        }
        if (${user.auditStatus==0}) {
            alert("请先实名认证");
            return;
        }
        $(".back_f").show();
        $(".back").show();
    }

    $("#goToNext").on("click", function () {
        var isBinding = "${user.isBinding}";
        var auditStatus = "${user.auditStatus}";
        if (isBinding == 0) {
            alert("请绑定手机号");
            return;
        }
        if (auditStatus == 1) {
            alert("您的实名认证正在审核中,请耐心等候!");
            return;
        } else if (auditStatus == 0) {
            $("#realNameVerifyDiv").show();
            $(".back").show();
            return;
        } else if (auditStatus == 3) {
            alert("您的实名认证未通过,请重新提交!");
            return;
        }
        <%--if (${isUserForcus==false}) {--%>
        <%--alert("请去完成关注公众号!");--%>
        <%--return;--%>
        <%--}--%>
        $(this).html("请稍后...");
        window.location.href = "${path}/userApply/register.shtml?skuId=${skuId}";

    });

    $("#quxiao").on("click", function () {
        $("#realNameVerifyDiv").hide();
        $(".back").hide();
    });

    $(".down").toggle(function () {
        $(this).attr("src", "${path}/static/images/top.png")
        $(this).parent().css("borderBottom", "none")
        $(this).parent().next().css("display", "-webkit-box");
    }, function () {
        $(this).attr("src", "${path}/static/images/down.png")
        $(this).parent().next().hide();
        $(this).parent().css("borderBottom", "1px solid #f6f6f6")
    })
    $(".row").on("click", function () {
        $(".paidanqi").show();
    });
    $(".kNow").on("click", function () {
        $(".paidanqi").hide();
    });
</script>
</html>