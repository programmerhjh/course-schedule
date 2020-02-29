package com.neusoft.course.schedule.handler.exceptions;

import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/17.
 */
@ControllerAdvice
@Slf4j
public class CourseScheduleExceptionHandler {

    @ExceptionHandler
    @ResponseBody
    public JsonResult handler(Exception e){
        log.error("error: " + e.getMessage());
        return ResultGeneratorUtils.fail(e.getMessage());
    }

}
