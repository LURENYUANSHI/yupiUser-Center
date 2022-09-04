package com.yys.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yys.entity.User;
import com.yys.service.UserService;
import com.yys.mapper.UserMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Administrator
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-09-02 21:37:56
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Resource
private     UserMapper userMapper;

    /**
     * 用户与实现类
     *
     * @param userAccount   账户
     * @param userPassword  密码
     * @param checkPassword 效验密码
     * @return
     */
    @Override
    public long userRegister(String userAccount, String userPassword, String checkPassword) {
        //1.效验非空
        if (StringUtils.isAnyBlank(userAccount, userPassword, checkPassword)) {
            return -1;
        }
        //账户位数小于4
        if (userAccount.length() < 4) {
            return -1;
        }
        //账户密码位数小于8
        if (userPassword.length() < 8 || checkPassword.length() < 8) {
            return -1;
        }
        //验证名称是否包含特殊字符
        String regEx = "[` ~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return -1;
        }
        //密码和校验密码不相同
        if (!userPassword.equals(checkPassword)) {
            return -1;
        }
        //账户不能重复
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        int count = userMapper.selectCount(queryWrapper);
        if (count > 0) {
            return -1;
        }
        //2.加密密码
        final String SALT="yys";
        //加点盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //3.插入数据
        User user=new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        //防止long类型拆箱失败
        boolean save = this.save(user);
        if (!save){
            return -1;
        }
        return user.getId();
    }
}




