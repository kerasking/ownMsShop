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
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <link rel="stylesheet" href="<%=path%>/static/css/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/css/main.css">
    <link rel="stylesheet" href="<%=path%>/static/css/commodity.css">
    <link rel="stylesheet" href="<%=path%>/static/css/alert.css"/>
</head>
<body>
<div class="wrap">
    <header>
        <a href="javascript:window.location.replace('<%=basePath%>shop/manage/index')"><img src="<%=path%>/static/shop/images/xq_rt.png" alt=""></a>
        <p>商品管理</p>
    </header>
    <p class="header">由于微信支付原因，麦链平台暂时关闭店主发货功能</p>
    <div class="nav">
        <p>
            <span class="on" onclick="onsale()">出售中</span>
            <span onclick="outSale()">仓库中</span>
        </p>
<%--        <div>
            <span>筛选条件：</span>
            <label class="goods">
                <b></b>
                <select id="goods" class="search">
                    <option value="" selected="selected">全部</option>
                    <option value="0">平台发货</option>
                    <option value="1">店主发货</option>
                </select>
            </label>
        </div>--%>
    </div>
    <main>
        <div class="floor">
            <c:forEach items="${skuInfoList}" var="sku">
                <div class="sec1">
                    <c:if test="${sku.isOwnShip==0}">
                        <h1><img src="<%=path%>/static/images/commodity.png" alt=""><b>平台发货</b></h1>
                    </c:if>
                    <c:if test="${sku.isOwnShip==1}">
                        <h1><img src="<%=path%>/static/images/commodity2.png" alt=""><b>店主发货</b></h1>
                    </c:if>
                    <div>
                        <p><img src="${sku.comSkuImageUrl}" alt=""></p>
                        <div>
                            <h1>${sku.skuName}</h1>
                            <p style="color: #ff5200;">￥${sku.priceRetail}</p>
                            <p>已售：<span style="margin-right: 5px;">${sku.saleNum}</span>
                                库存: <span>${sku.stock}</span>
                                <c:if test="${sku.stock==0}">
                                    <span style="color: red;">已售磬,请补货</span>
                                </c:if>
                            </p>
                        </div>
                    </div>
                    <ul>
                        <c:if test="${sku.isOwnShip==0 && not empty sku.flagSelf}">
                            <li onclick="showDown('${sku.shopSkuId}')"><b><img src="<%=path%>/static/images/commodity3.png" alt="">下架</b>
                            </li>
                        </c:if>
                        <c:if test="${sku.isOwnShip==0 && empty sku.flagSelf}">
                            <li onclick="showDown('${sku.shopSkuId}')"><b><img src="<%=path%>/static/images/commodity3.png" alt="">下架</b>
                            </li>
<%--                            <c:if test="${ not empty sku.wxqrCode}">
                                <li class="right myself" onclick="selfclick('${sku.shopSkuId}')"><b><img src="<%=path%>/static/images/commodity4.png" alt="">我要自己发货</b>
                                </li>
                            </c:if>
                            <c:if test="${empty sku.wxqrCode}">
                                <li class="right myself" onclick="applyWXCode();"><b><img src="<%=path%>/static/images/commodity4.png" alt="">我要自己发货</b>
                                </li>
                            </c:if>--%>
                        </c:if>
                    </ul>
                    <c:if test="${sku.isOwnShip==1}">
                        <ul>
                            <li onclick="showDown('${sku.shopSkuId}')"><b><img src="<%=path%>/static/images/commodity3.png" alt="">下架</b></li>
                            <li onclick="showStock('${sku.stock}','${sku.skuId}')"><b><img src="<%=path%>/static/images/commodity5.png" alt="" style="width:18px;">库存设置</b></li>
                        </ul>
                    </c:if>
                </div>
            </c:forEach>
        </div>
    </main>
</div>
<div class="black down">
    <div class="backb"></div>
    <div class="set">
        <input type="text" id="shopSkuId1" style="display: none">
        <h1>确认下架？</h1>
        <p>下架后的商品将不在店铺展示，消费者也将无法购买。</p>
        <h3>
            <button onclick="clickHide()">取消</button>
            <button onclick="applyxiajia()">确认下架</button></h3>
    </div>
