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
    <link rel="stylesheet" href="${lepayNew}/css/changeStore.css"/>
    <script src="${lepayNew}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="out-div top">
        <div class="ttl">当前门店</div>
        <c:if test="${temporaryMerchantUserShop==''}">
            <p class="store-name">当前未绑定</p>
            <input id="temporaryMerchantUserShop" type="hidden" value="">
        </c:if>
        <c:if test="${temporaryMerchantUserShop!=''}">
            <p class="store-name">${temporaryMerchantUserShop.merchant.name}</p>
            <input id="temporaryMerchantUserShop" type="hidden" value="${temporaryMerchantUserShop.merchant.id}">
        </c:if>
        <input type="hidden" id="data" value="${data}"/>
    </div>


    <div class="out-div">
        <p class="ttl-choice">请选择您要管理的门店</p>
        <div class="store-choice">

            <c:forEach items="${merchantList}" var="merchant">
                <label class="focusClass">
                    <input name="storeChoice" type="radio" value="${merchant.id}" id="a${merchant.id}" checked/>
                        ${merchant.name}
                    <span class="icon-radio icon-yes"></span>
                </label>
            </c:forEach>

            <div class="btn-setting btn-yes" onclick="switchOverAck()">确认切换<div>
        </div>
    </div>
</div>
<script>

    var radioList = $('.main .store-choice label');
    $(function () {
        var merchantId = $("#temporaryMerchantUserShop").val();
        if (merchantId == "") {
            radioList.each(function (i) {
                radioList.eq(i).find('span.icon-radio').removeAttr('class').attr('class', 'icon-no icon-radio');
                radioList.eq(i).removeClass('focusClass').addClass('initClass');
            })
        }
        radioList.each(function (j, item) {
            // 你要实现的业务逻辑
            radioList.eq(j).find('input').removeAttr('checked')

            if (radioList.eq(j).find('input').val() == merchantId) {
                radioList.eq(j).find('input').prop('checked', 'checked');
                state.radioState();
                return;
            }
        });

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
        var data = $("#data").val();
        if (merchantId != "" && merchantId != null) {
            location.href = "/wx/confirmMerchantChange?data=" + data + "&&merchantId=" + merchantId;
        }


    }


</script>
</body>
</html>
