package com.doghome.easybuy.service;


import com.doghome.easybuy.entity.User;
import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.Map;

public interface LoginService {


    Map<String,Object> login(User user) throws JsonProcessingException;
}
