package com.jifenke.lepluslive.shiro.controller;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.global.util.LejiaResult;
import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.global.util.MvUtil;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.service.MerchantService;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.util.SavedRequest;
import org.apache.shiro.web.util.WebUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.net.URLEncoder;

/**
 * Created by wcg on 16/3/31.
 */
@Controller
public class ShiroController {

  private String appId = Constants.APPID;

  private String weixinRootUrl = Constants.WEI_XIN_ROOT_URL;

  @Inject
  private MerchantService merchantService;

  @Inject
  private MerchantWeiXinUserService merchantWeiXinUserService;


  @RequestMapping(value = "/wx", method = RequestMethod.GET)
  public ModelAndView wxLoginForm(HttpServletResponse response, HttpServletRequest request) {
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    if (openId == null) {
      SavedRequest savedRequest = WebUtils.getSavedRequest(request);
      String callbackUrl = null;
      // 获取保存的URL
      if (savedRequest != null && savedRequest.getRequestUrl() != null) {
        callbackUrl = weixinRootUrl + "/wx/userRegister?action=" + savedRequest.getRequestUrl();
      } else {
        callbackUrl = weixinRootUrl + "/wx/userRegister";
      }
      String
          redirectUrl = null;
      try {
        redirectUrl =
            "https://open.weixin.qq.com/connect/oauth2/authorize?appid=" + appId + "&redirect_uri="
            +
            URLEncoder.encode(callbackUrl, "UTF-8")
            + "&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
        //  request.getRequestDispatcher(redirectUrl).forward(request,response);
        response.sendRedirect(redirectUrl);
        return null;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }

    return MvUtil.go("/weixin/login");
  }

  @RequestMapping(value = "/wx", method = RequestMethod.POST)
  public
  @ResponseBody
  LejiaResult wxLogin(@RequestBody MerchantUser user, HttpServletRequest request) {
    String username = user.getName();
    UsernamePasswordToken
        token =
        new UsernamePasswordToken(username, MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
    //获取当前的Subject
    Subject currentUser = SecurityUtils.getSubject();
    //token.setRememberMe(true);
    LejiaResult lejiaResult = new LejiaResult();
    try {
      //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
      //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
      //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
      currentUser.login(token);
    } catch (Exception uae) {
      lejiaResult.setMsg("用户名或密码不正确");
    }
    //验证是否登录成功
    if (currentUser.isAuthenticated()) {
//        model.addAttribute("data", orderService.accountTurnover());
      String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
      try {
        merchantWeiXinUserService
            .bindMerchantUser(openId, merchantService.findMerchantUserByName(user.getName()));
      } catch (Exception e) {
        lejiaResult.setStatus(500);
        lejiaResult.setMsg("无权登录");
        token.clear();
        return lejiaResult;
      }
      lejiaResult.setStatus(200);
      SavedRequest savedRequest = WebUtils.getSavedRequest(request);
      // 获取保存的URL
      if (savedRequest != null && savedRequest.getRequestUrl() != null) {
        lejiaResult.setData(savedRequest.getRequestUrl());
      }
      //  lejiaResult.setData("/wx/index");
      return lejiaResult;
    } else {
      lejiaResult.setStatus(500);
      token.clear();
      return lejiaResult;
    }
  }

//  @RequestMapping(value = "/web", method = RequestMethod.GET)
//  public String webLoginForm() {
//    return "login";
//  }
//
//
//  @RequestMapping(value = "/web", method = RequestMethod.POST)
//  public String webLogin(MerchantUser user, RedirectAttributes redirectAttributes, Model model) {
//
//    String username = user.getName();
//    UsernamePasswordToken
//        token =
//        new UsernamePasswordToken(username, MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
//    //获取当前的Subject
//    Subject currentUser = SecurityUtils.getSubject();
//    try {
//      //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
//      //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
//      //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
//      currentUser.login(token);
//    } catch (UnknownAccountException uae) {
//      redirectAttributes.addFlashAttribute("message", "未知账户");
//    } catch (IncorrectCredentialsException ice) {
//      redirectAttributes.addFlashAttribute("message", "密码不正确");
//    } catch (LockedAccountException lae) {
//      redirectAttributes.addFlashAttribute("message", "账户已锁定");
//    } catch (ExcessiveAttemptsException eae) {
//      redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
//    } catch (AuthenticationException ae) {
//      //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
//      ae.printStackTrace();
//      redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
//    }
//    //验证是否登录成功
//    if (currentUser.isAuthenticated()) {
////        model.addAttribute("data", orderService.accountTurnover());
//      return "index";
//    } else {
//      token.clear();
//      return "redirect:/manage/login";
//    }
//  }



  @RequestMapping(value = "/wx/logout", method = RequestMethod.GET)
  public String logoutConfrim(HttpServletRequest request, HttpServletResponse response) {
    //使用权限管理工具进行用户的退出，跳出登录，给出提示信息
    String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
    merchantWeiXinUserService.unbindMerchantUser(openId);

    Subject subject = SecurityUtils.getSubject();
    if (subject.isAuthenticated()) {
      subject.logout(); // session 会销毁，在SessionListener监听session销毁，清理权限缓存
    }
    try {
      response.sendRedirect("/wx");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }


}
