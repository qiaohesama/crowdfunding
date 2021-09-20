<%--
  Created by IntelliJ IDEA.
  User: qiaoh
  Date: 2021/8/16
  Time: 21:33
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html lang="zh-CN">
<head>
    <title>permission</title>
    <%@include file="/WEB-INF/include/include-header.jsp" %>
    <link rel="stylesheet" href="ztree/zTreeStyle.css">

    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }
    </style>
</head>
<body>
<%@include file="/WEB-INF/include/include-navbar.jsp" %>

<div class="container-fluid">
    <div class="row">
        <%@include file="/WEB-INF/include/include-sidebar.jsp" %>

        <div class="col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main">

            <div class="panel panel-default">
                <div class="panel-heading"><i class="glyphicon glyphicon-th-list"></i> 权限菜单列表
                    <div style="float:right;cursor:pointer;" data-toggle="modal" data-target="#myModal"><i
                            class="glyphicon glyphicon-question-sign"></i></div>
                </div>
                <div class="panel-body">
                    <%--                    这里是树所依附的静态元素--%>
                    <ul id="treeDemo" class="ztree"></ul>
                </div>
            </div>
        </div>
    </div>
</div>
<!--模态框响应按钮组-->
<%@include file="/WEB-INF/include/modal-menu-add.jsp" %>
<%@include file="/WEB-INF/include/modal-menu-confirm.jsp" %>
<%@include file="/WEB-INF/include/modal-menu-edit.jsp" %>

<script src="jquery/jquery-2.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/docs.min.js"></script>
<script src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="layer/layer.js" charset="UTF-8"></script>
<script src="js/menu.js"></script>
</body>
</html>
