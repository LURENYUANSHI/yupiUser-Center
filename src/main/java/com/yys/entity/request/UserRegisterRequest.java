package com.yys.entity.request;


import lombok.Data;

import java.io.Serializable;

/**
 * 用户注册请求体
 *
 * @author yys
 */
@Data
public class UserRegisterRequest implements Serializable {


    private static final long serialVersionUID = -4006652780565661585L;
    private String userAccount;
    private String userPassword;
    private String checkPassword;


}
