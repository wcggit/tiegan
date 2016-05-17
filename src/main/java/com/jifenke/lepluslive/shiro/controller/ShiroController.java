package com.jifenke.lepluslive.shiro.controller;

import com.jifenke.lepluslive.global.util.MD5Util;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.ExcessiveAttemptsException;
import org.apache.shiro.authc.IncorrectCredentialsException;
import org.apache.shiro.authc.LockedAccountException;
import org.apache.shiro.authc.UnknownAccountException;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * Created by wcg on 16/3/31.
 */
@Controller
public class ShiroController {


  @RequestMapping(value = "/manage/login", method = RequestMethod.GET)
  public String loginForm() {
    return "login";
  }

  @RequestMapping(value = "/manage/login", method = RequestMethod.POST)
  public String login(MerchantUser user, RedirectAttributes redirectAttributes, Model model) {

    String username = user.getName();
    UsernamePasswordToken
        token =
        new UsernamePasswordToken(username, MD5Util.MD5Encode(user.getPassword(), "UTF-8"));
    //获取当前的Subject
    Subject currentUser = SecurityUtils.getSubject();
    try {
      //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
      //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
      //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
      currentUser.login(token);
    } catch (UnknownAccountException uae) {
        redirectAttributes.addFlashAttribute("message", "未知账户");
      } catch (IncorrectCredentialsException ice) {
        redirectAttributes.addFlashAttribute("message", "密码不正确");
      } catch (LockedAccountException lae) {
        redirectAttributes.addFlashAttribute("message", "账户已锁定");
      } catch (ExcessiveAttemptsException eae) {
        redirectAttributes.addFlashAttribute("message", "用户名或密码错误次数过多");
      } catch (AuthenticationException ae) {
        //通过处理Shiro的运行时AuthenticationException就可以控制用户登录失败或密码错误时的情景
        ae.printStackTrace();
        redirectAttributes.addFlashAttribute("message", "用户名或密码不正确");
      }
      //验证是否登录成功
      if (currentUser.isAuthenticated()) {
//        model.addAttribute("data", orderService.accountTurnover());
        return "index";
      } else {
        token.clear();
        return "redirect:/manage/login";
    }
  }

  @RequestMapping(value = "/manage/logout", method = RequestMethod.GET)
  public String logout(HttpServletRequest request, HttpServletResponse response) {
    //使用权限管理工具进行用户的退出，跳出登录，给出提示信息

    HttpSession session = request.getSession();
    session.invalidate();
    try {
      response.sendRedirect("/manage/login");
    } catch (IOException e) {
      e.printStackTrace();
    }
    return null;
  }

  @RequestMapping("/manage/403")
  public String unauthorizedRole() {
    return "403";
  }


  @RequestMapping("/manage/index")
  public String goIndex(Model model) {
    return "index";
  }

  @RequestMapping("/manage")
  public String goIndex(){
    return "login";
  }

}
