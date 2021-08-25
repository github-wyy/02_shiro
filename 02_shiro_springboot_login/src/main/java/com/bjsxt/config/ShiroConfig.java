package com.bjsxt.config;

import at.pollux.thymeleaf.shiro.dialect.ShiroDialect;
import com.bjsxt.shiro.MyRealm;
import org.apache.shiro.authc.credential.HashedCredentialsMatcher;
import org.apache.shiro.cache.ehcache.EhCacheManager;
import org.apache.shiro.codec.Base64;
import org.apache.shiro.io.ResourceUtils;
import org.apache.shiro.spring.web.config.DefaultShiroFilterChainDefinition;
import org.apache.shiro.spring.web.config.ShiroFilterChainDefinition;
import org.apache.shiro.web.mgt.CookieRememberMeManager;
import org.apache.shiro.web.mgt.DefaultWebSecurityManager;
import org.apache.shiro.web.servlet.SimpleCookie;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.InputStream;

@Configuration
public class ShiroConfig {
    //声明MyRealm属性
    @Autowired
    private MyRealm myRealm;
    //声明bean方法
    @Bean
    public DefaultWebSecurityManager securityManager(){
        DefaultWebSecurityManager defaultWebSecurityManager=new DefaultWebSecurityManager();
        //创建凭证匹配器
        HashedCredentialsMatcher matcher=new HashedCredentialsMatcher();
        //设置匹配器的加密算法
        matcher.setHashAlgorithmName("md5");
        //设置匹配器的迭代加密次数
        matcher.setHashIterations(2);
        //将匹配器注入到自定义的认证策略对象中
        myRealm.setCredentialsMatcher(matcher);
        //将自定义的认证策略对象注入到SecurityManager
        defaultWebSecurityManager.setRealm(myRealm);
        //将CookieRememberMeManager对象注入到SecurityManager，开启了rememberme功能
        defaultWebSecurityManager.setCacheManager(ehCacheManager());
        return defaultWebSecurityManager;
    }
    //设置Cookie的信息
    public SimpleCookie rememberMeCookie(){
        SimpleCookie simpleCookie=new SimpleCookie("rememberMe");
        //设置有效路径
        simpleCookie.setPath("/");
        //设置声明周期
        simpleCookie.setMaxAge(30*24*60*60);
        //返回设置的cookie
        return simpleCookie;
    }

    //创建rememberMeManager对象
    public CookieRememberMeManager rememberMeManager(){
        //创建CookieRememberMeManager对象
        CookieRememberMeManager cookieRememberMeManager=new CookieRememberMeManager();
        //注入Cookie对象
        cookieRememberMeManager.setCookie(rememberMeCookie());
        //设置密钥
        cookieRememberMeManager.setCipherKey(Base64.decode("MTIzNDU2Nzg="));
        //返回
        return cookieRememberMeManager;

    }
    //自定义shiro过滤器参数bean
    @Bean
    public ShiroFilterChainDefinition shiroFilterChainDefinition(){
        DefaultShiroFilterChainDefinition definition = new DefaultShiroFilterChainDefinition();
        definition.addPathDefinition("/login", "anon");
        definition.addPathDefinition("/userLogin2", "anon");
        //开启shiro内置的退出过滤器，完成退出功能
        definition.addPathDefinition("/logout", "logout");
        //definition.addPathDefinition("/main", "anon");
        definition.addPathDefinition("/**", "user");
        return definition;
    }
    //创建Bean方法，创建CarManager对象
    @Bean
    public EhCacheManager ehCacheManager(){
        //创建ehCacheManager对象
        EhCacheManager  ehCacheManager=new EhCacheManager();
        //获取配置文件的流对象
        InputStream is=null;
        try{
             is = ResourceUtils.getInputStreamForPath("classpath:ehcache/ehcache-shiro.xml");

        }catch (Exception e){
            e.printStackTrace();
        }
        //获取CarManager对象
        net.sf.ehcache.CacheManager cacheManager = new net.sf.ehcache.CacheManager(is);
        ehCacheManager.setCacheManager(cacheManager);
        //返回
        return  ehCacheManager;

    }
    //创建解析Thymeleaf中的shiro属性的对象，由SpringBoot项目底层自动调用
    @Bean
    public ShiroDialect shiroDialect() {
        return new ShiroDialect();
    }

}
