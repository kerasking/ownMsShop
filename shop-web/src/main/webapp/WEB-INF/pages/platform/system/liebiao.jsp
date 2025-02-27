<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>

<%
String path = request.getContextPath();
String basePath = request.getScheme()+"://"+request.getServerName()+":"+request.getServerPort()+path+"/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"> 
    <title>麦链合伙人</title>
    <link rel="stylesheet" href="<%=path%>/static/css/header.css">
    <link rel="stylesheet" href="<%=path%>/static/css/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/css/base.css">
    <link rel="stylesheet" href="<%=path%>/static/plugins/swipwr/swiper.3.1.7.min.css">
    <script src="<%=path%>/static/js/jquery-1.8.3.min.js"></script>
    <script src="<%=path%>/static/plugins/swipwr/swiper.3.1.7.min.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="<%=path%>/static/js/hideWXShare.js"></script>
    <style>
        /* Generated by less 2.5.1 */
        .wrap {
            width: 100%;
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            overflow: auto;
            position: relative;
        }
        .wrap .box {
            width: 100%;
            height: 100%;
        }
        .wrap .box .banner {
            width: 100%;
        }
        .wrap .box .banner img {
            display: block;
            width: 100%;
        }
        .wrap .box .all {
            display: -webkit-box;
            background: white;
            padding: 0 10px;
            -webkit-box-align: center;
        }
        .wrap .box .all > img {
            display: block;
            width: 15px;
            height: 15px;
        }
        .wrap .box .all p {
            line-height: 42px;
            text-indent: 10px;
            -webkit-box-flex: 1;
        }
        .wrap .box .all p > img {
            float: right;
            display: block;
            width: 10px;
            margin-top: 12px;
        }
        .wrap .box .main {
            width: 100%;
            -webkit-box-flex: 1;
            margin-top: 10px;
        }
        .wrap .box .main .title {
            background: white;
            padding: 10px 10px;
            text-indent: 10px;
            border-bottom: 1px solid #DFE0E1;
        }
        .wrap .box .main .title img {
            display: block;
            width: 15px;
            float: left;
        }
/*
        .wrap .box .main .sec1 {
            width: 100%;
            background: white;
            display: -webkit-box;
            -webkit-box-align: center;
            display: -moz-box;
            -moz-box-align: center;
            padding: 5px 0;
        }
        .wrap .box .main .sec1 .photo {
            width: 90px;
            margin-left: 5px;
        }
        .wrap .box .main .sec1 .photo img {
            display: block;
            width: 90px;
            height: 90px;
            border: 1px solid #ddd;
        }
        .wrap .box .main .sec1 div {
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-orient: vertical;
            display: -moz-box;
            -moz-box-pack: center;
            -moz-box-orient: vertical;
            margin-left: 10px;
        }
        .wrap .box .main .sec1 div h2 {
            padding-bottom: 5px;
        }
        .wrap .box .main .sec1 div h3 {
            font-size: 12px;
            color: #666;
        }
        .wrap .box .main .sec1 div h3:nth-child(2) {
            border-bottom: 1px solid #f6f6f6;
            padding-bottom: 4px;
        }
        .wrap .box .main .sec1 div h3:nth-child(2) b {
            float: right;
            color: #999;
            font-size: 12px;
            margin-right: 10px;
        }
        .wrap .box .main .sec1 div h3:nth-child(3) {
            padding-top: 4px;
            height: 18px;
        }
        .wrap .box .main .sec1 div h3 span{
            font-size: 12px;;
        }
        .wrap .box .main .sec1 + h2 {
            padding: 10px 10px;
            background: #fff;
            border-top: 1px solid #f6f6f6;
            font-size: 12px;
            margin-bottom: 5px;
            color: #999;
        }
        .wrap .box .main .sec1 + h2 span {
            font-size: 12px;
        }
        .wrap .box .main .sec1 + h2 button {
            float: right;
            padding: 4px 15px;
            border-radius: 3px;
            color: #fff;
            background: #ff5200;
            margin-top: -3px;
        }
        .wrap .box .main .sec1 + h2 button.btn{
            float: right;
            padding: 2px 0px;
            border-radius: 3px;
            color: #f74a11;
            background: transparent;
            margin-top: -1px;
        }

*/
        .wrap .box .main .sec1 {
            width: 100%;
            background: white;
            display: -webkit-box;
            -webkit-box-align: center;
            display: -moz-box;
            -moz-box-align: center;
            padding: 5px 0;
            margin-bottom: 5px;
        }
        .sec1 .photo {
            width: 120px;
            margin-left: 5px;
        }
        .sec1 .photo img {
            display: block;
            width: 120px;
            height: 120px;
            border: 1px solid #f6f6f6;
        }
         .sec1 div {
            -webkit-box-flex: 1;
            -moz-box-flex: 1;
            overflow: hidden;
            display: -webkit-box;
            -webkit-box-pack: center;
            -webkit-box-orient: vertical;
            display: -moz-box;
            -moz-box-pack: center;
            -moz-box-orient: vertical;
            margin-left: 10px;
        }
        .sec1 div h3 {
            font-size: 12px;
            color: #999;
        }
        .sec1 div h3 span{
            font-size: 12px;
            color: #999;
        }
        .sec1 div h3.three {
            border-bottom: 1px solid #f6f6f6;
            padding-bottom: 4px;
            margin-top: 9px;
        }
        .hid{
            height: 18px;
        }
        .sec1 div h3:nth-child(4) b {
            float: right;
            color: #999;
            font-size: 12px;
            margin-right: 10px;
        }
        .sec1 div h3:nth-child(3) {
            /* padding-top: 4px; */
            height: 18px;
            margin-top: -3px;
        }
        .sec1 div> h2:last-child{
            background: #fff;
            border-top: 1px solid #f6f6f6;
            font-size: 12px;
            /* margin-bottom: 5px; */
            color: #999;
            padding: 10px 10px 0 0;
        }
        .sec1 div> h2:last-child span {
            font-size: 12px;
        }
        .sec1 div> h2:last-child button {
            float: right;
            padding: 2px 15px;
            border-radius: 3px;
            color: #fff;
            background: #ff5200;
            margin-top: -1px;
        }
        .sec1 div> h2:last-child button.btn{
            float: right;
            padding: 2px 0px;
            border-radius: 3px;
            color: #f74a11;
            background: transparent;
            margin-top: -1px;
            background: url("/static/images/next.png") no-repeat 100%;
            background-size: 7px 13px;
            padding-right: 16px;
        }
        .sec1 div h2 {
            display: block;
            white-space:nowrap;
            overflow:hidden;
            text-overflow:ellipsis;
            padding-right: 15px;
        }
    </style>
