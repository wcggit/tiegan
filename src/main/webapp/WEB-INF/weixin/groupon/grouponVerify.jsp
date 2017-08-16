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
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1">
    <!--强制以webkit内核来渲染-->
    <meta name="viewport"
          content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>Title</title>
    <link rel="stylesheet" href="${grouponResourceUrl}/framework/reset.css">
    <link rel="stylesheet" href="${grouponResourceUrl}/grouphexiao/grouphexiao.css">
    <script src="${grouponResourceUrl}/framework/zepto.min.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="text">
            团购订单：<span>${grouponOrder.orderSid}</span>
        </div>
        <div class="icon-btn scanCode">重新扫一扫</div>
    </div>

    <div class="bottom" id="wrapper">
        <div class="wrapper-inner">
            <!--三种状态，waiting：确认核销，yiguoqi：已过期，yihexiao：已核销-->
            <c:forEach items="${grouponOrder.grouponCodes}" var="code">
                <div class="list ">
                    <div class="img">
                        <img src="${code.grouponProduct.displayPicture}" alt="">
                    </div>
                    <div class="detail">
                        <h3 class="ttl">${code.grouponProduct.name}</h3>

                        <h3 class="buyTime">购买时间：<fmt:formatDate value="${code.createDate}"
                                                                 type="both"
                                                                 pattern="yyyy.M.d HH:mm"/></h3>

                        <h3 class="lastTime">到期时间：<fmt:formatDate value="${code.expiredDate}"
                                                                  type="both"
                                                                  pattern="yyyy.M.d"/></h3>
                    </div>
                    <div class="state">
                        <c:if test="${code.state==0}">
                            <div class="btn lss">确认核销<input type="hidden" class="id-hidden" value="${code.sid}"></div>
                        </c:if>
                        <c:if test="${code.state==1}">
                            <div class="state1">已核销</div>
                        </c:if>
                        <c:if test="${code.state==4}">
                            <div class="state2">已过期</div>
                        </c:if>
                        <c:if test="${code.state==2||code.state==3}">
                            <div class="state2">退款</div>
                        </c:if>
                    </div>
                </div>
            </c:forEach>
            <%--<div class="list waiting">--%>
            <%--<div class="img">--%>
            <%--<img src="" alt="">--%>
            <%--</div>--%>
            <%--<div class="detail">--%>
            <%--<h3 class="ttl">棉花糖KTV大包间1套</h3>--%>
            <%--<h3 class="buyTime">购买时间：2016.6.6 14:24</h3>--%>
            <%--<h3 class="lastTime">到期时间：2018.1.6</h3>--%>
            <%--</div>--%>
            <%--<div class="state">--%>
            <%--<div class="state1">已核销</div>--%>
            <%--<div class="state2">已过期</div>--%>
            <%--<div class="btn">确认核销</div>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--<div class="list yiguoqi">--%>
            <%--<div class="img">--%>
            <%--<img src="" alt="">--%>
            <%--</div>--%>
            <%--<div class="detail">--%>
            <%--<h3 class="ttl">棉花糖KTV大包间1套</h3>--%>
            <%--<h3 class="buyTime">购买时间：2016.6.6 14:24</h3>--%>
            <%--<h3 class="lastTime">到期时间：2018.1.6</h3>--%>
            <%--</div>--%>
            <%--<div class="state">--%>
            <%--<div class="state1">已核销</div>--%>
            <%--<div class="state2">已过期</div>--%>
            <%--<div class="btn">确认核销</div>--%>
            <%--</div>--%>
            <%--</div>--%>
            <%--<div class="list yihexiao">--%>
            <%--<div class="img">--%>
            <%--<img src="" alt="">--%>
            <%--</div>--%>
            <%--<div class="detail">--%>
            <%--<h3 class="ttl">棉花糖KTV大包间1套</h3>--%>
            <%--<h3 class="buyTime">购买时间：2016.6.6 14:24</h3>--%>
            <%--<h3 class="lastTime">到期时间：2018.1.6</h3>--%>
            <%--</div>--%>
            <%--<div class="state">--%>
            <%--<div class="state1">已核销</div>--%>
            <%--<div class="state2">已过期</div>--%>
            <%--<div class="btn">确认核销</div>--%>
            <%--</div>--%>
            <%--</div>--%>
        </div>
    </div>
    <!--确认核销弹窗-->
    <div class="shadow confirm">
        <div class="window">
            <div class="detail">
                <h3 class="ttl">确认核销当前团购券吗？</h3>

                <h3 class="code code-number"></h3>
            </div>
            <div class="btn-wrapper">
                <div class="btn-confirm">确认核销</div>
                <div class="btn-cancel">取消</div>
            </div>
        </div>
    </div>
    <!--核销成功弹窗-->
    <div class="shadow second success">
        <div class="window">
            <div class="logo"></div>
            <h3 class="ttl">核销成功</h3>

            <div class="btn">返回</div>
        </div>
    </div>

    <!--已核销弹窗-->
    <div class="shadow second fail">
        <div class="window">
            <div class="logo"></div>
            <h3 class="ttl" id="code-name"></h3>

            <div class="btn">返回</div>
        </div>
    </div>
