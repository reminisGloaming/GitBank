package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.ProductCategory;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface PCService {


    List<ProductCategory> showPCList(Map<String,Object>params);

    PageInfo<ProductCategory> getPageInfo(Map<String, Object> params);

    ProductCategory selectParentProductCategory(int id);

    List<ProductCategory> selectCategoryByType(int type);

    boolean updateCategory(Map<String, Object> params);

    ProductCategory selectProductCategoryByName(String name);


    List<ProductCategory> findCategory(int id);


    boolean deleteCategory(int id);


    List<ProductCategory> selectParent(int parentId);


    //新增商品分类
    boolean addCategory(Map<String, Object> params);


    int selectProduct(int id);

}
