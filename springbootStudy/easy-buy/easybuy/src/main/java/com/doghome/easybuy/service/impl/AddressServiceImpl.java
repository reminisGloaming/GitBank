package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.UserAddress;
import com.doghome.easybuy.mapper.AddressMapper;
import com.doghome.easybuy.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class AddressServiceImpl implements AddressService {

    @Autowired
    private AddressMapper addressMapper;

    @Override
    public boolean addAddress(Map<String, Object> params) {
        return addressMapper.addAddress(params)>0;
    }


    @Override
    public List<UserAddress> selectAddressList(Map<String,Object>params) {
        params.put("orderBy","createTime desc");
        return addressMapper.selectAddressList(params);
    }

    @Override
    public UserAddress findAddress(int id) {
        return addressMapper.findAddress(id);
    }

    @Override
    public boolean updateAddress(Map<String, Object> params) {
        return addressMapper.updateAddress(params)>0;
    }

    @Override
    public boolean deleteAddress(int id) {
        return addressMapper.deleteAddress(id)>0;
    }

    @Override
    public boolean setIsDefault(int id) {
        return addressMapper.setIsDefault(id)>0;
    }


    @Override
    public int updateAddressNotDefault(int userId) {
        return addressMapper.updateAddressNotDefault(userId);
    }


}
