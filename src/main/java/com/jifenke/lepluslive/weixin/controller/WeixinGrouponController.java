package com.jifenke.lepluslive.weixin.controller;

import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.groupon.domain.entities.GrouponCode;
import com.jifenke.lepluslive.groupon.service.GrouponService;
import com.jifenke.lepluslive.merchant.domain.entities.Merchant;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.TemporaryMerchantUserShop;
import com.jifenke.lepluslive.merchant.service.MerchantUserService;
import com.jifenke.lepluslive.merchant.service.TemporaryMerchantUserService;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.subject.Subject;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;

/**
 * Created by wcg on 2017/6/19.
 */
@RestController("/wx")
public class WeixinGrouponController {

  @Inject
  private MerchantUserService merchantUserService;

  @Inject
  private TemporaryMerchantUserService temporaryMerchantUserService;

  @Inject
  private GrouponService grouponService;


  @RequestMapping(value = "/groupon", method = RequestMethod.GET)
  public ModelAndView goGrouponPage(Model model) {
    Merchant merchant = getMerchant();
    model.addAttribute("groupons", grouponService.findGrouponCodeByMerchant(merchant));
    return MvUtil.go("/weixin/groupon/groupon");
  }

  @RequestMapping(value = "/groupon/{sid}", method = RequestMethod.GET)
   public LejiaResult findGrouponCodeBySid(@PathVariable String sid) {
    GrouponCode grouponCode = grouponService.findGrouponCodeBySid(sid);
    if(grouponCode!=null){
      if(grouponCode.getState()==0){
        return LejiaResult.build(200,"",grouponCode);
      }else {
        return LejiaResult.build(500,"无效的卷码");
      }
    }else {

      return LejiaResult.build(500,"不存在的卷码");
    }
  }

  @RequestMapping(value = "/groupon/check/{sid}", method = RequestMethod.GET)
  public LejiaResult checkGrouponCode(@PathVariable String sid) {
    GrouponCode grouponCode = grouponService.findGrouponCodeBySid(sid);
    Subject currentUser = SecurityUtils.getSubject();
    PrincipalCollection principals = currentUser.getPrincipals();
    String userName = (String) principals.getPrimaryPrincipal();
    MerchantUser merchantUser = merchantUserService.findByName(userName);
    if(grouponCode.getGrouponProduct().getMerchantUser().getId()==merchantUser.getId()){
      if(grouponCode.getState()!=0){
        return LejiaResult.build(500,"无效的卷码");
      }else {
        grouponService.chargeOffGrouponCode(grouponCode,getMerchant(),merchantUser.getName());
        return LejiaResult.build(200,"核销成功");
      }
    }else {
      return LejiaResult.build(500,"无权限核销");
    }
  }

  public Merchant getMerchant() {
    Subject currentUser = SecurityUtils.getSubject();
    PrincipalCollection principals = currentUser.getPrincipals();
    String userName = (String) principals.getPrimaryPrincipal();
    MerchantUser merchantUser = merchantUserService.findByName(userName);
    TemporaryMerchantUserShop
        temporaryMerchantUserShop =
        temporaryMerchantUserService.findByMerchantUserId(merchantUser.getId());
    Merchant merchant = temporaryMerchantUserShop.getMerchant();
    return merchant;
  }

}
