package com.neusoft.course.schedule.service.impl;

import com.neusoft.course.schedule.constants.ServiceConstants;
import com.neusoft.course.schedule.enums.ResultCode;
import com.neusoft.course.schedule.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * 用户登录认证逻辑
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException(ResultCode.PARAM_IS_BLANK.getMessage());
        }
        //根据用户名查询用户
        com.neusoft.course.schedule.entity.User user = userService.selectUserPasswordByAccount(username);
        if (user == null) {
            throw new BadCredentialsException(ResultCode.USER_ACCOUNT_NOT_EXIST.getMessage());
        }
        List<GrantedAuthority> grantedAuthorities = new ArrayList<>();
        if (user != null) {
            //获取该用户所拥有的权限
            String[] privilege = user.getPrivilege().split(",");
            // 声明用户授权
            for (String p :privilege) {
                GrantedAuthority grantedAuthority = new SimpleGrantedAuthority(ServiceConstants.PERMISSION_ROLE_PRE + p);
                grantedAuthorities.add(grantedAuthority);
            }
        }
        if (user.getDelete() == 1){
            if (StringUtils.isEmpty(user.getName()) || username.equals(user.getAccount())){
                return new User(user.getAccount(), user.getPassword(), true, true, true, false, grantedAuthorities);
            }
            return new User(user.getName(), user.getPassword(), true, true, true, false, grantedAuthorities);
        }
        if (StringUtils.isEmpty(user.getName()) || username.equals(user.getAccount())){
            return new User(user.getAccount(), user.getPassword(), true, true, true, true, grantedAuthorities);
        }
        return new User(user.getName(), user.getPassword(), true, true, true, true, grantedAuthorities);
    }




}
