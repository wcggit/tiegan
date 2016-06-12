package com.jifenke.lepluslive.shiro.config;

import com.jifenke.lepluslive.global.filter.WeiXinFilter;
import com.jifenke.lepluslive.merchant.service.MerchantWeiXinUserService;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.filter.authc.FormAuthenticationFilter;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.servlet.Filter;

/**
* Created by wcg on 16/3/31.
*/
@Configuration
@Lazy
public class ShiroConfig implements ApplicationContextAware {


  private ApplicationContext applicationContext;


  @Bean
  @Lazy
  public EhCacheManager getEhCacheManager() {
    EhCacheManager em = new EhCacheManager();
    em.setCacheManagerConfigFile("classpath:shiro-ehcache.xml");
    return em;
  }

  @Bean(name = "myShiroRealm")
  @Lazy
  public MyShiroRealm myShiroRealm(EhCacheManager cacheManager) {
    MyShiroRealm realm = new MyShiroRealm();
    realm.setCacheManager(cacheManager);
    return realm;
  }

  @Bean(name = "lifecycleBeanPostProcessor")
  @Lazy
  public LifecycleBeanPostProcessor getLifecycleBeanPostProcessor() {
    return new LifecycleBeanPostProcessor();
  }


  @Bean
  @Lazy
  public DefaultWebSessionManager defaultWebSessionManager() {
    DefaultWebSessionManager defaultWebSessionManager = new DefaultWebSessionManager();
    defaultWebSessionManager.setGlobalSessionTimeout(600000);
    defaultWebSessionManager.setDeleteInvalidSessions(true);
    return defaultWebSessionManager;
  }

  @Bean
  @Lazy
  public DefaultAdvisorAutoProxyCreator getDefaultAdvisorAutoProxyCreator() {
    DefaultAdvisorAutoProxyCreator daap = new DefaultAdvisorAutoProxyCreator();
    daap.setProxyTargetClass(true);
    return daap;
  }

  @Bean(name = "securityManager")
  @Lazy
  public DefaultWebSecurityManager getDefaultWebSecurityManager(MyShiroRealm myShiroRealm) {
    DefaultWebSecurityManager dwsm = new DefaultWebSecurityManager();
    dwsm.setRealm(myShiroRealm);
//      <!-- 用户授权/认证信息Cache, 采用EhCache 缓存 -->
    dwsm.setCacheManager(getEhCacheManager());
    dwsm.setSessionManager(defaultWebSessionManager());
    return dwsm;
  }


  @Bean
  @Lazy
  public AuthorizationAttributeSourceAdvisor getAuthorizationAttributeSourceAdvisor(
      DefaultWebSecurityManager securityManager) {
    AuthorizationAttributeSourceAdvisor aasa = new AuthorizationAttributeSourceAdvisor();
    aasa.setSecurityManager(securityManager);
    return aasa;
  }

  @Bean
  @Lazy
  public ShiroFilterFactoryBean shiroFilterFactoryBean(DefaultWebSecurityManager securityManager) {
    ShiroFilterFactoryBean shiroFilterFactoryBean = new ShiroFilterFactoryBean();
    shiroFilterFactoryBean.setSecurityManager(securityManager);
//    shiroFilterFactoryBean.setLoginUrl("/manage/login");
//    shiroFilterFactoryBean.setSuccessUrl("/manage/index");
//    shiroFilterFactoryBean.setUnauthorizedUrl("/manage/403");
    Map<String, Filter> loginFilter = new LinkedHashMap<String, Filter>();
//    FormAuthenticationFilter wxLoginFilter = new FormAuthenticationFilter();
//    wxLoginFilter.setLoginUrl("/wx");
//    wxLoginFilter.setSuccessUrl("/wx/index");

   // loginFilter.put("wxLoginFilter", wxLoginFilter);
    FormAuthenticationFilter webLoginFilter = new FormAuthenticationFilter();
    webLoginFilter.setLoginUrl("/web");
    webLoginFilter.setSuccessUrl("/web/index");
    loginFilter.put("webLoginFilter", webLoginFilter);
    shiroFilterFactoryBean.setFilters(loginFilter);

    WeiXinFilter weiXinFilter = new WeiXinFilter();
    weiXinFilter.setApplicationContext(applicationContext);
    weiXinFilter.setLoginUrl("/wx");
    loginFilter.put("wxFilter", weiXinFilter);

    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
    // anon：它对应的过滤器里面是空的,什么都没做
    filterChainDefinitionMap.put("/wx/userRegister", "anon");//anon 可以理解为不拦截
    //filterChainDefinitionMap.put("/**", "authc");//anon 可以理解为不拦截
    filterChainDefinitionMap.put("/wx", "anon");//anon 可以理解为不拦截
    filterChainDefinitionMap.put("/wx/**", "wxFilter");//anon 可以理解为不拦截
    //filterChainDefinitionMap.put("/wx/**", "wxLoginFilter");//anon 可以理解为不拦截
    filterChainDefinitionMap.put("/web/**", "webLoginFilter");//anon 可以理解为不拦截

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }


  @Override
  public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
    this.applicationContext = applicationContext;
  }
}
