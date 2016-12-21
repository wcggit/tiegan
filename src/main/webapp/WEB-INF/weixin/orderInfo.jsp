<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/20
  Time: 下午1:36
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/css/contract.css"/>
</head>
<body>
<ul class="paySuccess">
    <li class="info">
        <span class="left">乐付金额</span>
        <span class="right">￥${order.totalPrice/100}</span>
    </li>
    <c:if test="${order.rebateWay!=1}">
        <li class="info">
            <span class="left">手续费</span>
            <span class="right">￥${order.ljCommission/100}</span>
        </li>
    </c:if>
    <c:if test="${order.rebateWay==1||order.rebateWay==3}">
        <li class="info">
            <span class="left">乐加折扣</span>
            <span class="right">${commission}折</span>
        </li>
    </c:if>
    <li class="info">
        <span class="right one">实际到账 <font class="red">￥${order.transferMoney/100}</font></span>
    </li>
</ul>
<ul class="paySuccess">
    <li class="info">
        <span class="left">订单编号</span>
        <span class="right">${order.orderSid}</span>
    </li>
    <li class="info">
        <span class="left">乐付确认码</span>
        <span class="right">${order.lepayCode}</span>
    </li>
    <li class="info">
        <span class="left">交易开始时间</span>
        <span class="right"><fmt:formatDate
                value="${order.createdDate}" type="both"/></span>
    </li>
    <li class="info">
        <span class="left">交易完成时间</span>
    <span class="right"><fmt:formatDate
            value="${order.completeDate}" type="both"/></span>
    </li>
    <li class="info">
        <span class="left">会员ID</span>
        <span class="right">${order.leJiaUser.userSid}</span>
    </li>
</ul>
</body>
</html>
