<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <link rel="stylesheet" href="<%=basePath%>static/css/main.css">
    <link rel="stylesheet" href="<%=basePath%>static/css/reset.css">
    <link rel="stylesheet" href="<%=basePath%>static/css/yunfeishezhi.css">
</head>
<body>
<header>
    <a href="javascript:window.location.replace('<%=basePath%>shop/manage/index');"><img src="<%=basePath%>static/images/xq_rt.png" alt=""></a>
    <p>运费设置</p>
</header>

    <div class="wrap">
        <form id="freightForm" action="<%=basePath%>shop/manage/updateShop" method="post">
            <input type="hidden" name="id" value="${sfShop.id}">
            <input type="hidden" name="shipAmount" value="${sfShop.shipAmount}">
        <div>
            <p><span>运费设置</span></p>
            <h1>
                <p class="btn <c:if test="${sfShop.shipAmount <= 0}">active</c:if>"><input type="hidden" name="shipAmount0" value="0"><label for="">包邮</label></p>
                <p class="btn <c:if test="${sfShop.shipAmount > 0}">active</c:if>"><input type="text" name="shipAmount1" value="${sfShop.shipAmount}" placeholder="　　请输入金额"></p>
            </h1>
        </div>
        <button type="submit" id="save">保存</button>
        </form>
    </div>

</body>
<script src="<%=basePath%>static/js/jquery/jquery-1.8.3.min.js"></script>
<script>
    $(".btn").on("click",function(){
        $(this).addClass("active").siblings().removeClass("active");
    });
    $('#freightForm').submit(function(){
        $('input[name="shipAmount"]').val($('p.btn.active').children('input').val());
        return true;
    });
</script>
</html>