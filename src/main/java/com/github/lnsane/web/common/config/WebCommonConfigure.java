package com.github.lnsane.web.common.config;

import org.aopalliance.aop.Advice;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import java.util.List;

/**
 * @author lnsane
 */
@ConfigurationProperties(prefix = "web.common.config")
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
}
