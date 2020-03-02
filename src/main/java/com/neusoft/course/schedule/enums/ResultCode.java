package com.neusoft.course.schedule.enums;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 * @Description: 返回码定义
 * 规定:
 * #1 表示成功
 * #1001～1999 区间表示参数错误
 * #2001～2999 区间表示用户错误
 * #3001～3999 区间表示接口异常
 */
public enum ResultCode {

    /* 成功 */
    SUCCESS(200, "成功"),
    UPDATE_PASSWORD_SUCCESS(200, "密码修改成功"),

    /* 默认失败 */
    COMMON_FAIL(999, "系统异常。请稍后再试"),
    /* 初始账号时，教师忘记密码但没有邮箱不能找回；有邮箱数据想找回密码，但忘了邮箱账号 */
    FORGET_EMAIL(888, "请联系管理员重置密码"),

    /* 参数错误：1000～1999 */
    PARAM_NOT_VALID(1001, "参数无效"),
    PARAM_IS_BLANK(1002, "参数为空"),
    PARAM_TYPE_ERROR(1003, "参数类型错误"),
    PARAM_NOT_COMPLETE(1004, "参数缺失"),

    /* 用户错误 */
    USER_NOT_LOGIN(2001, "用户未登录"),
    USER_ACCOUNT_EXPIRED(2002, "账号已过期"),
    USER_CREDENTIALS_ERROR(2003, "密码错误"),
    USER_CREDENTIALS_EXPIRED(2004, "密码过期"),
    USER_ACCOUNT_DISABLE(2005, "账号不可用"),
    USER_ACCOUNT_LOCKED(2006, "账号被锁定"),
    USER_ACCOUNT_NOT_EXIST(2007, "账号不存在"),
    USER_ACCOUNT_ALREADY_EXIST(2008, "账号已存在"),
    USER_ACCOUNT_USE_BY_OTHERS(2009, "账号下线"),
    USER_ACCOUNT_OR_CREDENTIALS_ERROR(2010, "账号或密码错误"),
    USER_EMAIL_ERROR(2011, "邮箱错误"),
    USER_NAME_ALREADY_EXIST(2012, "用户名已存在"),
    USER_NAME_ACCOUNT_REPEAT(2013, "用户名和账号重复"),

    FACULTY_NAME_ALREADY_EXIST(2014, "院系名已存在"),
    TERM_NAME_ALREADY_EXIST_THIS_FACULTY(2015, "学年名已在该院系下存在"),
    USER_INFO_NOT_COMPLETE(2016, "账户信息未完善"),
    EXCEL_SHEET_SIZE_ERROR(2018, "密码修改成功"),

    TERM_UNOPEN_APPLY(2019, "该学期尚未开放申请"),

    /* 业务错误 */
    NO_PERMISSION(3001, "没有权限");

    private Integer code;
    private String message;

    ResultCode(Integer code, String message) {
        this.code = code;
        this.message = message;
    }

    /**
     * 根据code获取message
     * @param code
     * @return
     */
    public static String getMessageByCode(Integer code) {
        for (ResultCode ele : values()) {
            if (ele.getCode().equals(code)) {
                return ele.getMessage();
            }
        }
        return null;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
