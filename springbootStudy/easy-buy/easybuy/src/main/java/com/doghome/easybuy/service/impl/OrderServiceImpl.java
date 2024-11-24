package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.*;
import com.doghome.easybuy.mapper.OrderMapper;
import com.doghome.easybuy.mapper.ProductMapper;
import com.doghome.easybuy.service.OrderService;
import com.doghome.easybuy.service.ProductService;
import com.doghome.easybuy.util.AjaxResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

@Service

public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Autowired
    private StringRedisTemplate stringRedisTemplate;

    @Autowired
    private ProductService productService;


    /**
     * 订单分页
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<Order> getPageInfo(Map<String, Object> params) {
        params.put("orderBy", "createTime desc");


        String loginName = "";
        if (params.containsKey("loginName") && !StringUtils.isNullOrEmpty(params.get("loginName").toString())) {
            loginName = "%" + params.get("loginName").toString() + "%";
        }

        params.put("loginName", loginName);

        //PageHelper要放在List上面 不然list不会根据条件查询
        PageHelper.startPage(params);
        List<Order> orderList = orderMapper.getOrderList(params);
        return new PageInfo<>(orderList, 2);
    }


    /**
     * 订单详情列表
     *
     * @param params
     * @return
     */
    @Override
    public List<OrderDetail> getOrderDetailList(Map<String, Object> params) {
        List<OrderDetail> orderDetailList = orderMapper.getOrderDetailList(params);
        return orderDetailList;
    }

    @Override
    public boolean updateOrder(Map<String, Object> params) {
        return orderMapper.updateOrder(params) > 0;
    }


    @Override
    public boolean deleteOrder(int id) {
        return orderMapper.deleteOrder(id) > 0;
    }


    /**
     * 添加订单、 订单详情、修改产品
     *
     * @param userId
     * @param loginName
     * @param list
     * @param address
     * @return
     */
    @Override
    public AjaxResult addOrder(int userId, String loginName, List<CarDetail> list, UserAddress address) {
        Order order = new Order();
        order.setUserId(userId);
        order.setLoginName(loginName);
        order.setUserAddress(address.getProvince() + "省" + address.getCity() + "市" + address.getArea() + "区");

        double price = 0;
        for (CarDetail carDetail : list) {
            price = price + carDetail.getTotalPrice();
        }

        order.setCost(price);

        order.setSerialNumber(UUID.randomUUID().toString());

        try {
            int isAdd = orderMapper.addOrder(order);


            //获取生成订单自动生成的id
            int orderId = order.getId();

            ObjectMapper objectMapper = new ObjectMapper();
            String orderJson;
            orderJson = objectMapper.writeValueAsString(order);


            stringRedisTemplate.opsForValue().set(orderId + "-order", orderJson, 5, TimeUnit.MINUTES);

            OrderDetail orderDetail;

            for (CarDetail carDetail : list) {
                orderDetail = new OrderDetail();
                orderDetail.setOrderId(orderId);
                orderDetail.setProductId(Integer.parseInt(carDetail.getProductId()));
                orderDetail.setQuantity(carDetail.getBuyNum());
                orderDetail.setCost(carDetail.getTotalPrice());
                orderMapper.addOrderDetail(orderDetail);
            }


            Product product = new Product();

            for (CarDetail carDetail : list) {

                //生成订单的时候改库存
                product = productService.selectProduct(Integer.parseInt(carDetail.getProductId()));
                product.setStock(product.getStock() - carDetail.getBuyNum());
                product.setPreStock(product.getPreStock() + carDetail.getBuyNum());

                productService.updateProduct(product);
            }

            return AjaxResult.success().add("isAdd", true).add("orderId", orderId);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public Order getOrder(Map<String, Object> params) {
        return orderMapper.getOrder(params);
    }

}
