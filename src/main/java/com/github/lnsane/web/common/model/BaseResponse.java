package com.github.lnsane.web.common.model;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.github.lnsane.web.common.core.exception.BaseCodeEnums;
import com.github.lnsane.web.common.core.exception.CustomizeError;

import java.time.Instant;
import java.time.ZonedDateTime;

/**
 * 响应数据结构
 *
 * @author lnsane
 */
public class BaseResponse<T> {

    private String status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String devMessage;
    private Long timestamp;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private String trackId;
    private Boolean isOk = Boolean.TRUE;
    private T data;

    public Long getSpeedTime() {
        return speedTime;
    }

    public void setSpeedTime(Long speedTime) {
        this.speedTime = speedTime;
    }

    private Long speedTime;

    public BaseResponse() {
    }

    public BaseResponse(CustomizeError customizeError, String message, T data) {
        this.status = customizeError.status();
        this.message = message;
        this.data = data;
        this.timestamp = System.currentTimeMillis();
    }

    public BaseResponse(CustomizeError customizeError, String message, T data, String trackId) {
        this.status = customizeError.status();
        this.message = message;
        this.data = data;
        this.trackId = trackId;
        this.timestamp = System.currentTimeMillis();
    }


    public String getDevMessage() {
        return devMessage;
    }

    public void setDevMessage(String devMessage) {
        this.devMessage = devMessage;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTrackId() {
        return trackId;
    }

    public void setTrackId(String trackId) {
        this.trackId = trackId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }


    public static <T> BaseResponse<JSONObject> ok() {
        return new BaseResponse<>(BaseCodeEnums.SUCCESS,BaseCodeEnums.SUCCESS.message(), JSONUtil.createObj());
    }


    /**
     * 成功 返回数据
     *
     * @param data 数据
     * @param <T>  类型
     * @return 封装的对象
     */
    public static <T> BaseResponse<T> ok(T data) {
        return new BaseResponse<>(BaseCodeEnums.SUCCESS, BaseCodeEnums.SUCCESS.message(), data);
    }

    /**
     * 成功 自定义消息 和 数据
     *
     * @param message 消息
     * @param data    数据
     * @param <T>     类型
     * @return 封装的对象
     */
    public static <T> BaseResponse<T> ok(String message, T data) {
        return new BaseResponse<>(BaseCodeEnums.SUCCESS, message, data);
    }

    public static <T> BaseResponse<JSONObject> ok(String message) {
        return new BaseResponse<>(BaseCodeEnums.SUCCESS, message,  JSONUtil.createObj());
    }
//
//    public static <T> BaseResponse<T> fail(T data) {
//        return new BaseResponse<>(BaseCodeEnums.BAD_REQUEST_EXCEPTION, BaseCodeEnums.BAD_REQUEST_EXCEPTION.message(), data);
//    }
//
//    public static <T> BaseResponse<T> fail(String message, T data) {
//        return new BaseResponse<>(BaseCodeEnums.BAD_REQUEST_EXCEPTION, message, data);
//    }
//
//    public static <T> BaseResponse<T> fail(CustomizeError customizeError, String message, T data) {
//        return new BaseResponse<>(customizeError, message, data);
//    }


    private Instant getNowTime() {
        return ZonedDateTime.now().toInstant();
    }

    public Boolean getOk() {
        return isOk;
    }

    public void setOk(Boolean ok) {
        isOk = ok;
    }

}
