package com.neusoft.course.schedule.handler;

import com.alibaba.fastjson.JSON;
import com.neusoft.course.schedule.entity.User;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.service.UserService;
import com.neusoft.course.schedule.utils.AuthorityStringUtils;
import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/5.
 */
@Component
public class LoginAuthenticationFailureHandler implements AuthenticationFailureHandler {

    @Autowired
    private UserService userService;

    @Override
    public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        //返回json数据
        JsonResult result;
        if (e instanceof BadCredentialsException || e instanceof InternalAuthenticationServiceException) {
            //密码错误
            result = ResultGeneratorUtils.fail(ResultCode.USER_ACCOUNT_OR_CREDENTIALS_ERROR);
        } else if (e instanceof LockedException) {
            //账号不可用
            result = ResultGeneratorUtils.fail(ResultCode.USER_ACCOUNT_DISABLE);
        } else if (e instanceof AccountExpiredException) {
            String username = httpServletRequest.getParameter("username");
            User user = userService.selectUserPasswordByAccount(username);
            HashMap<String, String> map = new HashMap<String, String>(3){{
                put("url", "/user/completeUserInfoUI");
                put("id", user.getId().toString());
                put("account", user.getAccount().toString());
            }};
            //账户信息未完善
            result = ResultGeneratorUtils.success(map);
        } else {
            //其他错误
            result = ResultGeneratorUtils.fail(ResultCode.COMMON_FAIL);
        }
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(result));
    }

}
