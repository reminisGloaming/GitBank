package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.Product;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository

public interface ProductMapper {

    List<Product> saveProductListToEs();

    List<Product> getProductList(Map<String,Object>params);


    int deleteProduct(int id);


    Product selectProduct(int id);

    int updateProduct(Product product);


    int addProduct(Product product);

    Product checkProductName(String name);
}
