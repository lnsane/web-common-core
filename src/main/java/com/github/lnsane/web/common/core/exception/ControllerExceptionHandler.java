package com.github.lnsane.web.common.core.exception;

import com.github.lnsane.web.common.model.BaseResponse;
import org.apache.tomcat.util.ExceptionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.http.HttpStatus;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.*;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.time.ZonedDateTime;

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
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setMessage(e.getMessage());
        baseResponse.setStatus(e.getError().status());
        baseResponse.setTimestamp(e.timestamp());
        return baseResponse;
    }


    /**
     * 全局异常处理，异常返回统一格式的Result
     *
     * @param e 异常
     */
    @ExceptionHandler(value = Exception.class)
    public BaseResponse<?> globalException(Exception e) {
        BaseResponse<?> baseResponse = handleBaseException(e);
        baseResponse.setStatus(BaseCodeEnums.GLOBAL_EXCEPTION.status());
        baseResponse.setMessage(BaseCodeEnums.GLOBAL_EXCEPTION.message());
        baseResponse.setTimestamp(ZonedDateTime.now().toInstant());
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
