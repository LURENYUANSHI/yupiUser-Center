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
import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.yys.constant.UserConstant.USER_LOGIN_STATE;

/**
 * @author Administrator
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2022-09-02 21:37:56
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    //盐
    private static final String SALT = "yys";
    /**
     * 用户登录态
     */
    @Resource
    private UserMapper userMapper;

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
            //todo 修改为自定义异常
            return -1;
        }
        //账户位数小于4
        if (userAccount.length() < 4) {
            return -1;
        }
        //密码位数小于8
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

        //加点盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //3.插入数据
        User user = new User();
        user.setUserAccount(userAccount);
        user.setUserPassword(encryptPassword);
        //防止long类型拆箱失败
        boolean save = this.save(user);
        if (!save) {
            return -1;
        }
        return user.getId();
    }

    /**
     * 用户登录
     *
     * @param userAccount
     * @param userPassword
     * @return 脱敏后的信息
     */
    @Override
    public User userLogin(String userAccount, String userPassword, HttpServletRequest request) {
        //1.效验非空
        if (StringUtils.isAnyBlank(userAccount, userPassword)) {
            return null;
        }
        //账户位数小于4
        if (userAccount.length() < 4) {
            return null;
        }
        //验证名称是否包含特殊字符
        String regEx = "[` ~!@#$%^&*()+=|{}':;',\\\\[\\\\].<>/?~！@#￥%……&*（）——+|{}【】‘；：”“’。，、？]";
        Matcher matcher = Pattern.compile(regEx).matcher(userAccount);
        if (matcher.find()) {
            return null;
        }

        //2.加密密码
        //加点盐
        String encryptPassword = DigestUtils.md5DigestAsHex((SALT + userPassword).getBytes(StandardCharsets.UTF_8));
        //查询用户是否存在
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("userAccount", userAccount);
        queryWrapper.eq("userPassword", encryptPassword);
        User user = userMapper.selectOne(queryWrapper);
        //用户不存在
        if (user == null) {
            log.info("user login failed,userAccount cannot match");
            return null;
        }
        //3.用户脱敏
        User safetyUser=getSafeUser(user);
        //4.记录用户的登录态
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        return safetyUser;
    }

    /**
     * 用户脱敏
     * @param user
     * @return
     */

    @Override
    public User getSafeUser(User user) {
        User safetyUser = new User();
        safetyUser.setId(user.getId());
        safetyUser.setUsername(user.getUsername());
        safetyUser.setAvatarUrl(user.getAvatarUrl());
        safetyUser.setUserRole(user.getUserRole());
        safetyUser.setGender(user.getGender());
        safetyUser.setPhone(user.getPhone());
        safetyUser.setEmail(user.getEmail());
        safetyUser.setUserStatus(user.getUserStatus());
        safetyUser.setCreateTime(user.getCreateTime());
        safetyUser.setUserAccount(user.getUserAccount());
        return safetyUser;
    }
}




