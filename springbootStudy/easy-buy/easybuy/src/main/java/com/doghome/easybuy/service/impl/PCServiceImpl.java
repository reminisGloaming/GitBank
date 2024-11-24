package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.ProductCategory;
import com.doghome.easybuy.mapper.PCMapper;
import com.doghome.easybuy.service.PCService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class PCServiceImpl implements PCService {

    @Autowired
    private PCMapper pcMapper;


    @Override
    public List<ProductCategory> showPCList(Map<String, Object> params) {
        return pcMapper.showPCList(params);
    }

    public PageInfo<ProductCategory> getPageInfo(Map<String, Object> params) {
        params.put("orderBy", "id");
        PageHelper.startPage(params);
        List<ProductCategory> list = pcMapper.pcByPage(params);
        return new PageInfo<>(list, 3);

    }

    @Override
    public ProductCategory selectParentProductCategory(int id) {
        return pcMapper.selectParentProductCategory(id);
    }

    @Override
    public List<ProductCategory> selectCategoryByType(int type) {
        return pcMapper.selectCategoryByType(type);
    }


    @Override
    public boolean updateCategory(Map<String, Object> params) {
        return pcMapper.updateCategory(params) > 0;
    }

    @Override
    public ProductCategory selectProductCategoryByName(String name) {
        return pcMapper.selectProductCategoryByName(name);
    }


    @Override
    public List<ProductCategory> findCategory(int id) {
        List<ProductCategory> productCategorys=pcMapper.findCategory(id);
        System.out.println(productCategorys.size());
        return productCategorys;
    }


    @Override
    public boolean deleteCategory(int id) {
        return pcMapper.deleteCategory(id) > 0;
    }

    @Override

    //根据一级分类查找二级分类
    public List<ProductCategory> selectParent(int parentId) {
        return pcMapper.selectParent(parentId);
    }


    //增加分类
    @Override
    public boolean addCategory(Map<String, Object> params) {
        return pcMapper.addCategory(params)>0;
    }

    @Override
    public int selectProduct(int id) {
        return pcMapper.selectProduct(id);
    }


}
