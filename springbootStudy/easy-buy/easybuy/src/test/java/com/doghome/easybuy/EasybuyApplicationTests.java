package com.doghome.easybuy;

import com.doghome.easybuy.entity.Product;
import com.doghome.easybuy.mapper.ProductEsDao;
import com.doghome.easybuy.mapper.ProductMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

@SpringBootTest
class EasybuyApplicationTests {


    @Autowired
    private ProductEsDao productEsDao;

    @Autowired
    private ProductMapper productMapper;

    /**
     * 创建es索引库
     */
    @Test
    void saveES() {
        List<Product> productList = productMapper.saveProductListToEs();
        System.out.println("数据已经保存到es数据库");
        productEsDao.saveAll(productList);
    }

    /**
     * 对es索引库的删除
     */
    @Test
    void deleteProducts() {
        productEsDao.deleteAll();
    }

}
