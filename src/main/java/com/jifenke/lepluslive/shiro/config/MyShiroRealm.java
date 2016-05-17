package com.jifenke.lepluslive.shiro.config;


import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

/**
 * Created by wcg on 16/3/31.
 */
public class MyShiroRealm extends AuthorizingRealm implements ApplicationContextAware {


  private ApplicationContext applicationContext;


  @Override
  protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principalCollection) {
    return null;
  }

  @Override
  protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken authenticationToken)
      throws AuthenticationException {
//    ManageUserRepository manageUserRepository =
//        (ManageUserRepository) applicationContext.getBean("manageUserRepository");
//    UsernamePasswordToken token=(UsernamePasswordToken) authenticationToken;
//    ManageUser manageUser = manageUserRepository.findUserByName(token.getUsername());
//    if(manageUser!=null){
//      return new SimpleAuthenticationInfo(manageUser.getName(), manageUser.getPassword(), getName());
//    }

    return null;
  }

  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
