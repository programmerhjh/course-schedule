/**
 * Created by acer on 2020/2/18.
 */

layui.use(['form','layer','laydate','table','laytpl'],function(){
    var form = layui.form,
        layer = parent.layer === undefined ? layui.layer : top.layer,
        $ = layui.jquery,
        laydate = layui.laydate,
        laytpl = layui.laytpl,
        table = layui.table;

    $("#umg-bc-t").on('click',function () {
        layer.open({
            title: '批量生成老师账号',
            btn: ['导出到Excel'],
            content: '<input type="text" class="layui-input account" id="num" lay-verify="required|number" placeholder="请输入生成数目(最多200)"><br><input type="password" id="password" class="layui-input account" lay-verify="required" placeholder="请输入默认密码">',
            yes: function(index, layero){
                axios({
                    method: 'post',
                    url: "/admin/createAndDownloadUsersExcel",
                    data: {
                        privilege : '0',
                        num: $("#num").val(),
                        password: $("#password").val()
                    },
                    responseType: 'blob'
                }).then(
                    function(res){
                        var blob = new Blob([res.data],{type: res.headers['content-type']});
                        var downloadElement = document.createElement('a');
                        var href = window.URL.createObjectURL(blob); //创建下载的链接
                        downloadElement.href = href;
                        downloadElement.download = '创建老师用户信息表.xlsx'; //下载后文件名
                        document.body.appendChild(downloadElement);
                        downloadElement.click(); //点击下载
                        document.body.removeChild(downloadElement); //下载完成移除元素
                        window.URL.revokeObjectURL(href); //释放掉blob对象
                    }
                );
                layer.close(index);
            }
        });
    })

    $("#umg-bc-dm").on('click',function () {
        var options = '';
        axios.post("/admin/getFacultyData",{}).then(function (res) {
            var dataArr = res.data.data;
            for (var i=0;i<dataArr.length;i++){
                options += '<option value="' + dataArr[i].id + '">&nbsp;' + dataArr[i].name + '</option>'
            }
            layer.open({
                title: '批量生成院系负责人账号',
                btn: ['导出到Excel'],
                content: '<input type="text" class="layui-input account" id="num" lay-verify="required|number" placeholder="请输入生成数目(最多200)"><select style="width: 220px;height: 38px;" class="layui-select-none" lay-search id="createdm"><option value="">&nbsp;请选择</option>' + options + '</select><br><input type="password" id="password" class="layui-input account" lay-verify="required" placeholder="请输入默认密码">',
                yes: function(index, layero){
                    axios({
                        method: 'post',
                        url: "/admin/createAndDownloadUsersExcel",
                        data: {
                            faculty: parseInt($('.faculty select').val()) === NaN ? null : parseInt($('.faculty select').val()),
                            privilege : '0,1',
                            num: $("#num").val(),
                            password: $("#password").val()
                        },
                        responseType: 'blob'
                    }).then(
                        function(res){
                            var blob = new Blob([res.data],{type: res.headers['content-type']});
                            var downloadElement = document.createElement('a');
                            var href = window.URL.createObjectURL(blob); //创建下载的链接
                            downloadElement.href = href;
                            downloadElement.download = '创建院系负责人用户信息表.xlsx'; //下载后文件名
                            document.body.appendChild(downloadElement);
                            downloadElement.click(); //点击下载
                            document.body.removeChild(downloadElement); //下载完成移除元素
                            window.URL.revokeObjectURL(href); //释放掉blob对象
                        }
                    );
                    layer.close(index);
                }
            });
        })
    })

    $("#excel_operator").hide();

    $("#excel-export").on('click', function () {
        layer.open({
            title: "导出课表",
            type: 2,
            content: "/admin/excelExportUI",
            area: ['430px', '230px'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
            }
        })
    })

    $("#excel-import").on('click', function () {
        layer.open({
            title: "导入课表",
            type: 2,
            content: "/admin/excelImportUI",
            area: ['430px', '270px'],
            success: function (layero, index) {
                var body = layui.layer.getChildFrame('body', index);
            }
        })
    })
})
