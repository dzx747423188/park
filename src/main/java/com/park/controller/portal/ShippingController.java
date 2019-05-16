package com.park.controller.portal;

import com.park.common.Const;
import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.pojo.Shipping;
import com.park.pojo.User;
import com.park.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;
import java.util.List;

/**
 * Created by Park on 2018-12-20.
 */
@Controller
@RequestMapping("/shipping/")
public class ShippingController {
    @Autowired
    private IShippingService shippingService;


    @RequestMapping(value = "add.do" ,method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse add(HttpSession session , Shipping shipping){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.save(currentUser.getId(),shipping);
    }
    @RequestMapping(value = "delete.do")
    @ResponseBody
    public ServerResponse delete(HttpSession session ,Integer shippingId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return  shippingService.delete(currentUser.getId(),shippingId);
    }
    @RequestMapping(value = "update.do",method = RequestMethod.POST)
    @ResponseBody
    public ServerResponse update(HttpSession session ,Shipping shipping){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.update(currentUser.getId(),shipping);
    }

    @RequestMapping(value = "list.do")
    @ResponseBody
    public ServerResponse<List<Shipping>> list(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return shippingService.list(currentUser.getId());
    }

    @RequestMapping(value = "select.do")
    @ResponseBody
    public ServerResponse<Shipping> select(HttpSession session , Integer shippingId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null || shippingId == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
            return shippingService.select(currentUser.getId(),shippingId);
    }
}

