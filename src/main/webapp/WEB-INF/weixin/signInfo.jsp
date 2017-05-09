<%--
  Created by IntelliJ IDEA.
  User: lss
  Date: 17/3/19
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
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${lepayNew}/framework/common.css"/>
    <link rel="stylesheet" href="${lepayNew}/css/signInfo.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <ul class="out-div">
        <li class="list">
            <div class="left">门店名称</div>
            <div class="right">${merchant.name}</div>
        </li>
        <li class="list">
            <div class="left">门店协议</div>
            <c:if test="${merchant.partnership==0}">
                <div class="right">普通协议</div>
           </c:if>
            <c:if test="${merchant.partnership==1}">
                <div class="right">联盟协议</div>
            </c:if>
        </li>
        <li class="list">
            <div class="left">扫码费率</div>
            <div class="right">
                <c:if test="${merchant.partnership==0}">
                    <p>普通订单<span class="font-orange">${merchant.ljCommission}%</span></p>
                </c:if>
                <c:if test="${merchant.partnership==1}">
                    <p>普通订单<span class="font-orange">${merchant.ljBrokerage}%</span></p>

                <c:if test="${merchant.memberCommission==merchant.ljBrokerage}">
                    <p>会员订单<span class="font-orange"> ${merchant.memberCommission}%
                </span></p>
                </c:if>
                <c:if test="${merchant.memberCommission==merchant.ljCommission}">
                    <p>会员订单<span class="font-orange"> ${(100-merchant.memberCommission)/10}/折
                    </span></p>
                </c:if>
                    <p>导流订单<span class="font-orange">${(100-merchant.ljCommission)/10}/折</span></p>
                </c:if>
            </div>
        </li>
    </ul>
    <ul class="out-div">
        <li class="list">
            <div class="left">银行卡号</div>
            <div class="right"> <span class="right">${bank1}*****************${bank2}</span></div>
        </li>
        <li class="list">
            <div class="left">开户支行</div>
            <div class="right">${merchant.merchantBank.bankName}</div>
        </li>
        <li class="list">
            <div class="left">收款人</div>
            <div class="right">${merchant.payee}</div>
        </li>
    </ul>
    <p class="tel">
        如需更改签约信息<br>
        请拨打客服电话：<a href="tel:400-0412-800">400-0412-800</a>
    </p>
</div>
<script>

</script>
</body>
</html>