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
    <link rel="stylesheet" href="${grouponResourceUrl}/groupManage/groupManage.css">
    <script src="${grouponResourceUrl}/framework/zepto.min.js"></script>
    <script src="http://res.wx.qq.com/open/js/jweixin-1.0.0.js"></script>
</head>
<body>
<div class="main">
    <div class="top">
        <div class="search">
            <input type="number" id="codeSid" class="input-text" placeholder="请输入12位号码券">

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
            <div class="hexiao" id="hexiao"></div>
            <div class="daozhang" id="daozhang"></div>

        </div>
    </div>
    <div class="shadow check">
        <div class="window">
            <div class="detail">
                <div class="pic"><img src="" alt="" id="code-picture"></div>
                <div class="content">
                    <h3 class="ttl" id="code-name"></h3>

                    <h3 class="buyTime" id="code-buy"></h3>

                    <h3 class="lastTime" id="code-expired"></h3>
                </div>
            </div>
            <div class="btn-wrapper">
                <div class="btn-confirm" id="check-code">确认核销</div>
                <div class="btn-cancel">取消</div>
            </div>
            <span class="close"></span>
        </div>
    </div>
    <div class="shadow second success">
        <div class="window">
            <div class="logo"></div>
            <h3 class="ttl">核销成功</h3>
            <div class="btn">返回</div>
        </div>
    </div>
    <div class="shadow second fail">
        <div class="window">
            <div class="logo"></div>
            <h3 class="ttl" id="code-fail"></h3>

            <div class="btn">返回</div>
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
        $('.btn-search').on("touchstart", function (e) {
            var codeSid = $("#codeSid").val()
            if (codeSid.length != 12) {
                $("#code-fail").html("输入正确的卷码")
                    show(e,"fail")
            } else {
                $.ajax({
                           type: 'GET',
                           url: '/wx/groupon/' + codeSid,
                           dataType: 'json',
                           success: function (data) {
                               if (data.status == 200) {
                                   $("#code-name").html(data.data.grouponProduct.name);
                                   $("#code-buy").html("购买时间:"
                                                       + new Date(data.data.createDate).format("yyyy.MM.dd HH:mm:ss"));
                                   $("#code-expired").html("失效时间:"
                                                           + new Date(data.data.expiredDate).format("yyyy.MM.dd HH:mm:ss"));
                                   $("#code-picture").attr("src",
                                                           data.data.grouponProduct.displayPicture);
                                   $("#check-code").bind("touchstart",
                                                         function (e) {
                                                             var id = data.data.sid;
                                                             $.ajax({
                                                                        type: 'GET',
                                                                        url: '/wx/groupon/check/'
                                                                             + id,
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
                                   show(e, "check")
                               } else {
                                   $("#code-fail").html(data.msg)
                                   show(e,"fail")
                               }
                           }
                       })
            }
        });
    </script>
    <script>
        var index = 0;
        var cityWrapper = document.querySelector('#wrapper');
        var page1 = 1;
        var page2 = 1;
        var flag1 = true;
        var flag2 = true;
        getCodeByAjax();
        getStatisticByAjax();
        //滚动
        var scroll = new window.BScroll(cityWrapper, {
            startX: 0,
            startY: 0,
            probeType: 3,
            preventDefault: false,
            bounce: true
        });
        scroll.on('scroll', function () {

            if (-this.y >= 5) {
                $(".top").css({"margin-top": "-49.333vw"});
                scroll.refresh();
            }
            if (-this.y < 5) {
                $(".top").css({"margin-top": 0});
                scroll.scrollTo(0, 0);
                scroll.refresh();
            }
            reFresh(index);
        });
        //上拉刷新时加载的函数
        function reFresh(index) {
            //index是现在的tab状态，0为核销记录，1为到账记录
            if (index == 0) {
                if (flag1) {
                    getCodeByAjax();
                }
            } else {
                if (flag2) {
                    getStatisticByAjax();
                }
            }

        }

        function getCodeByAjax() {
            $.ajax({
                       type: 'GET',
                       url: '/wx/groupon/code/' + page1,
                       dataType: 'json',
                       success: function (data) {
                           page1++
                           var codes = data.data.content;
                           var results = ''
                           if (codes.length < 10) {
                               flag1 = false
                           }
                           if (codes.length != 0) {
                               if (codes.length != 0) {
                                   for (var i = 0; i < codes.length; i++) {
                                       if (codes[i].codeType == 0) {
                                           results += "<div class='list'>"
                                       } else {
                                           results += "<div class='list vip'>"
                                       }
                                       results += "<h3 class='ttl'><span class='left good'>"
                                       results += codes[i].grouponProduct.name
                                       results += "</span><span class='right date'>"
                                       results +=
                                       new Date(codes[i].checkDate).format("yyyy.MM.dd HH:mm:ss")
                                       results += "</span></h3><h3 class='quan'>券码: "
                                       results += codes.sid
                                       results += "</h3><h3 class='shop'>核销门店: "
                                       results += codes[i].merchant.name
                                       results += "</h3></div>"
                                   }
                               }
                               document.getElementById("hexiao").innerHTML += results;
                           }
                       }
                   })
        }

        function getStatisticByAjax() {
            $.ajax({
                       type: 'GET',
                       url: '/wx/groupon/statistic/' + page2,
                       dataType: 'json',
                       success: function (data) {
                           page2++
                           var codes = data.data.content;
                           var results = ''
                           if (codes.length < 10) {
                               flag2 = false
                           }
                           if (codes.length != 0) {
                               for (var i = 0; i < codes.length; i++) {
                                   results +=
                                   "<div class='list lss'><h3 class='ttl clearfix'><span class='left num'>核销:<span>"
                                   results += codes[i].check
                                   results += "</span>笔</span><span class='right date'>"
                                   results += new Date(codes[i].balanceDate).format("yyyy年MM月dd日")
                                   results += "</span></h3><h3 class='money'>到账金额<span>"
                                   results += codes[i].transferMoney / 100.0
                                   results +=
                                   "</span>元</h3><span class='id-hidden' style='display:none'>"
                                   + codes[i].sid + "</span></div>"
                               }
                               document.getElementById("daozhang").innerHTML += results;
                               $(".lss").each(function (i) {
                                   $(".lss").eq(i).bind("click",
                                                        function () {
                                                            var id = $(this).find(".id-hidden").text();
                                                            location.href =
                                                            "/wx/groupon/statistic_detail/"
                                                            + id;
                                                        });
                               });
                           }

                       }
                   })
        }

        //    tab切换
        $(".tab .tab-true").on("touchstart", function () {
            $(".tab .tab-true").removeClass("active");
            $(this).addClass("active");
            index = $(this).index() == 2 ? 1 : 0;
            $(".bottom > div > div").css("display", "none");
            $(".bottom > div > div").eq(index).css("display", "block");
            scroll.refresh();
        });

        //弹窗
        //    $('.scanCode').on("touchstart", function (e) {
        //        show(e, "shadow");
        //    });
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

        Date.prototype.format = function (fmt) {
            var o = {
                "M+": this.getMonth() + 1, //月份
                "d+": this.getDate(), //日
                "h+": this.getHours() % 12 == 0 ? 12 : this.getHours() % 12, //小时
                "H+": this.getHours(), //小时
                "m+": this.getMinutes(), //分
                "s+": this.getSeconds(), //秒
                "q+": Math.floor((this.getMonth() + 3) / 3), //季度
                "S": this.getMilliseconds() //毫秒
            };
            var week = {
                "0": "\u65e5",
                "1": "\u4e00",
                "2": "\u4e8c",
                "3": "\u4e09",
                "4": "\u56db",
                "5": "\u4e94",
                "6": "\u516d"
            };
            if (/(y+)/.test(fmt)) {
                fmt =
                fmt.replace(RegExp.$1, (this.getFullYear() + "").substr(4 - RegExp.$1.length));
            }
            if (/(E+)/.test(fmt)) {
                fmt =
                fmt.replace(RegExp.$1,
                            ((RegExp.$1.length > 1) ? (RegExp.$1.length > 2 ? "\u661f\u671f"
                                    : "\u5468")
                                    : "") + week[this.getDay() + ""]);
            }
            for (var k in o) {
                if (new RegExp("(" + k + ")").test(fmt)) {
                    fmt =
                    fmt.replace(RegExp.$1,
                                (RegExp.$1.length == 1) ? (o[k]) : (("00" + o[k]).substr((""
                                                                                          + o[k]).length)));
                }
            }
            return fmt;
        }
    </script>
</div>
</body>
</html>