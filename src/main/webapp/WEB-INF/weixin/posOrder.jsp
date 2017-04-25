<%--
  Created by IntelliJ IDEA.
  User: lss
  Date: 2017/4/24
  Time: 10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!doctype html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no"/>
    <title></title>
    <link href="${lepayNew}/framework/mobiscroll_1.css" rel="stylesheet" type="text/css">
    <link href="${lepayNew}/framework/mobiscroll_2.css" rel="stylesheet" type="text/css">
    <link rel="stylesheet" href="${lepayNew}/framework/common.css"/>
    <link rel="stylesheet" href="${lepayNew}/css/POSOrder.css"/>
    <script src="${lepayNew}/framework/jquery-2.0.3.min.js"></script>
    <script src="${lepayNew}/framework/date.js"></script>
    <script src="${lepayNew}/framework/mobiscroll_1.js" type="text/javascript"></script>
    <script src="${lepayNew}/framework/mobiscroll.js" type="text/javascript"></script>
    <script src="${resourceUrl}/js/dropload.min.js"></script>
</head>
<body>
<div class="main">
    <!--上半部分——详情-->
    <div class="top">
        <div class="store-info">
            <span class="btn"   onclick="merchantChange()">切换门店</span>
            <span class="icon-store"></span>
            <p id="merchantName" class="store-name">${merchant.name}</p>
        </div>
    </div>
    <%--<!--下半部分——列表-->--%>
    <div class="bottom">
        <div class="select-wrapper">
            <div class="select-tab">
                <div class="select-item">
                    <div class="select-down">本月</div>
                </div>
                <div class="select-line"></div>
                <div class="select-item">
                    <div class="select-down">全部类型</div>
                </div>
            </div>
            <div class="select-content">
                <ul class="content-timeRange">
                    <li>本月</li>
                    <li>上月</li>
                    <li>今日</li>
                    <li>昨日</li>
                    <li>过去7天</li>
                    <li>
                        自定义时间
                        <input value="" readonly="readonly" name="appDateTime" id="appDateTime" type="text">
                    </li>
                </ul>
                <ul class="content-type">
                    <li>全部类型</li>
                    <li>普通订单</li>
                    <li>乐加订单</li>
                </ul>
            </div>
        </div>
        <div class="select-result" id="someMessage">
            <p>筛选后</p><p>共<span>2</span>笔订单，<span>¥385</span>流水</p>
        </div>
        <div class="order-out-div">
        <div class="order-in-div"  id="posOrderList">
            <%--<div class="order-list">--%>
                <%--<div class="icon icon-card"></div>--%>
                <%--<div class="desc">--%>
                    <%--<p class="clearfix"><span class="left">刷卡支付</span><span class="right origin-color">乐加订单</span></p>--%>
                    <%--<p class="clearfix"><span class="left">2016-03-14 12:20:30</span><span class="right">￥100.00</span></p>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="order-list">--%>
                <%--<div class="icon icon-wc"></div>--%>
                <%--<div class="desc">--%>
                    <%--<p class="clearfix"><span class="left">刷卡支付</span><span class="right">乐加订单</span></p>--%>
                    <%--<p class="clearfix"><span class="left">2016-03-14 12:20:30</span><span class="right">￥100.00</span></p>--%>
                <%--</div>--%>
            <%--</div>--%>
            <%--<div class="order-list">--%>
                <%--<div class="icon icon-zfb"></div>--%>
                <%--<div class="desc">--%>
                    <%--<p class="clearfix"><span class="left">刷卡支付</span><span class="right origin-color">乐加订单</span></p>--%>
                    <%--<p class="clearfix"><span class="left">2016-03-14 12:20:30</span><span class="right">￥100.00</span></p>--%>
                <%--</div>--%>
            <%--</div>--%>
        </div>
    </div>
    </div>
