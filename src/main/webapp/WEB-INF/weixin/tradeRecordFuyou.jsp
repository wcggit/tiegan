<%--
  Created by IntelliJ IDEA.
 User: lss
  Date: 17/3/19
  Time: 上午11:40
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
    <link rel="stylesheet" href="${lepayNew}/css/tradeRecord2.css"/>
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
            <p class="store-name">
                <span  class="icon-store"></span>
                <span id="merchantName"></span>
                <span class="btn" id="show" onclick="merchantChange()">切换门店</span>
            </p>
        </div>
        <ul class="store-detail">
            <li>
                <div>
                    <p>今日总流水（元）</p>
                    <p>${integerList[0]/100.0}</p>
                </div>
            </li>
            <li>
                <div>
                    <p>今日总入账（元）</p>
                    <p>${integerList[1]/100.0}</p>
                </div>
            </li>
        </ul>
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
            <p>筛选后</p>
            <p>共35笔订单／¥385流水／手续费（折扣）¥35／应入账<span class="">¥350</span></p>
        </div>
        <div class="order-out-div">
            <div class="order-in-div"  id="paySuccess"></div>
        </div>
    </div>
</div>
<script>
    function currentOutHeight() {
        $('.order-out-div').height(($('body').get(0).offsetHeight-$('.top').get(0).offsetHeight-$('.select-tab').get(0).offsetHeight-$('.select-result').get(0).offsetHeight)+'px');
    }
    currentOutHeight();
    var content = document.getElementById("paySuccess");
    var content2 = document.getElementById("someMessage");
    var ScanCodeOrderCriteria = {};
    ScanCodeOrderCriteria.offset = 1;
    var timeString=dateRange(0);
    arr1 =timeString.split("-");
    var startDate1=arr1[0];
    var endDate1=arr1[1];
    ScanCodeOrderCriteria.startDate=startDate1;
    ScanCodeOrderCriteria.endDate=endDate1;
    $(function () {
                getScanCodeOrderByAjax(ScanCodeOrderCriteria);
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
            ScanCodeOrderCriteria.offset++;
            getScanCodeOrderByAjax(ScanCodeOrderCriteria);
        }
    })


    function getScanCodeOrderByAjax(ScanCodeOrderCriteria) {
        $.ajax({
            type: "post",
            url: "/wx/scanCodeOrder",
            async: false,
            data: JSON.stringify(ScanCodeOrderCriteria),
            contentType: "application/json",
            success: function (data) {
                content2.innerHTML = "<p>筛选后</p><p>共" + data.data.integerList[0] + "笔订单／流水¥" + data.data.integerList[1] / 100 + "／手续费(折扣)¥" + data.data.integerList[3] / 100 + "／应入账<span>¥" + data.data.integerList[2] / 100 + "</span></p>";
                currentOutHeight();
                var result = '';
                var orders = data.data.content;
                var date = null;
                $("#merchantName").html(data.data.merchantName);
                if (orders.length != 0) {
                    for (var i = 0; i < orders.length; i++) {

                        var currentDate =
                            new Date(orders[i].completeDate).getMonth();

                        date = currentDate;



                        result+='<li class="info"><input  class="id-hidden" type="hidden" value="'+ orders[i].orderSid+'"> <div class="order-list order-ordinary"> '  +
                            '<p>'
                        if (orders[i].orderType.id == 12004||orders[i].orderType.id==12005) {
                            result +=
                                '<span class="left">乐加订单</span>';
                        } else{
                            if (orders[i].orderType.id==12006||orders[i].orderType.id==12002){
                                result += ' <span class="left">普通订单</span> <span class="icon-member">会员</span>';
                            }else {
                                result +=
                                    '<span class="left">普通订单</span>';
                            }

                        }
                        result += '  <span class="right">¥'+ orders[i].totalPrice / 100+'</span>  ' ;
                        result += '</p><p>乐付确认码：'+orders[i].lePayCode+'</p>' ;
                        result += '<p>'+new Date(orders[i].completeDate).format("MM月dd日")+'&nbsp;&nbsp;&nbsp;'+new Date(orders[i].completeDate).format("HH:mm:ss")+'</p>' ;
                        result += ' </div></li>';
                    }
                    content.innerHTML += result;
                    $(".info").each(function (i) {
                        $(".info").eq(i).bind("click",
                            function () {
                                var id = $(this).find(".id-hidden").val();
                                location.href =
                                    "/wx/scanCodeOrder/"
                                    + id;
                            });
                    });
                }
            }
        });
    }

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

                ScanCodeOrderCriteria.startDate=startDate1;
                ScanCodeOrderCriteria.endDate=endDate1;

                ScanCodeOrderCriteria.offset=1;

                content.innerHTML="";


                getScanCodeOrderByAjax(ScanCodeOrderCriteria);
            },300);
        })
    });
    //        自定义时间
    $('.content-timeRange li:last-child').on('touchstart',function () {
        $('.select-tab .select-item:first-child div').empty().text($(this).text());
    });
    //        选择订单类型
    $('.content-type li').each(function (k) {
        $('.content-type li').eq(k).on('touchstart',function () {
            $('.select-tab .select-item:last-child div').empty().text($(this).text());
            setTimeout(function () {

                setTimeout(function () {
                    selectInit();
                },100)

                if(k==1||k==2){
                    ScanCodeOrderCriteria.orderType=k;
                }
                if(k==0){
                    ScanCodeOrderCriteria.orderType=null;
                }
                ScanCodeOrderCriteria.offset=1;
                content.innerHTML="";
                getScanCodeOrderByAjax(ScanCodeOrderCriteria);
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
        ScanCodeOrderCriteria.startDate=startDate1;
        ScanCodeOrderCriteria.endDate=endDate1;
        ScanCodeOrderCriteria.offset=1;
        content.innerHTML="";
        getScanCodeOrderByAjax(ScanCodeOrderCriteria);
    }
     function merchantChange () {
        location.href= "/wx/merchantChange";
}

</script>
</body>
</html>
