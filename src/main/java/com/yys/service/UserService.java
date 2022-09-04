package com.yys.service;

import com.yys.entity.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【user】的数据库操作Service
* @createDate 2022-09-02 21:37:56
*/
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount 账户
     * @param userPassword 密码
     * @param checkPassword 效验密码
     * @return
     */
    long userRegister(String userAccount,String userPassword ,String checkPassword);




}
