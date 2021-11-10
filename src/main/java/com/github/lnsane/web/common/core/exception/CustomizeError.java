package com.github.lnsane.web.common.core.exception;


/**
 * @author lnsane
 */
public interface CustomizeError {


    /**
     * 错误的状态码
     *
     * @return 返回状态
     */
    String status();


    /**
     * 返回错误的信息
     * @return 返回错误的信息
     */
    String message();


}
