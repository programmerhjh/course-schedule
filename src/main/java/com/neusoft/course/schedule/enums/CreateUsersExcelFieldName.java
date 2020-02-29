package com.neusoft.course.schedule.enums;

import lombok.*;

/**
 * 批量创建用户的EXCEL 中可展示字段
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/19.
 */
@Getter
public enum CreateUsersExcelFieldName{

    UC_ACCOUNT("uc_account", "账号"),
    UC_PASSWORD("uc_password", "密码")
    ;

    private String fieldName;
    private String detail;

    CreateUsersExcelFieldName(String fieldName, String detail) {
        this.fieldName = fieldName;
        this.detail = detail;
    }

    /**
     * 根据code获取message
     * @param fieldName
     * @return
     */
    public static String getDetailByFieldName(String fieldName) {
        for (CreateUsersExcelFieldName ele : values()) {
            if (ele.getFieldName().equals(fieldName)) {
                return ele.getDetail();
            }
        }
        return null;
    }
}
