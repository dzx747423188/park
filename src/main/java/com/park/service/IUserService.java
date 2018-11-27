package com.park.service;

import com.park.common.ServerResponse;
import com.park.pojo.User;

/**
 * Created by Park on 2018-8-28.
 */
public interface IUserService {
    //用户登录
    ServerResponse<User> login(String username , String password);
    //用户注册
    ServerResponse<String> register(User user);
    //校验
    ServerResponse<String> checkValid(String str , String type);
    ServerResponse<String> selectQuestion (String name);

    ServerResponse<String> checkAnswer(String username, String question, String answer);
    ServerResponse<String> forgetResetPassword (String username,String newPassword , String forgetToken);
    ServerResponse<String> resetPassoword(String oldPassword,String newPassword,User user);
    ServerResponse<User> updateInformation (User user);
    ServerResponse<String> checkAdminRole(User user);
}
