package com.doghome.easybuy.controller;

import com.doghome.easybuy.entity.Car;
import com.doghome.easybuy.entity.CarDetail;
import com.doghome.easybuy.service.CarService;
import com.doghome.easybuy.util.AjaxResult;
import com.doghome.easybuy.util.JwtUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/car")
public class CarController {

    @Autowired
    private CarService carService;

    @RequestMapping("/addCar")
    public AjaxResult addCar(@RequestParam Map<String, Object> params) {
        return AjaxResult.success().add("addCar", carService.addCar(params));
    }

    @RequestMapping("/getCar")
    public AjaxResult getCar(@RequestHeader(name = "Authorization") String token) {
        String loginName = JwtUtil.getLoginName(token);
        Car car = carService.getCar(loginName);
        return AjaxResult.success().add("car", car).add("carMap", car.getCarMap());
    }


    @RequestMapping("/deleteCar")
    public AjaxResult deleteCar(@RequestHeader(name = "Authorization") String token, String productId) {
        String loginName = JwtUtil.getLoginName(token);
        return carService.deleteCar(loginName, productId);
    }


    @RequestMapping("/updateCar")
    public AjaxResult updateCar(@RequestHeader(name = "Authorization") String token, @RequestBody Map<String, Object> params) {
        String loginName = JwtUtil.getLoginName(token);

        String listJson = params.get("updateCarList").toString();
        ObjectMapper objectMapper = new ObjectMapper();

        List<CarDetail> list = new ArrayList<>();
        try {
            list = objectMapper.readValue(listJson, new TypeReference<ArrayList<CarDetail>>() {
            });
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        return carService.updateCar(list, loginName);
    }
}
