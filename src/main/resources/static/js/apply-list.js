axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
layui.use(['form', 'layer', 'laydate', 'table', 'laytpl'], function () {
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    $("#operaButton").hide();
    var tableIns;

    $(document).ready(function () {
        var url = "/admin/facultyList";
        axios.post(url,{}).then(
            function (res) {
                if (!res.data.success) {
                    layer.msg(res.data.errorMsg);
                    return;
                }
                var dataArr = res.data.data.data;
                for (var i=0;i<dataArr.length;i++){
                    $("#facultyList").append('<option value="' + dataArr[i].id + '">&nbsp;' + dataArr[i].name + '</option>');
                }
                form.render();
            }
        );
    })

    //列表操作
    table.on("tool(applyListTable)", function (obj) {
        var layEvent = obj.event,
            data = obj.data,
            stateData;
        if (layEvent === 'del') { //删除
            layer.confirm('确定删除这个申请吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/deleteApply", {
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
        } else if(layEvent === 'pass') {
            axios.post("/admin/checkApplyState", {
                taskId: data.courseId
            }).then(function (res) {
                if (!res.data.success) {
                    layer.msg(res.data.errorMsg);
                    return;
                }
                stateData = { id : data.id, userId: data.userId, taskId: data.courseId, state : 1 };
                if (res.data.data != null){
                    layer.confirm('该课程已有老师：' + res.data.data + '，确定更换吗 ', {icon: 3, title: '提示信息'}, function (index) {
                        axios.post("/admin/saveApplyState", stateData).then(function (res) {
                            if (!res.data.success) {
                                layer.msg(res.data.errorMsg);
                                return;
                            }
                            layer.close(index);
                            tableIns.reload();
                        });
                    })
                    return;
                }
                axios.post("/admin/saveApplyState", stateData).then(function (res) {
                    if (!res.data.success) {
                        layer.msg(res.data.errorMsg);
                        return;
                    }
                    tableIns.reload();
                });
            });
        } else {
            stateData = { id : data.id, userId: data.userId, taskId: data.courseId, state : 2 };
            axios.post("/admin/saveApplyState", stateData).then(function (res) {
                if (!res.data.success) {
                    layer.msg(res.data.errorMsg);
                    return;
                }
                tableIns.reload();
            });
        }
    });

    $(".search_btn").on("click", function () {
        if ($(".searchVal").val() != '') {
            tableIns.reload({
                url: '/admin/searchApply',
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
                    limit: this.limit,
                    fcId: $("#facultyList").val(),
                    termId: $("#termList").val()
                }
            })
        } else {
            layer.msg("请输入搜索的内容");
        }
    });

    // 批量删除
    $(".delAll_btn").click(function () {
        var checkStatus = table.checkStatus('applyListTable'),
            data = checkStatus.data,
            ids = [];
        if (data.length > 0) {
            for (var i in data) {
                ids.push(data[i].id);
            }
            layer.confirm('确定删除选中的申请吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/batchDeleteApplies", ids)
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
            layer.msg("请选择需要删除的申请");
        }
    })

    form.on('select(facultySelect)', function (d) {
        if (d.value === ''){
            return;
        }
        $("#operaButton").show();
        termListRender(d.value);
        var url = "/admin/applyList/" + d.value;
        // 申请列表
        renderFaculty(url);
    })

    form.on('select(termSelect)', function (d) {
        var url;
        if (d.value === '') {
            url = "/admin/applyList/" + $("#facultyList").val();
            renderFaculty(url);
            return;
        } else if(d.value === '' && $("#facultyList").val() === "") {
            return;
        } else {
            url = "/admin/applyList/" + $("#facultyList").val() + "/" + d.value;
        }
        renderTerm(url);
    })

    function renderFaculty(url) {
        tableIns = table.render({
            elem: '#applyListTable',
            url: url,
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
                    {field: 'courseId', title: 'ID', hide: true},
                    {field: 'facultyId', title: 'ID', hide: true},
                    {field: 'termId', title: 'ID', hide: true},
                    {field: 'termName', title: '学期', align: "center"},
                    {field: 'name', title: '申请人', align: "center"},
                    {field: 'reason', title: '申请理由', align: "center"},
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
                    {field: 'state', title:'申请状态', width:120, fixed: "right", align: "center",  templet: function (d) {
                        if (d.state == 0) {
                            return "<h4>新申请</h4>";
                        } else if (d.state == 1){
                            return "<h4 style='color: #01AAED'>已通过</h4>";
                        } else {
                            return "<h4 style='color: red'>未通过</h4>";
                        }
                    }},
                    {title: '操作', width: 180, toolbar: '#applyListBar', fixed: "right", align: "center"}
                ]
            ]
        });
        form.render();
    }
    
    function renderTerm(url) {
        table.render({
            elem: '#applyListTable',
            url: url,
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
                    {field: 'courseId', title: 'ID', hide: true},
                    {field: 'facultyId', title: 'ID', hide: true},
                    {field: 'termId', title: 'ID', hide: true},
                    {field: 'name', title: '申请人', align: "center"},
                    {field: 'reason', title: '申请理由', align: "center"},
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
                    {field: 'state', title:'申请状态', width:120, fixed: "right", align: "center",  templet: function (d) {
                        if (d.state == 0) {
                            return "<h4>新申请</h4>";
                        } else if (d.state == 1){
                            return "<h4 style='color: #01AAED'>已通过</h4>";
                        } else {
                            return "<h4 style='color: red'>未通过</h4>";
                        }
                    }},
                    {title: '操作', width: 180, toolbar: '#applyListBar', fixed: "right", align: "center"}
                ]
            ]
        });
        form.render();
    }
    
    function termListRender(fcId) {
        var url = '/admin/termList/' + fcId;
        axios.get(url).then(
            function (res) {
                if (!res.data.success) {
                    layer.msg(res.data.errorMsg);
                    return;
                }
                var dataArr = res.data.data.data;
                $("#termList").empty();
                $("#termList").append('<option value="">请选择学年</option>');
                for (var i=0;i<dataArr.length;i++){
                    $("#termList").append('<option value="' + dataArr[i].id + '">&nbsp;' + dataArr[i].name + '</option>');
                }
                form.render();
            }
        );
    }


})