</div>
<script src="${grouponResourceUrl}/framework/bscroll.min.js"></script>

<script>
    $('body').on('touchmove', function (event) {
        event.preventDefault();
    });
    wx.config({
                  "debug": false, // 开启调试模式,调用的所有api的返回值会在客户端alert出来，若要查看传入的参数，可以在pc端打开，参数信息会通过log打出，仅在pc端时才会打印。
                  "appId": "${wxConfig['appId']}", // 必填，公众号的唯一标识
                  "timestamp": "${wxConfig['timestamp']}", // 必填，生成签名的时间戳
                  "nonceStr": "${wxConfig['noncestr']}", // 必填，生成签名的随机串
                  "signature": "${wxConfig['signature']}",// 必填，签名，见附录1
                  "jsApiList": [
                      'scanQRCode'
                  ] // 必填，需要使用的JS接口列表，所有JS接口列表见附录2
              });
    $('.scanCode').on("touchstart", function (e) {
        wx.scanQRCode({
                          // 默认为0，扫描结果由微信处理，1则直接返回扫描结果
                          needResult: 0,
                          desc: 'scanQRCode desc'
                      });
    });
</script>
<!--下拉加载-->
<script>
    var index = 0;
    var cityWrapper = document.querySelector('#wrapper');
    var code = ""
    //滚动
    var scroll = new window.BScroll(cityWrapper, {
        startX: 0,
        startY: 0,
        probeType: 3,
        preventDefault: false,
        bounce: true
    });
    scroll.on('scroll', function () {
        if ($(".wrapper-inner").height() - $("#wrapper").height() + this.y == 0) {
            reFresh(index);
        }
    });

    //上拉刷新时加载的函数
    function reFresh(index) {
        //index是现在的tab状态，0为核销记录，1为到账记录

    }
</script>
<!--弹窗-->
<script>
    //    点击确认核销按钮
    $('.list .state .btn').on("touchstart", function (e) {
        $(".code-number").html("卷码:"+$(this).find(".id-hidden").val())
        code  = $(this).find(".id-hidden").val()
        show(e, "confirm")
    });
    //    点击确认核销窗口的取消按钮
    $('.confirm .btn-wrapper .btn-cancel').on("touchstart", function (e) {
        hide("confirm");
    });
    //    点击确认核销窗口的确认按钮
    $('.confirm .btn-wrapper .btn-confirm').on("touchstart", function (e) {
        hide("confirm");
        $.ajax({
                   type: 'GET',
                   url: '/wx/groupon/check/'
                        + code,
                   dataType: 'json',
                   success: function (data) {
                       hide("check")
                       if (data.status
                           == 200) {
                           show(e, "success")
                       } else {
                           $("#code-name").html(data.msg)
                           show(e, "fail")
                       }
                   }
               })
    });

    //----------------------以下不能动----------------------------
    //点击window窗口无反应的方法
    $('.window').on("touchstart", function (e) {
        e.stopPropagation();//阻止事件向上冒泡
    });

    //点击提示窗的返回按钮
    $(".shadow .window .btn").on("touchstart", function (e) {
        $(this).parents(".shadow").css("display", "none");
    });

    //    弹窗显示，e为必备参数，dom为弹窗的类名
    function show(e, dom) {
        var dom1 = "." + dom;
        $(dom1).css("display", "block");
        setTimeout(function () {
            $(dom1).find(".window").css("opacity", 1);
        }, 100);

        $(dom1).on('touchstart', function (e) {
            hide(dom);
        });
        e.stopPropagation();//阻止事件向上冒泡
    }
    //    弹窗消失，dom为弹窗的类名
    function hide(dom) {
        var dom1 = "." + dom;
        $(dom1).find(".window").css("opacity", 0);
        setTimeout(function () {
            $(dom1).css("display", "none");
        }, 500);
    }
</script>
</body>
</html>