</head>
<body>
    <div class="wrap">
        <div class="box">
                <header class="xq_header" style="margin-bottom:0;">
                        <a href="<%=path%>/marketGood/market"><img src="<%=path%>/static/images/xq_rt.png" alt=""></a>
                        <p>所有商品</p>
                    </header>
                <main><div class="main"><c:forEach items="${indexComSkus}"  var="Sku">
                    <a href="<%=path%>/product/skuDetails.shtml?skuId=${Sku.id}">
                        <%--<section class="sec1">
                            <p class="photo">
                                <img src="${Sku.imgUrl}" alt="">
                            </p>
                            <div>
                                <h2>${Sku.comSku.name}</h2>
                                <h3><span style="margin-right:10px;font-size:14px;color:red">￥${Sku.comSku.priceRetail}</span>
                                    <b>${Sku.discountLevel}</b>
                                </h3>
                                <c:if test="${Sku.isTrial==1}"><h3>试用费：<span>${Sku.shipAmount}</span>元</h3></c:if>
                                <h3>保证金：<span>${Sku.bailLevel}</span>元</h3>
                            </div>
                        </section>
                        <h2>
                            <c:if test="${Sku.agentNum>=9999}">超过</c:if><span>${Sku.agentNum}</span>人合伙
                            <c:if test="${empty Sku.uid}"><button>我要合伙</button></c:if>
                            <c:if test="${not empty Sku.uid}"><button class="btn">您已合伙</button></c:if>
                        </h2>--%>
                            <section class="sec1">
                                <p class="photo">
                                    <img src="${Sku.imgUrl}" alt="">
                                </p>
                                <div>
                                    <h2>${Sku.comSku.name}</h2>
                                    <h3 class="hid"> <c:if test="${Sku.isTrial==1}">试用费：<span>${Sku.shipAmount}</span>元</c:if></h3>
                                    <h3>保证金：<span>${Sku.bailLevel}</span>元</h3>
                                    <h3 class="three"><span style="margin-right:10px;font-size:14px;color:red">￥${Sku.comSku.priceRetail}</span>
                                        <b>${Sku.discountLevel}</b>
                                    </h3>

                                    <h2>
                                        <c:if test="${Sku.agentNum>=9999}">超过</c:if><span>${Sku.agentNum}</span>人合伙
                                        <c:if test="${empty Sku.uid}">
                                            <button>我要合伙</button>
                                        </c:if>
                                        <c:if test="${not empty Sku.uid}">
                                            <button class="btn">您已合伙</button>
                                        </c:if>
                                    </h2>
                                </div>
                            </section>
                    </a>
                </c:forEach></div>
                       <!--<section class="sec1">-->
                        <!--<div>-->
                            <!--<a href="xiangqing.html"><img src="<%=path%>/static/images/cp_1.png" alt=""></a>-->
                            <!--<h2>抗引力—快速瘦脸精华</h2>-->
                            <!--<h1>￥328 <span>成为合伙人可查看</span></h1>-->
                            <!--<h3>-->
                                <!--<p>超过<span>9999</span>人代理</p>-->
                                <!--<a href="shenqing.html" class="he">我要合伙</a>-->
                                <!--<a href="javascript:;" class="ok">您已合伙</a>-->
                            <!--</h3>-->
                        <!--</div>-->
                        <!--<div>-->
                            <!--<a href="xiangqing.html"><img src="<%=path%>/static/images/cp_1.png" alt=""></a>-->
                            <!--<h2>抗引力—快速瘦脸精华</h2>-->
                            <!--<h1>￥328 <span>成为合伙人可查看</span></h1>-->
                            <!--<h3>-->
                                <!--<p>超过<span>9999</span>人代理</p>-->
                                <!--<a href="shenqing.html" class="he">我要合伙</a>-->
                                <!--<a href="javascript:;" class="ok">您已合伙</a>-->
                            <!--</h3>-->
                        <!--</div>-->
                    <!--</section>-->
                 </main>
        </div>
    </div>
</body>
</html>