</div>
<div class="black generate">
    <div class="backb"></div>
    <div class="set">
        <input type="text" id="shopSkuId" style="display: none">
        <h1>生成店主发货类型商品？</h1>
        <p>确认“我要自己发货”后，系统将生成一个店主发货类型的商品。此商品您可以编辑库存。当您想销售自己手中的商品时，可以使用此功能。</p>
        <h3>
            <button onclick="clickHide()">取消</button>
            <button class="queren">确认</button></h3>
    </div>
</div>
<div class="black stock">
    <div class="backb"></div>
    <div class="set">
        <h1>库存设置</h1>
        <input type="text" id="updateStock" style="display: none">
        <div>
            <span>当前库存：</span>
            <b id="selfStock"></b>
        </div>
        <div>
            <span>编辑库存：</span>
            <h4>
                <b class="jian">-</b>
                <input type="tel" class="number" value="1">
                <b class="jia">+</b>
            </h4>
        </div>
        <h5>*为了保证发货效率，请正确维护库存。麦链不会为此承担任何责任</h5>
        <h3>
            <button onclick="clickHide()">取消</button>
            <button onclick="updateStock()">确认</button></h3>
    </div>
</div>
<div class="black wxcode">
    <div class="backb"></div>
    <div class="set warnwxcode">
        <p>您还未上传店主二维码，请去店铺设置中上传</p>
        <h3>
            <button onclick="clickHide()">取消</button>
            <button onclick="javascript:window.location.replace('<%=basePath%>shop/manage/setupShop');">点击前往</button></h3>
    </div>
</div>
<script src="<%=path%>/static/shop/js/jquery-1.8.3.min.js"></script>
<script src="<%=path%>/static/js/definedAlertWindow.js"></script>
<script src="${path}/static/js/repetitionForm.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="<%=path%>/static/js/hideWXShare.js"> </script>
<script>
    var i = 1;
    $(".jia").on("click", function () {
        i++;
        $(".number").val(i)
    })
    $(".number").on("change", function () {
        i = $(this).val();
    })
    $(".jian").on("click", function () {
        if (i == 1) {
            return false;
        }
        i--;
        $(".number").val(i)
    })
    var index;
    $(document).ready(function(){
        $(".goods b").html($("#goods option:selected").text());
    })
    function showDown(a){
        $(".down").show();
        $("#shopSkuId1").val(a);
    }
    function selfclick(a){
        $(".generate").show();
        index=$(this).parents(".floor").index();
        $("#shopSkuId").val(a);
    }
    function applyWXCode(){
        $(".wxcode").show();
    }
    function showStock(a,b){
        $(".number").val(1);
        i=1;
        $(".stock").show();
        $("#selfStock").html(a);
        $("#updateStock").val(b);
    }
    function clickHide(){
        $(".black").hide();
    }

    $(".queren").on("click",function(){
        //生成自己的发货类型
        var shopSkuId = $("#shopSkuId").val();
        $.ajax({
            url: '<%=basePath%>shop/addSelfDelivery.do',
            type: 'post',
            data: {shopSkuId: shopSkuId},
            dataType: 'json',
            async:false,
            beforeSend:function(){
                fullShow();
            },
            success: function (data) {
                if (data.isError == false) {
                    alert("恭喜您，您可以自己发货了！");
                }
            },
            complete:function(){
                fullHide();
            }
        });
        window.location.reload(true);
        $(".floor").eq(index).find(".myself").remove();
        $(".generate").hide();
    })

    //下架
    function applyxiajia() {
        var shopSkuId = $("#shopSkuId1").val();
        $.ajax({
            url: '<%=basePath%>shop/updateSale.do',
            type: 'post',
            data: {shopSkuId: shopSkuId, isSale: 0},
            dataType: 'json',
            async:false,
            beforeSend: function () {
                fullShow();
            },
            success: function (data) {
                if (data.isError == false) {
                    alert("下架成功！");
                }
            },
            complete: function () {
                fullHide();
            }
        });
        $(".black").hide();
        window.location.reload(true);
    }
    //上架
    function shangjia(a){
        //ajax 上架
        $.ajax({
            url: '<%=basePath%>shop/updateSale.do',
            type: 'post',
            data: {shopSkuId: a,isSale:1},
            dataType: 'json',
            async:false,
            success: function (data) {
                if (data.isError == false) {
                    alert("上架成功！");
                }
            }
        });
        window.location.reload(true);
    }
    function returnfloat(value){
        var sxd =value.toString().split(".");
        if(sxd.length==1){
            value=value.toString()+".00";
            return value;
        }
        if(sxd.length>1){
            if(sxd[1].length<2){
                value=value.toString()+"0";
            }
            return value;
        }
    }

    //自己发货编辑库存
    function updateStock() {
        var selfStock = i;
        var uskId = $("#updateStock").val();
        $.ajax({
            url: '<%=basePath%>shop/updateStockBySelf.do',
            type: 'post',
            data: {stock: selfStock, skuId: uskId},
            dataType: 'json',
            async:false,
            success: function (data) {
                if (data.isError == false) {
                    alert("库存更新成功！");
                }
            }
        });
        window.location.reload(true);
    }
