package com.github.lnsane.web.common.core.exception;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.github.lnsane.web.common.model.BaseResponse;
import com.github.lnsane.web.common.utils.ValidationUtils;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.util.Assert;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Map;

/**
 * @author lnsane
 */

//@ConditionalOnExpression("'${web.common.config.rest-controller-advice-exception-enable}'== true ")
@ControllerAdvice
@ResponseBody
public class ControllerExceptionHandler {

    Logger log = LoggerFactory.getLogger(ControllerExceptionHandler.class);


    /**
     * 全局异常处理，异常返回统一格式的Result
     *
     * @param e 异常
     */
    @ExceptionHandler(value = AbstractRuntimeException.class)
    public BaseResponse<?> abstractRuntimeException(AbstractRuntimeException e) {
        BaseResponse<JSONObject> baseResponse = handleBaseException(e);
        baseResponse.setMessage(e.getMessage());
        baseResponse.setStatus(e.getError().status());
        baseResponse.setTimestamp(e.timestamp());
        baseResponse.setData(JSONUtil.createObj());
        return baseResponse;
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public BaseResponse<?> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException e) {
        BaseResponse<JSONObject> baseResponse = handleBaseException(e);
        baseResponse.setStatus(BaseCodeEnums.BAD_REQUEST_EXCEPTION.status);
        baseResponse.setTimestamp(System.currentTimeMillis());
        baseResponse.setSpeedTime(0L);
        baseResponse.setMessage("缺失请求主体");
        baseResponse.setData(JSONUtil.createObj());
        return baseResponse;
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public BaseResponse<?> handleHttpRequestMethodNotSupportedException(
            HttpRequestMethodNotSupportedException e) {
        BaseResponse<JSONObject> baseResponse = handleBaseException(e);
        baseResponse.setStatus(BaseCodeEnums.BAD_REQUEST_EXCEPTION.status);
        baseResponse.setTimestamp(System.currentTimeMillis());
        baseResponse.setSpeedTime(0L);
        baseResponse.setMessage("接口不支持该方法请求");
        baseResponse.setData(JSONUtil.createObj());
        return baseResponse;
    }


    @ExceptionHandler(MissingServletRequestParameterException.class)
    public BaseResponse<?> handleMissingServletRequestParameterException(
            MissingServletRequestParameterException e) {
        BaseResponse<JSONObject> baseResponse = handleBaseException(e);
        baseResponse.setMessage(String.format("请求字段缺失, 类型为 %s，名称为 %s", e.getParameterType(), e.getParameterName()));
        baseResponse.setSpeedTime(0L);
        baseResponse.setMessage("缺失请求主体");
        baseResponse.setData(JSONUtil.createObj());
        baseResponse.setTimestamp(System.currentTimeMillis());
        return baseResponse;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public BaseResponse<?> handleMethodArgumentNotValidException(
            MethodArgumentNotValidException e) {
        BaseResponse<Map<String, String>> baseResponse = handleBaseException(e);
        baseResponse.setStatus(BaseCodeEnums.BAD_REQUEST_EXCEPTION.status);
        baseResponse.setMessage("字段验证错误，请完善后重试！");
        baseResponse.setTimestamp(System.currentTimeMillis());
        baseResponse.setSpeedTime(0L);
        Map<String, String> errMap =
                ValidationUtils.mapWithFieldError(e.getBindingResult().getFieldErrors());
        baseResponse.setData(errMap);
        return baseResponse;
    }


    /**
     * 全局异常处理，异常返回统一格式的Result
     *
     * @param e 异常
     */
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<?> globalException(Exception e) {
        BaseResponse<JSONObject> baseResponse = handleBaseException(e);
        baseResponse.setStatus(BaseCodeEnums.GLOBAL_EXCEPTION.status);
        baseResponse.setMessage(BaseCodeEnums.GLOBAL_EXCEPTION.message);
        baseResponse.setTimestamp(System.currentTimeMillis());
        baseResponse.setData(JSONUtil.createObj());
        return baseResponse;
    }


    private <T> BaseResponse<T> handleBaseException(Throwable t) {
        Assert.notNull(t, "Throwable must not be null");

        BaseResponse<T> baseResponse = new BaseResponse<>();
        baseResponse.setMessage(t.getMessage());

        log.error("Captured an exception:", t);

        if (log.isDebugEnabled()) {
            final StringWriter sw = new StringWriter();
            final PrintWriter pw = new PrintWriter(sw, true);
            ExceptionUtils.unwrapInvocationTargetException(t).printStackTrace(pw);
            baseResponse.setDevMessage(sw.getBuffer().toString());
        }
        baseResponse.setOk(Boolean.FALSE);
        return baseResponse;
    }
}
