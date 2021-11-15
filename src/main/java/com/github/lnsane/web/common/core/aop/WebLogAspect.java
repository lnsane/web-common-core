package com.github.lnsane.web.common.core.aop;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.json.JSONUtil;
import com.github.lnsane.web.common.content.PreRequestHandle;
import com.github.lnsane.web.common.content.RequestContext;
import com.github.lnsane.web.common.model.BaseResponse;
import com.github.lnsane.web.common.model.ExceptionFlag;
import com.github.lnsane.web.common.model.RequestSupport;
import com.github.lnsane.web.common.model.WebLog;
import com.github.lnsane.web.common.utils.ServletUtils;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author lnsane
 */
@Aspect
@Component
@Order(1)
public class WebLogAspect {
    private static final Logger log = LoggerFactory.getLogger(WebLogAspect.class);

    @Pointcut("@within(org.springframework.web.bind.annotation.RestController)")
    public void restController() {
    }

    @Pointcut("@within(org.springframework.stereotype.Controller)")
    public void controller() {
    }

    @AfterThrowing(throwing = "e", pointcut = "controller() || restController()")
    public void exceptionDeal(JoinPoint joinPoint, RuntimeException e) {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        Signature signature = joinPoint.getSignature();
        //记录请求信息(通过Logstash传入Elasticsearch)
        WebLog webLog = new WebLog();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(log.value());
        }
        String urlStr = request.getRequestURL().toString();
        webLog.setBasePath(urlStr);
        webLog.setClazzMethodName(methodSignature.getMethod().getName());
        webLog.setClazzName(methodSignature.getMethod().getDeclaringClass().getName());
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(resolveRequestBody(method, joinPoint.getArgs()));
        RequestSupport requestSupport = RequestContext.get();
        if (ObjectUtil.isNotNull(requestSupport)) {
            webLog.setTrackId(requestSupport.getTrackId());
        }
        ExceptionFlag exceptionFlag = PreRequestHandle.get();
        webLog.setIp(ServletUtils.getClientIP(request));
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(this.resolveRequestParam(urlStr, method, joinPoint.getArgs()));
        log.info("请求追踪ID: {}-{}\n" + "请求全类名: {}\n" + "请求类方法名: {}\n" + "请求IP: {}\n" +
                        "请求BaseUrl: {} \n请求Uri: {}\n请求Url: {}\n请求方法: {}\n请求参数: {}\n发生的时间: {}\n请求耗时: {}毫秒",
                webLog.getTrackId(),
                webLog.getTrackId() != null ? requestSupport.getTrackIdIndex() : null,
                webLog.getClazzName(),
                webLog.getClazzMethodName(),
                webLog.getIp(),
                webLog.getBasePath(),
                webLog.getUri(),
                webLog.getUrl(),
                webLog.getMethod(),
                new GsonBuilder().serializeNulls().create().toJson(webLog.getParameter()),
                DateUtil.format(DateTime.of(exceptionFlag.getStartTime()), DatePattern.NORM_DATETIME_MS_FORMATTER),
                System.currentTimeMillis() - exceptionFlag.getStartTime());
    }


    @Around("controller() || restController()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息(通过Logstash传入Elasticsearch)
        WebLog webLog = new WebLog();
        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature =  (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(log.value());
        }
        long endTime = System.currentTimeMillis();
        String urlStr = request.getRequestURL().toString();
        webLog.setBasePath(urlStr);
        webLog.setClazzMethodName(methodSignature.getMethod().getName());
        webLog.setClazzName(methodSignature.getMethod().getDeclaringClass().getName());
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(resolveRequestBody(method, joinPoint.getArgs()));
        webLog.setResult(result);
        BaseResponse<?> commonResult = null;
        if (result instanceof BaseResponse) {
            commonResult = (BaseResponse<?>) result;
            commonResult.setSpeedTime(endTime - startTime);
        }
        RequestSupport requestSupport = RequestContext.get();
        if (ObjectUtil.isNotNull(requestSupport)) {
            webLog.setTrackId(requestSupport.getTrackId());
        }
        webLog.setSpendTime((int) (endTime - startTime));
        webLog.setStartTime(startTime);
        webLog.setIp(ServletUtils.getClientIP(request));
        webLog.setUri(request.getRequestURI());
        webLog.setUrl(this.resolveRequestParam(urlStr, method, joinPoint.getArgs()));
        Map<String, Object> logMap = new HashMap<>();
        logMap.put("url", webLog.getUrl());
        logMap.put("method", webLog.getMethod());
        logMap.put("parameter", webLog.getParameter());
        logMap.put("spendTime", webLog.getSpendTime());
        logMap.put("description", webLog.getDescription());
//        LOGGER.info("{}", JSONUtil.parse(webLog));
        log.info("请求追踪ID: {}-{}\n" + "请求全类名: {}\n" + "请求类方法名: {}\n" + "请求IP: {}\n" +
                        "请求BaseUrl: {} \n请求Uri: {}\n请求Url: {}\n请求方法: {}\n请求参数: {}\n响应结果: {}\n发生的时间: {}\n请求耗时: {}毫秒",
                webLog.getTrackId(),
                webLog.getTrackId() != null ? requestSupport.getTrackIdIndex() : null,
                webLog.getClazzName(),
                webLog.getClazzMethodName(),
                webLog.getIp(),
                webLog.getBasePath(),
                webLog.getUri(),
                webLog.getUrl(),
                webLog.getMethod(),
                new GsonBuilder().serializeNulls().create().toJson(webLog.getParameter()),
                new GsonBuilder().serializeNulls().create().toJson(webLog.getResult()),
                DateUtil.format(DateTime.of(webLog.getStartTime()), DatePattern.NORM_DATETIME_MS_FORMATTER),
                webLog.getSpendTime());
        log.debug(JSONUtil.parse(webLog).toString());
//        log.info(JSONUtil.parse(webLog).toString());
        if (result instanceof BaseResponse) {
            return commonResult;
        }
        return result;
    }

    /**
     * 根据方法和传入的参数获取请求参数
     */
    private Object resolveRequestBody(Method method, Object[] args) {
        List<Object> argList = new ArrayList<>();
        Parameter[] parameters = method.getParameters();
        for (int i = 0; i < parameters.length; i++) {
            //将RequestBody注解修饰的参数作为请求参数
            RequestBody requestBody = parameters[i].getAnnotation(RequestBody.class);
            if (requestBody != null) {
                argList.add(args[i]);
            }

        }
        if (argList.size() == 0) {
            return null;
        } else if (argList.size() == 1) {
            return argList.get(0);
        } else {
            return argList;
        }
    }

    private String resolveRequestParam(String baseUrl, Method method, Object[] args) {
        StringBuilder sb = new StringBuilder(baseUrl);
        Parameter[] parameters = method.getParameters();
        //将RequestParam注解修饰的参数作为请求参数
        for (int i = 0; i < parameters.length; i++) {
            RequestParam requestParam = parameters[i].getAnnotation(RequestParam.class);
            if (requestParam != null) {
                if (i == 0) {
                    sb.append("?");
                }
                String key = parameters[i].getName();
                if (StringUtils.hasText(requestParam.value())) {
                    key = requestParam.value();
                }
                sb.append(key).append("=").append(args[i].toString());
                if (i != parameters.length - 1) {
                    sb.append("&");
                }
            }
        }
        return sb.toString();
    }
}
