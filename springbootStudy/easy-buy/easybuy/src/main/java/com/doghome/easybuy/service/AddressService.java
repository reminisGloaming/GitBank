package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.UserAddress;

import java.util.List;
import java.util.Map;

public interface AddressService {

    boolean addAddress(Map<String,Object> params);

    List<UserAddress> selectAddressList(Map<String,Object>params);


    UserAddress findAddress(int id);

    boolean updateAddress(Map<String,Object>params);

    boolean deleteAddress(int id);

    boolean setIsDefault(int id);

    int updateAddressNotDefault(int userId);
}
