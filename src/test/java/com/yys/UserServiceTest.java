package com.yys;

import org.junit.jupiter.api.Assertions;
import org.opentest4j.AssertionFailedError;
import com.yys.service.UserService;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import javax.annotation.Resource;

@SpringBootTest
public class UserServiceTest {

    @Resource
    private UserService userService;

    @Test
    void userRegister(){
        String username="yyss";
        String userPassword="";
        String checkPassword="123456";
        long result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        username="yy";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        username="yyss";
        userPassword="123456";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        username="yu pi";
        userPassword="12345678";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        checkPassword="123456789";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        username="123";
        userPassword="12345678";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertEquals(-1,result);
        username="yupiss";
        userPassword="123456789";
        checkPassword="123456789";
        result = userService.userRegister(username, userPassword, checkPassword);
        Assertions.assertTrue(result>0);
    }



}
