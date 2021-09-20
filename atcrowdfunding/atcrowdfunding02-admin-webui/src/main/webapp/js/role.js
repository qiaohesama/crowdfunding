//绘制树形
function createTree() {
    //获取所有权限数据然后绘制树
    $.ajax({
        url: "assign/get/auth/info",
        dataType: "json",
        success: (data) => {
            if (data.operationResult === "SUCCESS") {

                initTree(data.queryData)

            } else {
                layer.msg(data.operationMessage)
            }
        },
        error: () => {
            layer.msg("错误！")
        }
    });

    // 获取已经被分配给该角色的权限数据，然后回显到树
    $.ajax({
        url: "assign/get/auth/by/role",
        dataType: "json",
        data: {
            roleId: window.id
        },
        success: (data) => {
            if (data.operationResult === "SUCCESS") {

                echoData(data.queryData)

            } else {
                layer.msg(data.operationMessage)
            }
        },
        error: () => {
            layer.msg("错误！")
        }

    })
}

//跟绘制树相关的操作
function initTree(arr) {
    let setting = {
        data: {

            simpleData: {
                enable: true,
                pIdKey: "categoryId"
            },
            key: {
                name: "title"
            }
        },
        check: {
            enable: true
        }
    }
    // 初始化树
    $.fn.zTree.init($("#treeDemo"), setting, arr);
    // 获取树对象
    let ztreeObj = $.fn.zTree.getZTreeObj("treeDemo");
    // 展开所有节点
    ztreeObj.expandAll(true);

}

//回显数据在树
function echoData(authArr) {
    // 获取树对象
    var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo");
    // 遍历authId的数组,通过id获取到节点，然后设置节点的选中状态
    for (const id of authArr) {
        let zNode = {}
        if (id != null) {
            zNode = zTreeObj.getNodeByParam("id", id);
        }
        // 参数1:节点，参数2：选中，参数3：不联动
        if (zNode != null) {
            zTreeObj.checkNode(zNode, true, false);

        }
    }
}

//删除角色统一函数，传入角色数组
function deleteRole(roleArray) {
    if (roleArray == null || roleArray == undefined || roleArray.length === 0) {
        layer.msg("未选择删除的角色")
        return;
    }
    //打开删除确认模态框
    $("#confirmModal").modal("show");
    //清空原先模态框的数据
    let toBeDeleted = $("#toBeDeleted")
    toBeDeleted.empty()

    //给确认区域添加角色名称
    for (let roleArrayElement of roleArray) {
        toBeDeleted.append(roleArrayElement.name + "<br/>")
    }

    $("#confirmDeleteBtn").click(function () {
        let jsonStr = JSON.stringify(roleArray)
        $.ajax({
            url: "role/delete",
            contentType: "application/json;charset=utf-8",
            dataType: "json",
            data: jsonStr,
            type: "post",
            success: (data) => {
                if (data.operationResult === "SUCCESS") {
                    layer.msg("删除成功！")

                    // 更新页面

                    generatePage();

                    //关闭模态框
                    $("#confirmModal").modal("hide");

                } else {
                    layer.msg(data.operationMessage)
                }
            },
            error: () => {
                layer.msg("错误！")
            }

        })
    })
}


//执行分页，生成页面效果，任何时候调用这个函数都会重新加载页面
function generatePage() {

    //获取分页数据,相应成功后回调绘制表格
    getPageInfoRemote();


}

//获取远程的页面json
function getPageInfoRemote() {

    // 获取数据,设置为同步的，获取到了数据再绘图
    $.ajax({
        url: "role/get/pageInfo",
        type: "POST",
        dataType: "json",//指定为json的时候能自动转换为对象。
        data: {
            "pageNum": pageNum,
            "pageSize": pageSize,
            "keyword": keyword
        },
        success: (response) => {

            if (response.operationResult === "FAILED") {
                layer.msg(response.operationMessage)
            } else {

                fillTableBody(response.queryData)
            }
        },
        error: (response) => {
            console.log(response)
            layer.msg("网络出错，请稍后重试")
        },

    })
}

//填充表格
function fillTableBody(pageInfo) {
    //先把之前的数据清空再插入
    let roleBody = $("#roleBody")

    roleBody.empty()
    $("#Pagination").empty()


    if (pageInfo.list == null || pageInfo.list.length === 0) {
        roleBody.append('<tr><td colSpan="4" align="center">抱歉！没有查询到您搜索的数据！</td></tr>')
        return
    }
    for (let role of pageInfo.list) {
        let perInfo = '<tr>\n' +
            '                                <td>' + role.id + '</td>\n' +
            '                                <td><input class="itemBox" type="checkbox"></td>\n' +
            '                                <td>' + role.name + '</td>\n' +
            '                                <td>\n' +
            '                                    <button type="button" class="btn btn-success btn-xs checkBtn"><i\n' +
            '                                            class=" glyphicon glyphicon-check"></i></button>\n' +
            '                                    <button type="button" class="btn btn-primary btn-xs pencilBtn"><i\n' +
            '                                            class=" glyphicon glyphicon-pencil "></i></button>\n' +
            '                                    <button type="button" class="btn btn-danger btn-xs removeBtn"><i\n' +
            '                                            class=" glyphicon glyphicon-remove "></i></button>\n' +
            '                                </td>\n' +
            '                            </tr>';
        roleBody.append(perInfo);
    }
    generateNavigator(pageInfo)
}

//绘制分页导航条
function generateNavigator(pageInfo) {
    //获取总记录数
    const totalRecode = pageInfo.total


    const properties = {
        num_edge_entries: 3,                                // 边缘页数
        num_display_entries: 5,                             // 主体页数
        current_page: pageInfo.pageNum - 1,   // 当前页， pageNum 从 1 开始，必须-1 后才可以赋值
        prev_text: "上一页",
        next_text: "下一页",
        items_per_page: pageInfo.pageSize,   // 每页显示 几 项
        callback: (pageIndex, jquery) => {
            // pageIndex 是当前页页码的索引，相对于 pageNum 来说， pageIndex 比 pageNum 小 1
            window.pageNum = pageIndex + 1;

            // 执行页面跳转也就是实现“翻页”
            generatePage()

            // 取消当前超链接的默认行为
            return false;
        }
    };

    $("#Pagination").pagination(totalRecode, properties)
}