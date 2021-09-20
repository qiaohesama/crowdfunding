$(function () {
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

    createTree();

    //添加按钮绑定事件
    $("#treeDemo").on("click", ".addBtn", function () {
        // 打开模态框
        $("#menuAddModal").modal("show");
        //把当前的id当作父id
        window.pid = this.id
        return false;
    });
    $("#menuSaveBtn").click(function () {
        // 收集表单项中用户输入的数据
        var name = $.trim($("#menuAddModal [name=name]").val());
        var url = $.trim($("#menuAddModal [name=url]").val());

        // 单选按钮要定位到“被选中” 的那一个
        var icon = $("#menuAddModal [name=icon]:checked").val();

        $.ajax({
            url: "menu/add/child",
            type: "post",
            dataType: "json",
            data: {
                "pid": window.pid,
                "name": name,
                "url": url,
                "icon": icon
            },
            success: (response) => {

                if (response.operationResult === "FAILED") {
                    layer.msg(response.operationMessage)
                } else {
                    //刷新树
                    createTree();
                    // 关闭模态框
                    $("#menuAddModal").modal("hide");
                    // 清空表单
                    // jQuery 对象调用 click()函数， 里面不传任何参数， 相当于用户点击了一下
                    $("#menuResetBtn").click();
                }
            },
            error: (response) => {
                console.log(response)
                layer.msg("网络出错，请稍后重试")
            },
        })
    })

    //更新按钮绑定事件
    $("#treeDemo").on("click", ".editBtn", function () {
        //把当前按钮对应的id保存到全局变量中
        window.id = this.id;
        //打开模态框
        $("#menuEditModal").modal("show");
        // 获取 zTreeObj 对象
        var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo")
        //获取当前的节点对象
        var currentNode = zTreeObj.getNodeByParam("id", window.id);
        //回显数据
        $("#menuEditModal [name=name]").val(currentNode.name);
        $("#menuEditModal [name=url]").val(currentNode.url);

        //回显选中选项
        // 回显 radio 可以这样理解： 被选中的 radio 的 value 属性可以组成一个数组，
        // 然后再用这个数组设置回 radio， 就能够把对应的值选中
        $("#menuEditModal [name=icon]").val([currentNode.icon]);
        return false;
    });
    //更新按钮绑定
    $("#menuEditBtn").click(function () {
        // 收集表单数据
        var name = $("#menuEditModal [name=name]").val();
        var url = $("#menuEditModal [name=url]").val();
        var icon = $("#menuEditModal [name=icon]:checked").val();

        $.ajax({
            url: "menu/update/by/id",
            type: "post",
            dataType: "json",
            data: {
                "name": name,
                "url": url,
                "icon": icon,
                "id": window.id
            },
            success: (response) => {

                if (response.operationResult === "FAILED") {
                    layer.msg(response.operationMessage)
                } else {
                    // 关闭模态框
                    $("#menuEditModal").modal("hide");
                    //刷新树
                    createTree();

                }
            },
            error: (response) => {
                console.log(response)
                layer.msg("网络出错，请稍后重试")
            },
        })
    });

    //删除按钮绑定事件
    $("#treeDemo").on("click", ".removeBtn", function () {
        //显示模态框
        $("#menuConfirmModal").modal("show")
        //表单回显
        // 获取 zTreeObj 对象
        var zTreeObj = $.fn.zTree.getZTreeObj("treeDemo")
        //获取当前的节点对象
        var currentNode = zTreeObj.getNodeByParam("id", this.id);
        window.id = currentNode.id
        var nodeName = currentNode.name
        //回显名字
        $("#removeNodeSpan").html("  " + nodeName + "  ")
        return false;
    });
    $("#confirmBtn").click(function (){
        $.ajax({
            url: "menu/delete/by/id",
            type: "post",
            dataType: "json",
            data: {
                "id": window.id
            },
            success: (response) => {

                if (response.operationResult === "FAILED") {
                    layer.msg(response.operationMessage)
                } else {
                    // 关闭模态框
                    $("#menuConfirmModal").modal("hide");
                    //刷新树
                    createTree();

                }
            },
            error: (response) => {
                console.log(response)
                layer.msg("网络出错，请稍后重试")
            },
        })
    })
})

