package servlet;

import bean.News;
import bean.Topic;
import service.NewsService;
import service.TopicService;
import service.impl.NewsServiceImpl;
import service.impl.TopicServiceImpl;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class UpdateNewsServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.setCharacterEncoding("UTF-8");
        TopicService topicService = new TopicServiceImpl();
        List<Topic> topicList = topicService.getTopicList();

        NewsService newsService = new NewsServiceImpl();
        News news = newsService.getNewsById(Integer.parseInt(request.getParameter("nid")));

        request.setAttribute("topicList",topicList);
        request.setAttribute("news", news);

       try {
           request.getRequestDispatcher("/update.jsp").forward(request,response);
       }catch (Exception e){
           e.printStackTrace(); // 或者使用日志框架记录异常
           throw new ServletException("Error in doGet method", e);
       }
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setCharacterEncoding("UTF-8");
        News news = new News();

        news.setNid(Integer.parseInt(request.getParameter("nid")));
        news.setNtid(Integer.parseInt(request.getParameter("ntid")));
        news.setNtitle(request.getParameter("ntitle"));
        news.setNsummary(request.getParameter("nsummary"));
        news.setNauthor(request.getParameter("nauthor"));
        news.setNcontent(request.getParameter("ncontent"));

        NewsService newsService = new NewsServiceImpl();
        int isAdd = newsService.updateNews(news);

        if (isAdd > 0) {
         response.sendRedirect("/index");
        }
        else {
         response.sendRedirect("/index");
        }
    }
}
