<%--
  Created by IntelliJ IDEA.
  User: HUAWEI
  Date: 2024/8/16
  Time: 下午2:51
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

        button {
            margin-top: 10px;
            margin-left: 10px;
        }

        a {
            margin-left: 6px;
        }

        span {
            margin-left: 4px;
        }
    </style>

    <script src="JQuery/jQuery.js"></script>
    <script>
        $(function () {
            init(1, "", "");

            $(document).on("click","#query",function (event){
                event.preventDefault();

                init(1, $("#search").val(), $("#selectT").val());
            })
        });

        function init(pageNow, title, type) {
            $.ajax({
                url: "/pagination",
                type: "get",
                dateType: "json",
                data: {
                    "pageNow": pageNow,
                    "title": title,
                    "type": type
                },
                success: function (data) {
                    let page = data.page;
                    let paperTypeList = data.paperTypeList;

                    //清空内容
                    let pageBox = $("#pageBox");
                    pageBox.empty();

                    //下拉列表
                    selector(paperTypeList);

                    pageBox.append(paginationFunction(page.items, paperTypeList));

                    pageBox.append(paginationBar(page));
                }
            });
        }

        //下拉列表
        function selector(paperTypeList) {
            let selectT = $("#selectT");

            paperTypeList.forEach((paperType) => {
                let opt = $("<option>")
                opt.val(paperType.id).text(paperType.type);
                selectT.append(opt);
            })
        }

        //分页信息表格
        function paginationFunction(paperList, paperTypeList) {
            let table = $('<table>');

            let thead = $('<thead>');

            let tr = $('<tr>')
            tr.append($('<td>').text("论文标题"));
            tr.append($('<td>').text("作者"))
            tr.append($('<td>').text("论文类型"));
            tr.append($('<td>').text("发表时间"));
            tr.append($('<td>').text("修改时间"));
            tr.append($('<td>').text("操作"));

            thead.append(tr);
            table.append(thead);

            let tbody = $('<tbody>');

            paperList.forEach((paper) => {
                tr = $('<tr>');

                tr.append($("<td>").text(paper.title));
                tr.append($("<td>").text(paper.crateBy));
                tr.append($("<td>").text(typeMatch(paper.type, paperTypeList)));
                tr.append($("<td>").text(dateFormat(paper.createDate)));
                tr.append($("<td>").text(dateFormat(paper.modifyDate)));

                let updateButton = $('<button>').text("修改").data("id", paper.id);
                updateButton.click(function () {
                    let id = $(this).data("id");

                    if (confirm("是否修改：" + paper.title)) {
                        window.location.href = "/update.jsp" + "?id=" + id;
                    }
                });

                let deleteButton = $("<button>").text("删除").data("id", paper.id);
                deleteButton.click(function () {
                    let id = $(this).data("id");

                    if (confirm("是否删除：" + paper.title)) {
                        deleteFun(id);
                    }
                });

                tr.append(updateButton).append(deleteButton);

                tbody.append(tr)
            });

            table.append(tbody);
            return table;
        }

        //类型匹配
        function typeMatch(paperTypeId, paperTypeList) {
            let type = null;
            paperTypeList.forEach((paperType) => {
                if (paperTypeId === paperType.id) {
                    type = paperType.type;
                }
            });
            return type;
        }

        //日期格式化
        function dateFormat(dateStr) {
            return new Date(dateStr).toLocaleDateString()
        }

        //删除函数
        function deleteFun(id) {
            $.ajax({
                url: "/delete",
                type: "get",
                dateType: "json",
                data: {
                    "id": id
                },
                success: function (data) {
                    init(1, $("#search").val(), $("#selectT").val());
                }
            })
        }

        function paginationBar(page) {
            let pagination = $("<div>");

            pagination.append($("<span>").text("总数量：" + page.dataTotal));
            pagination.append($("<span>").text("当前页：" + page.pageNow));
            pagination.append($("<span>").text("总页数：" + page.totalPage));

            pagination.append(createActiveLink("首页", 1, page));
            pagination.append(createActiveLink("上一页", page.pageNow - 1, page));
            pagination.append(createActiveLink("下一页", page.pageNow + 1, page));
            pagination.append(createActiveLink("尾页", page.totalPage, page));

            pagination.append($("<span>").text("跳转到："));
            pagination.append($("<input type='text' size='2' id='numberIn' name='number'>"));
            pagination.append($("<button>").text("跳转").click(function () {
                let number = parseInt($("#numberIn").val());

                if (!Number.isInteger(number) || number < 1 || number > page.totalPage) {
                    $("#numberIn").val();
                    alert("请输入1~" + page.totalPage + "之间的数据");
                }
                else {
                    init(number, $("#search").val(), $("#selectT").val());
                }
            }));

            return pagination;
        }

        function createActiveLink(text, targetPage, page) {
            return $("<a>").text(text).attr({
                href: "#",
                "data-target": targetPage
            }).click(function (e) {
                e.preventDefault();
                let number = $(this).data("target");
                if (number >= 1 && number <= page.totalPage) {
                    init(number, $("#search").val(), $("#selectT").val());
                }
            })
        }
    </script>
</head>
<body>
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

    <div id="pageBox">

    </div>
</main>
</body>
</html>
