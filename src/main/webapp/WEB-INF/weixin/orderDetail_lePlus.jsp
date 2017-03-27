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
    <link rel="stylesheet" href="${lepayNew}/css/orderDetail_lePlus.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <!--上半部分——详情-->
    <div class="top">
        <div class="store-info">
            <div class="date">
                <p><fmt:formatDate value="${balanceDate}" type="both" pattern="yyyy-MM-dd"/></p>
                <p><fmt:formatDate value="${balanceDate}" type="both" pattern="EEEE"/></p>
            </div>
            <p class="store-ttl">应入账金额（元）</p>
            <p class="store-money">${transferMoney/100}</p>
        </div>
    </div>
    <div class="order-div order-common">
        <div class="ttl">
            <p class="left">普通订单</p>
            <p class="right">微信入账<span class="color-orange">${weiXinOrderWeiXinTransferMoney/100}</span></p>
            <span class="icon-click click-down"></span>
        </div>
        <table>
            <thead>
            <tr>
                <td>时间</td>
                <td>消费金额</td>
                <td>费率</td>
                <td>应入账</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${weiXinoffLineOrders}" var="weiXinoffLineOrder">
            <tr>
                <td><fmt:formatDate value="${weiXinoffLineOrder.completeDate}" type="both" pattern="HH:mm:ss"/></td>
                <td>¥${weiXinoffLineOrder.totalPrice/100}</td>
                <td>¥${weiXinoffLineOrder.ljCommission/100}</td>
                <td>¥${weiXinoffLineOrder.transferMoney/100}</td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="order-div order-member">
        <div class="ttl">
            <p class="left">乐加订单</p>
            <p class="right">红包入账<span class="color-orange">${lePlusOrderScoreaTransferMoney/100}</span>微信入账<span class="color-orange">${lePlusOrderWeiXinTransferMoney/100}</span></p>
            <span class="icon-click click-down"></span>
        </div>
        <table style="width: 100%">
            <thead>
            <tr>
                <td>时间</td>
                <td>消费金额</td>
                <td>费率</td>
                <td>应入账</td>
            </tr>
            </thead>
            <tbody>
            <c:forEach items="${lePlusoffLineOrders}" var="lePlusoffLineOrder">
            <tr>
                <td><fmt:formatDate value="${lePlusoffLineOrder.completeDate}" type="both" pattern="HH:mm:ss"/></td>
                <td>¥${lePlusoffLineOrder.totalPrice/100}</td>
                <td>${(100-merchant.ljCommission)/10}折</td>
                <td>¥${lePlusoffLineOrder.transferMoney/100}</td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <ul class="billing-info">
        <li><span class="left">结算信息</span></li>
        <li><span class="left">银行卡</span><span class="right">${bank1}**********${bank2}</span></li>
        <li><span class="left">收款人</span><span class="right">${merchant.payee}</span></li>
        <li> <c:if test="${financial.state==0}">
            <span class="right">T+1</span>
        </c:if>
            <c:if test="${financial.state==1}">
            <span class="right"><fmt:formatDate value="${financial.transferDate}" type="both"
                                                pattern="yyy-MM-dd HH:mm:ss"/></span>
            </c:if>
        </li>
    </ul>
    <p class="tel">
        客服电话：<a href="tel:400-0412-800">400-0412-800</a>
    </p>
</div>
<script>
    $('table').css({'display':'none'});
    $('.order-div').on('touchstart',function () {
        if($(this).find('table').css('display')=='block'){
            $(this).find('table').css({'display':'none'});
            $(this).find('.ttl').removeClass('border-bottom');
            $(this).find('.icon-click').removeClass('click-up').addClass('click-down');
        }else {
            $(this).find('table').css({'display':'block'});
            $(this).find('.ttl').addClass('border-bottom');
            $(this).find('.icon-click').removeClass('click-down').addClass('click-up');
        }
    })
</script>
</body>
</html>