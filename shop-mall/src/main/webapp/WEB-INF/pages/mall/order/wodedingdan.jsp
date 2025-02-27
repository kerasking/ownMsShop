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
    <title>麦链商城</title>
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/base.css">
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/reset.css">
    <link rel="stylesheet" href="<%=path%>/static/css/pageCss/wodedingdan.css">
</head>
<body>
       <div class="wrap">
           <div class="box">
                <header class="xq_header">
                   <a href="<%=path%>/sfOrderManagerController/borderManagement.html"><img src="<%=path%>/static/images/xq_rt.png" alt=""></a>
                        <p>我的订单</p>
                </header>
                <nav>
                    <ul>
                        <li class="active"><a href="javascript:;">全部</a></li>
                        <li><a href="javascript:;">待付款</a></li>
                        <li><a href="javascript:;">待发货</a></li>
                        <li><a href="javascript:;">待收货</a></li>
                        <li><a href="javascript:;">已完成</a></li>
                        <li><a href="javascript:;">已取消</a></li>
                    </ul>
                </nav>
                <main>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                    <div class="all"><c:forEach items="${sfOrders}" var="pb">
                        <section class="sec1">
                            <h2>
                                <span onclick="javascript:window.location.replace('<%=path%>/${pb.shopId}/0/shop.shtml');">${pb.shopName}</span>
                                <b class="querenshouhuo_${pb.id}">${pb.orderStatusDes}</b>
                            </h2>
                            <c:forEach items="${pb.sfOrderItems}" var="pbi">
                                <div class="shangpin" onclick="javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id=${pb.id}');">
                                    <p class="photo">
                                        <a>
                                            <img src="${pbi.skuUrl}" alt="">
                                        </a>
                                    </p>
                                    <div>
                                        <h2><i>${pbi.skuName}</i><span>零售价：￥${pbi.unitPrice}</span></h2>
                                        <p class="defult"><span style="float:none;color:#666666;">x${pbi.quantity}</span><b>合计：￥${pb.orderAmount}</b></p>
                                    </div>
                                </div></c:forEach>
                            <div class="ding">
                                <p>时间：<span><fmt:formatDate value="${pb.createTime}" pattern="yyyy-MM-dd HH:mm" /></span></p>
                                <c:if test="${pb.orderStatus ==8 ||pb.orderStatus ==0}">
                                    <p>
                                        <c:if test="${pb.orderStatus ==8}"><button id="querenshouhuo_${pb.id}" onclick="querenshouhuo('${pb.id}')">确认收货</button></c:if>
                                        <c:if test="${pb.orderStatus ==0}"><button onclick="javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId=${pb.id}');">继续支付</button></c:if>
                                    </p>
                                </c:if>
                            </div>
                        </section></c:forEach>
                    </div>
                </main>
           </div>
       </div>
        <div class="back_que" style="display: none">
                    <p>确认减库存?</p>
                    <h4>快递公司:<span><select><option>顺风</option></select></span></h4>
                    <h4>快递单号:<span><input type="text"/></span></h4>
                    <h3>发货</h3>
        </div>
        <div class="shouhuo" style="display: none">
            <p>收货人信息</p>
            <h4><span>姓　名:</span><span></span></h4>
            <h4><span>地　址:</span><span>阿斯科利的asdasdasdasdas将阿</span></h4>
            <h4><span>手机号:</span><span></span></h4>
            <h4><span>邮　编:</span><span></span></h4>
            <h3 class="close">关闭</h3>
        </div>
        <div class="back_shouhuo" style="display: none">
           <p>确认收到货品?</p>
           <h4>亲，请您核对商品后再操作确认收货</h4>

           <h3>
               <span class="que_qu">取消</span>
               <span class="que_que">确认</span>
           </h3>
        </div>
           <div class="back" style="display: none">
               
           </div>
       <script src="<%=path%>/static/js/plugins/jquery/jquery-1.8.3.min.js"></script>
       <script src="<%=path%>/static/js/pageJs/jinhuoshijian.js"></script>
       <script src="<%=path%>/static/js/common/definedAlertWindow.js"></script>
       <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
       <script src="<%=path%>/static/js/pageJs/hideWXShare.js"></script>
       <script>
           $(function(){
               $("li").on("click",function(){
                   var index=$(this).index();
                   $(".all").html("");
                   $(".all").eq(index).show().siblings().hide();
                   $("li").removeClass("active");
                   $(this).addClass("active");
                   $.ajax({
                       type:"POST",
                       url : "<%=path%>/sfOrderManagerController/clickSfOrderType.do",
                       data:{index:index},
                       dataType:"Json",
                       success:function(data){
                           var trHtml = "";
                           $.each(data, function(i, sfOrder) {
                               var time2 = new Date(sfOrder.createTime).Format("yyyy-MM-dd hh:mm");
                               trHtml+="<section class='sec1'>";
                               trHtml+="<h2>";
                               trHtml+="<span onclick=\"javascript:window.location.replace('<%=path%>/"+sfOrder.shopId+"/0/shop.shtml');\">"+sfOrder.shopName+"</span>";
                               trHtml+="<b class=\"querenshouhuo_"+sfOrder.id+"\">"+sfOrder.orderStatusDes+"</b>";
                               trHtml+="</h2>";
                               $.each(sfOrder.sfOrderItems, function(i, sfOrderItem) {
                                   trHtml+="<div class=\"shangpin\" onclick=\"javascript:window.location.replace('<%=path%>/sfOrderManagerController/borderDetils.html?id="+sfOrder.id+"');\">";
                                   trHtml+="<p class=\"photo\">";
                                   trHtml+="<a><img src=\""+sfOrderItem.skuUrl+"\" alt=\"\"></a></p>";
                                   trHtml+="<div>";
                                   trHtml+="<h2><i>"+sfOrderItem.skuName+"</i><span>零售价：￥"+sfOrderItem.unitPrice+"</span></h2>";
                                   trHtml+="<p class=\"defult\"><span style=\"float:none;color:#666666;\">x"+sfOrderItem.quantity+"</span><b>合计：￥"+sfOrder.orderAmount+"</b></p>";
                                   trHtml+="</div></div>";
                               })
                               trHtml+="<div class=\"ding\"><p>时间："+time2+"</p>";
                               if(sfOrder.orderStatus ==8 ||sfOrder.orderStatus ==0){
                                   trHtml+="<p>";
                                   if(sfOrder.orderStatus ==8 ){
                                       trHtml+="<button id=\"querenshouhuo_"+sfOrder.id+"\" onclick=\"querenshouhuo('"+sfOrder.id+"')\">确认收货</button></p>";
                                   }
                                   if(sfOrder.orderStatus ==0 ){
                                       trHtml+="<button onclick=\"javascript:window.location.replace('<%=path%>/orderPay/getOrderInfo.html?orderId="+sfOrder.id+"');\">继续支付</button>";
                                   }
                               }
                               trHtml+="</div></section>";
                           });
                           $(".all").eq(index).html(trHtml);
                       }
                   })
               });
           })

            $(document).ready(function(){
                var index=${index};
                $("li").removeClass("active");
                $("li").eq(index).addClass("active");
                $(".all").eq(index).show().siblings().hide();
            });
            var oid = "";
            function querenshouhuo(id){
                $(".back").css("display","-webkit-box");
                $(".back_shouhuo").css("display","-webkit-box");
                oid = id;
            }

           $(function(){
               $(".que_que").on("click",function(){
                   $(".back_shouhuo").hide();
                   $(".back").hide();
                   var aa="#querenshouhuo_"+oid;
                   var bb=".querenshouhuo_"+oid;
                   $.ajax({
                       type:"POST",
                       url : "<%=path%>/sfOrderManagerController/deliverSfOrder.do",
                       data:{orderId:oid},
                       dataType:"Json",
                       success:function(date){
                           $(""+aa+"").css("display","none");
                           $(""+bb+"").html("已完成");
                           location.reload(true);
                       }
                   });
               });
           });


            $(".que_qu").on("click",function(){
                $(".back_shouhuo").hide();
                $(".back").hide();
            })
            $(".sh").on("click",function(){
                $(".back").css("display","-webkit-box");
                $(".shouhuo").css("display","-webkit-box");
            })
            $(".close").on("click",function(){
                $(".shouhuo").hide();
                $(".back").hide();
            });
       </script>
</body>
</html>