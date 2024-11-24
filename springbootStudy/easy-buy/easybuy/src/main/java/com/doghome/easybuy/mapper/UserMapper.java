package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository

public interface UserMapper {

    User selectUserByLoginName(String loginName);

    List<User> UserByPage(Map<String,Object>params);


    int addUser(Map<String,Object>params);

    User selectUserById(int id);

    int updateUser(Map<String,Object>params);

    int deleteUser(int id);
}
