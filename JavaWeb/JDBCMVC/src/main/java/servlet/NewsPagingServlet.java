package servlet;

import bean.News;
import bean.Page;
import bean.Topic;
import com.alibaba.fastjson2.JSON;
import com.mysql.cj.util.StringUtils;
import service.NewsService;
import service.TopicService;
import service.impl.NewsServiceImpl;
import service.impl.TopicServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

public class NewsPagingServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //解决响应中文乱码的问题
        request.setCharacterEncoding("utf-8");
        response.setCharacterEncoding("utf-8");
        response.setContentType("application/json");

        NewsService newsService = new NewsServiceImpl();
        TopicService topicService = new TopicServiceImpl();

        List<Topic> topicList = topicService.getTopicList();

        HashMap<String, Object> params = new HashMap<>();

        if (!StringUtils.isNullOrEmpty(request.getParameter("pageNo"))) {
            params.put("pageNo", request.getParameter("pageNo"));
        }

        if (!StringUtils.isNullOrEmpty(request.getParameter("ntid"))) {
            params.put("ntid", request.getParameter("ntid"));
        }

        if (!StringUtils.isNullOrEmpty(request.getParameter("ntitle"))) {
            params.put("ntitle",  request.getParameter("ntitle") );
        }

        //使用HashMap<String, Object> responseData = new HashMap<>();将数据封装响应给前端
        HashMap<String, Object> responseData = new HashMap<>();
        Page<News> newsPage = newsService.newsPaging(params);
        responseData.put("newsPage", newsPage);
        responseData.put("topicList", topicList);

        //将响应的数据转为字符串
        response.getWriter().write(JSON.toJSONString(responseData));
    }
}
