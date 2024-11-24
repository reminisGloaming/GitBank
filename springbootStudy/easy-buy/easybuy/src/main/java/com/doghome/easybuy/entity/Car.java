package com.doghome.easybuy.entity;

import java.util.Map;

public class Car {

    private String id;
    private double totalPrice;
    private int totalNum;

    private int userId;

    private Map<String,CarDetail> carMap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public int getTotalNum() {
        return totalNum;
    }

    public void setTotalNum(int totalNum) {
        this.totalNum = totalNum;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public Map<String, CarDetail> getCarMap() {
        return carMap;
    }

    public void setCarMap(Map<String, CarDetail> carMap) {
        this.carMap = carMap;
    }
}
