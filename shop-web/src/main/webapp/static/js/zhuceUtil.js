

$(function () {
    var weixinCheckFun = function (data) {
        if ($(data).val() == "") {
            alert("微信号不能为空");
            return false;
        }
        if (!isNumber($(data).val())) {
            alert("请输入6~20个字符，字母、数字、下划线或减号。");
            return false;
        }
        return true;
        function isNumber(s) {
            var patrn = /^[a-zA-Z0-9_-]{6,20}$/;
            if (!patrn.exec(s)) {
                return false;
            }
            return true;
        }

    }
    var mobileCheckFun = function (data) {
        var bl = false;
        if ($('input[name="danx"]:checked').attr("class") == "shi") {
            if ($(data).val() == "") {
                alert("手机号不能为空");
                return false;
            }

            if (!isMobile($(data).val())) {
                alert("手机号格式不正确");
                return false;
            }
            var para = {};
            var checkLevel = $("div.active");
            if (checkLevel != null) {
                para.agentLevel = checkLevel.attr("levelId");
            }
            para.skuId = skuId;
            para.pMobile = $(data).val();
            $.ajax({
                url: path + "userApply/checkPMobile.do",
                data: para,
                dataType: "json",
                type: "POST",
                async: false,
                success: function (rdata) {
                    if (rdata && rdata.isError == false) {
                        pUserId = rdata.pUserId;
                        if (sendType == 0) {
                            sendType = rdata.sendType;
                        }
                        bl = true;
                    } else {
                        alert(rdata.message);
                        bl = false;
                    }
                }
            });
        }
        if (bl) {
            return true;
        }
        else {
            return false;
        }
        function isMobile(s) {
            var patrn = /^(0|86|17951)?(13[0-9]|15[012356789]|17[678]|18[0-9]|14[57])[0-9]{8}$/;
            if (!patrn.exec(s)) {
                return false;
            }
            return true;
        }
    }
    $("#next").click(function () {
        if (!weixinCheckFun($("#weixin"))) {
            return;
        }
        if ($("div.active").length == 0) {
            alert("请选择合伙人套餐");
            return;
        }
        if ($("#q").prop("checked") == true) {
            if (!mobileCheckFun($("#pMobile"))) {
                return;
            }
            $("#q_pMobile").html($("#pMobile").val());
            $("#q_pMobile").parent().css("display", "-webkit-box");
        } else {
            $("#q_pMobile").parent().css("display", "none");
        }
        // 获取微信号
        $("#q_weixinId").html($("#weixin").val());
        // 获取合伙人套餐
        $("#q_levelName").html($("div.active").attr("agentFee") + "元套餐");
        // 获取所缴纳货款
        $("#q_amount").html("￥" + $("div.active").attr("agentFee"));

        // 弹出确认框
        $(".back_que").css("display", "-webkit-box");
        $(".back").show();
    });
    $("#submit").click(function (event) {
        var thisObj = $(this);
        if (thisObj.html() == "正在提交...") {
            return;
        }
        thisObj.html("正在提交...");
        var event = event || event.window;
        event.stopPropagation();
        var para = {};
        para.skuId = skuId;
        para.pUserId = pUserId;
        $.ajax({
            url: path + "userApply/register/save.do",
            data: para,
            dataType: "json",
            type: "POST",
            success: function (rdata) {
                if (rdata && rdata.isError == false) {
                    //还没有选择拿货方式
                    if (sendType == 0) {
                        var paraData = "?";
                        paraData += "skuId=" + skuId;
                        paraData += "&agentLevelId=" + $("div.active").attr("levelId");
                        paraData += "&weiXinId=" + $("#q_weixinId").html();
                        window.location.href = path + "border/setUserSendType.shtml" + paraData;
                    } else {
                        var paraData = "?";
                        paraData += "skuId=" + skuId;
                        paraData += "&agentLevelId=" + $("div.active").attr("levelId");
                        paraData += "&weiXinId=" + $("#q_weixinId").html();
                        paraData += "&sendType=" + sendType;
                        paraData += "&previousPageType=0";
                        window.location.href = path + "BOrderAdd/agentBOrder.shtml" + paraData;
                    }
                } else {
                    alert(rdata.message);
                }
            }
        });
    });
    /*
     * 是否有推荐人
     * */
    $("[name='danx']").on("click", function () {
        if ($(this).attr("class") == "shi") {
            $(".dengji input").attr("disabled", false)
            $("#hehuo").show();
        } else {
            $("#hehuo").hide();
        }
    });
    /*
     * 选择合伙人等级
     * */
    $(".dengji").on("click", ".floor", function () {
        $(this).addClass("active").siblings().removeClass("active");
    })
    /*
     * 返回修改
     * */
    $("#getBack").on("click", function (event) {
        var event = event || event.window;
        event.stopPropagation();
        $(".back_que").hide();
        $(".back").hide();
    })
    $(".row").on("click", function () {
        $(".paidanqi").show();
    });
    $(".kNow").on("click", function () {
        $(this).parent().parent().hide();
    });

    $(".daili").on("click", function () {
        $(".xieyi").show();
    })
});

