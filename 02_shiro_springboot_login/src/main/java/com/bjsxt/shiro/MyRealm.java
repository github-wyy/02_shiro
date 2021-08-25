package com.bjsxt.shiro;

import com.bjsxt.pojo.User;
import com.bjsxt.service.UserService;
import org.apache.shiro.authc.AuthenticationException;
import org.apache.shiro.authc.AuthenticationInfo;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.SimpleAuthenticationInfo;
import org.apache.shiro.authz.AuthorizationInfo;
import org.apache.shiro.authz.SimpleAuthorizationInfo;
import org.apache.shiro.realm.AuthorizingRealm;
import org.apache.shiro.subject.PrincipalCollection;
import org.apache.shiro.util.ByteSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class MyRealm extends AuthorizingRealm {
    //声明业务层属性
    @Autowired
    private UserService userService;
    //自定义授权策略
    @Override
    protected AuthorizationInfo doGetAuthorizationInfo(PrincipalCollection principals) {
        //1.从数据库中获取用户的权限信息
        //2.将权限信息存储到shiro授权对象中
        System.out.println("我是授权认证方法，我被执行了");
        SimpleAuthorizationInfo info=new SimpleAuthorizationInfo();
        info.addRole("role1");
        info.addRole("role2");
        info.addStringPermission("user:insert");
        info.addStringPermission("user:update");
        info.addStringPermission("sys:*");
        return info;
    }
    //自定义认证策略
    @Override
    protected AuthenticationInfo doGetAuthenticationInfo(AuthenticationToken token) throws AuthenticationException {
        //声明认证代码
                //1.获取用户传递的用户名信息
                Object principal = token.getPrincipal();
                //2.根据用户名获取数据库中的用户信息
                User user = userService.selUserInfoService((String) principal);
                //3.认证
                if(user!=null){//用户名是正确的
                    //4.认证密码
                    AuthenticationInfo info=  new SimpleAuthenticationInfo(principal,user.getPwd(), ByteSource.Util.bytes(user.getUid()+""),user.getUname());
                    return info;
            }
        return null;
    }
}
