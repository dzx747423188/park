package com.park.controller.portal;

import com.park.common.Const;
import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.pojo.User;
import com.park.service.ICategoryService;
import com.park.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpSession;

/**
 * Created by Park on 2018-11-5.
 */
@Controller
@RequestMapping("/manage/category/")
public class CategoryController {
    @Autowired
    private IUserService iUserService;
    @Autowired
    private ICategoryService iCategoryService;

    @RequestMapping(value = "get_category.do",method = RequestMethod.POST)
    public ServerResponse  getCategory(HttpSession session , @RequestParam(value = "parentId" ,defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user ==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录!");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
                return  iCategoryService.getChildrenParallelCategory(parentId);
        }else {
                return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限!");
        }
    }

    public ServerResponse addCategory(HttpSession session , String categoryName , @RequestParam(value = "parentId" ,defaultValue = "0") Integer parentId){
        User user = (User) session.getAttribute(Const.CURRENT_USER);
        if(user == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),"用户未登录，请先登录!");
        }
        if(iUserService.checkAdminRole(user).isSuccess()){
            return  null;
        }else {
            return  ServerResponse.createByErrorMessage("无权限操作，需要管理员权限!");
        }
        return null;
    }
}
