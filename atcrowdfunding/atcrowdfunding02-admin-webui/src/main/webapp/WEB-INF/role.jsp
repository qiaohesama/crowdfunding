<%--
  Created by IntelliJ IDEA.
  User: qiaoh
  Date: 2021/8/14
  Time: 11:01
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="zh-CN">
<head>
    <title>role</title>

    <%@include file="/WEB-INF/include/include-header.jsp" %>
    <link rel="stylesheet" href="css/pagination.css"/>
    <link rel="stylesheet" href="ztree/zTreeStyle.css">
    <style>
        .tree li {
            list-style-type: none;
            cursor: pointer;
        }

        table tbody tr:nth-child(odd) {
            background: #F4F4F4;
        }

        table tbody td:nth-child(even) {
            color: #C00;
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
                <div class="panel-heading">
                    <h3 class="panel-title"><i class="glyphicon glyphicon-th"></i> 数据列表</h3>
                </div>
                <div class="panel-body">
                    <form class="form-inline" role="form" style="float:left;">
                        <div class="form-group has-feedback">
                            <div class="input-group">
                                <div class="input-group-addon">查询条件</div>
                                <input id="searchBar" class="form-control has-success" type="text"
                                       placeholder="请输入查询条件">
                            </div>
                        </div>
                        <button id="searchBtn" type="button" class="btn btn-warning"><i
                                class="glyphicon glyphicon-search"></i> 查询
                        </button>
                    </form>
                    <button id="removeSummary" type="button" class="btn btn-danger"
                            style="float:right;margin-left:10px;"><i
                            class=" glyphicon glyphicon-remove"></i> 删除
                    </button>
                    <button type="button" class="btn btn-primary" style="float:right;"
                            id="addRoleBtn"><i class="glyphicon glyphicon-plus"></i> 新增
                    </button>
                    <br>
                    <hr style="clear:both;">
                    <div class="table-responsive">
                        <table class="table  table-bordered">
                            <thead>
                            <tr>
                                <th width="30">#</th>
                                <th width="30"><input id="summaryCheck" type="checkbox"></th>
                                <th>名称</th>
                                <th width="100">操作</th>
                            </tr>
                            </thead>
                            <tbody id="roleBody">
                            </tbody>
                            <tfoot>
                            <tr>
                                <td colspan="6" align="center">
                                    <div id="Pagination" class="pagination"><!-- 这里显示分页 --></div>
                                </td>
                            </tr>

                            </tfoot>
                        </table>
                    </div>
                </div>
            </div>
        </div>
    </div>
</div>
<!--模态框，弹出表单，新增角色-->
<div id="addModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <form class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input
                                type="text" name="roleName"
                                class="form-control" id="inputSuccess4" placeholder="请输入角色名称"
                                autofocus>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="saveRoleBtn" type="button" class="btn btn-primary"> 保 存
                </button>
            </div>
        </div>
    </div>
</div>
<!--模态框，弹出表单，修改角色-->
<div id="editModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <form class="form-signin" role="form">
                    <div class="form-group has-success has-feedback">
                        <input
                                type="text" name="roleName"
                                class="form-control" placeholder="请输入角色名称"
                                autofocus>
                    </div>
                </form>
            </div>
            <div class="modal-footer">
                <button id="editRoleBtn" type="button" class="btn btn-success" disabled> 保 存
                </button>
            </div>
        </div>
    </div>
</div>
<!--模态框，弹出表单，确认删除角色-->
<div id="confirmModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <h4>你确定要删除以下角色吗</h4>
                <div id="toBeDeleted"></div>
            </div>
            <div class="modal-footer">
                <button id="confirmDeleteBtn" type="button" class="btn btn-success"> 确 认
                </button>
            </div>
        </div>
    </div>
</div>
<!--模态框，分配权限-->
<div id="authModal" class="modal fade" tabindex="-1" role="dialog">
    <div class="modal-dialog" role="document">
        <div class="modal-content">
            <div class="modal-header">
                <button type="button" class="close" data-dismiss="modal"
                        aria-label="Close">
                    <span aria-hidden="true">&times;</span>
                </button>
                <h4 class="modal-title">尚筹网系统弹窗</h4>
            </div>
            <div class="modal-body">
                <ul id="treeDemo" class="ztree"></ul>
            </div>
            <div class="modal-footer">
                <button id="confirmAssignAuth" type="button" class="btn btn-success"> 确 认
                </button>
            </div>
        </div>
    </div>
</div>


<script src="jquery/jquery-2.1.1.min.js"></script>
<script src="bootstrap/js/bootstrap.min.js"></script>
<script src="script/docs.min.js"></script>
<script type="text/javascript" src="jquery/jquery.pagination.js"></script>
<script src="ztree/jquery.ztree.all-3.5.min.js"></script>
<script src="layer/layer.js" charset="UTF-8"></script>
<script type="text/javascript" src="js/role.js" charset="UTF-8"></script>
<script type="text/javascript">
    $(function () {
        //把分页相关的数据设置为全局变量，每次修改后重新获取分页数据
        window.pageNum = 1;
        window.pageSize = 5;
        window.keyword = "";
        //执行分页
        generatePage()

        $(".list-group-item").click(function () {
            if ($(this).find("ul")) {
                $(this).toggleClass("tree-closed");
                if ($(this).hasClass("tree-closed")) {
                    $("ul", this).hide("fast");
                } else {
                    $("ul", this).show("fast");
                }
            }
        });
        //搜索框绑定事件
        $("#searchBtn").click(() => {
            window.keyword = $("#searchBar").val();
            generatePage()
        })
        /*---------------------------------------------------------*/
        //添加角色模态框显示按钮
        $("#addRoleBtn").click(function () {
            $("#addModal").modal("show")
        });
        //保存添加角色按钮
        $("#saveRoleBtn").click(() => {
            let roleName = $.trim($("#addModal [name=roleName]").val());
            $.ajax({
                url: "role/add/newRole",
                type: "POST",
                dataType: "json",
                data: {
                    name: roleName
                },
                success: (data) => {
                    if (data.operationResult === "SUCCESS") {
                        layer.msg("新增角色成功！")

                        // 跳转到最后一页查看插入的数据，更新页面
                        window.pageNum = 9999;
                        generatePage();

                        //关闭模态框
                        $("#addModal").modal("hide");
                        //清除数据
                        $("#addModal [name=roleName]").val("");
                    } else {
                        layer.msg(data.operationMessage)
                    }
                },
                error: () => {
                    layer.msg("系统出现异常，请稍后重试")
                }
            })
        });

        /*---------------------------------------------------------*/

        //更新角色按钮动态绑定
        $("#roleBody").on("click", ".pencilBtn", function () {
            //显示模态框
            $("#editModal").modal("show")
            //填充输入框,设置为全局变量准备给提交使用
            let roleName = $(this).parent().prev().text()
            window.roleId = $(this).parent().prev().prev().prev().text()

            // 使用 roleName 的值设置模态框中的文本框
            let inputRoleName = $("#editModal [name=roleName]")
            inputRoleName.val(roleName)
            inputRoleName.change(function () {

                $("#editRoleBtn").removeAttr("disabled");
            })
        });
        //保存更新角色按钮绑定单击事件
        $("#editRoleBtn").click(function () {

            $.ajax({
                url: "role/update",
                type: "POST",
                dataType: "json",
                data: {
                    id: window.roleId,
                    name: $("#editModal [name=roleName]").val()
                },
                success: (data) => {
                    if (data.operationResult === "SUCCESS") {
                        layer.msg("修改角色成功！")

                        // 更新页面

                        generatePage();

                        //关闭模态框
                        $("#editModal").modal("hide");

                    } else {
                        layer.msg(data.operationMessage)
                    }
                },
                error: () => {
                    layer.msg("错误！")
                }

            })
        })

        /*---------------------------------------------------------*/
        //删除单个，动态绑定
        $("#roleBody").on("click", ".removeBtn", function () {
            let roleArray = []
            let role = {
                id: $(this).parent().prev().prev().prev().text(),
                name: $(this).parent().prev().text()
            }

            roleArray.push(role)
            deleteRole(roleArray)
        });
        /*---------------------------------------------------------*/
        //删除多个的删除按钮
        $("#removeSummary").click(function () {
            let roleArray = []
            $(".itemBox:checked").each(function () {
                let roleId = $(this).parent().prev().text()
                let roleName = $(this).parent().next().text();
                let role = {
                    id: roleId,
                    name: roleName
                };
                roleArray.push(role)
            });
            if (roleArray.length === 0) {
                layer.msg("当前无已选择准备删除的角色")
                return
            }
            deleteRole(roleArray)
        })


        /*---------------------------------------------------------*/
        //分配权限模态框绑定事件
        $("#roleBody").on("click", ".checkBtn", function () {
            $("#authModal").modal("show")
            window.id = $(this).parent().prev().prev().prev().text()
            //给模态框绘制树形
            createTree()
        })
        //绑定更新权限按钮
        $("#confirmAssignAuth").click(function () {
            //先把已选中的节点的id放到数组中
            let checkedArr = []
            // 获取 zTreeObj 对象
            var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo")
            let nodes = zTreeObj.getCheckedNodes();
            for (const n of nodes) {
                checkedArr.push(n.id)
            }

            //map对象存放信息，不用新建一个实体类，
            // 为了使用<string,List>接收数据，将roleId也放到数组里
            let dataMap = {
                authIdArr: checkedArr,
                roleId: [window.id]
            };

            $.ajax({
                url: "assign/update/auth/for/role",
                dataType: "json",
                type: "post",
                contentType: "application/json",
                data: JSON.stringify(dataMap),
                success: (response) => {
                    if (response.operationResult === "FAILED") {
                        layer.msg(response.operationMessage)
                    } else {
                        layer.msg("更新权限成功")
                        $("#authModal").modal("hide")
                    }
                },
                error: (response) => {
                    console.log(response)
                    layer.msg("网络出错，请稍后重试")
                },
            })
        })
    });

    $("tbody .btn-success").click(function () {
        window.location.href = "assignPermission.html";
    });
</script>
</body>
</html>
