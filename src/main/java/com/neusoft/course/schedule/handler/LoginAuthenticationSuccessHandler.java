package com.neusoft.course.schedule.handler;

import com.alibaba.fastjson.JSON;
import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.service.FacultyService;
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
import org.springframework.util.ObjectUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/5.
 */
@Component
public class LoginAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Autowired
    private UserService userService;

    @Autowired
    private FacultyService facultyService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        com.neusoft.course.schedule.entity.User sysUser = new com.neusoft.course.schedule.entity.User();
        sysUser.setAccount(user.getUsername());
        sysUser.setLastlogin(new Date());
        userService.updateUserLastLogin(sysUser);
        sysUser = userService.selectUserPasswordByAccount(user.getUsername());
        String fcName = facultyService.getFacultyDataById(sysUser.getFaculty()).getName();
        ArrayList<String> privileges = AuthorityStringUtils.generatorAuthoritiesString(user.getAuthorities());
        httpServletRequest.getSession().setAttribute("user", user);
        httpServletRequest.getSession().setAttribute("privilege", privileges);
        if (!privileges.contains("2") && !ObjectUtils.isEmpty(sysUser.getFaculty())){
            httpServletRequest.getSession().setAttribute("fcId", sysUser.getFaculty());
            httpServletRequest.getSession().setAttribute("fcName", fcName);
            httpServletRequest.getSession().setAttribute("userId", sysUser.getId());
        }
        httpServletResponse.setContentType("application/json;charset=utf-8");
        httpServletResponse.getWriter().write(JSON.toJSONString(ResultGeneratorUtils.success()));
    }

}
