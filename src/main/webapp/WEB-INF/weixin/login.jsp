<%--
  Created by IntelliJ IDEA.
  User: lss
  Date: 16/5/18
  Time: 上午11:10
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
    <link rel="stylesheet" href="${lepayNew}/css/login.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="logo"></div>
    <div class="form">
        <label class="input-use">
            <input type="text" id="name" class="border" placeholder="请输入您的用户名">
        </label>
        <label class="input-password">
            <input type="password" id="password" class="border" placeholder="请输入您的密码">
        </label>
        <p class="ttl"><span style="color: #F3981E;font-size: 3.2vw">* </span>登录账号和密码后，您的微信号将自动和该账号绑定</p>
        <div id="pay-confrim" type="button" class="login login-disabled">登录</div>
    </div>
    <p class="tel">客服电话：<a href="tel:014-0148-800">014-0148-800</a></p>
</div>
<script>
    var flag = true;
    $('.input-use input').on('touchstart',function () {
        $('.input-use input').addClass('border-red');
        $('.input-password input').removeClass('border-red');
    })
    $('.input-password input').on('touchstart',function () {
        $('.input-password input').addClass('border-red');
        $('.input-use input').removeClass('border-red');
    })
    $('.input-password input').on('input propertychange',function () {
        isEmpty();
    })
    function isEmpty() {
        if ($('.input-use input').val()!=''&&$('.input-password input').val()!=''){
            $('.login').attr({'disabled':false}).css({'background-color':'#F3981E'});
        }else {
            $('.login').attr({'disabled':true}).css({'background-color':'#D9D9D9'});
        }
    }
    $("#pay-confrim").bind("touchstart", function () {
        if (flag) {
            flag = false;
            var user = {};
            user.name = $("#name").val();
            user.password = $("#password").val();
            $.ajax({
                type: "post",
                url: "/wx",
                data: JSON.stringify(user),
                contentType: "application/json",
                success: function (data) {
//                       location.href = "/manage/topic";
                    if (data.status == 200) {
                        // location.href = data.data;
                        location.href ="/wx/merchantChoose";
                    } else {
                        alert(data.msg);
                        flag = true;
                    }
                }
            });
        }
    });


</script>
</body>
</html>