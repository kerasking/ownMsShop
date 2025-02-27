<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN"
"http://www.w3.org/TR/html4/loose.dtd">
<html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链合伙人</title>
    <%@ include file="/WEB-INF/pages/common/head.jsp" %>
    <link rel="stylesheet" href="${path}/static/css/tijiaosq.css">
</head>
<body>
<div class="wrap">
    <div class="box">
        <header class="xq_header">
            <a href="javascript:;" onClick="javascript :history.go(-1);"><img src="${path}/static/images/xq_rt.png"
                                                                              alt=""></a>
            <p>实名认证</p>
        </header>
        <div class="tai">
            <img src="${path}/static/images/icon_43.png" alt="">
            <h1>您的资料审核失败了！</h1>
            <p>失败原因:<span>${comUser.auditReason}</span></p>
        </div>
        <p class="cp">
            <span>实名认证通过后才能申请合伙人:</span>
        </p>

        <main>
            <div class="sf">
                姓名：
                <input type="text" id="name" name="name" value="${comUser.realName}">
            </div>
            <div class="sf">
                身份证号：
                <input type="text" id="idCard" name="idCard" value="${comUser.idCard}">
            </div>
            <div class="sf" style="border-bottom:none;">
                身份证照片：
            </div>
            <input id="idCardFrontName" style="display: none" value="${idCardFrontName}">
            <input id="idCardBackName" style="display: none" value="${idCardBackName}">
            <div class="sfphoto">
                <input type="file" id="idCardImg" name="idCardImg" onchange="uploadIdCardImg()"
                       style="display: none;">
                <label class="zheng only">
                    <img src="${idCardFrontUrl}" alt="" id="idCardFront" name="idCardPre"
                         onclick="F_Open_dialog(0)">
                    重新上传
                </label>
                <label class="fan only" style="margin-left:10px;">
                    <img src="${idCardBackUrl}" alt="" id="idCardBack" name="idCardPre"
                         onclick="F_Open_dialog(1)">
                    重新上传
                </label>
            </div>

        </main>
        <a href="javascript:;" class="tijiao" onclick="submit();">重新提交</a>
    </div>
</div>
</body>

<%@ include file="/WEB-INF/pages/common/foot.jsp" %>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${path}/static/js/hideWXShare.js"></script>
<script src="${path}/static/js/ajaxfileupload.js"></script>
<script>
    var isRuningF = true;
    var isRuningB = true;
    var isReUpload = false;
    oNly()
    function oNly() {
        var oNlywidth = $(".only").width();
        $(".only").height(oNlywidth + "px")
    }
    var checkImg = 0;
    function F_Open_dialog(data) {
        checkImg = data;
        document.getElementById("idCardImg").click();
    }
    function uploadIdCardImg() {
        isReUpload = true;
        var selector = !checkImg ? 'idCardFront' : 'idCardBack';
        if (selector == "idCardFront" ){
            isRuningF = false;
        }else{
            isRuningB = false;
        }
        $('#' + selector).attr('src', '${path}/static/images/loading2.gif');
        $.ajaxFileUpload({
            url: "${path}/userCertificate/idCardImgUpload.do",
            data: "",
            type: "POST",
            secureuri: false,
            fileElementId: ['idCardImg'],
            dataType: "json",
            success: function (rdata) {
                var data = JSON.parse(rdata);
                if (data.code == 1) {
                    $("#" + selector).attr("src", "${path}" + data.imgPath);
                    if (selector == "idCardFront") {
                        isRuningF = true;
                    }
                    if (selector == "idCardBack") {
                        isRuningB = true;
                    }
                    $("#" + selector+"Name").val(data.fileName);
                } else {
                    alert(data.msg);
                }

            }
        });
    }
    function submit() {
        var cd = /(^\d{15}$)|(^\d{18}$)|(^\d{17}(\d|X|x)$)/;//定义身份证匹配算法
        var legalIdCard = $("#idCard").val();//获取前台界面字段值
        if (nameCheckFun($("#name")) == false) {
            return;
        }
        if (legalIdCard != null && legalIdCard != "") {//运用匹配方法直接判断
            if (cd.test(legalIdCard) == false) {
                alert("身份证号不合法!");
                return;
            }
        } else {
            alert("身份证号不能为空!");
            return;
        }
        var fCardUrl = $("#idCardFront").attr("src");
        if (fCardUrl == "${path}/static/images/shenfen.png") {
            alert("请上传身份证正面!");
            return;
        }
        var bCardUrl = $("#idCardBack").attr("src");
        if (bCardUrl == "${path}/static/images/shenfenf.png") {
            alert("请上传身份证反面!");
            return;
        }
        if (isReUpload){
            if (!isRuningF || !isRuningB) {
                alert("图片正在上传中，请稍后!");
                return;
            }
        }
        fCardUrl = fCardUrl.substr(fCardUrl.lastIndexOf('/') + 1);
        bCardUrl = bCardUrl.substr(bCardUrl.lastIndexOf('/') + 1);
        var paraData = {};
        paraData.name = $("#name").val();
        paraData.idCard = legalIdCard;
        paraData.idCardFrontName = $("#idCardFrontName").val();
        paraData.idCardBackName = $("#idCardBackName").val();
        paraData.type = 0;
        $.ajax({
            url: "${path}/identityAuth/sumbitAudit.do",
            type: "post",
            data: paraData,
            dataType: "json",
            success: function (data) {
                if (data.isError == false) {
                    window.location.href = "${path}/identityAuth/toWaitIdentityPage.html?skuId=${skuId}&returnPageIdentity=${returnPageIdentity}";
                }
                else {
                    alert(data.message);
                    return false;
                }
            }
        });
    }
    function nameCheckFun(data) {
        if ($(data).val() == "") {
            alert("姓名不能为空");
            return false;
        }
        if (getStrLen($(data).val())>20) {
            alert("姓名不能超过10个汉字");
            return false;
        }
    }
    function getStrLen(str){
        var len = 0;
        if (str!=null && str!=""){
            for (var i = 0 ; i < str.length; i++){
                var  c = str.charCodeAt(i);
                if (c>127||c==94){
                    len += 2;
                }else{
                    len++;
                }
            }
        }
        return len;
    }
</script>
</html>