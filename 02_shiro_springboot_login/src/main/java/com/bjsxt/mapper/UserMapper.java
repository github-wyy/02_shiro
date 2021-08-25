package com.bjsxt.mapper;

import com.bjsxt.pojo.User;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.thymeleaf.spring5.processor.SpringUErrorsTagProcessor;

public interface UserMapper {
    //根据用户名查询用户信息
    @Select("select * from t_user where uname=#{uname}")
    User selUserInfoMapper(@Param("uname") String uname);
}
