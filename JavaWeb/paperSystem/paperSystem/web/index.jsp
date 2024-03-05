<%--
  Created by IntelliJ IDEA.
  User: HUAWEI
  Date: 2024/8/11
  Time: 下午4:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>论文系统首页</title>
    <style>
        h1 {
            text-align: center;
        }

        table {
            width: 100%;
            border-collapse: collapse;
            border: 1px solid black; /* 添加边框 */
        }

        th, td {
            padding: 8px;
            text-align: left;
            border: 1px solid #ddd; /* 添加单元格之间的分割线 */
        }

        tbody tr:nth-child(2n) {
            background-color: orangered;
        }
    </style>

    <script src="JQuery/jQuery.js"></script>
    <script>
        $(function () {
            init(1, "", "");
        });

        function init(pageNow, title, type) {
            $.ajax({
                url: "/pagination",
                type: "get",
                dataType: "json",
                data: {
                    "pageNow": pageNow,
                    "title": title,
                    "type": type
                },
                success: function (data) {
                    let page = data.page;
                    let paperTypeList = data.paperTypeList;

                    // 清空内容
                    let pageBox = $('#pageBox');
                    pageBox.empty();

                    // 下拉列表
                    selector(paperTypeList, page.items);

                    pageBox.append(pageFunction(paperTypeList, page.items));

                    pageBox.append(paginationBar(page));
                }
            });
        }

        // 给 select 添加数据
        function selector(paperTypeList) {
            let sel = $('#selectT');

            paperTypeList.forEach((paperType) => {
                let opt = $('<option>');
                opt.val(paperType.id).text(paperType.type);
                sel.append(opt);
            });
        }

        // 分页框
        function pageFunction(paperTypeList, paperList) {
            let table = $('<table>');

            let thead = $('<thead>');
            let tr = $('<tr>');

            tr.append($('<th>论文题目</th>'));
            tr.append($('<th>作者</th>'));
            tr.append($('<th>论文类型</th>'));
            tr.append($('<th>发布时间</th>'));
            tr.append($('<th>修改时间</th>'));
            tr.append($('<th>操作</th>'));

            thead.append(tr);
            table.append(thead);

            let tbody = $('<tbody>');

            paperList.forEach((paper) => {
                tr = $('<tr>');

                let detailsLink = $("<a>").attr({
                    href: "/details.jsp" + "?id=" + paper.id
                }).text(paper.title);
                tr.append($('<td>').append(detailsLink));
                tr.append($('<td>').text(paper.crateBy));
                tr.append($('<td>').text(typeMatch2(paper.type, paperTypeList)));
                tr.append($('<td>').text(dateFormate(paper.createDate)));
                tr.append($('<td>').text(dateFormate(paper.modifyDate)));

                // 创建修改按钮并绑定点击事件
                let updateButton = $('<button>').text("修改").data('id', paper.id);
                updateButton.click(function () {
                    let paperId = $(this).data('id');
                    if (confirm('确定要修改这篇论文吗？')) {
                        window.location.href='/update.jsp' + '?id=' +paperId;
                    }
                });

                // 创建删除按钮并绑定点击事件
                let deleteButton = $('<button>').text("删除").data('id', paper.id);
                deleteButton.click(function () {
                    var paperId = $(this).data('id');
                    if (confirm('确定要删除这篇论文吗？')) {
                        deletePaper(paperId);
                    }
                });

                tr.append($('<td>').append(updateButton).append(deleteButton));
                tbody.append(tr);
            });

            table.append(tbody);
            return table;
        }

        // 论文类型匹配
        // function typeMatch(typeId, paperTypeList) {
        //     for (let paperType of paperTypeList) {
        //         if (typeId === paperType.id) {
        //             return paperType.type;
        //         }
        //     }
        // }

        function typeMatch2(typeId, paperTypeList) {
            let matchedType;
            paperTypeList.forEach((paperType) => {
                if (typeId === paperType.id) {
                    matchedType = paperType.type;
                }
            });
            return matchedType;
        }

        // 日期类型转换
        function dateFormate(dateStr) {
            let date = new Date(dateStr);
            return date.toLocaleDateString();
        }

        // 分页信息栏
        function paginationBar(page) {
            let pagination = $('<div>');

            pagination.append($('<span>').addClass('total-count').text('共' + page.dataTotal + '条数据'));
            pagination.append($('<span>').addClass('separator').text("  "));
            pagination.append($('<span>').addClass('current-page').text('当前页:' + page.pageNow));
            pagination.append($('<span>').addClass('separator').text("  "));
            pagination.append($('<span>').addClass('total-pages').text('总页数：' + page.totalPage));

            pagination.append(createNavigationLink('首页', 1, page));
            pagination.append(createNavigationLink('上一页', page.pageNow - 1, page));
            pagination.append(createNavigationLink('下一页', page.pageNow + 1, page));
            pagination.append(createNavigationLink('尾页', page.totalPage, page));

            pagination.append($('<span>').addClass('separator').text("  "));

            pagination.append($('<span>').addClass('jump-label').text('跳转至:'));
            pagination.append($('<input type="text" size="2" id="number" name="goto">'));
            pagination.append($('<button type="submit">跳转</button>').on('click', function () {
                let number = parseInt($('#number').val());

                if (!Number.isInteger(number) || number < 1 || number > page.totalPage) {
                    $('#number').val('').focus();
                    alert('请输入有效的页面号（1~' + page.totalPage + '）');
                } else {
                    init(number, $('#search').val(), $('#selectT').val());
                }
            }));

            return pagination;
        }

        // 创建带有动作的链接
        function createNavigationLink(text, targetPage, page) {
            return $('<a>').text(text).attr({
                href: '#',
                'data-target': targetPage
            }).on('click', function (e) {
                e.preventDefault();
                let number = $(this).data('target');

                if (number >= 1 && number <= page.totalPage) {
                    init(number, $('#search').val(), $('#selectT').val());
                }
            });
        }


        // 通过 AJAX 删除论文
        function deletePaper(paperId) {
            $.ajax({
                url: `/delete`, // 假设这是删除论文的服务器端路由
                type: "get",
                dataType: "json",
                data: {"id": paperId},
                success: function () {
                    // 删除成功后的操作
                    // 例如，可以刷新当前页面或通知用户
                    init(1, "", ""); // 刷新页面
                }
            });
        }

        // 查询操作
        $(document).on('click', '#query', function (event) {
            event.preventDefault(); // 阻止默认行为
            let title = $('#search').val();
            let type = $('#selectT').val();
            init(1, title, type);
        });
    </script>
</head>
<body>
<header>
    <h1>论文管理系统</h1>
</header>

<main>
    <div class="searchBox">
        <label for="search">
            论文题目：
            <input id="search" name="title">
        </label>

        <label for="selectT">
            论文类型：
            <select id="selectT" name="type">
                <option value="">--请选择--</option>
            </select>
        </label>
        &nbsp;

        <button type="submit" id="query">查询</button> &nbsp;&nbsp;&nbsp;

        <a href="/add.jsp">添加论文</a>
    </div>

    <div id="pageBox"></div>
</main>
</body>
</html>