<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/19
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
    <div class="bottom order-out-div">
        <ul id="financialList" class="order-in-div"></ul>
    </div>
</div>
<script>
    $('.order-out-div').height(($('body').get(0).offsetHeight-$('.top').get(0).offsetHeight)+'px');
    var content = document.getElementById("financialList");

    var page = 1;
    var totalPrice = 0;
    var date = null;
    var flag = true;

    $(function () {
        getFinancialListByAjax();
//        上拉加载
        var outHeight=$('.order-out-div').height();
        $('.bottom').on('touchend',function () {
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
            page++;
            getFinancialListByAjax();
        }
    })



    function getFinancialListByAjax() {
        $.ajax({
            type: 'GET',
            url: '/wx/financial?page=' + page,
            dataType: 'json',
            success: function (data) {
                $("#merchantName").html(data.data.merchantName);

                var result = '';
                var financials = data.data.content;
                if (financials.length != 0) {
                    for (var i = 0; i < financials.length; i++) {
                        var currentDate =
                            new Date(financials[i].balanceDate).getMonth()+1;

                        if (page == 1 && flag) {
                            flag = false;
                            totalPrice =
                                getMontlyIncome(financials[i].balanceDate);
                            result +=
                                '<li class="date-ttl"><p><span class="left">'
                                + new Date(financials[i].balanceDate).format("yyyy年MM月")
                                + '</span><span class="right">转账金额：￥'
                                + totalPrice / 100
                                + '</span></p> </li>'
                        }

                        if (date != null && date
                            != currentDate) {
                            totalPrice =
                                getMontlyIncome(financials[i].balanceDate);
                            result +=
                                '<li class="date-ttl"><p><span class="left">'
                                + new Date(financials[i].balanceDate).format("yyyy年MM月")
                                + '</span><span class="right">转账金额：￥'
                                + totalPrice / 100
                                + '</span></p> </li>'
                        }
                        date = currentDate;


                        if (financials[i].state == 0) {
                            result +=' <ul class="lss"><li class="order-list list-ing">';
                            result +=
                                '<p><span class="left">转账中</span><span class="right">¥'+(financials[i].transferPrice+financials[i].appTransfer+financials[i].posTransfer) / 100+'</span></p>';
                        } else {
                            result +=' <ul class="lss"><li class="order-list list-ed">';
                            result +='<p><span class="left">已到账</span><span class="right">¥'+(financials[i].transferPrice+financials[i].appTransfer+financials[i].posTransfer) / 100+'</span></p>';
                        }

                        result +=
                        '<p><span class="left">'+new Date(financials[i].balanceDate).format("MM月dd日")+'</span></p>' +
                        '</li><span class="id-hidden" style="display:none">'+ financials[i].statisticId+'</span></ul>';
                    }

                    content.innerHTML += result;

                    $(".lss").each(function (i) {
                        $(".lss").eq(i).bind("click",
                            function () {
                                var id = $(this).find(".id-hidden").text();
                                location.href =
                                    "/wx/financial/"
                                    + id;
                            });
                    });
//                    $(".list-ed").each(function (i) {
//                        $(".list-ed").eq(i).bind("click",
//                            function () {
//                                var id = $(this).find(".id-hidden").text();
//                                location.href =
//                                    "/wx/financial/"
//                                    + id;
//                            });
//                    });
                }
            }
        })
    }


    function getMontlyIncome(date) {
        var totalPrice = null;
        $.ajax({
            type: "get",
            url: "/wx/sumMonthlyFinancialIncome?date=" + date,
            async: false,
            contentType: "application/json",
            success: function (data) {
                totalPrice = data.data;

            }
        });
        return totalPrice;
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

    function merchantChange () {
        location.href= "/wx/merchantChange?data=/wx/financialList";
    }
    function post(URL, PARAMS) {
        var temp = document.createElement("form");
        temp.action = URL;
        temp.method = "post";
        temp.style.display = "none";
        for (var x in PARAMS) {
            var opt = document.createElement("textarea");
            opt.name = x;
            opt.value = PARAMS[x];
            // alert(opt.name)
            temp.appendChild(opt);
        }
        document.body.appendChild(temp);
        temp.submit();
        return temp;
    }
</script>
</body>
</html>
