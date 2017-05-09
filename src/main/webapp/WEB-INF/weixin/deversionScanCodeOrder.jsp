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
    <link rel="stylesheet" href="${lepayNew}/css/order.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <ul class="out-div top">
        <li>
            <span class="head-pic"><img src="${scanCodeOrder.leJiaUser.weiXinUser.headImageUrl}" alt=""></span>
            <p class="member-single">【乐加会员】</p>
            <p class="personal">${scanCodeOrder.leJiaUser.weiXinUser.nickname}   ${scanCodeOrder.leJiaUser.phoneNumber}</p>
            <span class="icon icon-deversion">导流订单</span>
        </li>
        <li class="list">
            <div class="left">消费金额</div>
            <div class="right">
                <p>¥${scanCodeOrder.totalPrice/100}</p>
                <p></p>
            </div>
        </li>
        <li class="list">
            <div class="left">乐加折扣</div>
            <div class="right">${(100-scanCodeOrder.merchantRate)/10}/折</div>
        </li>
        <li class="list">
            <div class="left">实际到账</div>
            <div class="right">
                <p>¥${scanCodeOrder.transferMoney/100}</p>
                <p></p>
            </div>
        </li>
    </ul>
    <ul class="out-div bottom">
        <li>
            <span class="left">订单编号</span>
            <span class="right">${orscanCodeOrderder.orderSid}</span>
        </li>
        <li>
            <span class="left">确认码</span>
            <span class="right">${scanCodeOrder.lePayCode}</span>
        </li>
        <li>
            <span class="left">交易开始时间</span>
            <span class="right"><fmt:formatDate value="${scanCodeOrder.createdDate}" type="both"
                                                pattern="yyy-MM-dd HH:mm:ss"/></span>
        </li>
        <li>
            <span class="left">交易结束时间</span>
            <span class="right"><fmt:formatDate value="${scanCodeOrder.completeDate}" type="both"
                                                pattern="yyy-MM-dd HH:mm:ss"/></span>
        </li>
    </ul>
</div>
<script>

</script>
</body>
</html>