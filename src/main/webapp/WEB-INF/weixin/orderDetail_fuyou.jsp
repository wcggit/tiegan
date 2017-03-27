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
    <link rel="stylesheet" href="${lepayNew}/css/orderDetail_fuyou.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <!--上半部分——详情-->
    <div class="top">
        <div class="store-info">
            <div class="date">
                <p><fmt:formatDate value="${date}" type="both" pattern="yyyy-MM-dd"/><span class="problem-icon"></span></p>
                <p><fmt:formatDate value="${date}" type="both" pattern="EEEE"/></p>
                <p class="problem-ttl">总入账金额分为普通、乐加、红包三笔，分别汇入您的结算账户</p>
            </div>
            <p class="store-ttl">应入账金额（元）</p>
            <p class="store-money">¥${(commonTotal+leplusTotal)/100}</p>
        </div>
        <ul class="store-detail">
            <li>
                <div>
                    <p>普通订单入账</p>
                    <p>¥${commonTotal/100}</p>
                </div>
            </li>
            <li>
                <div>
                    <p>乐加订单入账</p>
                    <p>¥${leplusWeixin/100}</p>
                </div>
            </li>
            <li>
                <div>
                    <p>红包支付入账</p>
                    <p>¥${leplusScorea/100}</p>
                </div>
            </li>
        </ul>
    </div>
    <div class="order-div order-common">
        <div class="ttl">
            <p class="left">普通订单</p>
            <p class="right">应入账¥<span class="color-orange">${commonTotal/100}</span>
              (微信¥<span class="color-orange">${commonWeixin/100}</span>+红包¥<span class="color-orange">${commonScorea/100}</span>)
            </p>
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
            <c:forEach items="${commonScanCodeOrderList}" var="commonScanCodeOrder">
            <tr>
                <td><fmt:formatDate value="${commonScanCodeOrder.completeDate}" type="both" pattern="HH:mm:ss"/></td>
                <td>¥${commonScanCodeOrder.totalPrice/100}</td>
                <td>¥${commonScanCodeOrder.commission/100}</td>
                <td class="order-member-icon">¥${commonScanCodeOrder.transferMoney/100}
                <c:if test="${commonScanCodeOrder.orderType.id=='12006'}">
                    <span class="order-icon member-icon">会员</span>
                </c:if>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <div class="order-div order-member">
        <div class="ttl">
            <p class="left">乐加订单</p>
            <p class="right">
              应入账¥<span class="color-orange">${leplusTotal/100}</span>
                (微信¥<span class="color-orange">${leplusWeixin/100}</span>+红包¥<span class="color-orange">${leplusScorea/100}</span>)
            </p>
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
            <c:forEach items="${leplusScanCodeOrderList}" var="leplusScanCodeOrder">
            <tr>
                <td><fmt:formatDate value="${leplusScanCodeOrder.completeDate}" type="both" pattern="HH:mm:ss"/></td>
                <td>¥${leplusScanCodeOrder.totalPrice/100}</td>
                <td>${(100-leplusScanCodeOrder.merchantRate)/10}折</td>
                <td class="order-member-icon">
                    ¥${leplusScanCodeOrder.transferMoney/100}
                    <c:if test="${leplusScanCodeOrder.orderType.id=='12005'}">
                    <span class="order-icon member-icon">会员</span>
                    </c:if>
                    <c:if test="${leplusScanCodeOrder.orderType.id=='12004'}">
                        <span class="order-icon member-icon">导流</span>
                    </c:if>
                </td>
            </tr>
            </c:forEach>
            </tbody>
        </table>
    </div>
    <ul class="billing-info">
        <li><span class="left">结算信息</span></li>
        <li><span class="left">银行卡</span><span class="right">${bank1}**********${bank2}</span></li>
        <li><span class="left">收款人</span><span class="right">${merchant.payee}</span></li>
        <li><span class="left">到账时间</span><span class="right"><fmt:formatDate value="${scanCodeMerchantStatement.createdDate}" type="both"
                                                                              pattern="yyy-MM-dd HH:mm:ss"/></span></li>
    </ul>
    <div class="tel">
        <div>
            <span>注意：</span><br>
            1、对于微信入账的金额，支付通道有10元的结算门槛。且乐加订
            单和普通订单是分开进行结算的。<br>
            2、乐加订单的微信入账金额之和超过10元才结算，红包入账金额
            无结算门槛。<br>
            3、普通订单的微信入账金额也需超过10元才结算。
        </div>
        客服电话：<a href="tel:400-0412-800">400-0412-800</a>
    </div>
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
    $('.problem-icon').on('touchstart',function () {
        if($('.problem-ttl').css('display')=='none'){
            $('.problem-ttl').css({'display':'block'});
        }else {
            $('.problem-ttl').css({'display':'none'});
        }

    })
</script>
</body>
</html>