package com.park.controller.portal;

import com.park.common.Const;
import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.pojo.User;
import com.park.service.ICartService;
import com.park.vo.CartVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

/**
 * Created by Park on 2018-12-19.
 */
@Controller
@RequestMapping("/cart/")
public class CartController {
    @Autowired
    private ICartService cartService;
    @RequestMapping(value = "/add.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse add(HttpSession session ,Integer count ,Integer productId){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser==null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.add(currentUser.getId(),productId,count);
    }
    @RequestMapping(value = "/list.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> list(HttpSession session){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.list(currentUser.getId());
    }
    @RequestMapping(value = "/update.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> update(HttpSession session ,Integer productId , Integer count){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.update(currentUser.getId(),productId,count);
    }
    @RequestMapping(value = "/delete_product.do" ,method = RequestMethod.GET)
    @ResponseBody
    public ServerResponse<CartVo> deleteProducts(HttpSession session ,String productIds){
        User currentUser = (User) session.getAttribute(Const.CURRENT_USER);
        if(currentUser == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.NEED_LOGIN.getCode(),ResponseCode.NEED_LOGIN.getDesc());
        }
        return cartService.deleteProduct(currentUser.getId(),productIds);
    }
}
