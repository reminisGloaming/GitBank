package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.Car;
import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.service.CarService;
import com.doghome.easybuy.util.AjaxResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class CarServiceImpl implements CarService {


    @Autowired
    private StringRedisTemplate stringRedisTemplate;


    @Override
    public AjaxResult addCar(Map<String, Object> params) {
        Car car = new Car();
        CarDetail carDetail = new CarDetail();
        if (params.containsKey("loginName") && !StringUtils.isNullOrEmpty(params.get("loginName").toString())) {
            car.setId(params.get("loginName").toString() + "-car");
        }

        if (params.containsKey("productId") && !StringUtils.isNullOrEmpty(params.get("productId").toString())) {
            carDetail.setProductId(params.get("productId").toString());
        }

        if (params.containsKey("productName") && !StringUtils.isNullOrEmpty(params.get("productName").toString())) {
            carDetail.setProductName(params.get("productName").toString());
        }

        if (params.containsKey("fileName") && !StringUtils.isNullOrEmpty(params.get("fileName").toString())) {
            carDetail.setFileName(params.get("fileName").toString());
        }

        if (params.containsKey("stock") && !StringUtils.isNullOrEmpty(params.get("stock").toString())) {
            carDetail.setStock(Integer.parseInt(params.get("stock").toString()));
        }

        if (params.containsKey("buyNum") && !StringUtils.isNullOrEmpty(params.get("buyNum").toString())) {
            carDetail.setBuyNum(Integer.parseInt(params.get("buyNum").toString()));
        }


        if (params.containsKey("price") && !StringUtils.isNullOrEmpty(params.get("price").toString())) {
            carDetail.setPrice(Double.parseDouble(params.get("price").toString()));
            carDetail.setTotalPrice(carDetail.getBuyNum() * carDetail.getPrice());
        }


        //创建一个空的map用来存放购物车详情
        Map<String, CarDetail> map = new HashMap<>();

        //根据id查看redis是否有购物车
        if (stringRedisTemplate.opsForValue().get(car.getId()) != null) {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = stringRedisTemplate.opsForValue().get(car.getId());

            Car oldCar = null;
            try {
                oldCar = objectMapper.readValue(json, Car.class);
            } catch (JsonProcessingException e) {
                throw new RuntimeException(e);
            }
            map = oldCar.getCarMap();
        }


        //查看购物车里面是否有购买的商品,有数量和价格要增加

        if (map.containsKey(carDetail.getProductId())) {
            CarDetail oldCarDetail = map.get(carDetail.getProductId());

            oldCarDetail.setBuyNum(oldCarDetail.getBuyNum() + carDetail.getBuyNum());
            oldCarDetail.setTotalPrice(oldCarDetail.getTotalPrice() + carDetail.getTotalPrice());

            map.put(carDetail.getProductId(), oldCarDetail);
        } else {
            map.put(carDetail.getProductId(), carDetail);
        }


        //把购物详情放入购物车
        car.setCarMap(map);

        double totalPrice = 0;
        int totalNum = 0;

        //遍历详情集合,设置购物车的总价格和总数量
        for (Map.Entry<String, CarDetail> entry : car.getCarMap().entrySet()) {
            totalPrice = totalPrice + entry.getValue().getTotalPrice();
            totalNum = totalNum + entry.getValue().getBuyNum();
        }

        car.setTotalPrice(totalPrice);
        car.setTotalNum(totalNum);


        //把购物车放入到redis里面
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(car);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        stringRedisTemplate.opsForValue().set(car.getId(), json);
        return AjaxResult.success();
    }


    @Override
    public Car getCar(String loginName) {
        String key = loginName + "-car";

        Car car = new Car();

        String json = stringRedisTemplate.opsForValue().get(key);

        if (json == null) {
            return car;
        }

        //ObjectMapper转化json格式
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            car = objectMapper.readValue(json, Car.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return car;
    }


    @Override
    public AjaxResult deleteCar(String loginName, String productId) {

        //删除原来map里面的元素
        Car car = getCar(loginName);
        Map<String, CarDetail> map = car.getCarMap();
        map.remove(productId);


        //设置新的购物车内容
        Car newCar = new Car();
        newCar.setId(loginName + "-car");
        newCar.setCarMap(map);

        double totalPrice = 0;
        int totalNum = 0;

        //遍历详情集合,设置购物车的总价格和总数量
        for (Map.Entry<String, CarDetail> entry : map.entrySet()) {
            totalPrice = totalPrice + entry.getValue().getTotalPrice();
            totalNum = totalNum + entry.getValue().getBuyNum();
        }

        newCar.setTotalPrice(totalPrice);
        newCar.setTotalNum(totalNum);


        //把购物车放入到redis里面
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(newCar);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        stringRedisTemplate.opsForValue().set(newCar.getId(), json);
        return AjaxResult.success();
    }


    @Override
    public AjaxResult updateCar(List<CarDetail> list, String loginName) {
        Car car = getCar(loginName);
        Map<String, CarDetail> map = car.getCarMap();


        //把集合里面的元素重新赋值
        for (CarDetail carDetail : list) {
            if (map.containsKey(carDetail.getProductId())) {
                map.put(carDetail.getProductId(), carDetail);
            }
        }


        //设置新的购物车内容
        Car newCar = new Car();
        newCar.setId(loginName + "-car");
        newCar.setCarMap(map);

        double totalPrice = 0;
        int totalNum = 0;

        //遍历详情集合,设置购物车的总价格和总数量
        for (Map.Entry<String, CarDetail> entry : map.entrySet()) {
            totalPrice = totalPrice + entry.getValue().getTotalPrice();
            totalNum = totalNum + entry.getValue().getBuyNum();
        }

        newCar.setTotalPrice(totalPrice);
        newCar.setTotalNum(totalNum);


        //把购物车放入到redis里面
        ObjectMapper objectMapper = new ObjectMapper();
        String json = "";
        try {
            json = objectMapper.writeValueAsString(newCar);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        stringRedisTemplate.opsForValue().set(newCar.getId(), json);
        return AjaxResult.success();
    }


}
