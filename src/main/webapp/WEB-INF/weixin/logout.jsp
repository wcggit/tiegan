<%--
  Created by IntelliJ IDEA.
  User: wcg
  Date: 16/5/18
  Time: 下午5:36
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
    <style>
        /*all tag*/
        * {
            margin: 0;
            padding: 0;
            border: none;
            font-size: 1.5625vw;
        }

        html, body {
            height: 100%;
            overflow: hidden;
        }

        .logo {
            width: 30.67vw;
            height: 28vw;
            margin: 21.33vw auto 0;
            background: url("${resourceUrl}/images/logo.png") no-repeat;
            background-size: 100% 100%;
        }

        .btn {
            width: 92vw;
            height: 12vw;
            background-color: #fb991a;
            color: #fff;
            border-radius: 4px;
            text-align: center;
            line-height: 12vw;
            margin: 74vw 4vw 0;
            font-size: 5.067vw;
        }
    </style>
</head>
<body>
<div class="logo"></div>
<div class="btn" onclick="logout()">退出登录</div>
</body>
<script>
    function logout() {
        location.href = "/wx/logout/confrim"
    }
</script>
</html>