package com.github.lnsane.web.common.core.exception;

/**
 * @author lnsane
 */
public class BadRequestException extends AbstractRuntimeException{

    public BadRequestException() {
        super(BaseCodeEnums.BAD_REQUEST_EXCEPTION.message);
        setError(BaseCodeEnums.BAD_REQUEST_EXCEPTION);
    }

    public BadRequestException(CustomizeError customizeError) {
        super(customizeError);
    }

    public BadRequestException(CustomizeError customizeError, Throwable cause) {
        super(customizeError, cause);
    }

    public BadRequestException(String message) {
        super(message);
        super.setError(BaseCodeEnums.BAD_REQUEST_EXCEPTION);
    }

    public BadRequestException(Throwable cause) {
        super(cause);
        super.setError(BaseCodeEnums.BAD_REQUEST_EXCEPTION);
    }

    protected BadRequestException(CustomizeError customizeError, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(customizeError, cause, enableSuppression, writableStackTrace);
    }
}
