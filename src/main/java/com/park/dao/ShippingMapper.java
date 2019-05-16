package com.park.dao;

import com.park.pojo.Shipping;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ShippingMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Shipping record);

    int insertSelective(Shipping record);

    Shipping selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Shipping record);

    int updateByPrimaryKey(Shipping record);

    int updateByShipping(Shipping record);

    List<Shipping> selectByUserId(Integer userId);

    Shipping selectByShippingIdUserId(@Param(value = "userId") Integer userId , @Param(value = "shippingId") Integer shippingId);
}