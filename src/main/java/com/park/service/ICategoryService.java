package com.park.service;

import com.park.common.ServerResponse;
import com.park.pojo.Category;

import java.util.List;

/**
 * Created by Park on 2018-11-5.
 */
public interface ICategoryService {
    ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId);
    ServerResponse addCategory(String categoryName , Integer parentId) ;
    ServerResponse updateCategoryName(String categoryName,Integer categoryId);
    ServerResponse<List<Integer>> selectCategoryAndChildrenById(Integer categoryId);
}
