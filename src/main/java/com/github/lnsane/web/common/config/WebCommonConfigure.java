package com.github.lnsane.web.common.config;

import com.github.lnsane.web.common.core.aop.WebLogAspect;
import com.github.lnsane.web.common.core.exception.ControllerExceptionHandler;
import feign.Client;
import feign.RequestInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author lnsane
 */
@ConfigurationProperties(prefix = "web.common.config")
@Configuration
public class WebCommonConfigure {
    private List<String> restControllerAdviceExceptionList;

    public Boolean getRestControllerAdviceExceptionEnable() {
        return restControllerAdviceExceptionEnable;
    }

    public void setRestControllerAdviceExceptionEnable(Boolean restControllerAdviceExceptionEnable) {
        this.restControllerAdviceExceptionEnable = restControllerAdviceExceptionEnable;
    }

    private Boolean restControllerAdviceExceptionEnable;

    public List<String> getRestControllerAdviceExceptionList() {
        return restControllerAdviceExceptionList;
    }

    public void setRestControllerAdviceExceptionList(List<String> restControllerAdviceExceptionList) {
        this.restControllerAdviceExceptionList = restControllerAdviceExceptionList;
    }

    @Bean
    public WebLogAspect webLogAspect() {
        return new WebLogAspect();
    }

    @Bean
    public ControllerExceptionHandler controllerExceptionHandler() {
        return new ControllerExceptionHandler();
    }

    @Bean
    public SpringMvcConfigure mvcConfigure() {
        return new SpringMvcConfigure();
    }

    @ConditionalOnClass(value = RequestInterceptor.class)
    protected static class DispatcherServletConfiguration {
        @Bean
        public FeignRequestInterceptor feignRequestInterceptor() {
            return new FeignRequestInterceptor();
        }
        @Bean
        public Client client() {
            return new CustomFeignClient(null, null);
        }
    }
}
