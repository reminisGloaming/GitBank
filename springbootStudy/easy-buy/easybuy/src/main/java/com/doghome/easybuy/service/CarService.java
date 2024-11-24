package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.Car;
import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.util.AjaxResult;

import java.util.List;
import java.util.Map;

public interface CarService {

    /**
     * 增加购物车
     *
     * @param params
     * @return
     */
    AjaxResult addCar(Map<String, Object> params);


   Car getCar(String loginName);

   AjaxResult deleteCar(String loginName,String productId);



    AjaxResult updateCar(List<CarDetail> list, String loginName);
}
