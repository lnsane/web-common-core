package com.github.lnsane.web.common.core.aop;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateTime;
import cn.hutool.core.date.DateUtil;
import cn.hutool.json.JSONUtil;
import com.github.lnsane.web.common.model.BaseResponse;
import com.github.lnsane.web.common.model.WebLog;
import com.github.lnsane.web.common.utils.ServletUtils;
import com.google.gson.GsonBuilder;
import io.swagger.annotations.ApiOperation;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.Signature;
import org.aspectj.lang.annotation.*;
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

    @Pointcut
    public void webLog() {
    }

    @Before("webLog()")
    public void doBefore(JoinPoint joinPoint) throws Throwable {
    }

    @AfterReturning(value = "webLog()", returning = "ret")
    public void doAfterReturning(Object ret) throws Throwable {
    }

    @Around("webLog()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        //获取当前请求对象
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = attributes.getRequest();
        //记录请求信息(通过Logstash传入Elasticsearch)
        WebLog webLog = new WebLog();
        Object result = joinPoint.proceed();
        Signature signature = joinPoint.getSignature();
        MethodSignature methodSignature = (MethodSignature) signature;
        Method method = methodSignature.getMethod();
        if (method.isAnnotationPresent(ApiOperation.class)) {
            ApiOperation log = method.getAnnotation(ApiOperation.class);
            webLog.setDescription(log.value());
        }
        long endTime = System.currentTimeMillis();
        String urlStr = request.getRequestURL().toString();
        webLog.setBasePath(urlStr);
        webLog.setIp(request.getRemoteUser());
        webLog.setMethod(request.getMethod());
        webLog.setParameter(resolveRequestBody(method, joinPoint.getArgs()));
        webLog.setResult(result);
        BaseResponse<?> commonResult = null;
        if (result instanceof BaseResponse) {
            commonResult = (BaseResponse<?>) result;
            commonResult.setSpeedTime(endTime - startTime);
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
        log.info("请求IP:{},请求BaseUrl:{},请求Uri:{},请求Url:{},请求方法:{},请求参数:{},响应结果:{},发生的时间:{},请求耗时:{}毫秒",
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
                if (i == 0 ) {
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
