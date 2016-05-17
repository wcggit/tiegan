package com.jifenke.lepluslive.shiro.config;

import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.spring.LifecycleBeanPostProcessor;
import org.apache.shiro.spring.security.interceptor.AuthorizationAttributeSourceAdvisor;
import org.apache.shiro.spring.web.ShiroFilterFactoryBean;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.session.mgt.DefaultWebSessionManager;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;

import java.util.LinkedHashMap;
import java.util.Map;

/**
* Created by wcg on 16/3/31.
*/
@Configuration
@Lazy
public class ShiroConfig {

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
    shiroFilterFactoryBean.setLoginUrl("/manage/login");
    shiroFilterFactoryBean.setSuccessUrl("/manage/index");
    shiroFilterFactoryBean.setUnauthorizedUrl("/manage/403");
    Map<String, String> filterChainDefinitionMap = new LinkedHashMap<String, String>();
    // anon：它对应的过滤器里面是空的,什么都没做
    filterChainDefinitionMap.put("/**", "authc");//anon 可以理解为不拦截

    shiroFilterFactoryBean.setFilterChainDefinitionMap(filterChainDefinitionMap);
    return shiroFilterFactoryBean;
  }


}
