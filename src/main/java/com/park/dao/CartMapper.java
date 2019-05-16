package com.park.dao;

import com.park.pojo.Cart;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface CartMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(Cart record);

    int insertSelective(Cart record);

    Cart selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(Cart record);

    int updateByPrimaryKey(Cart record);
    Cart selectCartByUserIdProductId(@Param(value = "userId") Integer userId, @Param(value = "productId") Integer productId);
    List<Cart> selectCartByUserId(Integer userId);
    int selectCartProductCheckedStatusByUserId(Integer userId);
    int deleteByUserIdProductIds(@Param(value = "userId") Integer userId , @Param(value = "productIdsList") List<String> productIdsList);
}