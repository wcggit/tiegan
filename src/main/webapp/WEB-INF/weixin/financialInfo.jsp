<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/23
  Time: 下午3:45
  To change this template use File | Settings | File Templates.
--%>
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
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/css/accountDetail.css"/>
</head>
<body>
<div class="top">
    <p>当日流水金额</p>

    <p>￥${financial.transferPrice/100}</p>

    <p><fmt:formatDate value="${financial.balanceDate}" type="both" pattern="yyyy-MM-dd EEEE"/></p>
</div>
<ul class="payTime">
    <li class="head"><span>时间</span><span>金额</span><span>手续费</span><span>类型</span><span>支付方式</span>
    </li>
    <c:forEach items="${orders}" var="order">
        <li class="center"><span><fmt:formatDate value="${order.completeDate}" type="both"
                                                 pattern="HH:mm"/></span><span>￥${order.totalPrice/100}</span><span>￥${order.ljCommission/100}</span><c:if
                test="${order.rebateWay==1}"><span>导流订单</span></c:if><c:if
                test="${order.rebateWay!=1}"><span>普通订单</span></c:if><span><font
                class="wxzf"></font></span></li>
    </c:forEach>
    <li class="foot">
        <span class="left">总金额：￥${totalPrice/100}</span>
        <span class="right">总手续费：￥${ljCommission/100}</span>
    </li>
</ul>
<ul class="paySuccess">
    <li class="info">
        <c:if test="${merchant.merchantBank.bankName=='支付宝'}">
            <span class="left">支付宝号</span>
        </c:if>
        <c:if test="${merchant.merchantBank.bankName!='支付宝'}">
            <span class="left">银行卡号</span>
        </c:if>
        <span class="right">**********************${bank}</span>
    </li>
    <li class="info">
        <span class="left">收款人</span>
        <span class="right">${merchant.payee}</span>
    </li>
    <li class="info">
        <span class="left">到帐时间</span>
        <c:if test="${financial.state==0}">
            <span class="right">预计两个工作日内到达</span>
        </c:if>
        <c:if test="${financial.state==1}">
            <span class="right"><fmt:formatDate value="${financial.transferDate}" type="both"
                                                pattern="yyy-MM-dd HH:mm:ss"/></span>
        </c:if>
    </li>
</ul>
</body>
</html>
