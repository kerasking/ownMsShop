<%@ page language="java" import="java.util.*" contentType="text/html; utf-8" pageEncoding="UTF-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%
    String path = request.getContextPath();
    String basePath = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort() + path + "/";
%>

<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1" />
    <meta charset="utf-8" />
    <title>Tables - Masiis</title>

    <meta name="description" content="Static &amp; Dynamic Tables" />
    <meta name="viewport" content="width=device-width, initial-scale=1.0, maximum-scale=1.0" />

    <!-- bootstrap & fontawesome -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/tab.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/bootstrap.min.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/font-awesome.min.css" />

    <!-- page specific plugin styles -->

    <!-- text fonts -->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-fonts.css" />

    <!-- ace styles -->
    <link rel="stylesheet" href="<%=basePath%>/static/ace2/css/uncompressed/ace.css" id="main-ace-style" />

    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-part2.min.css" />
    <![endif]-->
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-skins.min.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-rtl.min.css" />
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/jquery.gritter.css" />
    <link rel="stylesheet" href="<%=basePath%>static/css/laydate.css" />
    <!--[if lte IE 9]>
    <link rel="stylesheet" href="<%=basePath%>static/ace2/css/ace-ie.min.css" />
    <![endif]-->

    <!-- inline styles related to this page -->

    <!-- ace settings handler -->
    <script src="<%=basePath%>static/ace2/js/uncompressed/ace-extra.js"></script>

    <!-- HTML5shiv and Respond.js for IE8 to support HTML5 elements and media queries -->

    <!--[if lte IE 8]>
    <script src="<%=basePath%>static/ace2/js/html5shiv.min.js"></script>
    <script src="<%=basePath%>static/ace2/js/respond.min.js"></script>
    <![endif]-->
</head>

<body class="no-skin">
<!-- /section:basics/navbar.layout -->
<div class="main-container" id="main-container">
    <script type="text/javascript">
        try{ace.settings.check('main-container' , 'fixed')}catch(e){}
    </script>


    <!-- /section:basics/sidebar -->
    <div class="main-content" style="margin: 0;">

        <!-- /section:basics/content.breadcrumbs -->
        <div class="page-content">

            <!-- /section:settings.box -->
            <div class="page-content-area">

                <div class="row">
                    <div class="col-xs-12">
                        <!-- PAGE CONTENT BEGINS -->

                        <div class="row">
                            <div class="col-xs-12">

                                <div>
                                    <div id="toolbar">
                                        <div class="form-inline">
                                            <button type="button" class="btn btn-default" id="addBtn">添加新品</button>
                                        </div>
                                    </div>
                                    <table class="table table-striped table-bordered table-hover dataTable no-footer" id="table" role="grid" aria-describedby="sample-table-2_info"
                                           data-toolbar="#toolbar"
                                           data-detail-view="false"
                                           data-detail-formatter="detailFormatter"
                                           data-minimum-count-columns="2"
                                           data-pagination="true"
                                           data-id-field="id"
                                           data-page-list="[10, 25, 50, 100, ALL]"
                                           data-show-footer="false"
                                           data-side-pagination="server"
                                           data-response-handler="responseHandler">
                                    </table>

                                </div>
                            </div>
                        </div>


                        <div id="modal-add" class="modal fade" tabindex="-1">
                            <div class="modal-dialog">
                                <div class="modal-content">
                                    <div class="modal-header no-padding">
                                        <div class="table-header">
                                            <button type="button" class="close" data-dismiss="modal" aria-hidden="true">
                                                <span class="white">&times;</span>
                                            </button>
                                            添加新品
                                        </div>
                                    </div>

                                    <div class="modal-body no-padding">
                                        <div>
                                            <div id="user-profile-1" class="user-profile row">
                                                <div class="col-xs-12 col-sm-12 col-sm-offset-0">

                                                    <!-- #section:pages/profile.info -->
                                                    <form id="skuNewForm">
                                                    <input type="hidden" name="id" id="skuNewId">
                                                    <div class="profile-user-info profile-user-info-striped">

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 商品 </div>

                                                            <div class="profile-info-value">
                                                                <select class="form-control" id="skuId" name="skuId">
                                                                </select>
                                                            </div>
                                                        </div>

                                                        <div class="profile-info-row">
                                                            <div class="profile-info-name"> 新品类型 </div>

                                                            <div class="profile-info-value">
                                                                <select class="form-control" id="skuNewType" name="type">
                                                                    <option value="0">世界市场新品</option>
                                                                    <option value="1">家族市场新品</option>
                                                                </select>
                                                            </div>
                                                        </div>

                                                    </div>
                                                    </form>

                                                </div>
                                            </div>
                                        </div>
                                    </div>

                                    <div class="modal-footer no-margin-top">
                                        <div class="col-xs-5 col-sm-5 col-sm-offset-4">
                                            <input id="gritter-light" checked="" type="checkbox" class="ace ace-switch ace-switch-5">
                                            <button class="btn btn-sm btn-danger pull-left" data-dismiss="modal">
                                                取消
                                            </button>
                                            <button class="btn btn-sm btn-info pull-left ok" id="submitSkuNewForm">
                                                确认
                                            </button>
                                        </div>
                                    </div>
                                </div><!-- /.modal-content -->
                            </div><!-- /.modal-dialog -->
                        </div><!-- PAGE CONTENT ENDS -->

                    </div><!-- /.col -->
                </div><!-- /.row -->
            </div><!-- /.page-content-area -->
        </div><!-- /.page-content -->
    </div><!-- /.main-content -->

    <a href="#" id="btn-scroll-up" class="btn-scroll-up btn btn-sm btn-inverse">
        <i class="ace-icon fa fa-angle-double-up icon-only bigger-110"></i>
    </a>
