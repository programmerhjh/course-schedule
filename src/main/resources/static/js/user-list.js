axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;
    $("#umg").addClass("layui-nav-itemed");
    var extendField;
    //用户列表
    var tableIns = table.render({
        elem: '#userListTable',
        url : '/admin/userList',
        method : 'post',
        contentType : 'application/json',
        parseData: function(res){
            extendField = res.data.extendField;
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
        cellMinWidth : 95,
        page : true,
        height : "full",
        limit : 12,
        limits : [12,20,50],
        cols : [
            [
                {type: "checkbox", fixed:"left", width:50},
                {field: 'id', title: 'ID', width:60, align:"center"},
                {field: 'name', title: '用户名'},
                {field: 'account', title: '账户', width:200, align:'center'},
                {field: 'privilege', title: '角色', width:110, align:'center', templet: function (d) {
                    return d.privilege === '0,1,2' ? '管理员' : d.privilege === '0,1' ? '院系负责人' : '老师';
                }},
                {field: 'faculty', title: '院系', width:150, align:'center', templet:function (d) {
                    return d.faculty === null ? "无数据" : extendField[d.faculty];
                }},
                {field: 'delete', title: '是否禁用', align:'center', templet:function (d) {
                    return d.delete === 1 ? '<p style="color: red">已被禁用</p>' : '<p style="color: #85ce61">未被禁用</p>';
                }},
                {field: 'email', title: '邮箱', width:150, align:'center'},
                {field: 'complete', title: '是否完善数据', width:120, align:'center', templet:function (d) {
                    return d.complete === 0 ? '未完善' : '以完善';
                }},
                {field: 'lastlogin', title: '最后登录时间', width:200, align:'center', templet:function(d){
                    if (d.lastlogin){
                        return layui.util.toDateString(d.lastlogin, 'yyyy年MM月dd日 HH:mm:ss');
                    }
                    return '无数据';
                }},
                {field: 'createTime', title: '创建时间', align:'center', width:200, templet:function(d){
                    if (d.createTime){
                        return layui.util.toDateString(d.createTime, 'yyyy年MM月dd日 HH:mm:ss');
                    }
                    return '无数据';
                }},
                {field: 'modifyTime', title: '最后修改时间', align:'center', width:200, templet:function(d){
                    if (d.modifyTime){
                        return layui.util.toDateString(d.modifyTime, 'yyyy年MM月dd日 HH:mm:ss');
                    }
                    return '无数据';
                }},
                {title: '操作', width:170, toolbar:'#userListBar',fixed:"right",align:"center"}
            ]
        ]
    });

    //列表操作
    table.on("tool(userListTable)", function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
            addUser(data);
        } else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除该用户吗？',{icon:3, title:'提示信息'},function(index){
                axios.post("/admin/deleteUser",{
                    id : data.id
                }).then(
                    function(res){
                        if (!res.data.success){
                            layer.msg(res.data.errorMsg);
                            return;
                        }
                        tableIns.reload();
                        layer.close(index);
                    }
                );
            });
        } else if(layEvent === 'forbidden'){ //禁用
            layer.confirm('确定'+ (data.delete === 0 ? '禁用' : '解禁') + '该用户吗？',{icon:3, title:'提示信息'},function(index){
                axios.post("/admin/forbiddenUser",{
                    id : parseInt(data.id),
                    delete : data.delete === 0 ? 1 : 0
                }).then(
                    function(res){
                        if (!res.data.success){
                            layer.msg(res.data.errorMsg);
                            return;
                        }
                        tableIns.reload();
                        layer.close(index);
                    }
                );
            })
        }
    });

    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            tableIns.reload({
                url: '/admin/searchUser',
                method: 'post',
                contentType: "application/json; charset=utf-8",
                parseData: function(res){
                    extendField = res.data.extendField;
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
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    //添加用户
    function addUser(edit){
        layui.layer.open({
            title : "用户信息操作页",
            type : 2,
            content : "/admin/userAddUI",
            area: ['500px', '500px'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                if(edit){
                    body.find(".id").val(edit.id);
                    body.find(".name").val(edit.name);
                    body.find(".account").val(edit.account);
                    body.find(".privilege select").val(edit.privilege);
                    body.find(".faculty select").val(edit.faculty);
                    body.find(".delete input[name='delete']").prop("checked", edit.delete);
                    body.find(".email").val(edit.email);
                }
            }
        })
    }

    $(".addUser_btn").click(function(){
        addUser();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            ids = [];
        if(data.length > 0) {
            for (var i in data) {
                ids.push(data[i].id);
            }
            layer.confirm('确定删除选中的用户吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/batchDeleteUsers",ids)
                    .then(function(res){
                    if (!res.data.success){
                        layer.msg(res.data.errorMsg);
                        return;
                    }
                    tableIns.reload();
                    layer.close(index);
                });
            })
        }else{
            layer.msg("请选择需要删除的用户");
        }
    })

    //批量禁用
    $(".forbiddenAll_btn").click(function(){
        var checkStatus = table.checkStatus('userListTable'),
            data = checkStatus.data,
            userList = [];
        if(data.length > 0) {
            for (var i in data) {
                userList.push({id : data[i].id, delete : data[i].delete === 0 ? 1 : 0});
            }
            layer.confirm('选中用户原先禁用的将不被禁用，没有被禁用的则开启禁用，请确认', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/batchForbiddenUsers",userList).then(
                    function(res){
                        if (!res.data.success){
                            layer.msg(res.data.errorMsg);
                            return;
                        }
                        tableIns.reload();
                        layer.close(index);
                    }
                );
            })
        }else{
            layer.msg("请选择需要禁用/解禁的用户");
        }
    })

})


