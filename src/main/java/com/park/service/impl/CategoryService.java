package com.park.service.impl;

import com.park.common.ServerResponse;
import com.park.dao.CategoryMapper;
import com.park.pojo.Category;
import com.park.service.ICategoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * Created by Park on 2018-11-5.
 */
@Service("iCategoryService")
public class CategoryService implements ICategoryService {

    private Logger logger = LoggerFactory.getLogger(CategoryService.class);

    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ServerResponse<List<Category>> getChildrenParallelCategory(Integer parentId) {
        List<Category> rtnList = categoryMapper.selectCategoryChildrenByParentId(parentId);
        if(CollectionUtils.isEmpty(rtnList)){
            logger.info("未找到当前品类的子分类");
        }
        return ServerResponse.createBySuccess(rtnList);
    }
}
