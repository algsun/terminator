package com.microwise.terminator.sys.shiro;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.microwise.terminator.common.security.shiro.session.CacheSessionDAO;
import com.microwise.terminator.common.security.shiro.session.SessionDAO;
import com.microwise.terminator.common.security.shiro.session.SessionManager;
import com.microwise.terminator.common.utils.IdGen;
import org.apache.shiro.cache.CacheManager;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.mgt.RememberMeManager;
import org.apache.shiro.realm.Realm;
import org.apache.shiro.spring.web.config.AbstractShiroWebConfiguration;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.autoproxy.DefaultAdvisorAutoProxyCreator;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.thymeleaf.dialect.IDialect;
import org.thymeleaf.spring4.SpringTemplateEngine;
import org.thymeleaf.templateresolver.TemplateResolution;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lijianfei on 17/2/14.
 */
@Configuration
@ControllerAdvice
public class ShiroConfiguration extends AbstractShiroWebConfiguration {

    private static Logger log = LoggerFactory.getLogger(ShiroConfiguration.class);


    @Bean
    public Realm realm() {
        return new SystemAuthorizingRealm();
    }

    @Bean
    public CacheManager cacheManager() {
        EhCacheManager cacheManager = new EhCacheManager();
        cacheManager.setCacheManagerConfigFile("classpath:cache/ehcache-shiro.xml");
        return cacheManager;
    }

    @Bean
    public SessionDAO sessionDAO(IdGen idGen) {
        CacheSessionDAO sessionDAO = new CacheSessionDAO();
        sessionDAO.setSessionIdGenerator(idGen);
        sessionDAO.setActiveSessionsCacheName("activeSessionsCache");
        sessionDAO.setCacheManager(cacheManager());
        return sessionDAO;
    }

    @Bean
    public SessionManager sessionManager(SessionDAO sessionDAO) {
        SessionManager sessionManager = new SessionManager();
        sessionManager.setSessionDAO(sessionDAO);
        return sessionManager;
    }

    @Bean
    public DefaultWebSecurityManager securityManager(List<Realm> realms, RememberMeManager rememberMeManager) {
        DefaultWebSecurityManager securityManager = (DefaultWebSecurityManager) super.securityManager(realms);
        securityManager.setSessionManager(sessionManager());
        securityManager.setCacheManager(cacheManager());
        securityManager.setRememberMeManager(rememberMeManager);
        return securityManager;
    }

    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition() {
        DefaultShiroFilterChainDefinition shiroFilterChainDefinition = new DefaultShiroFilterChainDefinition();
        // use permissive to NOT require authentication, our controller Annotations will decide that
        shiroFilterChainDefinition.addPathDefinition("/", "authc");
        shiroFilterChainDefinition.addPathDefinition("/static", "anon");
        shiroFilterChainDefinition.addPathDefinition("/servlet/**", "anon");

        shiroFilterChainDefinition.addPathDefinition("/autoconfig", "anon");
        shiroFilterChainDefinition.addPathDefinition("/login", "authc");
        shiroFilterChainDefinition.addPathDefinition("/logout", "logout");
        shiroFilterChainDefinition.addPathDefinition("/**", "user");
        shiroFilterChainDefinition.addPathDefinition("/webjars/**", "anon");
        // shiroFilterChainDefinition.addPathDefinition("/**","anon");
        return shiroFilterChainDefinition;
    }

    @Bean
    public DefaultAdvisorAutoProxyCreator defaultAdvisorAutoProxyCreator() {
        DefaultAdvisorAutoProxyCreator proxyCreator = new DefaultAdvisorAutoProxyCreator();
        proxyCreator.setProxyTargetClass(true);
        return proxyCreator;
    }

    @Bean
    public FormAuthenticationFilter formAuthenticationFilter() {
        return new FormAuthenticationFilter();
    }


    @Bean
    public FilterRegistrationBean delegatingFilterProxy() {
        FilterRegistrationBean filterRegistrationBean = new FilterRegistrationBean();
        DelegatingFilterProxy proxy = new DelegatingFilterProxy();
        proxy.setTargetFilterLifecycle(true);
        proxy.setTargetBeanName("shiroFilterFactoryBean");
        filterRegistrationBean.setFilter(proxy);
        return filterRegistrationBean;
    }

    /**
     * ShiroDialect，为了在thymeleaf里使用shiro的标签的bean
     *
     * @return
     */
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }
}
