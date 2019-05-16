package com.park.service;

import com.park.common.ServerResponse;
import com.park.vo.CartVo;

/**
 * Created by Park on 2018-12-19.
 */
public interface ICartService {
    ServerResponse<CartVo> add(Integer userId , Integer productId, Integer count);
    ServerResponse<CartVo> list(Integer userId);
    ServerResponse<CartVo> update (Integer userId, Integer productId,Integer count);
    ServerResponse<CartVo> deleteProduct(Integer userId , String productIds);
}
