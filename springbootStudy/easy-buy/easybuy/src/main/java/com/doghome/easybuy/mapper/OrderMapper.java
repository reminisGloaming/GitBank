package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.Order;
import com.doghome.easybuy.entity.OrderDetail;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface OrderMapper {

    List<Order> getOrderList(Map<String, Object> params);

    List<OrderDetail> getOrderDetailList(Map<String, Object> params);


    int updateOrder(Map<String, Object> params);

    int deleteOrder(int id);


    int addOrder(Order order);

    int addOrderDetail(OrderDetail orderDetail);

    Order getOrder(Map<String,Object>params);
}
