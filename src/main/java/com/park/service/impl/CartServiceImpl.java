package com.park.service.impl;

import com.google.common.base.Splitter;
import com.google.common.collect.Lists;
import com.park.common.Const;
import com.park.common.ResponseCode;
import com.park.common.ServerResponse;
import com.park.dao.CartMapper;
import com.park.dao.ProductMapper;
import com.park.pojo.Cart;
import com.park.pojo.Product;
import com.park.service.ICartService;
import com.park.util.BigDecimalUtil;
import com.park.util.PropertiesUtil;
import com.park.vo.CartProductVo;
import com.park.vo.CartVo;
import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

/**
 * Created by Park on 2018-12-19.
 */
@Service("iCartService")
public class CartServiceImpl implements ICartService {
    @Autowired
    private CartMapper cartMapper;
    @Autowired
    private ProductMapper productMapper;

    @Override
    public ServerResponse<CartVo> add(Integer userId, Integer productId, Integer count) {
        if(productId == null|| count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart == null){
            //购物车没有该商品，可以添加
            Cart cartItem = new Cart();
            cartItem.setUserId(userId);
            cartItem.setProductId(productId);
            cartItem.setQuantity(count);
            cartItem.setChecked(Const.Cart.CHECKED);
            int rowCount = cartMapper.insert(cartItem);
        }else {
            //购物车存在该商品，数量增加
            int quantity = cart.getQuantity();
            cart.setQuantity(quantity+count);
            int rowCount = cartMapper.updateByPrimaryKeySelective(cart);
        }
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> list(Integer userId) {
        CartVo cartVo = this.getCartVoLimit(userId);
        return ServerResponse.createBySuccess(cartVo);
    }

    @Override
    public ServerResponse<CartVo> update(Integer userId, Integer productId, Integer count) {
        if(productId == null || count == null){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        Cart cart = cartMapper.selectCartByUserIdProductId(userId,productId);
        if(cart !=null){
            cart.setQuantity(count);
        }
        cartMapper.updateByPrimaryKey(cart);
        return this.list(userId);
    }

    @Override
    public ServerResponse<CartVo> deleteProduct(Integer userId, String productIds) {
        List<String> productIdsList = Splitter.on(",").splitToList(productIds);
        if(CollectionUtils.isEmpty(productIdsList)){
            return ServerResponse.createByErrorCodeMessage(ResponseCode.ILLEGAL_ARGUMENT.getCode(),ResponseCode.ILLEGAL_ARGUMENT.getDesc());
        }
        cartMapper.deleteByUserIdProductIds(userId,productIdsList);
        return this.list(userId);
    }

    public  CartVo getCartVoLimit(Integer userId){
        CartVo cart = new CartVo();
        //根据user_id查询出购物车所有的商品
        List<Cart> cartList = cartMapper.selectCartByUserId(userId);
        List<CartProductVo> cartProductVoList = Lists.newArrayList();
        BigDecimal cartTotalPrice = new BigDecimal("0");
        if(CollectionUtils.isNotEmpty(cartList)){
            for(Cart cartItem :cartList){
                CartProductVo cartProductVo = new CartProductVo();
                cartProductVo.setId(cartItem.getId());
                cartProductVo.setProductId(cartItem.getProductId());
                cartProductVo.setUserId(userId);
                Product product = productMapper.selectByPrimaryKey(cartItem.getProductId());
                if(product !=null){
                    cartProductVo.setProductMainImage(product.getMainImage());
                    cartProductVo.setProductName(product.getName());
                    cartProductVo.setProductStatus(product.getStatus());
                    cartProductVo.setProductStock(product.getStock());
                    cartProductVo.setProductSubTitle(product.getSubtitle());
                    cartProductVo.setProductPrice(product.getPrice());
                    int buyLimitCount = 0;
                    if(product.getStock()>cartItem.getQuantity()){
                        //库存大于购物车数量
                        buyLimitCount = cartItem.getQuantity();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_SUCCESS);
                    }else{
                        buyLimitCount = product.getStock();
                        cartProductVo.setLimitQuantity(Const.Cart.LIMIT_NUM_FAIL);
                        //购物车更新库存
                        Cart cartForQuantity = new Cart();
                        cartForQuantity.setId(cartItem.getId());
                        cartForQuantity.setQuantity(buyLimitCount);
                        cartMapper.updateByPrimaryKeySelective(cartForQuantity);
                    }
                    cartProductVo.setQuantity(buyLimitCount);//设置数量
                    cartProductVo.setProductTotalPrice(BigDecimalUtil.mul(product.getPrice().doubleValue() , cartProductVo.getQuantity()));
                    cartProductVo.setProductChecked(cartItem.getChecked());
                }
                if(cartItem.getChecked() == Const.Cart.CHECKED){
                    cartTotalPrice = BigDecimalUtil.add(cartTotalPrice.doubleValue(),cartProductVo.getProductTotalPrice().doubleValue());
                }
                cartProductVoList.add(cartProductVo);
            }
        }
            cart.setCartTotalPrice(cartTotalPrice);
            cart.setAllChecked(this.getAllCheckedStatus(userId));
            cart.setCartProductVoList(cartProductVoList);
            cart.setImageHost(PropertiesUtil.getProperty("ftp.server.http.prefix"));
        return cart;
    }
    private  boolean getAllCheckedStatus(Integer userId){
       if(userId == null){
           return false;
       }
        return cartMapper.selectCartProductCheckedStatusByUserId(userId) ==0;
    }
}
