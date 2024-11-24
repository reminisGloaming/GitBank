package com.doghome.easybuy.controller;

import com.doghome.easybuy.service.NewsService;
import com.doghome.easybuy.util.AjaxResult;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/news")
public class NewsController {

    @Autowired
    private NewsService newsService;

//    @Value("${server.port}")
//    private int port;
//
//    private static final Logger logger =  LoggerFactory.getLogger(NewsController.class);

    @RequestMapping("/getNewsList")
    public AjaxResult getNewsList(@RequestParam Map<String, Object> params) {
        AjaxResult ajaxResult = AjaxResult.success().add("pageInfo", newsService.getPageInfo(params));

//        logger.info("服务器"+port+"执行分页操作");

        return ajaxResult;
    }

    @RequestMapping("addNews")
    public String addNews(@RequestParam Map<String,Object>params){
        
        if(newsService.addNews(params)){
            return "增加成功";
        }

        return "增加失败";
    }


    @RequestMapping("/selectNewsById")
    public AjaxResult selectNewsById(int id) {
        AjaxResult ajaxResult = AjaxResult.success().add("news", newsService.selectNewsById(id));
        return ajaxResult;
    }


    @RequestMapping("/updateNews")
    public String updateNews(@RequestParam Map<String, Object> params) {

        if (newsService.updateNews(params)) {
            return "修改成功";
        }

        return "修改失败";
    }

    @RequestMapping("/deleteNews")
    public String deleteNews(int id){
        if(newsService.deleteNews(id)){
            return "删除成功";
        }
        else{
            return "删除失败";
        }
    }
}
