package com.github.lnsane.web.common.config;

import com.github.lnsane.web.common.interceptor.GlobalRequestInterceptor;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author lnsane
 */
@Component
public class SpringMvcConfigure implements WebMvcConfigurer {
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new GlobalRequestInterceptor());
    }
}
