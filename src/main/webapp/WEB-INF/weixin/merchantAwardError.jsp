<%--
  Created by IntelliJ IDEA.
  User: xf
  Date: 2016/9/27
  Time: 17:41
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ include file="/WEB-INF/commen.jsp" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no"><!--按设备宽度缩放，并且用户不允许手动缩放-->
    <meta name="format-detection" content="telephone=no"><!--不显示拨号链接-->
    <title>Title</title>
    <link rel="stylesheet" href="${resourceUrl}/css/firstRed.css">
    <script src="${resourceUrl}/js/jquery-2.0.3.min.js"></script>
</head>
<script>
   $(function(){
      var displayType="${displayType}";
      if(displayType=='2') {
        $(".first-red2").show();
        $(".first-red3").hide();
      }else{
        $(".first-red3").show();
        $(".first-red2").hide();
      }
   });
</script>
<body>
    <!--第二种情况-->
    <div class="first-red2">
        <p class="ttl">您的店铺没有参与活动呦 ~ </p>
    </div>

    <!--第三种情况-->
    <div class="first-red3">
        <p class="ttl">只有店主账号才能查看福利明细<br>如有疑问请联系乐加客服：400-0412-800</p>
    </div>
</body>
</html>
