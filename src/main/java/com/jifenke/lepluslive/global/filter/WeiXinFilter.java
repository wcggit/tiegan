package com.jifenke.lepluslive.global.filter;

import com.jifenke.lepluslive.global.config.Constants;
import com.jifenke.lepluslive.global.util.CookieUtils;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantUser;
import com.jifenke.lepluslive.merchant.domain.entities.MerchantWeiXinUser;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.subject.Subject;
import org.apache.shiro.web.filter.authz.AuthorizationFilter;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Created by wcg on 16/4/1.
 */
@Configuration
public class WeiXinFilter extends AuthorizationFilter {



  private ApplicationContext applicationContext;

  private String appId = Constants.APPID;

  private String weixinRootUrl = Constants.WEI_XIN_ROOT_URL;


  @Override
  protected boolean isAccessAllowed(ServletRequest servletRequest, ServletResponse servletResponse,
                                    Object o) throws Exception {
    HttpServletRequest request = (HttpServletRequest) servletRequest;
    HttpServletResponse response = (HttpServletResponse) servletResponse;
    //CookieUtils.setCookie(request,response,"rememberMe",null,0);
    String action = request.getRequestURI();
    System.out.println(action);
    if (action.equals("/wx") || action.indexOf("/wx/userRegister") != -1 || action
        .equals("/wx/pay")||action.indexOf("/wx/groupon") != -1) {
      return true;
    }
    String ua = request.getHeader("user-agent")
        .toLowerCase();
    if (ua.indexOf("micromessenger") > 0) {// 是微信浏览器
      String openId = CookieUtils.getCookieValue(request, appId + "-user-open-id");
      if (openId != null) {
        MerchantWeiXinUserService merchantWeiXinUserService =
            (MerchantWeiXinUserService) applicationContext.getBean("merchantWeiXinUserService");
        MerchantWeiXinUser
            merchantWeiXinUser =
            merchantWeiXinUserService.findWeiXinUserByOpenId(openId);
        if (merchantWeiXinUser != null) {
          MerchantUser merchantUser = merchantWeiXinUser.getMerchantUser();
          if (merchantUser != null) {
            UsernamePasswordToken
                token =
                new UsernamePasswordToken(merchantUser.getName(), merchantUser.getPassword(),
                                          "UTF-8");
            //获取当前的Subject
            //token.setRememberMe(true);
            Subject currentUser = SecurityUtils.getSubject();
            try {
              //在调用了login方法后,SecurityManager会收到AuthenticationToken,并将其发送给已配置的Realm执行必须的认证检查
              //每个Realm都能在必要时对提交的AuthenticationTokens作出反应
              //所以这一步在调用login(token)方法时,它会走到MyRealm.doGetAuthenticationInfo()方法中,具体验证方式详见此方法
              currentUser.login(token);
            } catch (Exception uae) {

            }
            return true;
          }
        }
      }
    }
    return false;
  }

  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
