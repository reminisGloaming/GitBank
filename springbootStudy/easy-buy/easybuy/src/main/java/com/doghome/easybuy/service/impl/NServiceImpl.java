package com.doghome.easybuy.service.impl;

import com.doghome.easybuy.entity.News;
import com.doghome.easybuy.mapper.NewsMapper;
import com.doghome.easybuy.service.NewsService;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.github.pagehelper.util.StringUtil;
import com.mysql.cj.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class NServiceImpl implements NewsService {

    @Autowired
    private NewsMapper newsMapper;

    @Override
    public PageInfo<News> getPageInfo(Map<String, Object> params) {

        params.put("orderBy","createTime desc");

        String title="";

        if(params.containsKey("title")&& !StringUtils.isNullOrEmpty(params.get("title").toString())){
            title="%"+params.get("title").toString()+"%";
        }
        params.put("title",title);

        String createTime="";

        if(params.containsKey("createTime")&& !StringUtils.isNullOrEmpty(params.get("createTime").toString())){
            createTime="%"+params.get("createTime").toString()+"%";
        }
        params.put("createTime",createTime);

        PageHelper.startPage(params);
        List<News> newsList=newsMapper.newsByPage(params);
        return new PageInfo<>(newsList,3);
    }

    @Override
    public News selectNewsById(int id) {
        return newsMapper.selectNewsById(id);
    }

    @Override
    public boolean addNews(Map<String, Object> params) {
        return newsMapper.addNews(params)>0;
    }


    @Override
    public boolean updateNews(Map<String, Object> params) {
        return newsMapper.updateNews(params)>0;
    }


    @Override
    public boolean deleteNews(int id) {
        return newsMapper.deleteNews(id)>0;
    }


}