</script>
<script>
    //上下架ajax
    var index =0;
    function onsale(value){
        var shopxiajia = {};
        shopxiajia.shopId = "${shopId}";
        shopxiajia.isSale = 1;
        shopxiajia.currentPage = 1;
        shopxiajia.deliverType =value;
        $(".nav p span").eq(0).addClass("on").siblings().removeClass("on");
        $(".goods b").html($("#goods option:eq(0)").text());
        index=0;
        $.ajax({//上架中
            url: '<%=basePath%>shop/deliverSale.do',
            type: 'post',
            async: true,
            data: shopxiajia,
            dataType: 'json',
            beforeSend:function(){
                fullShow();
            },
            success: function (data) {
                var trHtml = "";
                $.each(data.skuInfoList, function (i, sku) {
                    var fahuoHtml ="";
                    var zijiHtml ="";
                    if (sku.isOwnShip == 0) {
                        fahuoHtml += "<h1><img src=\"<%=path%>/static/images/commodity.png\" alt=\"\"><b>平台发货</b></h1>";
                        if(sku.flagSelf==null || sku.flagSelf==""){
                            zijiHtml += "<li onclick=\"showDown("+sku.shopSkuId+")\">";
                            zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">下架</b>";
                            zijiHtml += "</li>";
                           /* if(sku.wxqrCode==null || sku.wxqrCode==""){
                                zijiHtml += "<li class=\"right myself\" onclick=\"applyWXCode()\">";
                            }else{
                                zijiHtml += "<li class=\"right myself\" onclick=\"selfclick("+sku.shopSkuId+")\">";
                            }
                            zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity4.png\" alt=\"\">我要自己发货</b>";
                            zijiHtml += "</li>";*/
                        }else{
                            zijiHtml = "<li onclick=\"showDown("+sku.shopSkuId+")\">";
                            zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">下架</b>";
                            zijiHtml += "</li>";
                        }
                    }
                    if (sku.isOwnShip == 1) {
                        fahuoHtml += "<h1><img src=\"<%=path%>/static/images/commodity2.png\" alt=\"\"><b>店主发货</b></h1>";
                        zijiHtml += "<li onclick=\"showDown("+sku.shopSkuId+")\">";
                        zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">下架</b>";
                        zijiHtml += "</li>";
                        zijiHtml += "<li onclick=\"showStock("+sku.stock+","+sku.skuId+")\">";
                        zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity5.png\" alt=\"\" style=\"width:18px;\">库存设置</b>";
                        zijiHtml += "</li>";
                    }
                    trHtml += "<div class=\"sec1\">";
                    trHtml += fahuoHtml;
                    trHtml += "<div>";
                    trHtml += "<p><img src=\"" + sku.comSkuImageUrl + "\" alt=\"\"></p>";
                    trHtml += "<div>";
                    trHtml += "<h1>" + sku.skuName + "</h1>";
                    trHtml += "<p style=\"color: #ff5200;\">￥" + returnfloat(sku.priceRetail) + "</p>";
                    //trHtml += "<p>已售：<span>" + sku.saleNum + "</span>&nbsp;&nbsp;库存: <span>" + sku.stock + "</span></p>";
                    trHtml += "<p>已售：<span>" + sku.saleNum + "</span>&nbsp;&nbsp;库存: <span>" + sku.stock + "</span>";
                    if(sku.stock==0){
                        trHtml += "<span style=\"color: red;\">已售磬,请补货</span>";
                    }
                    trHtml += "</p>";
                    trHtml += "</div>";
                    trHtml += "</div>";
                    trHtml += "<ul>";
                    trHtml += zijiHtml;
                    trHtml += "</ul>";
                    trHtml += "</div>";
                });
                $(".floor").empty().html(trHtml);
            },
            complete: function () {
                fullHide();
            }
        });
    }
    function outSale(value){
        var shopshangjia = {};
        shopshangjia.shopId = "${shopId}";
        shopshangjia.isSale = 0;
        shopshangjia.currentPage = 1;
        shopshangjia.deliverType =value;
        $(".nav p span").eq(1).addClass("on").siblings().removeClass("on");
        $("#goods option").attr("selected",false);
        $(".goods b").html($("#goods option:eq(0)").text());
        index=1;
        $.ajax({//仓库中
            url: '<%=basePath%>shop/deliverSale.do',
            type: 'post',
            async:true,
            data: shopshangjia,
            dataType: 'json',
            beforeSend:function(){
                fullShow();
            },
            success: function (data) {
                var trHtml = "";
                $.each(data.skuInfoList, function (i, sku) {
                    var fahuoHtml ="";
                    var zijiHtml ="";
                    if (sku.isOwnShip == 0) {
                        fahuoHtml += "<h1><img src=\"<%=path%>/static/images/commodity.png\" alt=\"\"><b>平台发货</b></h1>";
                        if(sku.flagSelf==null || sku.flagSelf==""){
                            zijiHtml += "<li onclick=\"shangjia("+sku.shopSkuId+")\">";
                            zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">上架</b>";
                            zijiHtml += "</li>";
                            <%--if(sku.wxqrCode==null || sku.wxqrCode==""){--%>
                                <%--zijiHtml += "<li class=\"right myself\" onclick=\"applyWXCode()\">";--%>
                            <%--}else{--%>
                                <%--zijiHtml += "<li class=\"right myself\" onclick=\"selfclick("+sku.shopSkuId+")\">";--%>
                            <%--}--%>
                            <%--zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity4.png\" alt=\"\">我要自己发货</b>";--%>
                            <%--zijiHtml += "</li>";*/--%>
                        }else{
                            zijiHtml = "<li onclick=\"shangjia("+sku.shopSkuId+")\">";
                            zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">上架</b>";
                            zijiHtml += "</li>";
                        }
                    }
                    if (sku.isOwnShip == 1) {
                        fahuoHtml += "<h1><img src=\"<%=path%>/static/images/commodity2.png\" alt=\"\"><b>店主发货</b></h1>";
                        zijiHtml += "<li onclick=\"shangjia("+sku.shopSkuId+")\">";
                        zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity3.png\" alt=\"\">上架</b>";
                        zijiHtml += "</li>";
                        zijiHtml += "<li onclick=\"showStock("+sku.stock+","+sku.skuId+")\">";
                        zijiHtml += "<b><img src=\"<%=path%>/static/images/commodity5.png\" alt=\"\" style=\"width:18px;\">库存设置</b>";
                        zijiHtml += "</li>";
                    }
                    trHtml += "<div class=\"sec1\">";
                    trHtml += fahuoHtml;
                    trHtml += "<div>";
                    trHtml += "<p><img src=\"" + sku.comSkuImageUrl + "\" alt=\"\"></p>";
                    trHtml += "<div>";
                    trHtml += "<h1>" + sku.skuName + "</h1>";
                    trHtml += "<p style=\"color: #ff5200;\">￥" + returnfloat(sku.priceRetail) + "</p>";
                    //trHtml += "<p>已售：<span>" + sku.saleNum + "</span>&nbsp;&nbsp;库存: <span>" + sku.stock + "</span></p>";
                    trHtml += "<p>已售：<span>" + sku.saleNum + "</span>&nbsp;&nbsp;库存: <span>" + sku.stock + "</span>";
                    if(sku.stock==0){
                        trHtml += "<span style=\"color: red;\">已售磬,请补货</span>";
                    }
                    trHtml += "</div>";
                    trHtml += "</div>";
                    trHtml += "<ul>";
                    trHtml += zijiHtml;
                    trHtml += "</ul>";
                    trHtml += "</div>";
                });
                $(".floor").empty().html(trHtml);
            },
            complete: function () {
                fullHide();
            }
        });
    }

    //search
    $(".search").on("change", function () {
        search();
    })
    function search() {
        var deliverType = $("#goods option:selected").val();
        if (index==0) { // 出售中
            onsale(deliverType);
        } else {
            outSale(deliverType);
        }
    }
    $("#goods").on("change",function(){
        var tabVal=$("#goods option:selected").text();
        $(".goods b").html(tabVal);
    })
</script>

</body>
</html>