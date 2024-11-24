package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.Collect;
import com.doghome.easybuy.entity.Product;

import com.doghome.easybuy.service.CollectService;
import com.doghome.easybuy.service.ProductService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

@Service
public class CollectServiceImpl implements CollectService {

    @Autowired
    private ProductService productService;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public boolean addCollect(int id, String loginName) {
        //根据id查找商品
        Product product = productService.selectProduct(id);

        //设置收藏
        Collect collect = new Collect();
        collect.setProductId(product.getId());
        collect.setProductName(product.getName());
        collect.setFileName(product.getFileName());
        collect.setProductPrice(product.getPrice());

        List<Collect> collectList = new ArrayList<>();


        ObjectMapper objectMapper = new ObjectMapper();

        String key = loginName + "-collect";

        //判断redis里面是否有收藏,如果有收藏就不可以继续收藏
        if (stringRedisTemplate.opsForValue().get(key) != null) {
            collectList = this.getCollectsList(loginName);

            for (Collect collect1 : collectList) {
                if (collect.getProductId() == collect1.getProductId()) {
                    return false;
                }
            }

            //判断收藏的大小 大于5移除第一个
            if (collectList.size() >= 5) {
                collectList.remove(0);
            }

        }

        collectList.add(collect);


        //redis设置收藏列表
        try {
            String collectListJson = objectMapper.writeValueAsString(collectList);
            stringRedisTemplate.opsForValue().set(key, collectListJson);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return true;
    }

    @Override
    public List<Collect> getCollectsList(String loginName) {


        List<Collect> collectList = new ArrayList<>();


        //如果redis有数据 就通过objectMapper把redis里面的json转化为实体类
        String key = loginName + "-collect";

        if (stringRedisTemplate.opsForValue().get(key) != null) {

            ObjectMapper objectMapper = new ObjectMapper();
            String collectJson = stringRedisTemplate.opsForValue().get(key);

            try {
                collectList = objectMapper.readValue(collectJson, new TypeReference<ArrayList<Collect>>() {
                });
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
        }

        return collectList;
    }

    @Override
    public boolean delAllCollects(String loginName) {
        String key = loginName + "-collect";

        if (stringRedisTemplate.delete(key)) {
            return true;
        }

        return false;
    }

    @Override
    public boolean delCollect(int id, String loginName) {


        List<Collect> collectList = new ArrayList<>();

        ObjectMapper objectMapper = new ObjectMapper();

        String key = loginName + "-collect";

        boolean flag = false;


        if (stringRedisTemplate.opsForValue().get(key) != null) {

            collectList = this.getCollectsList(loginName);

            Iterator<Collect> iterator = collectList.iterator();

            while (iterator.hasNext()) {

                Collect collect = iterator.next();

                if (collect.getProductId() == id) {
                    iterator.remove();
                    flag = true;
                    break;
                }
            }
        }

        if (flag == true) {
            try {
                String collectListJson = objectMapper.writeValueAsString(collectList);
                stringRedisTemplate.opsForValue().set(key, collectListJson);
                return true;
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }

        }

        return false;
    }
}
