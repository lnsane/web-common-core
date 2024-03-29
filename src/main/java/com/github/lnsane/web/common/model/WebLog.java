package com.github.lnsane.web.common.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author lnsane
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class WebLog {

    /**
     * 追踪Id
     */
    private String trackId;
    /**
     * 请求类名
     */
    private String clazzName;

    /**
     * 请求类方法名
     */
    private String clazzMethodName;
    /**
     * 操作描述
     */
    private String description;

    /**
     * 请求令牌
     */
    private String token;

    /**
     * 操作用户
     */
    private String username;

    /**
     * 操作时间
     */
    private Long startTime;

    /**
     * 消耗时间
     */
    private Integer spendTime;

    /**
     * 根路径
     */
    private String basePath;

    /**
     * URI
     */
    private String uri;

    /**
     * URL
     */
    private String url;

    /**
     * 请求类型
     */
    private String method;

    /**
     * IP地址
     */
    private String ip;

    /**
     * 请求参数
     */
    private Object parameter;

    /**
     * 返回结果
     */
    private Object result;
}
