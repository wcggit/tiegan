package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;
import com.jifenke.lepluslive.order.domain.entities.FinancialStatistic;
import com.jifenke.lepluslive.order.domain.entities.OffLineOrder;
import com.jifenke.lepluslive.order.service.FinanicalStatisticService;
import com.jifenke.lepluslive.order.service.OffLineOrderService;
import com.jifenke.lepluslive.weixin.service.WeiXinService;

import org.apache.commons.codec.binary.Base64;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.http.MediaType;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.List;

import javax.imageio.ImageIO;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/3/18.
 */
@RestController
public class WeixinController {

  private static Logger log = LoggerFactory.getLogger(WeixinController.class);

  private String appId = Constants.APPID;

  @Value("${weixin.weixinRootUrl}")
  private String weixinRootUrl;

  @Inject
  private WeiXinService weiXinService;

  @Inject
  private MerchantWeiXinUserService merchantWeiXinUserService;

  @Inject
  private OffLineOrderService offLineOrderService;

  @Inject
  private FinanicalStatisticService finanicalStatisticService;

  @RequestMapping(value = "/wx/user", method = RequestMethod.GET)
  public ModelAndView wxLoginForm() {

    return MvUtil.go("/weixin/user");
  }

  /**
   * 微信授权回调
   */
  @RequestMapping("/wx/userRegister")
  public String userRegister(@RequestParam(required = false) String action,
                             @RequestParam String code,
                             HttpServletRequest request, HttpServletResponse response)
      throws IOException {
    if (code == null) {
      return "禁止授权无法访问app";
    }
    Map<String, Object> map = weiXinService.getSnsAccessToken(code);
    String openid = map.get("openid").toString();
    //获取accessToken与openid
    if (map.get("errcode") != null) {
      log.error(map.get("errcode").toString() + map.get("errmsg").toString());
    }
    MerchantWeiXinUser weiXinUser = merchantWeiXinUserService.findWeiXinUserByOpenId(openid);
    String accessToken = map.get("access_token").toString();
    //2种情况 当用户不存在时,当上次登录距离此次已经经过了3天
    if (weiXinUser == null || new Date(
        weiXinUser.getLastUserInfoDate().getTime() + 3 * 24 * 60 * 60 * 1000)
        .before(new Date())) {
      Map<String, Object> userDetail = weiXinService.getDetailWeiXinUser(accessToken, openid);
      if (userDetail.get("errcode") != null) {
        log.error(userDetail.get("errcode").toString() + userDetail.get("errmsg").toString());
      }
      try {
        merchantWeiXinUserService.saveWeiXinUser(userDetail, map);
      } catch (IOException e) {
        e.printStackTrace();
      }
    } else {
      MerchantUser merchantUser = weiXinUser.getMerchantUser();
      if (merchantUser != null) {
        UsernamePasswordToken
            token =
            new UsernamePasswordToken(merchantUser.getName(), merchantUser.getPassword(),
                                      "UTF-8");
        //获取当前的Subject
        //  token.setRememberMe(true);
        Subject currentUser = SecurityUtils.getSubject();
        try {
          //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
          //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
          //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
          currentUser.login(token);
        } catch (Exception uae) {

        }
      }
    }
    try {
      CookieUtils.setCookie(request, response, appId + "-user-open-id", openid,
                            Constants.COOKIE_DISABLE_TIME);
     // request.getRequestDispatcher(action).forward(request, response);
      response.sendRedirect(action);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * 商户信息
   */
  @RequestMapping(value = "/wx/merchantInfo", method = RequestMethod.GET)
  public ModelAndView merchantInfo(HttpServletRequest request, Model model) {

    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    String bankNumber = merchant.getMerchantBank().getBankNumber();
    model.addAttribute("merchant", merchant);
    model.addAttribute("bank", bankNumber.substring(bankNumber.length() - 4, bankNumber.length()));
    return MvUtil.go("/weixin/merchantInfo");
  }

  /**
   * 商户二维码
   */
  @RequestMapping(value = "/wx/qrcode", method = RequestMethod.GET)
  public ModelAndView wxQrCode(HttpServletRequest request, Model model) {

    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    model.addAttribute("merchant", merchantWeiXinUser.getMerchantUser().getMerchant());
    return MvUtil.go("/weixin/merchantQrCode");
  }

  /**
   * 所有订单页面
   */
  @RequestMapping(value = "/wx/tradeList", method = RequestMethod.GET)
  public ModelAndView tradeList() {

    return MvUtil.go("/weixin/tradeList");
  }

  /**
   * 订单ajax请求
   */
  @RequestMapping(value = "/wx/offLineOrder", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult findOffLineOrderByPage(
      @RequestParam(value = "page", required = false) Integer offset,
      HttpServletRequest request) {
    if (offset == null) {
      offset = 1;
    }
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    Page page = offLineOrderService.findOffLineOrderByMerchantAndPage(merchant, offset);
    return LejiaResult.ok(page.getContent());
  }

  /**
   * 统计每月的总金额
   */
  @RequestMapping(value = "/wx/sumMonthlyIncome", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult getMonthlyIncome(
      @RequestParam Long date,
      HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    return LejiaResult.ok(offLineOrderService.sumTotalPriceByMonth(date, merchant));
  }

  /**
   * 每笔订单详情
   */
  @RequestMapping(value = "/wx/offLineOrder/{sid}", method = RequestMethod.GET)
  public ModelAndView goOrderInfo(
      @PathVariable String sid,
      Model model) {
    model.addAttribute("order", offLineOrderService.findOffLineOrderByOrderSid(sid));
    return MvUtil.go("/weixin/orderInfo");
  }

  /**
   * 转账页面
   */
  @RequestMapping(value = "/wx/financialList", method = RequestMethod.GET)
  public ModelAndView goFinanicalListPage() {
    return MvUtil.go("/weixin/financialList");
  }

  /**
   * 转账ajax
   */
  @RequestMapping(value = "/wx/financial", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult findFinancialByPage(
      @RequestParam(value = "page", required = false) Integer offset,
      HttpServletRequest request) {
    if (offset == null) {
      offset = 1;
    }
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    Page page = finanicalStatisticService.findFinanicalByMerchantAndPage(merchant, offset);
    return LejiaResult.ok(page.getContent());
  }


  /**
   * 转账每月统计
   */
  @RequestMapping(value = "/wx/sumMonthlyFinancialIncome", method = RequestMethod.GET)
  public
  @ResponseBody
  LejiaResult getMonthlyFinancialIncome(
      @RequestParam Long date,
      HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");

    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    return LejiaResult.ok(finanicalStatisticService.sumTotalPriceByMonth(date, merchant));
  }


  /**
   * 转账详情页面.包括转账单,线下听单,银行卡号
   */
  @RequestMapping(value = "/wx/financial/{id}", method = RequestMethod.GET)
  public ModelAndView goFinancialInfoPage(@PathVariable String id, Model model) {
    FinancialStatistic
        financialStatistic =
        finanicalStatisticService.findFinancialByStatisticId(id);
    Merchant merchant = financialStatistic.getMerchant();
    Date balanceDate = financialStatistic.getBalanceDate();
    Long ljCommission = 0L;
    Long totalPrice = 0L;
    List<OffLineOrder>
        offLineOrders =
        offLineOrderService
            .findOffLineOrderByMerchantAndDate(merchant, balanceDate);
    for (OffLineOrder offLineOrder : offLineOrders) {
        ljCommission += offLineOrder.getLjCommission();
      totalPrice += offLineOrder.getTotalPrice();
    }
    String bankNumber = merchant.getMerchantBank().getBankNumber();
    model.addAttribute("ljCommission", ljCommission);
    model.addAttribute("totalPrice", totalPrice);
    model.addAttribute("orders", offLineOrders);
    model.addAttribute("financial", financialStatistic);
    model.addAttribute("merchant", merchant);
    model.addAttribute("bank", bankNumber.substring(bankNumber.length() - 4, bankNumber.length()));

    return MvUtil.go("/weixin/financialInfo");
  }


  @RequestMapping(value = "/wx/uploadPicture", method = RequestMethod.POST, produces = MediaType.IMAGE_JPEG_VALUE)
  public void testphoto(String data) throws IOException {
    Base64 base64 = new Base64();
    byte[] k = base64.decode(data.substring("data:image/jpeg;base64,"
                                                .length()));

    InputStream is = new ByteArrayInputStream(k);
    String fileName = UUID.randomUUID().toString();

    //以下其实可以忽略，将图片压缩处理了一下，可以小一点

    double ratio = 1.0;
    BufferedImage image = ImageIO.read(is);
    int newWidth = (int) (image.getWidth() * ratio);
    int newHeight = (int) (image.getHeight() * ratio);
    Image newimage = image.getScaledInstance(newWidth, newHeight,
                                             Image.SCALE_SMOOTH);
    BufferedImage tag = new BufferedImage(newWidth, newHeight,
                                          BufferedImage.TYPE_INT_RGB);
    Graphics g = tag.getGraphics();
    g.drawImage(newimage, 0, 0, null);
    g.dispose();
    ImageIO.write(tag, "jpg", new File(""));
  }
}
