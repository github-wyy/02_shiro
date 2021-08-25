package com.bjsxt.controller;

import com.bjsxt.pojo.User;
import com.bjsxt.service.UserService;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.authc.AuthenticationToken;
import org.apache.shiro.authc.UsernamePasswordToken;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.apache.shiro.subject.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class UserController {
    //声明service属性
    @Autowired
    private UserService userService;
    //声明单元方法：使用shiro认证
    @RequestMapping("userLogin2")
    public String userLogin2(String uname,String pwd,@RequestParam(defaultValue = "false") Boolean rememberme){
        //1.获取subject对象
            Subject subject = SecurityUtils.getSubject();
        //2.认证
            //创建认证对象存储认证信息
            AuthenticationToken token= new UsernamePasswordToken(uname,pwd,rememberme);
            try{
                subject.login(token);
                return "redirect:main";
            }catch(Exception e){
                e.printStackTrace();
            }
           return "redirect:login";
    }
    //声明单元方法：登录认证
    @RequestMapping("userLogin")
    public String userLogin(String uname,String pwd){
        //1.根据用户名获取用户信息
        User user=userService.selUserInfoService(uname);
        //2.判断用户名是否合法
        if(user!=null){
            //3.校验密码
            if(user.getPwd().equals(pwd)){
                //认证成功
                return "main";
            }
        }
        return "error";
    }
    //声明单元方法:
    @RequiresPermissions("user:insert2222")
    @ResponseBody
    @RequestMapping("demo")
    public  String demo(){
        return "ok";
    }


    //声明公共单元方法完成页面的内部转发
    @RequestMapping("{uri}")
    public String getPage(@PathVariable String uri){
        return uri;
    }

}
