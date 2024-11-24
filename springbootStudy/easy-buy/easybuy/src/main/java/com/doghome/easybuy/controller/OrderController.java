package com.doghome.easybuy.controller;


import com.doghome.easybuy.annotation.CheckPermission;
import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.entity.Order;
import com.doghome.easybuy.entity.OrderDetail;
import com.doghome.easybuy.entity.UserAddress;
import com.doghome.easybuy.service.OrderService;
import com.doghome.easybuy.util.AjaxResult;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/order")
public class OrderController {

    @Autowired
    private OrderService orderService;


    @CheckPermission({"0", "1"})
    @RequestMapping("/getOrderList")
    public AjaxResult getOrderList(@RequestParam Map<String, Object> params) {
        if (params.get("userType").toString().equals("1")) {
            params.remove("userId");
        }

        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", orderService.getPageInfo(params));
        return ajaxResult;
    }

    @CheckPermission({"0", "1"})
    @RequestMapping("/getOrderDetailList")
    public AjaxResult getOrderDetailList(@RequestBody Map<String, Object> params) {
        List<OrderDetail> orderDetailList = orderService.getOrderDetailList(params);
        return AjaxResult.success().add("orderDetailList", orderDetailList);
    }

    @CheckPermission({"0", "1"})
    @RequestMapping("/updateOrder")
    public AjaxResult updateOrder(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = null;
        if (orderService.updateOrder(params)) {
            ajaxResult = AjaxResult.success().add("update", true);
        } else {
            ajaxResult = AjaxResult.success().add("update", false);
        }
        return ajaxResult;
    }

    @CheckPermission({"0", "1"})
    @RequestMapping("/deleteOrder")
    public AjaxResult deleteOrder(int id) {
        AjaxResult ajaxResult = null;
        if (orderService.deleteOrder(id)) {
            ajaxResult = AjaxResult.success().add("delete", true);
        } else {
            ajaxResult = AjaxResult.success().add("delete", false);
        }
        return ajaxResult;
    }

    @CheckPermission({"0", "1"})
    @RequestMapping("/addOrder")
    public AjaxResult addOrder(@RequestBody Map<String, Object> params) {
        String userId = params.get("userId").toString();
        String loginName = params.get("loginName").toString();

        String carDetailListJson = params.get("selectProductList").toString();
        String addressJson = params.get("address").toString();

        List<CarDetail> carDetailList = new ArrayList<>();
        UserAddress userAddress = new UserAddress();

        ObjectMapper objectMapper = new ObjectMapper();
        try {
            carDetailList = objectMapper.readValue(carDetailListJson, new TypeReference<ArrayList<CarDetail>>() {
            });
            userAddress = objectMapper.readValue(addressJson, UserAddress.class);

        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return orderService.addOrder(Integer.parseInt(userId), loginName, carDetailList, userAddress);
    }

    @CheckPermission({"0", "1"})
    @RequestMapping("/getOrder")
    public AjaxResult getOrder(@RequestParam Map<String, Object> params) {
        return AjaxResult.success().add("order", orderService.getOrder(params));
    }
}
