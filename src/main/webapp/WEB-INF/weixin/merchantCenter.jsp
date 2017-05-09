<%--
  Created by IntelliJ IDEA.
  User: lss
  Date: 2017/2/23
  Time: 14:40
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@include file="/WEB-INF/commen.jsp" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <meta name="format-detection" content="telephone=no, email=no"/>
    <title>商户中心</title>
    <link rel="stylesheet" href="${lepayNew}/framework/common.css"/>
    <link rel="stylesheet" href="${lepayNew}/css/merchantCenter.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <!--上半部分——详情-->
    <div class="top">
        <div class="store-info">
            <span class="btn" onclick="merchantChange()" >切换门店</span>
            <span class="icon-store"></span>
            <p class="store-name">${merchant.name}</p>
        </div>
        <div class="progress">
            <p><span class="left">门店锁定会员</span><span class="right"><span class="font-big">${lockList[1]}</span>/${lockList[3]}</span></p>
            <p><span style="width: ${lockList[1]/lockList[3]*100}%"></span></p>
        </div>
        <div class="progress">
            <p><span class="left">商户锁定会员</span><span class="right"><span class="font-big">${lockList[0]}</span>/${lockList[2]}</span></p>
            <p><span style="width: ${lockList[0]/lockList[2]*100}%"></span></p>
        </div>
    </div>
    <!--下半部分——列表-->
    <ul class="bottom">
        <li class="order-list list1"  onclick="qrCode()">
            <p><span class="left" >门店收款码</span><span class="right"></span></p>
        </li>
        <li class="order-list list2"  onclick="invitationCode()">
            <input type="hidden" id="qyCode" value="${merchant.merchantInfo.qrCode}">
            <p><span class="left" >会员邀请码</span><span class="right">(扫码注册即锁定本店)</span></p>
        </li>
        <li class="order-list list3" onclick="merchantInfo()">
            <p><span class="left">签约信息</span><span class="right"></span></p>
        </li>
    </ul>
    <div type="button" class="login-out">退出登录</div>
    <p class="tel">客服电话：<a href="tel:400-0412-800">400-0412-800</a></p>
    <div class="shadow">
        <div class="modal">
            <p>确定要退出登录吗？</p>
            <p>退出登录后，微信号将和该账号解除绑定</p>
            <ul>
                <li class="left btn-cancel">取消</li>
                <li class="right btn-confirm" onclick="logOut()">确定</li>
            </ul>
        </div>
    </div>
</div>
<script>
    $('.login-out').on('touchstart',function () {
        $('.main .shadow').css({'display':'block'});
        $('.main .modal').addClass('animation-in');
    })
    $('.main .btn-cancel').on('touchstart',function () {
        setTimeout(function () {
            $('.main .shadow').css({'display':'none'});
        },300);
    })

    function merchantChange () {
        location.href= "/wx/merchantChange?data="+"/wx/merchantCenter";
    }
    function qrCode() {
        location.href= "/wx/qrcode";
    }
    function invitationCode() {
        var  qyCode=$("#qyCode").val();
        if(qyCode === undefined){
            alert("当前商户没有邀请码,请联系客服开通");
        }else {
            if(qyCode==1){
                location.href= "/wx/invitationCode";
            }else {
                alert("当前商户没有邀请码,请联系客服开通");
            }
        }

    }
    function merchantInfo() {
        location.href="/wx/merchantInfo";
    }
    function logOut() {
        location.href="/wx/logout";
    }
</script>
</body>
</html>
