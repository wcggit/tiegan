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
    <link rel="stylesheet" href="${lepayNew}/framework/common.css"/>
    <link rel="stylesheet" href="${lepayNew}/css/dailyOrder.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <!--上半部分——详情-->
    <div class="top">
        <div class="store-info">
            <span class="btn"   onclick="merchantChange()">切换门店</span>
            <span class="icon-store"></span>
            <p id="merchantName" class="store-name"></p>
        </div>
        <ul class="store-detail">
            <li>
                <div>
                    <p>累积入账（元）</p>
                    <p>${total/100.0}</p>
                </div>
            </li>
            <li>
                <div>
                    <p>日均入账（元）</p>
                    <p>${average/100.0}</p>
                </div>
            </li>
        </ul>
    </div>
    <!--下半部分——列表-->
    <ul class="bottom order-in-div" id="financialList">

        <%--<!--日期-->--%>
        <%--<li class="date-ttl">--%>
            <%--<p>--%>
                <%--<span class="left">2016年11月</span>--%>
                <%--<span class="right">到账金额：¥10900.02</span>--%>
            <%--</p>--%>
        <%--</li>--%>
        <%--<!--订单列表-->--%>
        <%--<li class="order-list list-no">--%>
            <%--<p><span class="left">11月29日</span><span class="right">¥580.00</span></p>--%>
            <%--<p><span class="right">（微信支付¥580.00）</span></p>--%>
        <%--</li>--%>
    </ul>
</div>
<script>
    var content = document.getElementById("financialList");
    var page = 1;
    var date = null;
    var totalPrice = 0;
    var flag = true;
    $(function () {
        getFinancialListByAjax();
//        上拉加载
        var outHeight=$('.bottom').height();
        $('.bottom').on('touchend',function () {
            refresh()
        })
        function refresh() {
            var inHeight=$('.order-in-div').height();
            var scrollHeight=$('.bottom').scrollTop();
            console.log(inHeight+"-"+outHeight+"-"+scrollHeight);
            var finalHeight=inHeight-outHeight-scrollHeight;
            if(finalHeight<=10){
                refreshFun();
            }
        }
        function refreshFun() {
            page++;
            getFinancialListByAjax();
        }
    })
    function getFinancialListByAjax() {
        $.ajax({
            type: 'GET',
            url: '/wx/fuyouFinancial?page=' + page,
            dataType: 'json',
            success: function (data) {
                $("#merchantName").html(data.data.merchantName);
                var result = '';
                var financials = data.data.content;
                if (financials.length != 0) {

                    for (var i = 0; i < financials.length; i++) {

                        var dateStr=financials[i].tradeDate;
                        var dateStr1=dateStr.substring(4,6);
                        var dateStr2=dateStr.substring(6,8);
                        var dateStr3=dateStr.substring(0,4);
                        var m1=dateStr1.substring(0,1);
                        var m2=dateStr1.substring(1,2);
                        var currentDate=0;
                        if(m1==0){
                            currentDate=m2;
                        }else {
                            currentDate=dateStr1;
                        }
                        if (page == 1 && flag) {
                            flag = false;
                            totalPrice =
                                getMontlyIncome(dateStr3+"-"+dateStr1+"-"+dateStr2);
                            result +=
                                '<li class="date-ttl"><p><span class="left">'
                                + dateStr3+"年"+dateStr1+"月"
                                + '</span><span class="right">到账金额：￥'
                                + totalPrice / 100
                                + '</span></p> </li>'
                        }

                        if (date != null && date
                            != currentDate) {
                            totalPrice =
                                getMontlyIncome(dateStr3+"-"+dateStr1+"-"+dateStr2);
                            result +=
                                '<li class="date-ttl"><p><span class="left">'
                                + dateStr3+"年"+dateStr1+"月"
                                + '</span><span class="right">到账金额：￥'
                                + totalPrice / 100
                                + '</span></p> </li>'
                        }
                        date = currentDate;
                        result +=
                            '<li class="order-list list-ing"> <ul><p><span class="left">'
                            + dateStr1+"月"+dateStr2+"日"
                            + '</span> <span class="right">￥'
                            + (financials[i].transferMoney + financials[i].refundMoney) / 100
                            + '</span></p>  <input class="id-hidden" type="hidden" value="' + financials[i].orderSid + '"> </ul>';
                    }


                }
                content.innerHTML += result;
                $(".list-ing").each(function (i) {
                    $(".list-ing").eq(i).bind("click",
                            function () {
                                var id = $(this).find(".id-hidden").val();
                                location.href =
                                        "/wx/ScanCodeMerchantStatement/"
                                        + id;
                            });
                });
            }
        })
    }
    function merchantChange () {
        location.href= "/wx/merchantChange?data=/wx/financialList";
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
    function getMontlyIncome(date) {
        var totalPrice = null;
        $.ajax({
            type: "get",
            url: "/wx/sumMonthlyFuyouFinancialIncome?date=" + date,
            async: false,
            contentType: "application/json",
            success: function (data) {
                 totalPrice = data.data.totalPriceByMonth;
            }
        });
        return totalPrice;
    }
</script>
</body>
</html>