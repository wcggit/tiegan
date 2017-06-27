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
    <meta http-equiv="X-UA-Compatible" content="IE=edge,chrome=1"><!--强制以webkit内核来渲染-->
    <meta name="viewport" content="width=device-width, initial-scale=1, minimum-scale=1, maximum-scale=1, user-scalable=no">
    <title>Title</title>
    <link rel="stylesheet" href="${grouponResourceUrl}/framework/reset.css">
    <link rel="stylesheet" href="${grouponResourceUrl}/groupManage/groupManage.css">
    <script src="${grouponResourceUrl}/framework/zepto.min.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="search">
            <input type="text" class="input-text" placeholder="请输入9位号码券">
            <div class="input-line"></div>
            <div class="btn-search">点击查询</div>
        </div>
        <div class="scanCode">
            <div class="icon-btn"></div>
            <div class="text">
                <h3>扫一扫核销</h3>
                <h3>使用微信扫一扫功能，扫描团购券二维码核销</h3>
            </div>
            <div class="icon-back"></div>
        </div>
    </div>
    <div class="center">
        <div class="tab">
            <div class="tab-true active">核销记录</div>
            <div class="tab-line"></div>
            <div class="tab-true">到帐记录</div>
        </div>
    </div>
    <div class="bottom" id="wrapper">
        <div class="wrapper-inner">
            <div class="hexiao">
                <div class="list">
                    <h3 class="ttl">
                        <span class="left good">棉花糖</span>
                        <span class="right date">2016.06.12 12:24:45</span>
                    </h3>
                    <h3 class="quan">券码：1234 34567</h3>
                    <h3 class="shop">
                        核销门店：棉花糖KTV（一中店）
                    </h3>
                </div>
                <!--会员订单请加上类vip-->
                <div class="list vip">
                    <h3 class="ttl">
                        <span class="left good">棉花糖</span>
                        <span class="right date">2016.06.12 12:24:45</span>
                    </h3>
                    <h3 class="quan">券码：1234 34567</h3>
                    <h3 class="shop">
                        核销门店：棉花糖KTV（一中店）
                    </h3>
                </div>
            </div>
            <div class="daozhang">
                <div class="list">
                    <h3 class="ttl clearfix">
                        <span class="left num">核销：<span>22</span>笔</span>
                        <span class="right date">2016年5月6日</span>
                    </h3>
                    <h3 class="money">到账金额：<span>220</span>元</h3>
                </div>
                <div class="list">
                    <h3 class="ttl clearfix">
                        <span class="left num">核销：<span>22</span>笔</span>
                        <span class="right date">2016年5月6日</span>
                    </h3>
                    <h3 class="money">到账金额：<span>220</span>元</h3>
                </div>
            </div>
        </div>
    </div>
    <div class="shadow">
        <div class="window">
            <div class="detail">
                <div class="pic"><img src="" alt=""></div>
                <div class="content">
                    <h3 class="ttl">棉花糖KTV大包间1套</h3>
                    <h3 class="buyTime">购买时间：2016.6.6 14:24</h3>
                    <h3 class="lastTime">到期时间：2018.1.6</h3>
                </div>
            </div>
            <div class="btn-wrapper">
                <div class="btn-confirm">确认核销</div>
                <div class="btn-cancel">取消</div>
            </div>
            <span class="close"></span>
        </div>
    </div>
</div>
<script src="${grouponResourceUrl}/framework/bscroll.min.js"></script>

<script>
    var index = 0;
    var cityWrapper = document.querySelector('#wrapper');

    //滚动
    var scroll = new window.BScroll(cityWrapper, {
        startX: 0,
        startY: 0,
        probeType: 3,
        preventDefault: false,
        snapSpeed: 1500
    });
    scroll.on('scroll', function () {

        if(-this.y >= 5 ){
            $(".top").css({"margin-top":"-49.333vw"});
            scroll.refresh();
        }
        if(-this.y < 5 ){
            $(".top").css({"margin-top":0});
            scroll.refresh();
        }
        if($(".wrapper-inner").height() - $("#wrapper").height()+this.y == 0){
            reFresh(index);
        }
    });
    //上拉刷新时加载的函数
    function reFresh(index) {
        //index是现在的tab状态，0为核销记录，1为到账记录

    }

    //    tab切换
    $(".tab .tab-true").on("touchstart", function () {
        $(".tab .tab-true").removeClass("active");
        $(this).addClass("active");
        index = $(this).index() == 2 ?1:0;
        $(".bottom > div > div").css("display", "none");
        $(".bottom > div > div").eq(index).css("display", "block");
        scroll.refresh();
    });

    //弹窗
    $('.scanCode').on("touchstart", function (e) {
        show(e, "shadow");
    });
    $('.close').on("touchstart", function () {
        hide("shadow");
    });
    $('.btn-wrapper .btn-cancel').on("touchstart", function (e) {
        hide("shadow");
    });

    //----------------------以下不能动----------------------------

    $('.window').on("touchstart", function (e) {
        e.stopPropagation();//阻止事件向上冒泡
    });

    //    弹窗显示，e为必备参数，dom为弹窗的类名
    function show(e , dom) {
        var dom1 = "."+dom;
        $(dom1).css("display", "block");
        setTimeout(function () {
            $(dom1).find(".window").css("opacity", 1);
        },100);

        $(dom1).on('touchstart', function (e) {
            hide(dom);
        });
        e.stopPropagation();//阻止事件向上冒泡
    }
    //    弹窗消失，dom为弹窗的类名
    function hide(dom) {
        var dom1 = "."+dom;
        $(dom1).find(".window").css("opacity", 0);
        setTimeout(function () {
            $(dom1).css("display", "none");
        },500);
    }

</script>
</body>
</html>