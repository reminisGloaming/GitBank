<%@ page import="service.NewsService" %>
<%@ page import="service.impl.NewsServiceImpl" %>
<%@ page import="service.impl.TopicServiceImpl" %>
<%@ page import="service.TopicService" %>
<%@ page import="bean.News" %>
<%@ page import="java.util.List" %>
<%@ page import="bean.Topic" %>
<%@ page import="bean.Page" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.mysql.cj.util.StringUtils" %>

<%--
  Created by IntelliJ IDEA.
  User: HUAWEI
  Date: 2024/7/19
  Time: 上午11:05
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<html>
<head>
    <title>Title</title>
</head>
<body>
<script src="JQuery/jQuery.js"></script>
<script>
    $(function () {
        init(1, '', '');
    });

    function init(pageNo, ntid, ntitle) {
        let container = $('#table-and-pagination-container');

        container.empty();

        $.ajax({
            url: "/newsPaging",
            type: "get",
            data: {
                "pageNo": pageNo,
                "ntid": ntid,
                "ntitle": ntitle
            },
            dataType: "json",
            success: function (data) {

                let newsPage = data.newsPage;
                let topicList = data.topicList;

                // 清空并重新添加分类选项
                $('#tid').empty().append($('<option>').val('').text('--全部--'));
                for (let topic of topicList) {
                    let option = $('<option></option>');
                    option.val(topic.tid).text(topic.tname);
                    $('#tid').append(option);
                }

                // 创建表格内容
                let table = $('<table></table>');
                let thead = $('<thead></thead>');
                let tr = $('<tr></tr>');

                tr.append($('<th>序号</th>'));
                tr.append($('<th>类别</th>'));
                tr.append($('<th>标题</th>'));
                tr.append($('<th>作者</th>'));
                tr.append($('<th>时间</th>'));

                thead.append(tr);
                table.append(thead);

                let tbody = $('<tbody></tbody>');
                let index = 0;

                for (let news of newsPage.items) {
                    let tr = $('<tr></tr>');

                    tr.append($('<td>').text((newsPage.pageNo - 1) * newsPage.pageSize + (index++ + 1)));
                    tr.append($('<td>').text(getTopicName(news.ntid, topicList)));
                    tr.append($('<td>').text(news.ntitle));
                    tr.append($('<td>').text(news.nauthor));
                    tr.append($('<td>').text(formatDate(news.ncreateDate)));

                    tbody.append(tr);
                }

                table.append(tbody);

                container.append(table);

                // 页面跳转
                let pagination = createPagination(newsPage);
                container.append(pagination);
            }
        });
    }

    function getTopicName(ntid, topicList) {
        for (let topic of topicList) {
            if (topic.tid === ntid) {
                return topic.tname;
            }
        }
        return '未知';
    }

    function formatDate(dateString) {
        let date = new Date(dateString);
        return date.toLocaleDateString();
    }

    function createPagination(newsPage) {
        let pagination = $('<div class="pagination"></div>');

        pagination.append($('<span>').text("共" + newsPage.pageTotalCount + "条记录"));
        pagination.append($('<span>').text(newsPage.pageNo));
        pagination.append($('<span>').text("/" + newsPage.pageTotal));

        pagination.append($('<a href="javascript:;">首页</a>').click(function () {
            init(1, $('#tid').val(), $('#title').val());
        }));

        pagination.append($('<a href="javascript:;">上一页</a>').click(function () {
            let prevPage = Math.max(1, newsPage.pageNo - 1);
            init(prevPage, $('#tid').val(), $('#title').val());
        }));

        pagination.append($('<a href="javascript:;">下一页</a>').click(function () {
            let nextPage = Math.min(newsPage.pageTotal, newsPage.pageNo + 1);
            init(nextPage, $('#tid').val(), $('#title').val());
        }));

        pagination.append($('<a href="javascript:;">末页</a>').click(function () {
            init(newsPage.pageTotal, $('#tid').val(), $('#title').val());
        }));

        pagination.append($('<span>').text(''));
        pagination.append($('<input type="text" size="2" name="goto" id="in">'));
        pagination.append($('<button>查询</button>').click(function () {
            let gotoPage = parseInt($('#in').val());
            if (isNaN(gotoPage) || gotoPage < 1 || gotoPage > newsPage.pageTotal) {
                alert("请输入有效的页码！");
            } else {
                init(gotoPage, $('#tid').val(), $('#title').val());
            }
        }));

        return pagination;
    }

    // 使用事件委托为所有带有'id="qeury"'的按钮绑定点击事件
    $(document).on('click', '#qeury', function (event) {
        event.preventDefault(); // 阻止默认行为
        let ntid = $('#tid').val();
        let ntitle = $('#title').val();
        init(1, ntid, ntitle);
    });

</script>

<main>
    <div>
        <label for="tid">
            新闻分类：
        </label>
        <select name="ntid" id="tid">
            <option value="">--全部--</option>
        </select>

        <label for="title">新闻标题</label>
        <input type="text" name="ntitle" id="title">

        <button id="qeury">查询</button>
        <a href="add.jsp">添加新闻</a>
        <br>
    </div>
    <div id="table-and-pagination-container"></div> <!-- 明确的容器 -->
</main>
</body>
</html>
