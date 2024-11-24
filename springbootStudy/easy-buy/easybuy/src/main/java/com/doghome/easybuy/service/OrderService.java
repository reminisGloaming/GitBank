package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.entity.Order;
import com.doghome.easybuy.entity.OrderDetail;
import com.doghome.easybuy.entity.UserAddress;
import com.doghome.easybuy.util.AjaxResult;
import com.github.pagehelper.PageInfo;

import java.util.List;
import java.util.Map;

public interface OrderService {

    PageInfo<Order> getPageInfo(Map<String, Object> params);

    List<OrderDetail> getOrderDetailList(Map<String, Object> params);

    boolean updateOrder(Map<String, Object> params);

    boolean deleteOrder(int id);

    AjaxResult addOrder(int userId, String loginName, List<CarDetail> list, UserAddress address);


    Order getOrder(Map<String,Object>params);
}
