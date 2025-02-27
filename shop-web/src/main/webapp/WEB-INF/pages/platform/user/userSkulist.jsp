<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <link rel="stylesheet" href="<%=path%>/static/css/base.css">
    <link rel="stylesheet" href="<%=path%>/static/css/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/css/shangpin.css">
    <link rel="stylesheet" href="<%=path%>/static/css/header.css">
    <link rel="stylesheet" href="<%=path%>/static/css/loading.css">
</head>
<body>

<div class="wrap">
    <main>
        <header class="xq_header">
            <a href="javascript:window.location.replace('<%=basePath%>index')"><img src="<%=path%>/static/images/xq_rt.png" alt=""></a>
            <p>库存管理</p>
        </header>
        <div id="box">
            <div class="main">
                <div class="d_box">
                    <c:forEach items="${userProducts}" var="sku">
                        <h1>
                            <img src="<%=path%>/static/images/ip.png" alt="">
                            拿货方式：<span>平台代发货</span>
                        </h1>
                        <section class="sec2">
                            <p class="photo">
                                <img src="${sku.comSkuImage.fullImgUrl}" alt="">
                            </p>
                            <div>
                                <h2 id="sku1">${sku.name}</h2>
                                <h3>零售价：<span>￥${sku.priceRetail}</span></h3>
                                <p>在线库存：<span id="sku2">${sku.stock}</span>
                                     <c:if test="${sku.needStockNum>0}">
                                         <b>需进货${sku.needStockNum}件</b>
                                     </c:if>
                                    <c:if test="${sku.registerGiveSkuStock>0}">
                                        <b>（赠送库存为${sku.registerGiveSkuStock}件）</b>
                                    </c:if>
                                    </p>
                                <input type="hidden" id="pfuId" value="${sku.pfuId}">
                                <input type="text" id="skuId" value="${sku.id}" style="display: none">
                            </div>
                        </section>
                        <section class="sec3">
                                <p class="jianku"
                                   onclick="javascript:window.location.replace('<%=basePath%>product/user/applySkuInfo.list/?id=${sku.pfuId}');">
                                    <b>申请拿货</b></p>
                                    <%--<p class="jianku" onclick="clickShow()"><b>申请拿货</b></p>--%>
                                <%--<p class="buhuo"--%>
                                   <%--onclick="buhuokucun('${sku.name}','${sku.upperStock}','${sku.isQueue}','${sku.id}','${sku.userPid}')"><b>补货</b></p>--%>
                                 <p class="buhuo" onclick="clickShow()"><b>补货</b></p>
                        </section>
                    </c:forEach>
                </div>
            </div>
        </div>
        <div class="black">
            <div class="backb"></div>
                <div class="back_a">
                     <img src="${path}/static/images/ku.png" alt=""/>
                    <p>此功能仅支持在麦链合伙人APP使用</p>
                    <h1>
                        <span onclick="clickHide()">取消</span>
                        <span><a href="http://api.qc.iimai.com/static/html/wechatDownload.html" style="color:#fff;">去下载</a></span>
                    </h1>
                </div>
        </div>
        <div class="back">
        </div>
        <div class="back_b">
            <p>补货信息</p>
            <h4>商品:　　<span id="addsku"></span></h4>
            <input type="text" id="addSkuId" style="display: none">
            <input type="text" id="userPid" style="display: none">
            <%--<h4 id="xianshi">本次最多可补货数量:　　<span id="maxStock"></span></h4>--%>
            <h4>补货数量:　　
                <div>
                    <span class="jian">-</span>
                    <input type="tel" class="number" value="1"/>
                    <span class="jia">+</span>
                </div>
            </h4>
            <h4 class="queue">您的订单将进入排单期</h4>
            <div>
                <h1 class="b_qu">取消</h1>
                <h1 class="b_que">确定</h1>
            </div>
        </div>
        <%--<div class="back_applyInfo b" style="display:none;">--%>
            <%--<h4>亲，由于您是0元用户，系统不支持补货！</h4>--%>
            <%--<h1><span class="zhidao">知道了</span>--%>
            <%--</h1>--%>
        <%--</div>--%>
    </main>
</div>
<script src="<%=path%>/static/js/jquery/jquery-1.8.3.min.js"></script>
<script src="<%=path%>/static/js/commonAjax.js"></script>
<script src="<%=path%>/static/js/definedAlertWindow.js"></script>
<script src="<%=path%>/static/plugins/zepto.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="<%=path%>/static/js/hideWXShare.js"> </script>
<script>
    var i = 1;
    $(".jia").on("tap", function () {
        i++;
        $(".number").val(i)
    })
    $(".number").on("change", function () {
        i = $(this).val();
    })
    $(".jian").on("tap", function () {
        if (i == 1) {
            return false;
        }
        i--;
        $(".number").val(i)
    })
    function buhuokucun(a, b, c, d,e) {
        $(".number").val(1);
        i=1;
        $(".queue").hide();//init
        $("#addsku").html(a);
        /*$("#maxStock").html(b);*/
        if (c == 1) { //进入排单
            $(".queue").show();
//            $("#xianshi").hide();
        }
        $("#addSkuId").val(d);
        $("#userPid").val(e);
        $(".back").css("display", "-webkit-box");
        $(".back_b").show();
    }
    $(".b_qu").on("tap", function () {
        $(".back").css("display", "none");
        $(".back_b").hide();
    })
    function clickShow(){
        $(".black").show();
    }
    function clickHide(){
        $(".black").hide();
    }
//    function checkIsAddStock(){
//            $(".back_applyInfo").show();
//            $(".back").show();
//    }
//    $(".zhidao").on("click", function () {
//        $(this).parents(".b").hide();
//        $(".back").hide();
//    })
    $(".b_que").on("tap", function () {
        i = $(".number").val();
        if (i<=0) {
            alert("数量不能低于0件！");
            return;
        }
        var paraData = "?";
        paraData += "&skuId=" + $("#addSkuId").val();
        paraData += "&quantity=" + i;
        // 检查库存
        var paramData = {};
        paramData.skuId = $("#addSkuId").val();
        paramData.stock = i;
        paramData.pUserId = $("#userPid").val();
        $.ajax({
        url: '<%=basePath%>product/checkStock.do',
        type: 'GET',
        data: paramData,
        dataType: 'json',
            success: function (data) {
                if(data.isError==false){
                    if(data.stockStatus==2){
                        alert("库存不足，不可下单！");
                        return;
                    }else{
                        window.location.href = "<%=basePath%>BOrderAdd/supplementBOrder.shtml" + paraData;
                    }
                }
            }
        });
    })
</script>
</body>
</html>