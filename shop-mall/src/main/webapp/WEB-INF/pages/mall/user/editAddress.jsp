<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0, user-scalable=0">
    <title>麦链商城</title>
    <link rel="stylesheet" href="${path}/static/css/pageCss/base.css">
    <link rel="stylesheet" href="${path}/static/css/pageCss/reset.css">
    <link rel="stylesheet" href="${path}/static/css/pageCss/header.css">
    <link rel="stylesheet" href="${path}/static/css/pageCss/loading.css"/>
    <link rel="stylesheet" href="${path}/static/css/pageCss/xinjiandizhi.css">
</head>
<body>
<main>
    <div class="wrap">
        <div class="box">
            <header class="xq_header">
                <a href="${path}/userAddress/toManageAddressPage.html?addAddressJumpType=${addAddressJumpType}&manageAddressJumpType=${manageAddressJumpType}"><img src="${path}/static/images/xq_rt.png" alt=""></a>
                <p>编辑收货地址</p>
            </header>
            <div id="d_box">
                <p class="sf">
                    收货人姓名
                    <input type="text" id="addressId" style="display:none;" value="${comUserAddress.id}">
                    <input type="text" id="isDefaultId" style="display:none;" value="${comUserAddress.isDefault}">
                    <input type="text" id="name" class="name" value="${comUserAddress.name}">
                    <span class="onc"></span>
                    <b class="gao"></b>
                </p>
                <p class="sf">
                    手机号码
                    <input type="tel" id="phone" class="tel" value="${comUserAddress.mobile}">
                    <span class="onc"></span>
                    <b class="gao"></b>
                </p>
                <p class="sf">
                    邮政编码
                    <input type="tel" class="postcode" id="postcode" value="${comUserAddress.zip}">
                    <span class="onc"></span>
                    <b class="gao"></b>
                </p>
                <input id="oldProvinceId" value="${provinceId}" style="display: none" />
                <input id="oldCityId" value="${cityId}" style="display: none" />
                <input id="oldConntyId" value="${countyId}" style="display: none" />
                <div class="address">
                    联系地址
                    <select class="sel" id="s_province" name="s_province">
                        <option value='-1'>--省份--</option>
                    </select>
                    <select class="sel" id="s_city" name="s_city">
                        <option value='-1'>--地级市--</option>
                    </select>
                    <select class="sel" id="s_county" name="s_county">
                        <option value='-1'>--县/区--</option>
                    </select>
                </div>
                <p class="sf">
                    详细地址
                    <input type="text" id="detailAddress" class="dizhi" value="${comUserAddress.address}">
                    <span class="onc"></span>
                    <b class="gao"></b>
                </p>
            </div>
        </div>
        <input type="text" id="operateTypeId" style="display: none" value="update"/>
        <input type="text" id="addAddressJumpTypeId" style="display:none;" value="${addAddressJumpType}">
        <input type="text" id="manageAddressJumpTypeId" style="display:none;" value="${manageAddressJumpType}">
        <a onclick="updateAddress()" class="baocun">
            保存
        </a>
    </div>
</main>
<script src="${path}/static/js/plugins/jquery-1.8.3.min.js"></script>
<script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
<script src="${path}/static/js/definedAlertWindow.js"></script>
<script src="${path}/static/js/common/commonAjax.js"></script>
<script src="${path}/static/js/plugins/iscroll.js"></script>
<script src="${path}/static/js/common/checkUtil.js"></script>
<script src="${path}/static/js/common/comArea.js"></script>
<script src="${path}/static/js/pageJs/address.js"></script>
<script src="${path}/static/js/pageJs/hideWXShare.js"></script>
<script>
    comAreaJS.init("edit");
    addressJS.init();
    var promise =  $.Deferred().promise();
    function updateAddress() {
        var manageAddressJumpType = $("#manageAddressJumpTypeId").val();
        var addAddressJumpType = $("#addAddressJumpTypeId").val();
        var paramJson = addressJS.getJsonParam();
        if (addressJS.validateAddressInfo(paramJson)) {
            if(promise.state()=="pending"){
                promise = $.post("/userAddress/addOrUpdateAddress.do",
                        paramJson, function (data) {
                            if (data == "success") {
                                window.location.href = addressJS.basePath + "/userAddress/toManageAddressPage.html?manageAddressJumpType=" + manageAddressJumpType + "&addAddressJumpType=" + addAddressJumpType;
                            }
                        });
            }
            promise.then();
        }
    }
</script>
</body>
</html>
