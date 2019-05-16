package com.park.service.impl;

import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.dao.ShippingMapper;
import com.park.pojo.Shipping;
import com.park.service.IShippingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Park on 2018-12-20.
 */
@Service("iShippingSevice")
public class ShippingServiceImpl implements IShippingService {

    @Autowired
    private ShippingMapper shippingMapper;

    @Override
    public ServerResponse save(Integer userId, Shipping shipping) {
        shipping.setUserId(userId);
        int rowCount = shippingMapper.insert(shipping);
        if(rowCount > 0){
            Map<String , Integer> result = new HashMap<String ,Integer>();
            result.put("shippingId",shipping.getId());
            return  ServerResponse.createBySuccess("新建地址成功!",result);
        }
        return ServerResponse.createByErrorMessage("新建地址失败!");
    }

    @Override
    public ServerResponse delete(Integer userId, Integer shippingId) {
        if(shippingId == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        int rowCount = shippingMapper.deleteByPrimaryKey(shippingId);
        if(rowCount > 0 ){
            return  ServerResponse.createByErrorMessage("删除成功!");
        }
        return ServerResponse.createByErrorMessage("删除失败!");
    }

    @Override
    public ServerResponse update(Integer userId, Shipping shipping) {
        if(shipping == null){
            return  ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        shipping.setUserId(userId);
        int rowCount = shippingMapper.updateByShipping(shipping);
        if(rowCount<0){
            return  ServerResponse.createByErrorMessage("更新地址失败！");
        }
        return ServerResponse.createBySuccessMessage("更新地址成功!");
    }

    @Override
    public ServerResponse<List<Shipping>> list(Integer userId) {
       List<Shipping> shippingList = shippingMapper.selectByUserId(userId);
        return ServerResponse.createBySuccess(shippingList);
    }

    @Override
    public ServerResponse<Shipping> select(Integer userId, Integer shippingId) {
        Shipping shipping = shippingMapper.selectByShippingIdUserId(userId,shippingId);
        return ServerResponse.createBySuccess(shipping);
    }
}
