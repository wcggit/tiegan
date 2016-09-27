<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2016/9/26
  Time: 16:58
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/commen.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"><!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no"><!--不显示拨号链接-->
    <title>Title</title>
    <link rel="stylesheet" href="${resourceUrl}/css/firstRed.css">
    <link rel="stylesheet" href="${resourceUrl}/css/dropload.css">
</head>
<body>
<!--第一种情况-->
<div class="first-red1">
    <div class="top">
        <div class="face-img"><img src="${headImage}" alt=""></div>
        <p class="nickName">${nickName}</p>
        <p class="accountSource">${merchantName}</p>
    </div>
    <ul class="money">
        <li><span><font>${noRebate/100.0}</font> 元</span><br><span>待发放奖金</span></li>
        <li><span><font>${totalRebate/100.0}</font> 元</span><br><span>累计奖金</span></li>
    </ul>
    <p class="ttl">奖金满${minMoney/100}元后，乐加会以微信红包的形式发放给您</p>
     <div class="content">
        <ul class="lists" id="awardList">
        </ul>
    </div>
</div>
<!--第二种情况-->
<div class="first-red2">
    <p class="ttl">您的店铺没有参与活动呦</p>
</div>
<!--第三种情况-->
<div class="first-red3">
    <p class="ttl">只有店主账号才能查看福利明细<br>如有疑问请联系乐加客服：400-0412-800</p>
</div>

<!-- jQuery1.7以上 或者 Zepto 二选一，不要同时都引用 -->
<script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
<script src="${resourceUrl}/js/dropload.min.js"></script>
<script>
    var content = document.getElementById("awardList");
    $(function () {
        // 每页展示10个
        var page = 1;
        $('.content').dropload({
                                   scrollArea: window,
                                   loadDownFn: function (me) {
                                       $.ajax({
                                                  type: 'GET',
                                                  url: '/wx/merchant_award_list?page=' + page,
                                                  async: false,
                                                  dataType: 'json',
                                                  success: function (data) {
                                                      page++;
                                                      var result = '';
                                                      var orders = data.data.content;
                                                      //  var flag = true;
                                                      if (orders.length != 0) {
                                                         for (var i = 0; i < 10; i++) {
//                                                             if (page == 2 && flag) {
//                                                                  flag = false;
                                                                  result +=
                                                                    '<li><p>订单编号：'+orders[i].offLineOrder.orderSid+'<span>+'+(orders[i].rebate/100.0)+'</span>'
                                                                    + '<p>'+new Date(orders[i].createdDate).format("yyyy年MM月dd日 HH:mm:ss")+'</p>';
                                                                  if(orders[i].state==0) {
                                                                      result += '<p class="font-red">未结算</p>';
                                                                  }else if(orders[i].state==1) {
                                                                      result += '<p>已结算</p>';
                                                                  }
                                                                  result += '</li>';
//                                                              }
                                                              if (i + 1 == orders.length && orders.length < 10) {
                                                                 // 锁定
                                                                 me.lock();
                                                                 // 无数据
                                                                 me.noData();
                                                                 break;
                                                              }
                                                         }
                                                         content.innerHTML += result;
                                                      }else {
                                                          me.lock();
                                                          // 无数据
                                                          me.noData();
                                                      }
                                                      // 为了测试，延迟1秒加载
                                                      setTimeout(function () {
                                                          me.resetload();
                                                      }, 0);
                                                  },
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
</script>

</body>
</html>
