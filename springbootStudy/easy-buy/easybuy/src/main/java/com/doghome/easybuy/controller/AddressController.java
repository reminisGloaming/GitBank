package com.doghome.easybuy.controller;

import com.doghome.easybuy.service.AddressService;
import com.doghome.easybuy.util.AjaxResult;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/address")

public class AddressController {

    @Autowired
    private AddressService addressService;

    @RequestMapping("/addAddress")
    public AjaxResult addAddress(@RequestParam Map<String, Object> params) {

        AjaxResult ajaxResult = null;

        if (params.get("isDefault").toString().equals("1")) {
            addressService.updateAddressNotDefault(Integer.parseInt(params.get("userId").toString()));
        }

        if (addressService.addAddress(params)) {
            ajaxResult = AjaxResult.success().add("isAdd", true);
        } else {
            ajaxResult = AjaxResult.success().add("isAdd", false);
        }

        return ajaxResult;
    }

    @RequestMapping("/selectAddressList")
    public AjaxResult selectAddressList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("addressList", addressService.selectAddressList(params));
        return ajaxResult;
    }

    @RequestMapping("/findAddress")
    public AjaxResult findAddress(int id) {
        AjaxResult ajaxResult = AjaxResult.success().add("UserAddress", addressService.findAddress(id));
        return ajaxResult;
    }

    @RequestMapping("/updateAddress")
    public AjaxResult updateAddress(@RequestParam Map<String, Object> params) {

        AjaxResult ajaxResult = null;

        //如果设置为默认调用默认方法
        if (params.get("isDefault").toString().equals("1")) {
            addressService.updateAddressNotDefault(Integer.parseInt(params.get("userId").toString()));
            addressService.setIsDefault(Integer.parseInt(params.get("id").toString()));
        }

        if (addressService.updateAddress(params)) {
            ajaxResult = AjaxResult.success().add("isUpdate", true);
        } else {
            ajaxResult = AjaxResult.success().add("isUpdate", false);
        }

        return ajaxResult;
    }

    @RequestMapping("/deleteAddress")
    public AjaxResult deleteAddress(int id) {
        AjaxResult ajaxResult = null;

        if (addressService.deleteAddress(id)) {
            ajaxResult = AjaxResult.success().add("isDelete", true);
        } else {
            ajaxResult = AjaxResult.success().add("isDelete", false);
        }


        return ajaxResult;
    }

    @RequestMapping("/setIsDefault")
    public AjaxResult setIsDefault(int userId, int id) {
        AjaxResult ajaxResult = null;

        addressService.updateAddressNotDefault(userId);

        if (addressService.setIsDefault(id)) {
            ajaxResult = AjaxResult.success().add("isSet", true);
        } else {
            ajaxResult = AjaxResult.success().add("isSet", false);
        }
        return ajaxResult;
    }
}
