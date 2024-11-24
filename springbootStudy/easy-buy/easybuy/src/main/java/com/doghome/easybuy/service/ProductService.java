package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.Product;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface ProductService {

    List<Product> showProductList(Map<String,Object>params);

    PageInfo<Product> getProductList(Map<String,Object> params);

    boolean deleteProduct(int id);

    Product selectProduct(int id);


    boolean updateProduct(Product product);

    boolean addProduct(Product product);

    Product checkProductName(String name);

    PageInfo<Product> findProductByEs(Map<String,Object>params);
}
