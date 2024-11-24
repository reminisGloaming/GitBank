package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.Collect;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

public interface CollectService {

    boolean addCollect(int id, String loginName);

    List<Collect> getCollectsList(String loginName);


    boolean delAllCollects(String loginName);

    boolean delCollect(int id,String loginName);
}
