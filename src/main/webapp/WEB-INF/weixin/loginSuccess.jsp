<%--
  Created by IntelliJ IDEA.
  User: lss
  Date: 2017/2/9
  Time: 14:24
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
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title></title>
    <link rel="stylesheet" href="${lepayNew}/framework/common.css"/>
    <link rel="stylesheet" href="${lepayNew}/css/loginSuccess.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="out-div top">
        <div class="logo-success"></div>
        <div class="ttl">登录绑定成功!</div>

        <ul class="bind-info">
            <li class="bind-list">
                <span class="list-left">绑定商户</span>
                <span class="list-right">${merchantUserCreateUser.merchantName}</span>
            </li>
            <li class="bind-list">
                <span class="list-left">绑定账号</span>
                <span class="list-right">${merchantUser.name}</span>
            </li>
            <li class="bind-list">
                <span class="list-left">账号类型</span>
                <c:choose>
                    <c:when test="${merchantUser.type==0}">
                        <span class="list-right">收银员</span>
                    </c:when>
                    <c:when test="${merchantUser.type==1}">
                        <span class="list-right">店主</span>
                    </c:when>
                    <c:when test="${merchantUser.type==2}">
                        <span class="list-right">子账号</span>
                    </c:when>
                    <c:when test="${merchantUser.type==8}">
                        <span class="list-right">账号类型:管理员</span>
                    </c:when>
                    <c:when test="${merchantUser.type==9}">
                        <span class="list-right">账号类型:系统管理员</span>
                    </c:when>
                    <c:otherwise>
                        <span class="list-right">账号类型:未知</span>
                    </c:otherwise>

                </c:choose>
            </li>
        </ul>
    </div>
    <div class="out-div">
        <p class="ttl-choice">请选择您想接收信息推送的门店</p>
        <div class="store-choice">

            <c:forEach items="${merchantList}" var="merchant">
                <label class="focusClass">
                    <input name="storeChoice" type="radio" value="${merchant.id}" id="a${merchant.id}" checked/>
                        ${merchant.name}
                    <span class="icon-radio icon-yes"></span>
                </label>
            </c:forEach>

            <div class="btn-setting btn-yes" onclick="switchOverAck()">完成设置并进入商户中心</div>
        </div>
    </div>
</div>
<script>

    var radioList = $('.main .store-choice label');

    $(function () {
        radioList.each(function (i) {
                radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class', 'icon-no icon-radio');
                radioList.eq(i).removeClass('focusClass').addClass('initClass');
            })
        radioList.eq(0).find('input').prop('checked',true);
        radioList.eq(0).find('span.icon-radio').removeAttr('class').attr('class', 'icon-yes icon-radio');
        radioList.eq(0).removeClass('initClass').addClass('focusClass');
        beforeSwitchOverAck();
    })

    var state = {
        radioState: function () {
            radioList.each(function (i) {
                if (radioList.eq(i).find('input').prop('checked')) {
                    radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class', 'icon-yes icon-radio');
                    radioList.eq(i).removeClass('initClass').addClass('focusClass');
                } else if (!radioList.eq(i).find('input').prop('checked')) {
                    radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class', 'icon-no icon-radio');
                    radioList.eq(i).removeClass('focusClass').addClass('initClass');
                }
            })
        }
    };
    state.radioState();
    radioList.each(function (i) {
        radioList.on('click', function () {
            state.radioState();
        })
    })
    function switchOverAck() {
        var merchantId = $("input[name=storeChoice]:checked").val();
        if (merchantId != "" && merchantId != null) {
            location.href = "/wx/confirmMerchantChoose?merchantId=" + merchantId;
        }


    }
    //防止误操作
    function beforeSwitchOverAck() {
        var merchantId = $("input[name=storeChoice]:checked").val();
        if (merchantId != "" && merchantId != null) {
            $.ajax({
                type: "get",
                url: "/wx/beforeConfirmMerchantChoose?merchantId=" + merchantId,
                async: false,
                contentType: "application/json",
                success: function (data) {

                }})

        }


    }


</script>
</body>
</html>
