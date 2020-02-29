package com.neusoft.course.schedule.handler;

import com.alibaba.fastjson.JSON;
import com.neusoft.course.schedule.service.UserService;
import com.neusoft.course.schedule.utils.AuthorityStringUtils;
import com.neusoft.course.schedule.utils.JsonResult;
import com.neusoft.course.schedule.utils.ResultGeneratorUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/5.
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    UserService userService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.neusoft.course.schedule.entity.User sysUser = new com.neusoft.course.schedule.entity.User();
        sysUser.setAccount(user.getUsername());
        sysUser.setLastlogin(new Date());
        userService.updateUserLastLogin(sysUser);
        ArrayList<String> privileges = AuthorityStringUtils.generatorAuthoritiesString(user.getAuthorities());
        httpServletRequest.getSession().setAttribute("user", user);
        httpServletRequest.getSession().setAttribute("privilege", privileges);
        httpServletResponse.setContentType("text/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultGeneratorUtils.success()));
    }

}