function createTree() {
    $.ajax({
        url: "menu/get/whole/tree",
        dataType: "json",
        success: (response) => {
            if (response.operationResult === "FAILED") {
                layer.msg(response.operationMessage)
            } else {
                initTree(response.queryData)
            }
        },
        error: (response) => {
            console.log(response)
            layer.msg("网络出错，请稍后重试")
        },
    })
}

function addDiyDom(treeId, treeNode) {
    // treeId 是整个树形结构附着的 ul 标签的 id
    // console.log(treeId)
    // treeNode当前树形节点的全部的数据， 包括从后端查询得到的 Menu 对象的全部属性
    // console.log(treeNode)
    // zTree 生成 id 的规则
    // 例子： treeDemo_7_ico
    // 解析： ul 标签的 id_当前节点的序号_功能
    // 提示： “ul 标签的 id_当前节点的序号” 部分可以通过访问 treeNode 的 tId 属性得到
    // 根据 id 的生成规则拼接出来 span 标签的 id
    let iconId = treeNode.tId + "_ico";
    $("#" + iconId).removeClass().addClass(treeNode.icon)
}

//鼠标移入时添加按钮组
function addHoverDom(treeId, treeNode) {
    //按钮组的结构<span><a><i></i></a><a><i></i></a></span>
    //给按钮组再加上id，方便删除，命名规则  treeDemo_x_btnGrp
    let btnGroupId = treeNode.tId + "_btnGrp";

    // 判断一下以前是否已经添加了按钮组,因为鼠标移入的事件会多次触发
    if ($("#" + btnGroupId).length > 0) {
        return;
    }


    //按钮组的规则:叶子节点可删除，可修改，不可添加
    //           分支节点可添加，可修改，没有子节点可删除
    //           根节点可添加，不可修改，不可删除
    let editBtn = "<a id='" + treeNode.id + "' class=' editBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='修改子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-edit rbg '></i></a>";
    let addBtn = "<a id='" + treeNode.id + "' class=' addBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title='添加子节点'>&nbsp;&nbsp;<i class='fa fa-fw fa-plus rbg '></i></a>";
    let removeBtn = "<a id='" + treeNode.id + "' class=' removeBtn btn btn-info dropdown-toggle btn-xs' style='margin-left:10px;padding-top:0px;' href='#' title=' 删 除 节 点 '>&nbsp;&nbsp;<i class='fa fa-fw fa-times rbg '></i></a>";
    //当前节点数据
    let level = treeNode.level
    //根节点
    if (level === 0) {
        let grp = '<span id="' + btnGroupId + '">' + addBtn + '</span>'
        // 追加在超链接后面
        $("#" + treeNode.tId + "_a").after(grp)
        return;
    }
    //分支节点
    if (level === 1) {
        let grp = "";
        //如果没有子节点可以删除
        if (treeNode.children.length === 0) {
            grp = '<span id="' + btnGroupId + '">' + addBtn + editBtn + removeBtn + '</span>'
        } else {
            grp = '<span id="' + btnGroupId + '">' + addBtn + editBtn + '</span>'
        }
        // 追加在超链接后面
        $("#" + treeNode.tId + "_a").after(grp)
        return;
    }

    //叶子节点
    if (level === 2) {
        let grp = '<span id="' + btnGroupId + '">' + editBtn + removeBtn + '</span>';
        $("#" + treeNode.tId + "_a").after(grp)
    }
}

// 鼠标移走时删除按钮组
function removeHoverDom(treeId, treeNode) {
    let grpId = treeNode.tId + "_btnGrp";
    $("#" + grpId).remove()
}

function initTree(tree) {

    let setting = {
        "view": {
            //不可加括号
            "addDiyDom": addDiyDom,
            "addHoverDom": addHoverDom,
            "removeHoverDom": removeHoverDom
        },
        "data": {
            "key": {
                //把url设置成没有的属性就不会跳转
                "url": "cat"
            }
        }
    }
    $.fn.zTree.init($("#treeDemo"), setting, tree);
}