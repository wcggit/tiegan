<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/19
  Time: 上午10:37
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
    <meta name="format-detection" content="telephone=no"><!--不显示拨号链接-->
    <title></title>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
    <script src="${resourceUrl}/js/html2canvas.min.js"></script>
    <style>
        /*all tag*/
        * {
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1.5625vw;
            font-family: "Microsoft YaHei";
        }
        html, body {
            height: 100%;
            overflow: hidden;
            background-color: #f8f8f8;
        }
        .main{
            background: url("${resourceUrl}/images/lefuma.png") no-repeat;
            background-size: 100% 100%;
            width: 86.67vw;
            height: 120vw;
            margin: 13.33vw auto 0;
            position: relative;
            /*display: none;*/
        }
        .rvCode{
            width: 56vw;
            height: 56vw;
            position: absolute;
            top: 29.33vw;
            left: 0;
            right: 0;
            margin: auto;
            background: url("${merchant.qrCodePicture}") no-repeat center;
            background-size: 53.33vw 53.33vw;
            background-color: #fff;
        }
        .shopName{
            font-size: 5.333vw;
            color: #fff;
            width: 100%;
            text-align: center;
            position: absolute;
            top: 90.67vw;
        }
        .ttl{
            font-size: 2.933vw;
            color: #7d7d7d;
            position: absolute;
            top: 106vw;
            left: 33.33vw;
        }
        .btn{
            width: 92vw;
            height: 12vw;
            background-color: #fb991a;
            color: #fff;
            border-radius: 4px;
            text-align: center;
            line-height: 12vw;
            margin: 6.667vw auto 0;
            font-size: 5.067vw;
        }
        /*#myShowImage{*/
            /*position: absolute;*/
            /*width: 86.67vw;*/
            /*height: 120vw;*/
            /*top: 13.33vw;*/
            /*left: 0;*/
            /*right: 0;*/
            /*margin:auto;*/
        /*}*/
    </style>
</head>
<body>
<div class="main">
    <div class="rvCode"></div>
    <p class="shopName">${merchant.name}</p>
    <%--<p class="ttl">由乐＋钱包提供技术服务<br />客服电话：18710089228</p>--%>
</div>
<%--<img id="myShowImage" />--%>
<%--<div class="btn">请长按上面图片保存到本地</div>--%>

<%--<script type="text/javascript">--%>
    <%--$(function(){});--%>
    <%--html2canvas($(".main"), {--%>
        <%--allowTaint: true,--%>
        <%--taintTest: false,--%>
        <%--onrendered: function(canvas) {--%>
            <%--canvas.id = "mycanvas";--%>
            <%--var dataUrl = canvas.toDataURL();--%>
            <%--$("#myShowImage").attr("src",dataUrl);--%>
<%--//            $(".main").css({'display':'none'});--%>
        <%--}--%>
    <%--});--%>

<%--</script>--%>
</body>
</html>
