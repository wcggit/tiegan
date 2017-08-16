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
<%@ taglib uri = "http://java.sun.com/jsp/jstl/functions" prefix = "fn" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>Title</title>
    <link rel="stylesheet" href="${grouponResourceUrl}/framework/reset.css">
    <link rel="stylesheet" href="${grouponResourceUrl}/accountDetail/accountDetail.css">
    <script src="${grouponResourceUrl}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <h3 class="ttl">团购入账金额（元）</h3>
        <h3 class="money">${grouponStatistic.transferMoney/100.0}</h3>
        <h3 class="date"><fmt:formatDate value="${grouponStatistic.balanceDate}" type="both" pattern="yyyy月MM日dd"/> <fmt:formatDate value="${grouponStatistic.balanceDate}" type="both" pattern="EEEE"/></h3>
    </div>
    <div class="center">
        <div class="tab">
            <div class="tab-true active">普通订单</div>
            <div class="tab-line"></div>
            <div class="tab-true">乐+订单</div>
        </div>
        <p class="summary summary1">共<span>${normalStatistic[1]}笔</span>，普通团购价<span>${Double.valueOf(normalStatistic[2])/100.0}元</span>，手续费<span>${Double.valueOf(normalStatistic[3])/100.0}元</span>，应入账<span>${Double.valueOf(normalStatistic[0])/100.0}元</span></p>
        <p class="summary summary2">共<span>${userStatistic[1]}笔</span>，乐加会员价<span>${Double.valueOf(userStatistic[2])/100.0}元</span>，佣金<span>${Double.valueOf(userStatistic[3])/100.0}元</span>，应入账<span>${Double.valueOf(userStatistic[0])/100.0}元</span></p >
    </div>
    <div class="bottom" id="wrapper">
        <div class="wrapper-inner">
            <div class="putong">
                <c:forEach items="${normalCodes}" var="code">
                    <div class="list">
                        <h3 class="ttl">

                            <span class="left good">${code.grouponProduct.name}</span>
                            <span class="right date"><fmt:formatDate value="${code.checkDate}" type="both" pattern="yyyy.MM.dd HH:mm:ss"/></span>
                        </h3>
                        <h3 class="detail">
                            普通团购价：<span>${code.totalPrice/100.0}</span>元 手续费：<span>${code.commission/100.0}</span>元 入账：<span>${code.trasnferMoney/100.0}元</span>
                        </h3>
                    </div>
                </c:forEach>

            </div>
            <div class="leplus">
                <c:forEach items="${userCodes}" var="code">
                    <div class="list">
                        <h3 class="ttl">
                            <span class="left good">${code.grouponProduct.name}</span>
                            <span class="right date"><fmt:formatDate value="${code.checkDate}" type="both" pattern="yyyy.MM.dd HH:mm:ss"/></span>
                        </h3>
                        <c:set var="number" value="${code.leJiaUser.phoneNumber}"></c:set>
                        <c:set var="number2" value = "${fn:substring(number, 0, 3)}"></c:set>
                        <c:set var="number3" value = "${fn:substring(number, 7, 11)}"></c:set>
                        <h3 class="personal">消费会员:${number2}****${number3}</h3>
                        <h3 class="detail">
                            乐加会员价：<span>${code.totalPrice/100.0}</span>元 手续费：<span>${code.commission/100.0}</span>元 入账：<span>${code.trasnferMoney/100.0}元</span>
                        </h3>
                    </div>
                </c:forEach>
            </div>
        </div>
    </div>
</div>
<script src="${grouponResourceUrl}/framework/bscroll.min.js"></script>

<script>
    $('body').on('touchmove', function (event) {
        event.preventDefault();
    });
    var index = 0;
    var cityWrapper = document.querySelector('#wrapper');

    //滚动
    var scroll = new window.BScroll(cityWrapper, {
        startX: 0,
        startY: 0,
        probeType: 3,
        preventDefault: false,
        bounce: true
    });
    scroll.on('scroll', function () {

        if(-this.y >= 5 ){
            $(".top").css({"margin-top":"-52.667vw"});
            scroll.refresh();
        }
        if(-this.y < 5 ){
            $(".top").css({"margin-top":0});
            scroll.scrollTo(0, 0);
            scroll.refresh();
        }
        if($(".wrapper-inner").height() - $("#wrapper").height()+this.y == 0){
            reFresh(index);
        }
    });

    //    tab切换
    $(".tab .tab-true").on("touchstart", function () {
        $(".tab .tab-true").removeClass("active");
        $(this).addClass("active");
        index = $(this).index() == 2 ?1:0;
        $(".bottom > div > div").css("display", "none");
        $(".summary").css("display", "none");
        $(".bottom > div > div").eq(index).css("display", "block");
        $(".summary").eq(index).css("display", "block");
        scroll.refresh();
    });

    //上拉刷新时加载的函数
    function reFresh(index) {
        //index是现在的tab状态，0为普通订单，1为乐加订单
    }

</script>
</body>
</html>