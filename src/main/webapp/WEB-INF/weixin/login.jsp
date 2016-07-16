<%--
  Created by IntelliJ IDEA.
  User: wcg
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
    <meta name="format-detection" content="telephone=no"><!--不显示拨号链接-->
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${resourceUrl}/css/login.css"/>
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>
<body>
<ul class="paySuccess">
    <li class="info">
        <span class="name"></span><input id="name" type="text" placeholder="请输入用户名"/>
    </li>
    <li class="info">
        <span class="passWord"></span><input id="password" type="password" placeholder="请输入密码"/>
    </li>
</ul>
<div class="form-btn" id="pay-confrim">确认登录</div>
<p class="prac">
    <font>温馨提示：</font>使用该用户名和密码登录后，您的微信号将和该用户名对应的乐＋商户绑定，及时收到订单和转账信息。
</p>

<p class="tel"><span class="kefu"></span>客服电话：400-0412-800</p>
</body>
<script>
    var flag = true;
    $("#pay-confrim").bind("touchstart", function () {
        if (flag) {
            flag = false;
            var user = {};
            user.name = $("#name").val();
            user.password = $("#password").val();
//        $.post("/wx", JSON.stringify(user), function (data) {
//            if (data.status == 200) {
//                $("#cart-number").attr("style", "display:block")
//                $("#cart-number").text(data.msg);
//            } else {
//                $("#cart-number").attr("style", "display:none")
//            }
//        });
            $.ajax({
                       type: "post",
                       url: "/wx",
                       data: JSON.stringify(user),
                       contentType: "application/json",
                       success: function (data) {
//                       location.href = "/manage/topic";
                           if (data.status == 200) {
                               location.href = data.data;
                           } else {
                               alert(data.msg);
                               flag = true;
                           }
                       }
                   });
        }
    });

</script>
</html>