</div><!-- /.main-container -->

<!-- basic scripts -->

<!--[if !IE]> -->
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/ace2/js/jquery.min.js'>"+"<"+"/script>");
</script>

<!-- <![endif]-->

<!--[if IE]>
<script type="text/javascript">
    window.jQuery || document.write("<script src='<%=basePath%>static/ace2/js/jquery1x.min.js'>"+"<"+"/script>");
</script>
<![endif]-->
<script type="text/javascript">
    if('ontouchstart' in document.documentElement) document.write("<script src='<%=basePath%>static/ace2/js/jquery.mobile.custom.min.js'>"+"<"+"/script>");
</script>
<script src="<%=basePath%>static/ace2/js/bootstrap.min.js"></script>

<!-- page specific plugin scripts -->
<script src="<%=basePath%>static/ace2/js/jquery.dataTables.min.js"></script>
<script src="<%=basePath%>static/ace2/js/jquery.dataTables.bootstrap.js"></script>
<script src="<%=basePath%>static/ace2/js/jquery.gritter.min.js"></script>
<script src="<%=basePath%>static/ace2/js/uncompressed/bootbox.js"></script>
<script src="<%=basePath%>static/js/laydate.js"></script>
<script src="<%=basePath%>static/js/date-util.js"></script>
<script>
    laydate({
        elem: '#beginTime'
    });
    laydate({
        elem: '#endTime'
    });
