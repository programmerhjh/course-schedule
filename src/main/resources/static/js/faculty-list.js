axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
layui.use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
    //院系列表
    var tableIns = table.render({
        elem: '#facultyListTable',
        url: '/admin/facultyList',
        method: 'post',
        contentType: 'application/json',
        parseData: function (res) {
            return {
                "errorCode": res.errorCode,
                "errorMsg": res.errorMsg,
                "data": res.data.data,
                "count": res.data.totalSize
            };
        },
        response: {
            statusName: 'errorCode',
            statusCode: 200,
            msgName: 'errorMsg'
        },
        cellMinWidth: 95,
        page: true,
        height: "full",
        limit: 12,
        limits: [12, 20, 50],
        cols: [
            [
                {type: "checkbox", fixed: "left", width: 50},
                {field: 'id', title: 'ID', width: 60, align: "center"},
                {field: 'name', title: '院系名称', align: "center"},
                {
                    field: 'createTime', title: '创建时间', align: 'center', templet: function (d) {
                        if (d.createTime) {
                            return layui.util.toDateString(d.createTime, 'yyyy年MM月dd日 HH:mm:ss');
                        }
                        return '无数据';
                    }
                },
                {
                    field: 'modifyTime', title: '最后修改时间', align: 'center', templet: function (d) {
                        if (d.modifyTime) {
                            return layui.util.toDateString(d.modifyTime, 'yyyy年MM月dd日 HH:mm:ss');
                        }
                        return '无数据';
                    }
                },
                {title: '操作', width: 170, toolbar: '#facultyListBar', fixed: "right", align: "center"}
            ]
        ]
    });

    //列表操作
    table.on("tool(facultyListTable)", function (obj) {
        var layEvent = obj.event,
            data = obj.data;
        if (layEvent === 'edit') { //编辑
            addFaculty(data);
        } else if (layEvent === 'del') { //删除
            layer.confirm('确定删除这个院系吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/deleteFaculty", {
                    id: data.id
                }).then(
                    function (res) {
                        if (!res.data.success) {
                            layer.msg(res.data.errorMsg);
                            return;
                        }
                        tableIns.reload();
                        layer.close(index);
                    }
                );
            });
        }
    });

    $(".search_btn").on("click", function () {
        if ($(".searchVal").val() != '') {
            tableIns.reload({
                url: '/admin/searchFaculty',
                method: 'post',
                contentType: "application/json; charset=utf-8",
                parseData: function (res) {
                    return {
                        "errorCode": res.errorCode,
                        "errorMsg": res.errorMsg,
                        "data": res.data.data,
                        "count": res.data.totalSize
                    };
                },
                response: {
                    statusName: 'errorCode',
                    statusCode: 200,
                    msgName: 'errorMsg'
                },
                where: {
                    key: $(".searchVal").val(),
                    page: this.page,
                    limit: this.limit
                }
            })
        } else {
            layer.msg("请输入搜索的内容");
        }
    });

    //添加院系
    function addFaculty(edit) {
        layui.layer.open({
            title: "院系信息操作页",
            type: 2,
            content: "/admin/facultyAddUI",
            area: ['500px', '300px'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                if (edit) {
                    body.find(".id").val(edit.id);
                    body.find(".name").val(edit.name);
                }
            }
        })
    }

    $(".addFaculty_btn").click(function () {
        addFaculty();
    })

    //批量删除
    $(".delAll_btn").click(function () {
        var checkStatus = table.checkStatus('facultyListTable'),
            data = checkStatus.data,
            ids = [];
        if (data.length > 0) {
            for (var i in data) {
                ids.push(data[i].id);
            }
            layer.confirm('确定删除选中的院系吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/batchDeleteFaculties", ids)
                    .then(function (res) {
                        if (!res.data.success) {
                            layer.msg(res.data.errorMsg);
                            return;
                        }
                        tableIns.reload();
                        layer.close(index);
                    });
            })
        } else {
            layer.msg("请选择需要删除的院系");
        }
    })

})
