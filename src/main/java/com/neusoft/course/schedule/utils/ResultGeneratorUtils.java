package com.neusoft.course.schedule.utils;

import com.neusoft.course.schedule.enums.ResultCode;

/**
 * 返回体构造工具
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
public class ResultGeneratorUtils {

    public static JsonResult success() {
        return new JsonResult(true);
    }

    public static <T> JsonResult<T> success(T data) {
        return new JsonResult(true, data);
    }

    public static JsonResult fail() {
        return new JsonResult(false);
    }

    public static JsonResult fail(String errorMsg) {
        return new JsonResult(false, errorMsg);
    }

    public static JsonResult fail(ResultCode resultEnum) {
        return new JsonResult(false, resultEnum);
    }

}