</script>
<script>
    var $table = $('#table'),
            $remove = $('#remove'),
            selections = [];
    function initTable() {
        $table.bootstrapTable({
            url: '<%=basePath%>skuNew/list.do',
            //height: getHeight(),
            locale: 'zh-CN',
            striped: true,
            //multipleSearch: true,
            queryParamsType: 'pageNo',
            queryParams: function(params){
                if($('#orderCode').val()) params.orderCode = $('#orderCode').val();
                if($('#orderType').val()){
                    params.orderType = $('#orderType').val();
                }
                if($('#orderStatus').val()){
                    params.orderStatus = $('#orderStatus').val();
                }
                if($('#payStatus').val()){
                    params.payStatus = $('#payStatus').val();
                }
                if($('#shipStatus').val()){
                    params.shipStatus = $('#shipStatus').val();
                }
                if($('#payTypeId').val()){
                    params.payTypeId = $('#payTypeId').val();
                }
                if($('#beginTime').val()){
                    params.beginTime = $('#beginTime').val();
                }
                if($('#endTime').val()){
                    params.endTime = $('#endTime').val();
                }
                if($('#isCounting').val()){
                    params.isCounting = $('#isCounting').val();
                }
                return params;
            },
            rowStyle: function rowStyle(value, row, index) {
                return {
                    classes: 'text-nowrap another-class',
                    css: {}
                };
            },
            formatShowingRows: function (pageFrom, pageTo, totalRows) {
                return '当前显示 ' + pageFrom + " 到 " + pageTo + ', 总共 ' + totalRows;
            },
            formatRecordsPerPage: function (pageNumber) {
                return '每页显示' + pageNumber + '条数据';
            },
            formatSearch: function () {
                return "请输入关键字";
            },
            formatNoMatches: function () {
                return "没有找到数据哦!";
            },
            columns: [
                [
                    {
                        checkbox: true,
                        align: 'center',
                        valign: 'middle'
                    },
                    {
                        title: 'ID',
                        field: 'bo.id',
                        align: 'center',
                        valign: 'middle',
                        sortable: true,
                        footerFormatter: totalTextFormatter,
                        formatter: function(value, row, index){
                            if(row && row.id){
                                return row.id;
                            }
                        }
                    },
                    {
                        field: 'sku.name',
                        title: '商品',
                        sortable: true,
                        //editable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row && row.skuName){
                                return row.skuName;
                            }
                        }
                    },
                    {
                        field: 'sn.type',
                        title: '新品类型',
                        sortable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row && row.type == 0){
                                return '世界市场新品';
                            }
                            if(row && row.type == 1){
                                return '家族市场新品';
                            }
                        }
                    },
                    {
                        field: 'sn.sort',
                        title: '排序',
                        editable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row){
                                return row.sort;
                            }
                        }
                    },
                    {
                        field: 'sn.status',
                        title: '是否有效',
                        editable: true,
                        footerFormatter: totalNameFormatter,
                        align: 'center',
                        formatter: function(value, row, index){
                            if(row && row.status == 0){
                                return '无效';
                            }
                            if(row && row.status == 1){
                                return '有效';
                            }
                        }
                    },
                    {
                        title: '操作项',
                        align: 'center',
                        formatter: function(value, row, index){
                            return '<a class="edit" href="javascript:void(0);">编辑</a>';
                        },
                        events: {
                            'click .edit': function(e, value, row, index){
                                $.ajax({
                                    url: '<%=basePath%>skuNew/listSku.do',
                                    success: function (skus){
                                        skus = window.eval('('+skus+')');
                                        var options = '';
                                        for(var i in skus){
                                            if(skus[i].id == row.sku_id)
                                                options += '<option value="'+skus[i].id+'" selected>'+skus[i].name+'</option>';
                                            else
                                                options += '<option value="'+skus[i].id+'">'+skus[i].name+'</option>';
                                        }
                                        $('#skuId').html(options);
                                        $('#modal-add').modal('show');
                                    }
                                });
                                var optionsHtm  = row.type==0 ? '<option value="0" selected>世界市场新品</option>' : '<option value="0">世界市场新品</option>';
                                    optionsHtm += row.type==1 ? '<option value="1" selected>家族市场新品</option>' : '<option value="1">家族市场新品</option>';
                                $('#skuNewType').html(optionsHtm);
                                $('#skuNewId').val(row.id);
                                $('#modal-add').modal('show');
                            }
                        }
                    }
                ]
            ]
        });
        // sometimes footer render error.
        setTimeout(function () {
            $table.bootstrapTable('resetView');
        }, 200);
        $table.on('check.bs.table uncheck.bs.table ' +
                'check-all.bs.table uncheck-all.bs.table', function () {
            $remove.prop('disabled', !$table.bootstrapTable('getSelections').length);

            // save your data, here just save the current page
            selections = getIdSelections();
            // push or splice the selections if you want to save all data selections
        });
        $table.on('expand-row.bs.table', function (e, index, row, $detail) {
            $detail.html('数据加载中...');
            $.get('/user/load.shtml', {id: row.id}, function (res) {
                //$detail.html(res.replace(/\n/g, '<br>'));
                $detail.html(res);
            });
        });
        $table.on('editable-save.bs.table', function(e, fieId, row){
            var status = row.status;
            if(row['sn.status'] == '有效'){
                status = 1;
            }else if(row['sn.status'] == '无效'){
                status = 0;
            }

            $.ajax({
                url: '<%=basePath%>skuNew/addSkuNew.do',
                type: 'post',
                data: {id: row.id, sort: row['sn.sort'], status: status},
                success: function(result){
                    result = window.eval('('+result+')');
                    $.gritter.add({
                        title: '消息',
                        text: result.code=='success' ? '成功更新排序!' : '更新排序失败!',
                        class_name: 'gritter-success' + (!$('#gritter-light').get(0).checked ? ' gritter-light' : '')
                    });
                }
            })
        });
        $table.on('all.bs.table', function (e, name, args) {
            console.log(name, args);
        });
        $remove.click(function () {
            var ids = getIdSelections();
            console.log('remove: ' + ids);
            $table.bootstrapTable('remove', {
                field: 'id',
                values: ids
            });
            $remove.prop('disabled', true);
        });
        $(window).resize(function () {
            $table.bootstrapTable('resetView', {
                height: getHeight()
            });
        });

        $('#searchBtn').on('click', function(){
            $table.bootstrapTable('refresh');
        });
    }

    function getIdSelections() {
        return $.map($table.bootstrapTable('getSelections'), function (row) {
            return row.id
        });
    }

    function responseHandler(res) {
        $.each(res.rows, function (i, row) {
            row.state = $.inArray(row.id, selections) !== -1;
        });
            //订单类型
            if( res.orderTypeList !=null){
                var $select = $('#orderType');
                $select.empty();
                $select.append('<option value="" selected="selected">全部</option>')
                for(var i=0, len = res.orderTypeList.length;i<len;i++)  {
                    $select.append('<option value="'+res.orderTypeList[i].key+'">'+res.orderTypeList[i].value+'</option>');
                }
            }
            //支付方式
            if(res.payTypeList !=null){
                var $select = $('#payTypeId');
                $select.empty();
                $select.append('<option value="" selected="selected">全部</option>')
                for(var i=0, len = res.payTypeList.length;i<len;i++){
                    $select.append('<option value="'+res.payTypeList[i].key+'">'+res.payTypeList[i].value+'</option>');
                }
            }
            //订单状态
            if( res.orderStatusList !=null){
                var $select = $('#orderStatus');
                $select.empty();
                $select.append('<option value="" selected="selected">全部</option>')
                for(var i=0, len = res.orderStatusList.length;i<len;i++){
                    $select.append('<option value="'+res.orderStatusList[i].key+'">'+res.orderStatusList[i].value+'</option>');
                }
            }
            //物流状态
            if( res.wuliuList !=null){
                var $select = $('#shipStatus');
                $select.empty();
                $select.append('<option value="" selected="selected">全部</option>')
                for(var i=0, len = res.wuliuList.length;i<len;i++){
                    $select.append('<option value="'+res.wuliuList[i].key+'">'+res.wuliuList[i].value+'</option>');
                }
            }
        return res;
    }

    function detailFormatter(index, row) {
        var html = [];
        $.each(row, function (key, value) {
            html.push('<p><b>' + key + ':</b> ' + value + '</p>');
        });
        return html.join('');
    }

    function operateFormatter(value, row, index) {
        var arr = [];
        arr.push('&nbsp;<a class="edit" href="<%=basePath%>product/edit.shtml?skuId='+ row.comSku.id +'" title="Edit">编辑</a>');
        if(row.comSpu && row.comSpu.isSale == 0){
            arr.push('&nbsp;<a class="putaway" href="javascript:void(0)" title="Putaway">上架</a>');
        }else if(row.comSpu && row.comSpu.isSale == 1){
            arr.push('&nbsp;<a class="putaway" href="javascript:void(0)" title="Putaway">下架</a>');
        }

        return arr.join(' ');
    }

    function totalTextFormatter(data) {
        return 'Total';
    }

    function totalNameFormatter(data) {
        return data.length;
    }

    function totalPriceFormatter(data) {
        var total = 0;
        $.each(data, function (i, row) {
            total += +(row.price.substring(1));
        });
        return '$' + total;
    }

    function getHeight() {
        return $(window).height() - $('h1').outerHeight(true);
    }

    $(function () {
        var scripts = [
                    location.search.substring(1) || '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table.min.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table-export.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/tableExport.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-table-editable.js',
                    '<%=basePath%>static/class/bootstrap-3.3.5-dist/js/bootstrap-editable.js'
                ],
                eachSeries = function (arr, iterator, callback) {
                    callback = callback || function () {
                            };
                    if (!arr.length) {
                        return callback();
                    }
                    var completed = 0;
                    var iterate = function () {
                        iterator(arr[completed], function (err) {
                            if (err) {
                                callback(err);
                                callback = function () {
                                };
                            }
                            else {
                                completed += 1;
                                if (completed >= arr.length) {
                                    callback(null);
                                }
                                else {
                                    iterate();
                                }
                            }
                        });
                    };
                    iterate();
                };

        eachSeries(scripts, getScript, initTable);
    });

    function getScript(url, callback) {
        var head = document.getElementsByTagName('head')[0];
        var script = document.createElement('script');
        script.src = url;

        var done = false;
        // Attach handlers for all browsers
        script.onload = script.onreadystatechange = function () {
            if (!done && (!this.readyState ||
                    this.readyState == 'loaded' || this.readyState == 'complete')) {
                done = true;
                if (callback)
                    callback();

                // Handle memory leak in IE
                script.onload = script.onreadystatechange = null;
            }
        };

        head.appendChild(script);

        // We handle everything using the script element injection
        return undefined;
    }


    $('#addBtn').on('click', function(){
        loadSku();
    });

    function loadSku(){
        $.ajax({
            url: '<%=basePath%>skuNew/listSku.do',
            success: function (skus){
                skus = window.eval('('+skus+')');
                var options = '';
                for(var i in skus){
                    options += '<option value="'+skus[i].id+'">'+skus[i].name+'</option>';
                }
                $('#skuId').html(options);
                $('#skuNewId').val('');
                $('#modal-add').modal('show');
            }
        });
    }

    $('#submitSkuNewForm').on('click', function(){
        $.ajax({
            url: '<%=basePath%>skuNew/addSkuNew.do',
            data: $('#skuNewForm').serialize(),
            success: function(result){
                result = window.eval('('+result+')');
                if(result.code == 'success'){
                    $('#table').bootstrapTable('refresh');
                    $('#modal-add').modal('hide');
                }
                $.gritter.add({
                    title: '温馨提示',
                    text: result.msg,
                    class_name: result.code=='success' ? 'gritter-success' : 'gritter-error'
                });
            }
        })
    });


</script>
</body>
</html>
