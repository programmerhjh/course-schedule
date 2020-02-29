package com.neusoft.course.schedule.constants;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务常量
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/4.
 */
public class ServiceConstants {

    /**
     * 权限名称前缀
     */
    public static final String PERMISSION_ROLE_PRE = "ROLE_";

    /**
     * 默认注册密码
     */
    public static final String DEFAULT_PASSWORD = "123456";

    /**
     * 老师权限
     */
    public static final String PRIVILEGE_TEACHER = "ROLE_0";

    /**
     * 系部负责人权限
     */
    public static final String PRIVILEGE_DEPARTMENT_MAN = "ROLE_1";

    /**
     * 管理员权限
     */
    public static final String PRIVILEGE_ADMIN = "ROLE_2";

    /**
     * 申请审批不通过代码
     */
    public static final Integer APPLY_STATE_FAIL = 2;

    /**
     * 申请审批不通过代码
     */
    public static final Integer APPLY_STATE_PASS = 1;

    /**
     * 创建用户账号密码表
     */
    public static final String CREATE_USER_FORM = "创建用户账号密码表";

    /**
     * 模板文件地址
     */
    public static final String TEMPLATE_FILE_ADDRESS = "static/course_template.xls";

    /**
     * 临时作为交换文件的路径
     */
    public static final String TEMP_SAVE_FILE_ADDRESS = "C:\\Users\\acer\\Desktop\\毕设\\项目代码\\src\\main\\resources\\TempUploadTransferFile";

    /**
     * Excel 中没有数据
     */
    public static final String EXCEL_HAVE_NOTHING_DATA = "Excel 表中没有数据";

    /**
     * 写死的角色数据
     */
    public static final List<Role> ROLE_LIST = new ArrayList<Role>(3){
        {add(new Role("0", "老师"));}
        {add(new Role("0,1", "院系部负责人"));}
        {add(new Role("0,1,2", "管理员"));}
    };

    /**
     * 角色类
     */
    @Data
    @AllArgsConstructor
    public static final class Role{

        private String privilegeCode;

        private String privilegeName;

    }
}
