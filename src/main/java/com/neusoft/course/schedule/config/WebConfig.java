package com.neusoft.course.schedule.config;

import com.neusoft.course.schedule.interceptor.RequestInterceptor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author 洪家豪
 * Created by 洪家豪 on 2020/2/16.
 */
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // addPathPatterns 用于添加拦截规则
        // excludePathPatterns 用户排除拦截
//        registry.addInterceptor(new RequestInterceptor()).addPathPatterns("/**").excludePathPatterns("/user/**").order(Integer.MAX_VALUE);
    }

}
