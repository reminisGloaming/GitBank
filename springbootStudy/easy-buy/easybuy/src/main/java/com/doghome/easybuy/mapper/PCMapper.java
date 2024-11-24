package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.ProductCategory;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface PCMapper {


    //获得商品分类的列表
    List<ProductCategory> showPCList(Map<String, Object> params);


    //商品分类分页
    List<ProductCategory> pcByPage(Map<String, Object> params);


    //根据子id 查询父级分类
    ProductCategory selectParentProductCategory(int id);


    //把不同分类底下的分类名称查询出来
    List<ProductCategory> selectCategoryByType(int type);


    //修改商品分类
    int updateCategory(Map<String, Object> params);


    //校验商品分类是否重命名
    ProductCategory selectProductCategoryByName(String name);


    //查看商品分类下面是否还要商品分类
    List<ProductCategory> findCategory(int id);


    //删除商品分类
    int deleteCategory(int id);


    //查询一级分类底下的二级分类
    List<ProductCategory> selectParent(int parentId);


    //新增商品分类
    int addCategory(Map<String, Object> params);


    //根据id查找分类下的商品
    int selectProduct(int id);

}
