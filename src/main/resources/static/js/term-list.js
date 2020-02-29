/**
 * Created by acer on 2020/2/20.
 */
axios.defaults.headers.post['Content-Type'] = 'application/json; charset=utf-8';
layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    var tableIns;
    $(document).ready(function () {
        var url = '/admin/termList/' + $(".fcId").val();
        // 学年列表
        tableIns = table.render({
            elem: '#termListTable',
            url : url,
            method : 'post',
            contentType : 'application/json',
            parseData: function(res){
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
                    {field: 'name', title: '学年名称', align:"center"},
                    {field: 'createTime', title: '创建时间', align:'center', templet:function(d){
                        if (d.createTime){
                            return layui.util.toDateString(d.createTime, 'yyyy年MM月dd日 HH:mm:ss');
                        }
                        return '无数据';
                    }},
                    {field: 'modifyTime', title: '最后修改时间', align:'center', templet:function(d){
                        if (d.modifyTime){
                            return layui.util.toDateString(d.modifyTime);
                        }
                        return '无数据';
                    }},
                    {title: '操作', width:170, toolbar:'#termListBar',fixed:"right",align:"center"}
                ]
            ]
        });
    })

    //列表操作
    table.on("tool(termListTable)", function(obj){
        var layEvent = obj.event,
            data = obj.data;
        if(layEvent === 'edit'){ //编辑
            addTerm(data);
        } else if(layEvent === 'del'){ //删除
            layer.confirm('确定删除这个学年吗？',{icon:3, title:'提示信息'},function(index){
                axios.post("/admin/deleteTerm",{
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
        }
    });

    $(".search_btn").on("click",function(){
        if($(".searchVal").val() != ''){
            tableIns.reload({
                url: '/admin/searchTerm',
                method: 'post',
                contentType: "application/json; charset=utf-8",
                parseData: function(res){
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
                    fcId: $(".fcId").val() === undefined ? null : $(".fcId").val()
                }
            })
        }else{
            layer.msg("请输入搜索的内容");
        }
    });

    // 添加学年
    function addTerm(edit){
        layui.layer.open({
            title : "学年信息操作页",
            type : 2,
            content : "/admin/termAddUI",
            area: ['500px', '300px'],
            success : function(layero, index){
                var body = layui.layer.getChildFrame('body', index);
                body.find(".fcId").val($(".fcId").val());
                if(edit){
                    body.find(".id").val(edit.id);
                    body.find(".name").val(edit.name);
                }
            }
        })
    }

    $(".addTerm_btn").click(function(){
        addTerm();
    })

    //批量删除
    $(".delAll_btn").click(function(){
        var checkStatus = table.checkStatus('termListTable'),
            data = checkStatus.data,
            ids = [];
        if(data.length > 0) {
            for (var i in data) {
                ids.push(data[i].id);
            }
            layer.confirm('确定删除选中的学年吗？', {icon: 3, title: '提示信息'}, function (index) {
                axios.post("/admin/batchDeleteTerms",ids)
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
            layer.msg("请选择需要删除的学年");
        }
    })

})