</div>
<script>
    function currentOutHeight() {
        $('.order-out-div').height(($('body').get(0).offsetHeight-$('.top').get(0).offsetHeight-$('.select-tab').get(0).offsetHeight-$('.select-result').get(0).offsetHeight)+'px');
    }
    currentOutHeight();
    var content = document.getElementById("posOrderList");
    var content2 = document.getElementById("someMessage");
    var unionPosOrderCriteria = {};
    unionPosOrderCriteria.offset = 1;
    
    var timeString=dateRange(0);
    arr1 =timeString.split("-");
    var startDate1=arr1[0];
    var endDate1=arr1[1];
    unionPosOrderCriteria.startDate=startDate1;
    unionPosOrderCriteria.endDate=endDate1;
    
    
    $(function () {
        getPosOrderByAjax(unionPosOrderCriteria);
//        上拉加载
        var outHeight=$('.order-out-div').height();
        $('.order-out-div').on('touchend',function () {
            refresh()
        })
        function refresh() {
            var inHeight=$('.order-in-div').height();
            var scrollHeight=$('.order-out-div').scrollTop();
            console.log(inHeight+"-"+outHeight+"-"+scrollHeight);
            var finalHeight=inHeight-outHeight-scrollHeight;
            if(finalHeight<=10){
                refreshFun();
            }
        }
        function refreshFun() {
            unionPosOrderCriteria.offset++;
            getPosOrderByAjax(unionPosOrderCriteria);
        }
    })


    Date.prototype.format = function (fmt) {
        var o = {
            "M+": this.getMonth() + 1, //月份
            "d+": this.getDate(), //日
            "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
            "H+": this.getHours(), //小时
            "m+": this.getMinutes(), //分
            "s+": this.getSeconds(), //秒
            "q+": Math.floor((this.getMonth() + 3) / 3), //季度
            "S": this.getMilliseconds() //毫秒
        };
        var week = {
            "0": "\u65e5",
            "1": "\u4e00",
            "2": "\u4e8c",
            "3": "\u4e09",
            "4": "\u56db",
            "5": "\u4e94",
            "6": "\u516d"
        };
        if (/(y+)/.test(fmt)) {
            fmt = fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
        }
        if (/(E+)/.test(fmt)) {
            fmt =
                fmt.replace(RegExp.$1,
                    ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f" : "\u5468")
                        : "") + week[this.getDay() + ""]);
        }
        for (var k in o) {
            if (new RegExp("(" + k + ")").test(fmt)) {
                fmt =
                    fmt.replace(RegExp.$1, (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr((""
                    + o[k]).length)));
            }
        }
        return fmt;
    }



    var settingDateRange='';
    function hideDate(){
        settingDateRange=$('.dwv').text();
        $('.btn-next').css({'display':'none'});
        $('.date-ttl').empty().text('【终止时间】');
    };

    //        点击两个选择按钮
    $('.select-tab .select-item').each(function (i) {
        $('.select-tab .select-item').eq(i).on('touchstart',function () {
            selectInit();
            $('.select-tab .select-item').eq(i).find('div').removeAttr('class').addClass('select-up');
            if(i==0){
                if($('.select-content ul').eq(0).height()==0){
                    $('.select-content ul').eq(0).animate({'height':'293px'});
                }else {
                    $('.select-content ul').eq(0).animate({'height':'0px'});
                }

            } else if(i==1){
                if($('.select-content ul').eq(1).height()==0){
                    $('.select-content ul').eq(1).animate({'height':'146px'});
                }else {
                    $('.select-content ul').eq(1).animate({'height':'0px'});
                }
            }
        })
    });
    //        选择时间范围
    $('.content-timeRange li:not(:last-child)').each(function (j) {
        $('.content-timeRange li').eq(j).on('touchstart',function () {
            $('.select-tab .select-item:first-child div').empty().text($(this).text());
            setTimeout(function () {
                selectInit();
                var arr1 = new Array();
                var timeString=dateRange(j);
                arr1 =timeString.split("-");
                var startDate1=arr1[0];
                var endDate1=arr1[1];
                unionPosOrderCriteria.startDate=startDate1;
                unionPosOrderCriteria.endDate=endDate1;
                unionPosOrderCriteria.offset=1;
                content.innerHTML="";
                getPosOrderByAjax(unionPosOrderCriteria);
            },300);
        })
    })
    //        自定义时间
    $('.content-timeRange li:last-child').on('touchstart',function () {
        $('.select-tab .select-item:first-child div').empty().text($(this).text());
    });
    //        选择订单类型
    $('.content-type li').each(function (k) {
        $('.content-type li').eq(k).on('touchstart',function () {
            $('.select-tab .select-item:last-child div').empty().text($(this).text());
            setTimeout(function () {
                selectInit();
                if(k==1||k==2){
                    unionPosOrderCriteria.rebateWay=k;
                }
                if(k==0){
                    unionPosOrderCriteria.rebateWay=null;
                }
                unionPosOrderCriteria.offset=1;
                content.innerHTML="";
                getPosOrderByAjax(unionPosOrderCriteria);
            },300);
        })
    })
    //        样式初始化
    function selectInit() {
        $('.select-tab .select-item div').removeAttr('class').addClass('select-down');
        $('.select-content ul').css({'height':'0'});
    }


    //        时间选择器
    var currYear = (new Date()).getFullYear();
    var opt={};
    opt.datetime = {preset : 'datetime'};
    opt.default = {
        theme: 'android-ics light', //皮肤样式
        display: 'modal', //显示方式
        mode: 'scroller', //日期选择模式
        dateFormat: 'yyyy/mm/dd',
        timeFormat: 'HH:00:00',
        lang: 'zh',
        showNow: false,//不显示现在按钮
        startYear: 2016, //开始年份
        endYear: currYear, //结束年份
        onSelect:function(valueText,inst){
            getTime(valueText,inst);
            selectInit();
        }
    };

    var optDateTime = $.extend(opt['datetime'], opt['default']);
    $("#appDateTime").mobiscroll(optDateTime).datetime(optDateTime);
    //时间选择器获取之后的函数
    function getTime(valueText,inst) {
        settingDateRange+='-'+$('#appDateTime').val();
        var arr1 = new Array();
        arr1 =settingDateRange.split("-");
        var startDate1=arr1[0];
        var endDate1=arr1[1];
        unionPosOrderCriteria.startDate=startDate1;
        unionPosOrderCriteria.endDate=endDate1;
        unionPosOrderCriteria.offset=1;
        content.innerHTML="";
        getPosOrderByAjax(unionPosOrderCriteria);
    }
    function merchantChange () {
        location.href= "/wx/merchantChange?data=/wx/posOrderList";
    }

    function getPosOrderByAjax(unionPosOrderCriteria) {



        $.ajax({
            type: "post",
            url: "/wx/getPosOrderByAjax",
            async: false,
            data: JSON.stringify(unionPosOrderCriteria),
            contentType: "application/json",
            success: function (data) {
                content2.innerHTML="<p>筛选后</p><p>共<span>"+data.data.integerList[0]+"</span>笔订单，<span>¥"+data.data.integerList[1]/100+"</span>流水</p>";
                var result = '';
                var page = data.data.page;
                var date = null;
                var posOrders = page.content;
                if (posOrders.length!= 0){
                    for (var i = 0; i < posOrders.length; i++) {
                          if(posOrders[i].channel==0){
                              if(posOrders[i].rebateWay==1||posOrders[i].rebateWay==3){
                                  result+=' <div class="order-list"> <div class="icon icon-card"></div> <div class="desc"> '+
                                      '<p class="clearfix"><span class="left">刷卡支付</span><span class="right origin-color">乐加订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                      ' </div></div>'
                              }else{
                                  result+=' <div class="order-list"> <div class="icon icon-card"></div> <div class="desc"> '+
                                      '<p class="clearfix"><span class="left">刷卡支付</span><span class="right">普通订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                      ' </div></div>'
                              }
                          }
                          if(posOrders[i].channel==1){
                              if(posOrders[i].rebateWay==1||posOrders[i].rebateWay==3){
                                  result+=' <div class="order-list"> <div class="icon icon-wc"></div> <div class="desc"> '+
                                      '<p class="clearfix"><span class="left">微信支付</span><span class="right origin-color">乐加订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                 ' </div></div>'
                              }else{
                                  result+=' <div class="order-list"> <div class="icon icon-wc"></div> <div class="desc"> '+
                                      '<p class="clearfix"><span class="left">微信支付</span><span class="right">普通订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                      ' </div></div>'
                              }
                          }
                        if(posOrders[i].channel==2){
                            if(posOrders[i].rebateWay==1||posOrders[i].rebateWay==3){
                                result+=' <div class="order-list"> <div class="icon icon-zfb"></div> <div class="desc"> '+
                                    '<p class="clearfix"><span class="left">支付宝支付</span><span class="right origin-color">乐加订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                    ' </div></div>'
                            }else{
                                result+=' <div class="order-list"> <div class="icon icon-zfb"></div> <div class="desc"> '+
                                    '<p class="clearfix"><span class="left">支付宝支付</span><span class="right">普通订单</span></p> <p class="clearfix"><span class="left">'+new Date(posOrders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(posOrders[i].completeDate).format("HH:mm:ss")+'</span><span class="right">￥'+posOrders[i].totalPrice/100+'</span></p> '+
                                    ' </div></div>'
                            }
                        }
                    }
                    content.innerHTML+= result;
                }

            }});
    }






</script>
</body>
</html>
