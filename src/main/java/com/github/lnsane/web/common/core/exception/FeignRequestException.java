package com.github.lnsane.web.common.core.exception;

/**
 * @author lnsane
 */
public class FeignRequestException extends AbstractRuntimeException{

    public FeignRequestException() {
        super(BaseCodeEnums.FEIGN_REQUEST_EXCEPTION.message);
        setError(BaseCodeEnums.FEIGN_REQUEST_EXCEPTION);
    }

    public FeignRequestException(CustomizeError customizeError) {
        super(customizeError);
    }

    public FeignRequestException(CustomizeError customizeError, Throwable cause) {
        super(customizeError, cause);
    }

    public FeignRequestException(String message) {
        super(message);
        super.setError(BaseCodeEnums.FEIGN_REQUEST_EXCEPTION);
    }

    public FeignRequestException(Throwable cause) {
        super(cause);
        super.setError(BaseCodeEnums.FEIGN_REQUEST_EXCEPTION);
    }

    protected FeignRequestException(CustomizeError customizeError, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(customizeError, cause, enableSuppression, writableStackTrace);
    }
}
