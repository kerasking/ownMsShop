(function ($) {
    //备份jquery的ajax方法
    var _ajax = $.ajax;

    //重写jquery的ajax方法
    $.ajax = function (opt) {
        //备份opt中error和success方法
        var fn = {
            beforeSend: function () {
            },
            dataFilter: function () {
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                alert(XMLHttpRequest.responseText);
            },
            success: function (data, textStatus) {
            },
            complete: function (XMLHttpRequest, textStatus) {
            }
        }
        if (opt.beforeSend) {
            fn.beforeSend = opt.beforeSend;
        }
        if (opt.dataFilter) {
            fn.dataFilter = opt.dataFilter;
        }
        if (opt.error) {
            fn.error = opt.error;
        }
        if (opt.success) {
            fn.success = opt.success;
        }
        if (opt.complete) {
            fn.complete = opt.complete;
        }
        //扩展增强处理
        var _opt = $.extend(opt, {
            beforeSend: function (XMLHttpRequest) {
                fullShow();
                this;
            },
            error: function (XMLHttpRequest, textStatus, errorThrown) {
                fullHide();
                //错误方法增强处理
                fn.error(XMLHttpRequest, textStatus, errorThrown);
            },
            success: function (data, textStatus) {
                //成功回调方法增强处理
                fn.success(data, textStatus);
            },
            complete: function () {
                fullHide();
            }
        });
        _ajax(_opt);
    };
})(jQuery);

/*页面加载loading样式
 */
//.loadingPage_bg { background: none repeat scroll 0 0 #fff; display: block; height: 100%; left: 0; /*:rgba(0,0,0,0.5);*/ opacity: 0.1; filter: alpha(opacity=50); position: absolute; top: 0; width: 100%; z-index: 110; }
//#loadingPage { display: block; font-weight: bold; font-size: 12px; color: #595959; height: 28px; left: 50%; line-height: 27px; margin-left: -74px; margin-top: -14px; padding: 10px 10px 10px 50px; position: absolute; text-align: left; top: 50%; width: 148px; z-index: 111; background: url(img/loading.gif) no-repeat scroll 12px center #FFFFFF; border: 2px solid #86A5AD; }


var CommonPerson = {};
CommonPerson.Base = {};
CommonPerson.Base.LoadingPic = {
    operation: {
        timeTest: null,                     //延时器
        loadingCount: 0,                    //计数器 当同时被多次调用的时候 记录次数
        loadingImgUrl: "img/loading.gif",  //默认load图地址
        loadingImgHeight: 24,               //Loading图的高
        loadingImgWidth: 24                 //Loading图的宽
    },

    //显示全屏Loading图
    FullScreenShow: function (msg) {
        if (msg === undefined) {
            msg = "加载中...";
        }

        if ($("#div_loadingImg").length == 0) {
            $("body").append("<div id='div_loadingImg'></div>");
        }
        if (this.operation.loadingCount < 1) {
            this.operation.timeTest = setTimeout(function () {
                $("#div_loadingImg").append("<div id='loadingPage_bg' class='loadingPage_bg1'></div><div id='loadingPage'><p>" + msg + "</p></div>");
                $("#loadingPage_bg").height($(top.window.document).height()).width($(top.window.document).width());
            }, 100);
        }
        this.operation.loadingCount += 1;
    },

    //隐藏全屏Loading图
    FullScreenHide: function () {
        this.operation.loadingCount -= 1;
        if (this.operation.loadingCount <= 0) {
            clearTimeout(this.operation.timeTest);
            $("#div_loadingImg").empty();
            $("#div_loadingImg").remove();
            this.operation.loadingCount = 0;
        }
    },

    ////显示局部Loading图
    //PartShow: function (parentContainerID, url, msg) {
    //    $("#" + parentContainerID.replace("#", "").replace(".", "") + "_loadingImg").remove();
    //    var imgUrl = '';//图片路径
    //    if (url) {  //如果url值存在就启用赋值的图片路径
    //        imgUrl = url;
    //    } else {     //否则就启用默认图片路径
    //        imgUrl = this.operation.loadingImgUrl;
    //    }
    //
    //    if (msg === undefined) {
    //        msg = "此部分数据正在加载中, 请稍等...";
    //    }
    //
    //    var htmlText = ' <div id="' + parentContainerID + '_loadingImg" class="loadingPage_bg"><div style="display: block; font-weight: bold; font-size: 12px; color: #595959; height: 28px; left: 50%; line-height: 27px; padding: 10px 10px 10px 50px; width: 240px; z-index: 111; background: url(img/loading.gif) no-repeat scroll 12px center #FFFFFF; border: 2px solid #86A5AD;">' + msg + '</div></div>'
    //    $("#" + parentContainerID).append(htmlText);
    //},
    //
    ////局部隐藏loading图
    //PartHide: function (parentContainerID) {
    //    $("#" + parentContainerID.replace("#", "").replace(".", "") + "_loadingImg").remove();
    //},

    ////显示局部Loading图(遮罩层只有图片)
    //PartOnlyImgShow: function (parentContainerID, url) {
    //    $("#" + parentContainerID.replace("#", "").replace(".", "") + "_zhezhao").remove();
    //    //计算图片中心点到容器
    //    var parentContainer = $("#" + parentContainerID);
    //    var imgTop = parentContainer.height() / 2 - this.operation.loadingImgHeight / 2;
    //    var imgLeft = parentContainer.width() / 2 - this.operation.loadingImgWidth / 2;
    //
    //    var imgUrl = '';//图片路径
    //    if (url) {  //如果url值存在就启用赋值的图片路径
    //        imgUrl = url;
    //    } else {     //否则就启用默认图片路径
    //        imgUrl = this.operation.loadingImgUrl;
    //    }
    //
    //    var htmlText = '<div id="' + parentContainerID.replace("#", "").replace(".", "") + '_zhezhao" class="loadingPage_bg" style="margin:10px;display:block;position: absolute; width:' + parentContainer.width() + 'px; border: 1px solid #D6E9F1; z-index:1002;"><img style="position: absolute; top:' + imgTop + 'px; left:' + imgLeft + 'px; border: 1px solid #D6E9F1;" src="' + imgUrl + '"/> </div>'
    //    $("body").append(htmlText);
    //
    //    var zhezhao = $("#" + parentContainerID.replace("#", "").replace(".", "") + "_zhezhao");
    //    zhezhao.css("top", parentContainer.offset().top + "px");
    //    zhezhao.css("left", parentContainer.offset().left + "px");
    //    zhezhao.css("width", parentContainer.width() + "px");
    //},
    //
    ////局部隐藏loading图(遮罩层只有图片)
    //PartOnlyImgHide: function (parentContainerID) {
    //    $("#" + parentContainerID.replace("#", "").replace(".", "") + "_zhezhao").remove();
    //}

}
function fullHide() {
    CommonPerson.Base.LoadingPic.FullScreenHide();
}

function fullShow() {
    CommonPerson.Base.LoadingPic.FullScreenShow();
}