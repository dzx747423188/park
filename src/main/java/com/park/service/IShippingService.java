package com.park.service;

import com.park.common.ServerResponse;
import com.park.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * Created by Park on 2018-12-20.
 */
public interface IShippingService {
    ServerResponse save(Integer userId ,Shipping shipping);
    ServerResponse delete (Integer userId , Integer shippingId);
    ServerResponse update(Integer userId , Shipping shipping);
    ServerResponse <List<Shipping>> list(Integer userId);
    ServerResponse<Shipping> select(@Param(value = "userId") Integer userId , @Param(value = "shipping") Integer shippingId);
}
