package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.Product;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository

/**
 * 新建接口实现对es的操作
 */
public interface ProductEsDao extends ElasticsearchRepository<Product,String> {

}
