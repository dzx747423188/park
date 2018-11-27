package com.park.service.impl;

import com.park.common.Const;
import com.park.common.ServerResponse;
import com.park.common.TokenCache;
import com.park.dao.UserMapper;
import com.park.pojo.User;
import com.park.service.IUserService;
import com.park.util.MD5Util;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.UUID;

/**
 * Created by Park on 2018-8-28.
 */
@Service("iUserService")
public class UserService implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public ServerResponse<User> login(String username, String password) {
        int resultCount = userMapper.checkUserName(username);
        if (resultCount == 0){
            return  ServerResponse.createByErrorMessage("用户名不存在!");
        }
        //todo 密码登录MD5
        String MD5Password = MD5Util.MD5EncodeUtf8(password);
        User user = userMapper.selectLogin(username,MD5Password);
        if(user == null){
            return  ServerResponse.createByErrorMessage("密码错误!");
        }
        user.setPassword(StringUtils.EMPTY);
        return  ServerResponse.createBySuccess("登录成功!",user);
    }

    @Override
    public ServerResponse<String> register(User user) {
        int resultCount = userMapper.checkUserName(user.getUsername());
        if(resultCount>0){
            return  ServerResponse.createByErrorMessage("用户已存在!");
        }
        resultCount = userMapper.checkEmail(user.getEmail());
        if (resultCount>0){
            return  ServerResponse.createByErrorMessage("email已经存在!");
        }
        user.setRole(Const.role.ROLE_CUSTOMER);

        //MD5加密
        user.setPassword(MD5Util.MD5EncodeUtf8(user.getPassword()));
        resultCount = userMapper.insert(user);
        if(resultCount == 0){
                return  ServerResponse.createByErrorMessage("注册失败!");
        }
        return ServerResponse.createBySuccessMessage("注册成功!");
    }
    @Override
    public  ServerResponse<String> checkValid(String str , String type){
        if (StringUtils.isBlank(type)) {
            int resultCount ;
            if(Const.EMAIL.equals(type)){
                resultCount = userMapper.checkEmail(str);
                if (resultCount>0){
                    return  ServerResponse.createByErrorMessage("email已经存在!");
                }
            }
            if(Const.USERNAME.equals(type)){
                resultCount = userMapper.checkUserName(str);
                if(resultCount>0){
                    return  ServerResponse.createByErrorMessage("用户已存在!");
                }
            }

        }else {
            return  ServerResponse.createByErrorMessage("参数错误!");
        }
        return  ServerResponse.createBySuccessMessage("校验成功!");
    }

    @Override
    public ServerResponse<String> selectQuestion(String name) {
        ServerResponse<String>  validResponse = this.checkValid(name ,Const.USERNAME);
        if(validResponse.isSuccess()){
            return  ServerResponse.createByErrorMessage("用户不存在!");
        }
        String question  = userMapper.selectQuestionByUsername(name);
        if(StringUtils.isNotBlank(question)){
            return ServerResponse.createBySuccess(question);
        }
            return  ServerResponse.createByErrorMessage("找回密码的问题为空!");
    }

    @Override
    public ServerResponse<String> checkAnswer(String username, String question, String answer) {
        int resultCount = userMapper.checkAnswer(username,question,answer);
        if(resultCount>0){
            //说明问题及问题答案正确
            String forgetToken = UUID.randomUUID().toString();
            TokenCache.setKey("token_"+username , forgetToken);
            return  ServerResponse.createBySuccess(forgetToken);
        }
        return ServerResponse.createByErrorMessage("问题答案错误");
    }

    @Override
    public ServerResponse<String> forgetResetPassword(String username, String newPassword, String forgetToken) {
        if(StringUtils.isBlank(forgetToken)){
            return ServerResponse.createByErrorMessage("参数错误，forgetToken不能为空!");
        }
        ServerResponse validResponse = this.checkValid(username , Const.USERNAME);
        if(validResponse.isSuccess()){
            return  ServerResponse.createByErrorMessage("用户不存在!");
        }
        String token  = TokenCache.getKey("token_"+username);
        if(StringUtils.isBlank(token)){
            ServerResponse.createByErrorMessage("forgetToken无效或者已过期!");
        }
        if(StringUtils.equals(token , forgetToken)){
            String md5Password = MD5Util.MD5EncodeUtf8(newPassword);
            int row = userMapper.updatePasswordByUsername(username,md5Password);
            if(row > 0){
                return  ServerResponse.createBySuccess("密码修改成功!");
            }
        }else {
            return ServerResponse.createByErrorMessage("forgetToke错误,请重新获取重置密码的Token!");
        }
        return ServerResponse.createByErrorMessage("密码修改失败!");
    }

    @Override
    public ServerResponse<String> resetPassoword(String oldPassword, String newPassword, User user) {
            int resultCount = userMapper.checkPassword(MD5Util.MD5EncodeUtf8(oldPassword),user.getId());
            if(resultCount ==0){
                return  ServerResponse.createByErrorMessage("旧密码错误!");
            }
            user.setPassword(MD5Util.MD5EncodeUtf8(newPassword));
            int updateCount = userMapper.updateByPrimaryKeySelective(user);
            if(updateCount>0){
                return ServerResponse.createBySuccessMessage("密码修改成功!");
            }
            return  ServerResponse.createByErrorMessage("密码修改失败!");
    }

    @Override
    public ServerResponse<User> updateInformation(User user) {
        //username不能被更改
        //email也要做校验
        int resultCount = userMapper.checkEmailByUserId(user.getEmail(),user.getId());
        if(resultCount>0){
            return  ServerResponse.createByErrorMessage("email:"+user.getEmail()+"已经存在,请更换email!");
        }
        User updateUser = new User();
        updateUser.setId(user.getId());
        updateUser.setEmail(user.getEmail());
        updateUser.setPhone(user.getPhone());
        updateUser.setAnswer(user.getAnswer());
        updateUser.setQuestion(user.getQuestion());
        int updateCount = userMapper.updateByPrimaryKeySelective(updateUser);
        if(updateCount>0){
            return  ServerResponse.createBySuccess("更新个人信息成功",updateUser);
        }
            return  ServerResponse.createByErrorMessage("更新个人信息失败!");
    }

    @Override
    public ServerResponse<String> checkAdminRole(User user) {
        if(user!=null&&user.getRole().intValue() ==Const.role.ROLE_ADMIN){
            return ServerResponse.createBySuccess();
        }
        return ServerResponse.createByError();
    }

}
