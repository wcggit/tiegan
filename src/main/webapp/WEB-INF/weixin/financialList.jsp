<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/23
  Time: 上午10:22
  To change this template use File | Settings | File Templates.
--%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!doctype html>
<html lang="zh-cn">
<head>
    <meta name="viewport" content="initial-scale=1, user-scalable=0, minimal-ui" charset="UTF-8">
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <title>加载底部</title>
    <!-- UC强制全屏 -->
    <meta name="full-screen" content="yes">
    <!-- QQ强制全屏 -->
    <meta name="x5-fullscreen" content="true">
    <link rel="stylesheet" href="${resourceUrl}/css/dropload.css">
    <link rel="stylesheet" href="${resourceUrl}/css/payList.css"/>
</head>
<body>
<div class="content">
    <ul class="paySuccess" id="finanical">
    </ul>
</div>
<!-- jQuery1.7以上 或者 Zepto 二选一，不要同时都引用 -->
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/js/dropload.min.js"></script>
<script>
    $(function () {
        var page = 1;
        var totalPrice = 0;
        var date = null;
        var content = document.getElementById("finanical");
        // dropload
        $('.content').dropload({
                                   scrollArea: window,
                                   loadDownFn: function (me) {
                                       $.ajax({
                                                  type: 'GET',
                                                  url: '/wx/financial?page=' + page,
                                                  dataType: 'json',
                                                  success: function (data) {
                                                      page++;
                                                      var result = '';
                                                      var financials = data.data;
                                                      var flag = true;
                                                      if (financials.length != 0) {
                                                          for (var i = 0; i < 20; i++) {
                                                              if (page == 2 && flag) {
                                                                  flag = false;
                                                                  totalPrice =
                                                                  getMontlyIncome(financials[i].balanceDate);
                                                                  result +=
                                                                  '<li class="ttl"><span class="left">'
                                                                  + new Date(financials[i].balanceDate).format("yyyy年MM月")
                                                                  + '</span> <span class="right">转账金额：￥'
                                                                  + totalPrice / 100
                                                                  + '</span> </li>'
                                                              }
                                                              var currentDate =
                                                                      new Date(financials[i].balanceDate).getMonth();
                                                              if (date != null && date
                                                                                  != currentDate) {
                                                                  totalPrice =
                                                                  getMontlyIncome(financials[i].balanceDate);
                                                                  result +=
                                                                  '<li class="ttl"><span class="left">'
                                                                  + new Date(financials[i].balanceDate).format("yyyy年MM月")
                                                                  + '</span> <span class="right">转账金额：￥'
                                                                  + totalPrice / 100
                                                                  + '</span> </li>'
                                                              }
                                                              date = currentDate;
                                                              result +=
                                                              '<li class="info"><input class="id-hidden" type="hidden" value="'
                                                              + financials[i].statisticId
                                                              + '"><span class="left">'
                                                              + new Date(financials[i].balanceDate).format("MM月dd日")
                                                              + '</span> <span class="right">￥'
                                                              + financials[i].transferPrice / 100
                                                              + '</span>';
                                                              if (financials[i].state == 0) {
                                                                  result +=
                                                                  '<p class="font-orange">转账中</p></li>';
                                                              } else {
                                                                  result += '<p>已到账</p></li>';
                                                              }

                                                              if (i + 1 == financials.length
                                                                  && financials.length < 20) {
                                                                  // 锁定
                                                                  me.lock();
                                                                  // 无数据
                                                                  me.noData();
                                                                  break;
                                                              }
                                                          }

                                                          content.innerHTML += result;

                                                          $(".info").each(function (i) {
                                                              $(".info").eq(i).bind("click",
                                                                                    function () {
                                                                                        var id = $(this).find(".id-hidden").val();
                                                                                        location.href =
                                                                                        "/wx/financial/"
                                                                                        + id;
                                                                                    });
                                                          });
                                                      } else {
                                                          me.lock();
                                                          // 无数据
                                                          me.noData();
                                                      }

                                                      // 为了测试，延迟1秒加载
                                                      setTimeout(function () {
                                                          // 每次数据加载完，必须重置
                                                          me.resetload();
                                                      }, 0);
                                                  }

                                                  ,
                                                  error: function (xhr, type) {
                                                      console.log('Ajax error!');
                                                      // 即使加载出错，也得重置
                                                      me.resetload();
                                                  }
                                              });
                                   }
                               });
    });
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
                   url: "/wx/sumMonthlyFinancialIncome?date=" + date,
                   async: false,
                   contentType: "application/json",
                   success: function (data) {
//                       location.href = "/manage/topic";
                       totalPrice = data.data;

                   }
               });
        return totalPrice;
    }
</script>
</body>
</html>
