package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.Product;
import com.doghome.easybuy.mapper.ProductEsDao;
import com.doghome.easybuy.mapper.ProductMapper;
import com.doghome.easybuy.service.ProductService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.apache.lucene.queryparser.xml.builders.BooleanQueryBuilder;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class ProductServiceImpl implements ProductService {

    @Autowired
    private ProductMapper productMapper;

    @Autowired
    private ProductEsDao productEsDao;


    @Autowired
    private ElasticsearchRestTemplate esTemplate;

    @Override
    public List<Product> showProductList(Map<String, Object> params) {
        params.put("orderBy", "createTime desc");
        return productMapper.getProductList(params);
    }

    @Override
    public PageInfo<Product> getProductList(Map<String, Object> params) {

        params.put("orderBy", "createTime desc");

        String name = "";

        if (params.containsKey("name") && !StringUtils.isNullOrEmpty(params.get("name").toString())) {
            name = "%" + params.get("name") + "%";
        }

        params.put("name", name);

        String createTime = "";
        if (params.containsKey("createTime") && !StringUtils.isNullOrEmpty(params.get("createTime").toString())) {
            createTime = "%" + params.get("createTime") + "%";
        }

        params.put("createTime", createTime);

        PageHelper.startPage(params);
        List<Product> productList = productMapper.getProductList(params);
        return new PageInfo<>(productList, 3);
    }


    @Transactional(rollbackFor = {Exception.class})

    @Override
    public boolean deleteProduct(int id) {
        try {
            productMapper.deleteProduct(id);

            //这里要把id变为字符串
            String idStr = id + "";
            productEsDao.deleteById(idStr);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public Product selectProduct(int id) {
        return productMapper.selectProduct(id);
    }

    @Override
    public boolean updateProduct(Product product) {
        if (productMapper.updateProduct(product) > 0) {
            return true;
        }
        return false;
    }


    @Override
    public boolean addProduct(Product product) {
        if (productMapper.addProduct(product) > 0) {
            return true;
        }
        return false;
    }


    @Override
    public Product checkProductName(String name) {
        return productMapper.checkProductName(name);
    }


    //从es里面把商品拿出来
    @Override
    public PageInfo<Product> findProductByEs(Map<String, Object> params) {

        //初始化参数
        int pageNum = 1;
        int pageSize = 5;
        String name = "";
        int categoryId = 0;


        if (params.containsKey("pageNum") && !StringUtils.isNullOrEmpty(params.get("pageNum").toString())) {
            pageNum = Integer.parseInt(params.get("pageNum").toString());
        }

        if (params.containsKey("pageSize") && !StringUtils.isNullOrEmpty(params.get("pageSize").toString())) {
            pageSize = Integer.parseInt(params.get("pageSize").toString());
        }

        if (params.containsKey("name") && !StringUtils.isNullOrEmpty(params.get("name").toString())) {
            name = params.get("name").toString();
        }

        if (params.containsKey("categoryId") && !StringUtils.isNullOrEmpty(params.get("categoryId").toString())) {
            categoryId = Integer.parseInt(params.get("categoryId").toString());
        }

        //构建本地搜索查询构建器
        NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();


        //布尔查询构建器
        BoolQueryBuilder booleanQueryBuilder = QueryBuilders.boolQuery();

        if (name != "") {

            //设置高亮样式
            HighlightBuilder highlightBuilder = new HighlightBuilder();

            //设置高亮字段
            highlightBuilder.field("name");


            //在关键字上加前缀
            highlightBuilder.preTags("<font style='color: red'>");

            //后缀
            highlightBuilder.postTags("</font>");

            //添加高亮样式
            nativeSearchQueryBuilder.withHighlightBuilder(highlightBuilder);

            //文档 必须 匹配这些条件才能被包含进来。  (模糊查询)
            booleanQueryBuilder.must(QueryBuilders.matchQuery("name", name));
        }

        if (categoryId != 0) {

            //分类布尔查询构建器
            BoolQueryBuilder categoryBuilder = QueryBuilders.boolQuery();

            //文档 满足 一个条件就能被包含进来。  (精确查询)
            categoryBuilder.should(QueryBuilders.termQuery("categoryLevel1Id", categoryId));

            categoryBuilder.should(QueryBuilders.termQuery("categoryLevel2Id", categoryId));

            categoryBuilder.should(QueryBuilders.termQuery("categoryLevel3Id", categoryId));

            //文档 必须 匹配这些条件才能被包含进来
            booleanQueryBuilder.must(categoryBuilder);
        }

        nativeSearchQueryBuilder.withQuery(booleanQueryBuilder)
                .withPageable(PageRequest.of(pageNum-1,pageSize))
                .withSort(SortBuilders.fieldSort("createTime").order(SortOrder.DESC));


        //SearchHits 获得搜索结果的集合   esTemplate用于操作Elasticsearch的Java API封装。
        SearchHits<Product> searchHits = esTemplate.search(nativeSearchQueryBuilder.build(), Product.class);

        List<Product> list = new ArrayList<>();

        for (SearchHit<Product> searchHit : searchHits.getSearchHits()) {

            //获得产品
            Product product = searchHit.getContent();

            //判断是否模糊查询了产品,并且为产品设置高亮样式
            Map<String, List<String>> map = searchHit.getHighlightFields();
            if (map.containsKey("name") && !map.get("name").isEmpty()) {
                product.setName(map.get("name").get(0));
            }

            //往集合里面添加产品
            list.add(product);
        }

        PageInfo<Product> pageInfo = new PageInfo<>();

        pageInfo.setPageNum(pageNum);
        pageInfo.setPageSize(pageSize);

        pageInfo.setTotal(searchHits.getTotalHits());

        if (searchHits.getTotalHits() > 0) {
            pageInfo.setPages((int) (searchHits.getTotalHits() % pageSize == 0 ? searchHits.getTotalHits() / pageSize : searchHits.getTotalHits() / pageSize + 1));
        }

        pageInfo.setList(list);


        return pageInfo;
    }

}
