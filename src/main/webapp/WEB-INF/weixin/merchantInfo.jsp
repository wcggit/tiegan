<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/19
  Time: 上午11:07
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
    <meta name="format-detection" content="telephone=no">
    <!--不显示拨号链接-->
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/css/contract.css"/>
</head>
<body>
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
        <span class="left">开户支行</span>
        <span class="right">${merchant.merchantBank.bankName}</span>
    </li>

</ul>
<ul class="paySuccess">
    <li class="info">
        <span class="left">绑定手机号</span>
        <span class="right">${merchant.phoneNumber}</span>
    </li>
    <li class="info">
        <span class="left">结算周期</span>
        <c:if test="${merchant.cycle==1}">
            <span class="right">T+1</span>
        </c:if>
        <c:if test="${merchant.cycle==2}">
            <span class="right">T+2</span>
        </c:if>
    </li>
    <c:if test="${merchant.ljCommission==0}">
        <li class="info">
            <span class="left">手续费率</span>
            <span class="right">0.6%</span>
        </li>
    </c:if>
    <c:if test="${merchant.ljCommission!=0}">
        <li class="info">
            <span class="left">手续费率</span>
            <span class="right">${merchant.ljCommission}%</span>
        </li>
    </c:if>
</ul>
<p class="tel">如果您想更改银行卡，请联系客服：400-0412-800</p>
</body>
</html>