package com.park.service;

import com.github.pagehelper.PageInfo;
import com.park.common.ServerResponse;
import com.park.pojo.Product;
import com.park.vo.ProductDetailVo;

/**
 * Created by Park on 2018-11-30.
 */
public interface IProductService {
    ServerResponse saveOrUpdateProduct(Product product);
    ServerResponse<String> setSaleStatus(Integer productId , Integer status);
    ServerResponse selectByProductId(Integer productId);
    ServerResponse<ProductDetailVo> manageProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductList(Integer pageNum , Integer pageSize);
    ServerResponse<PageInfo> searchProduct(String productName, Integer productId,Integer pageNum,Integer pageSize);
    ServerResponse<ProductDetailVo> getProductDetail(Integer productId);
    ServerResponse<PageInfo> getProductByKeywordCategory(String keyword , Integer categoryId,int pageNum , int pageSize , String orderBy);
}
