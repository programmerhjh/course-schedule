package com.neusoft.course.schedule.config;

import com.neusoft.course.schedule.handler.CustomizeLogoutSuccessHandler;
import com.neusoft.course.schedule.handler.LoginAuthenticationFailureHandler;
import com.neusoft.course.schedule.handler.LoginAuthenticationSuccessHandler;
import com.neusoft.course.schedule.handler.entrypoint.CustomizeAuthenticationEntryPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/3.
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private CustomizeAuthenticationEntryPoint authenticationEntryPoint;

    @Autowired
    private LoginAuthenticationSuccessHandler loginAuthenticationSuccessHandler;

    @Autowired
    private LoginAuthenticationFailureHandler loginAuthenticationFailureHandler;

    @Autowired
    private CustomizeLogoutSuccessHandler logoutSuccessHandler;

    @Autowired
    @Qualifier("userDetailsServiceImpl")
    private UserDetailsService userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        // 设置默认的加密方式（强hash方式加密）
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(WebSecurity web) throws Exception {
        //解决静态资源被拦截的问题
        web.ignoring().antMatchers("/static/**");
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        //http相关的配置，包括登入登出、异常处理、会话管理等
        http.cors()
            .and().csrf().disable().userDetailsService(userDetailsService).headers().frameOptions().sameOrigin()
            .and().formLogin()
                .loginPage("/user/loginUI") //如果需要身份认证则跳转到这里
                .loginProcessingUrl("/user/login").usernameParameter("username").passwordParameter("password")
                .successHandler(loginAuthenticationSuccessHandler)
                .failureHandler(loginAuthenticationFailureHandler)
            .and().logout().logoutUrl("/user/logout").permitAll()
                .logoutSuccessHandler(logoutSuccessHandler).deleteCookies("JSESSIONID")
            .and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint) //匿名用户访问无权限资源时的异常处理
            .and().authorizeRequests()
                .antMatchers("/user/loginUI", "/user/logout", "/user/checkAccountWithEmail", "/user/updateNewPassword", "/user/completeUserInfoUI", "/admin/saveUser")
                .permitAll().anyRequest().authenticated(); //不校验我们配置的登录页面
    }

}
