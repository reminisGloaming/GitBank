package com.doghome.easybuy.service;

import com.doghome.easybuy.entity.News;
import com.github.pagehelper.PageInfo;

import java.util.Map;

public interface NewsService {

    PageInfo<News> getPageInfo(Map<String,Object>params);

    News selectNewsById(int id);

    boolean addNews(Map<String,Object>params);


    boolean updateNews(Map<String,Object>params);

    boolean deleteNews(int id);
}
