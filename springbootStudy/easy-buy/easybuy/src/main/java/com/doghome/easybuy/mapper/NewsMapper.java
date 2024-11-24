package com.doghome.easybuy.mapper;

import com.doghome.easybuy.entity.News;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Mapper
@Repository
public interface NewsMapper {

    List<News> newsByPage(Map<String,Object>params);


    News selectNewsById(int id);

    int addNews(Map<String,Object>params);


    int updateNews(Map<String,Object>params);

    int deleteNews(int id);
}
