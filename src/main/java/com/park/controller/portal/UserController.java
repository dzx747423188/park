package com.park.controller.portal;

import com.park.common.Const;
import com.park.common.ServerResponse;
import com.park.pojo.User;
import com.park.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Park on 2018-8-20.
 */
@Controller
@RequestMapping("/user/")
public class UserController {

    @Autowired
    private IUserService iUserService;

    @RequestMapping(value = "index.do" , method = RequestMethod.GET)
    @ResponseBody
    public String login(){
        return "index";
    }

    /**
     * 用户登录
     * @param username
     * @param password
     * @param session
     * @return
     */
    @RequestMapping(value = "login.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<User> login(String username, String password, HttpSession session){

        ServerResponse<User> response = iUserService.login(username,password);
        if (response.isSuccess()){
            session.setAttribute(Const.CURRENT_USER ,response.getData());
        }
        return  response;
    }

    @RequestMapping(value = "logout.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<String> logout (HttpSession session){
        session.removeAttribute(Const.CURRENT_USER);
        return  ServerResponse.createBySuccess();
    }
    @RequestMapping(value = "register.do" , method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> register(User user){
        ServerResponse<String> response = iUserService.register(user);
        return  response;
    }
    @RequestMapping(value = "check_valid.do" , method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<String> checkValid(String str , String type){
        ServerResponse response = iUserService.checkValid(str , type);
        return  response;
    }
    @RequestMapping(value = "get_user_info.do" , method = RequestMethod.GET)
    @ResponseBody
    public  ServerResponse<User> getUserInfo(HttpSession session){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user !=null){
            return  ServerResponse.createBySuccess(user);
        }
            return ServerResponse.createByErrorMessage("用户未登录,无法获取用户信息!");
    }
    @RequestMapping(value = "forget_get_question.do" , method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> forgetGetQuestion(String username){
        return iUserService.selectQuestion(username);
    }
    @RequestMapping(value = "forget_check_answer.do" , method = RequestMethod.POST)
    @ResponseBody
    public  ServerResponse<String> forgetCheckAnswer(String username , String question , String answer){
        return iUserService.checkAnswer(username,question ,answer);
    }
    @RequestMapping(value = "forget_reset_password.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> forgetResetPassword(String username , String newPassword , String forgetToken){
        return iUserService.forgetResetPassword(username,newPassword,forgetToken) ;
    }
    @RequestMapping(value = "reset_password.do" , method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse<String> resetPassword (HttpSession session ,String oldPassword , String newPassword){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return ServerResponse.createByErrorMessage("用户未登录!");
        }
        return iUserService.resetPassoword(oldPassword,newPassword,user);
    }
    @RequestMapping(value = "update_information.do" , method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<User> updateInformation(HttpSession session , User user){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorMessage("用户未登录!");
        }
        user.setId(currentUser.getId());
        user.setUsername(currentUser.getUsername());
        ServerResponse<User> response = iUserService.updateInformation(user);
        if(response.isSuccess()){
            response.getData().setUsername(currentUser.getUsername());
            session.setAttribute(Const.CURRENT_USER,response.getData());
        }
        return response;
    }
}
