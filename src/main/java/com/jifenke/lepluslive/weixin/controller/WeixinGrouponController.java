package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponOrder;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponStatistic;
import com.jifenke.lepluslive.groupon.service.GrouponService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.TemporaryMerchantUserShop;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.merchant.service.TemporaryMerchantUserService;
import com.jifenke.lepluslive.weixin.service.WeiXinPayService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.data.domain.Page;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by wcg on 2017/6/19.
 */
@RestController
public class WeixinGrouponController {

  private String appId = Constants.APPID;

  @Inject
  private MerchantUserService merchantUserService;

  @Inject
  private TemporaryMerchantUserService temporaryMerchantUserService;

  @Inject
  private GrouponService grouponService;

  @Inject
  private MerchantService merchantService;

  @Inject
  private WeiXinPayService weiXinPayService;

  /**
   * 团购页面
   * @param model
   * @param request
   * @return
   */
  @RequestMapping(value = "/wx/groupon", method = RequestMethod.GET)
  public ModelAndView goGrouponPage(Model model,HttpServletRequest request) {
    Merchant merchant = getMerchant();
    model.addAttribute("groupons", grouponService.findGrouponCodeByMerchant(merchant));
    model.addAttribute("wxConfig", getWeiXinPayConfig(request));
    return MvUtil.go("/weixin/groupon/groupon");
  }

  @RequestMapping(value = "/wx/groupon/code/{offset}", method = RequestMethod.GET)
  private LejiaResult findGrouponCodeByOffset(@PathVariable Integer offset) {
    Page
        grouponCodeList = grouponService.findGrouponCodeByOffset(offset, getMerchant().getId());
    return LejiaResult.build(200, "", grouponCodeList);
  }

  @RequestMapping(value = "/wx/groupon/statistic/{offset}", method = RequestMethod.GET)
  private LejiaResult findGrouponStatisticByOffset(@PathVariable Integer offset) {
    Page
        grouponCodeList =
        grouponService.findGrouponStatisticByOffset(offset, getMerchant().getId());
    return LejiaResult.build(200, "", grouponCodeList);
  }

  /**
   * 查询卷码
   * @param sid
   * @return
   */
  @RequestMapping(value = "/wx/groupon/{sid}", method = RequestMethod.GET)
  public LejiaResult findGrouponCodeBySid(@PathVariable String sid) {
    GrouponCode grouponCode = grouponService.findGrouponCodeBySid(sid);
    if (grouponCode != null) {
      if (grouponCode.getState() == 0) {
        return LejiaResult.build(200, "", grouponCode);
      } else {
        return LejiaResult.build(500, "无效的卷码");
      }
    } else {

      return LejiaResult.build(500, "不存在的卷码");
    }
  }

  /**
   * 核销卷码
   * @param sid
   * @return
   */
  @RequestMapping(value = "/wx/groupon/check/{sid}", method = RequestMethod.GET)
  public LejiaResult checkGrouponCode(@PathVariable String sid) {
    GrouponCode grouponCode = grouponService.findGrouponCodeBySid(sid);
//    Subject currentUser = SecurityUtils.getSubject();
//    PrincipalCollection principals = currentUser.getPrincipals();
//    String userName = (String) principals.getPrimaryPrincipal();
//    MerchantUser merchantUser = merchantUserService.findByName(userName);
    MerchantUser merchantUser = new MerchantUser(9L);
    if (grouponCode != null && merchantUser != null) {
      if (grouponCode.getGrouponProduct().getMerchantUser().getId() == merchantUser.getId()) {
        if (grouponCode.getState() != 0) {
          return LejiaResult.build(500, "无效的卷码");
        } else {
          grouponService.chargeOffGrouponCode(grouponCode, getMerchant(), merchantUser.getName());
          return LejiaResult.build(200, "核销成功");
        }
      } else {
        return LejiaResult.build(500, "无权限核销");
      }
    } else {
      return LejiaResult.build(500, "无效的卷码");
    }
  }

  /**
   * 核销单详情
   * @param model
   * @return
   */
  @RequestMapping(value = "/wx/groupon/statistic_detail/{sid}", method = RequestMethod.GET)
  public ModelAndView goGrouponStatisticDetailPage(Model model,@PathVariable String sid) {
    GrouponStatistic grouponStatistic = grouponService.findGrouponStatisticBySid(sid);
    Merchant merchant = grouponStatistic.getMerchant();
    Date end = grouponStatistic.getBalanceDate();
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(end);
    calendar.set(Calendar.HOUR_OF_DAY, 0);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date start = calendar.getTime();
    model.addAttribute("normalStatistic",grouponService.statisticCode(merchant, start, end, 0));
    model.addAttribute("userStatistic",grouponService.statisticCode(merchant,start,end,1));
    model.addAttribute("normalCodes",
                       grouponService.findGrouponCodeByMerchantAndDateAndType(merchant,start,end,0));
    model.addAttribute("userCodes",
                       grouponService.findGrouponCodeByMerchantAndDateAndType(merchant,start,end,1));
    model.addAttribute("grouponStatistic", grouponStatistic);
    return MvUtil.go("/weixin/groupon/statisticDetail");
  }


  /**
   * 团购扫二维码页面
   * @param model
   * @param request
   * @return
   */
  @RequestMapping(value = "/wx/groupon/verify/{sid}", method = RequestMethod.GET)
  public ModelAndView goGrouponVerifyPage(Model model,HttpServletRequest request,@PathVariable String sid) {
    GrouponOrder grouponOrder = grouponService.findGrouponOrderBySid(sid);
    if(grouponOrder!=null){
      model.addAttribute("grouponOrder", grouponOrder);
      model.addAttribute("wxConfig", getWeiXinPayConfig(request));
      return MvUtil.go("/weixin/groupon/grouponVerify");
    }
    return null;
  }



  public Merchant getMerchant() {
//    Subject currentUser = SecurityUtils.getSubject();
//    PrincipalCollection principals = currentUser.getPrincipals();
//    String userName = (String) principals.getPrimaryPrincipal();
//    MerchantUser merchantUser = merchantUserService.findByName(userName);
//    TemporaryMerchantUserShop
//        temporaryMerchantUserShop =
//        temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
//    Merchant merchant = temporaryMerchantUserShop.getMerchant();
    Merchant merchant = merchantService.findMerchantById(1L);
    return merchant;
  }

  private Map getWeiXinPayConfig(HttpServletRequest request) {
    Long timestamp = new Date().getTime() / 1000;
    String noncestr = MvUtil.getRandomStr();
    Map map = new HashMap<>();
    map.put("appId", appId);
    map.put("timestamp", timestamp);
    map.put("noncestr", noncestr);
    map.put("signature", weiXinPayService.getJsapiSignature(request, noncestr, timestamp));
    return map;
  }

}
