package com.jkr.common.model;

import lombok.Data;

/**
 * @Author：jikeruan
 * @Description: 返回给前台的通用包装
 * @Date: 2019/8/9 11:40
 */
@Data
public class ResponseData<T> {
    public static final String DEFAULT_SUCCESS_MESSAGE = "请求成功";

    public static final String DEFAULT_ERROR_MESSAGE = "网络异常";

    public static final Integer DEFAULT_SUCCESS_CODE = 200;

    public static final Integer DEFAULT_ERROR_CODE = 500;

    /**
     * 请求是否成功
     */
    private Boolean success;

    /**
     * 响应状态码
     */
    private Integer code;

    /**
     * 响应信息
     */
    private String message;

    /**
     * 响应对象
     */
    private T data;

    public ResponseData() {

    }

    public ResponseData(Boolean success, Integer code, String msg, T data) {
        this.success = success;
        this.code = code;
        this.message = msg;
        this.data = data;
    }

    //=================================成功返回==================================

    public static <T> ResponseData<T> success() {
        return new ResponseData<T>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, null);
    }

    public static <T> ResponseData<T> success(T object) {
        return new ResponseData<T>(true, DEFAULT_SUCCESS_CODE, DEFAULT_SUCCESS_MESSAGE, object);
    }

    public static <T> ResponseData<T> success(String msg, T object) {
        return new ResponseData<T>(true, DEFAULT_SUCCESS_CODE, msg, object);
    }

    //=================================失败返回==================================

    public static <T> ResponseData<T> error() {
        return new ResponseData<T>(false, ResponseData.DEFAULT_ERROR_CODE, DEFAULT_ERROR_MESSAGE, null);
    }

    public static <T> ResponseData<T> error(String msg) {
        return new ResponseData<T>(false, ResponseData.DEFAULT_ERROR_CODE, msg, null);
    }

    public static <T> ResponseData<T> error(Integer code, String msg) {
        return new ResponseData<T>(false, code, msg, null);
    }

    public static <T> ResponseData<T> error(Integer code, String msg, T object) {
        return new <T>ResponseData<T>(false, code, msg, object);
    }
}
