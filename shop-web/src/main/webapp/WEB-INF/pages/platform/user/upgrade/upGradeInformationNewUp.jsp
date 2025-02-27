<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0"> 
    <title>麦链合伙人</title>
    <%@include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/upGrade/shengjixinxi.css">
</head>
<body>
   <div class="wrap">
        <header class="xq_header">
            <a href="${basePath}upgradeInfo/lower?tabId=2"><img src="${path}/static/images/xq_rt.png" alt=""></a>
            <p>升级信息</p>
        </header>
        <main>
            <c:if test="${income == 1}">
                <div class="floor3" id="floor3">
                    <h1>${upGradeInfoPo.applyName} 的新上级 ${newUp} 需要给您一次性返利，具体数额请线下沟通 </h1>

                </div>
            </c:if>
            <c:if test="${income == 2}">
                <div class="floor3" id="floor3">
                    <h1>您需要给 ${upGradeInfoPo.applyName}  的原上级${former} 进行一次性返利，具体数额请线下沟通 </h1>
                </div>
            </c:if>
            <div class="floor">
                <h1>升级信息详情</h1>
                <p>
                    <span>商品名称：</span>
                    <span>${upGradeInfoPo.skuName}</span>
                </p>
                <p>
                    <span>姓名：</span>
                    <span>${upGradeInfoPo.applyName}</span>
                </p>
                <p>
                    <span>原上級：</span>
                    <span>${upGradeInfoPo.applyPName}</span>
                </p>
                <p>
                    <span>新上级：</span>
                    <span><b onclick="showDiv()">${newUpUser.realName}</b></span>
                </p>
                <p>
                    <span>原等级：</span>
                    <span>${upGradeInfoPo.orgAgentName}</span>
                </p>
                <p>
                    <span>升级后等级：</span>
                    <span>${upGradeInfoPo.wishAgentName}</span>
                </p>
                <p>
                    <span>状态：</span>
                    <span>${status}</span>
                </p>
                <p>
                    <span>申请时间：</span>
                    <span>${createTime}</span>
                </p>
                <p>
                    <span>升级编号：</span>
                    <span>${upGradeInfoPo.applyCode}</span>
                </p>
            </div>
        </main>
    </div>
    <div class="black" id="black">
        <div class="backb"></div>
        <div class="backt">
            <h1>新上级</h1>
            <p><span>手机号：</span><span>${newUpUser.mobile}</span></p>
            <p><span>姓　名：</span><span>${newUpUser.realName}</span></p>
            <button onclick="clickHide()">确定</button>
        </div>
    </div>
    <script src="${path}/static/js/jquery-1.8.3.min.js"></script>
    <script src="${path}/static/js/commonAjax.js"></script>
    <script src="${path}/static/js/definedAlertWindow.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
    <script src="${path}/static/js/hideWXShare.js"></script>
    <script>
       var path = "${path}";
       var basePath = "${basePath}";

        function showDiv(){
            $("#black").show();
        }
        function clickHide(){
            $("#black").hide();
        }
    </script>
</body>
</html>