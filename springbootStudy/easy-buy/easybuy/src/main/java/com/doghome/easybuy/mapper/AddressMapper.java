package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.UserAddress;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface AddressMapper {

    int addAddress(Map<String,Object>params);

    List<UserAddress> selectAddressList(Map<String,Object>params);

    UserAddress findAddress(int id);

    int updateAddress(Map<String,Object>params);

    int deleteAddress(int id);

    int setIsDefault(int id);

    int updateAddressNotDefault(int userId);
}
