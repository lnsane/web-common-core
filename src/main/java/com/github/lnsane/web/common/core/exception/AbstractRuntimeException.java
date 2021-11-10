package com.github.lnsane.web.common.core.exception;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * @author lnsane
 */
public abstract class AbstractRuntimeException extends RuntimeException {
    /**
     * 错误的信息
     */
    private CustomizeError customizeError;


    public AbstractRuntimeException() {
        super();
    }

    public AbstractRuntimeException(CustomizeError customizeError) {
        super(customizeError.message());
    }

    public AbstractRuntimeException(CustomizeError customizeError, Throwable cause) {
        super(customizeError.message(), cause);
    }

    public AbstractRuntimeException(String message) {
        super(message);
    }

    public AbstractRuntimeException(Throwable cause) {
        super(cause);
    }

    protected AbstractRuntimeException(CustomizeError customizeError, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(customizeError.message(), cause, enableSuppression, writableStackTrace);
    }

    /**
     * 获取当前时间戳
     * @return 返回时间戳 毫秒
     */
    public Instant timestamp() {
        return ZonedDateTime.now().toInstant();
    }

    public CustomizeError getError() {
        return this.customizeError;
    }

    public void setError(CustomizeError customizeError) {
        this.customizeError = customizeError;
    }
}
