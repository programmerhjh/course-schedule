/**
 * Created by acer on 2020/2/20.
 */
layui.use('table', function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    var tableIns = table.render({
        elem: '#facultyTermListTable',
        url:'/admin/facultyList',
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
                }
            ]
        ]
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

    //监听行单击事件（双击事件为：rowDouble）
    table.on('row(facultyTermListTable)', function(obj){
        //标注选中样式
        obj.tr.addClass('layui-table-click').siblings().removeClass('layui-table-click');
        var data = obj.data;
        layui.layer.open({
            title: data.name + " --- 学年管理页",
            type: 2,
            content: "/admin/termListUI",
            shade: 0,
            area: ['1400px', '700px'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
                body.find(".fcId").val(data.id);
                body.find("#facultyName").val(data.name);
            }
        })

    });

});