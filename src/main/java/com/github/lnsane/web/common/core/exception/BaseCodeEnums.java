package com.github.lnsane.web.common.core.exception;


/**
 * @author lnsane
 */
public enum BaseCodeEnums implements CustomizeError {

    SUCCESS("0", "成功"),
    BAD_REQUEST_EXCEPTION("400000", "请求异常"),
    GLOBAL_EXCEPTION("999999", "全局异常");


    final String status;

    final String message;


    BaseCodeEnums(String status, String message) {
        this.status = status;
        this.message = message;
    }


    @Override
    public String status() {
        return this.status;
    }

    @Override
    public String message() {
        return this.message;
    }

}
