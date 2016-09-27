package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;
import com.jifenke.lepluslive.weixin.domain.entities.Dictionary;
import com.jifenke.lepluslive.weixin.domain.entities.WeiXinUser;
import com.jifenke.lepluslive.weixin.service.DictionaryService;
import com.jifenke.lepluslive.weixin.service.WeiXinAwardService;
import com.jifenke.lepluslive.weixin.service.WeiXinUserService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by xf on 2016/9/26.
 */
@RestController
public class WeiXinAwardController {


  @Inject
  private MerchantWeiXinUserService merchantWeiXinUserService;
  @Inject
  private WeiXinAwardService weiXinAwardService;
  @Inject
  private WeiXinUserService weiXinUserService;
  @Inject
  private DictionaryService dictionaryService;

  private static Logger log = LoggerFactory.getLogger(WeiXinAwardController.class);

  private String appId = Constants.APPID;

  /**
   * 转发到页面 (用户信息 + 头像 + 统计)
   */
  @RequestMapping("/wx/merchant_award")
  public ModelAndView merchantAwardPage(HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    WeiXinUser weiXinUser = weiXinUserService.findWeiXinUserByOpenId(openId);
    MerchantWeiXinUser
        merchantWeiXinUser = merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    Long
        noRebate =
        weiXinAwardService.findRebateCountByMerchantAndState(merchant, 0);        // 未发放奖金
    Long
        totalRebate =
        weiXinAwardService.findRebateCountByMerchantAndState(merchant, 1);     // 累计奖金
    Long awardNum = weiXinAwardService.findAwardNumByMerchant(merchant);
    Dictionary dictionary = dictionaryService.findByName("最低发放金额");
    Long minMoney = null;
    if (dictionary != null) {
      minMoney =
          new Long(dictionary.getValue());                                             // 发放金额
    }
    ModelAndView modelAndView = new ModelAndView();
    modelAndView.setViewName("/weixin/merchantAward");
    modelAndView.addObject("nickName", weiXinUser.getNickname());
    modelAndView.addObject("headImage", weiXinUser.getHeadImageUrl());
    modelAndView.addObject("merchantName", merchant.getName());
    modelAndView.addObject("noRebate", noRebate);
    modelAndView.addObject("totalRebate", totalRebate);
    modelAndView.addObject("awardNum", awardNum);
    modelAndView.addObject("minMoney", minMoney);
    return modelAndView;
  }

  /**
   * 下拉分页加载数据
   */
  @RequestMapping(value = "/wx/merchant_award_list", method = RequestMethod.GET)
  public LejiaResult merchantAwardList(HttpServletRequest request,
                                       @RequestParam(value = "page", required = false) Integer offset) {
    if (offset == null) {
      offset = 1;
    }
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    MerchantWeiXinUser
        merchantWeiXinUser =
        merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
    Merchant merchant = merchantWeiXinUser.getMerchantUser().getMerchant();
    Page page = weiXinAwardService.findAwardOrderByMerchantSortByCreateDate(merchant, offset);
    return LejiaResult.ok(page);
  }

}
