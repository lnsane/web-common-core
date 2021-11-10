package com.github.lnsane.web.common.core.exception;

/**
 * 全局异常
 * @author lnsane
 */
public class GlobalException extends AbstractRuntimeException {
    public GlobalException() {
        super(BaseCodeEnums.GLOBAL_EXCEPTION.message);
        setError(BaseCodeEnums.GLOBAL_EXCEPTION);
    }

    public GlobalException(CustomizeError customizeError) {
        super(customizeError);
    }

    public GlobalException(CustomizeError customizeError, Throwable cause) {
        super(customizeError, cause);
    }

    public GlobalException(String message) {
        super(message);
        super.setError(BaseCodeEnums.BAD_REQUEST_EXCEPTION);
    }

    public GlobalException(Throwable cause) {
        super(cause);
        super.setError(BaseCodeEnums.BAD_REQUEST_EXCEPTION);
    }

    protected GlobalException(CustomizeError customizeError, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(customizeError, cause, enableSuppression, writableStackTrace);
    }
}
