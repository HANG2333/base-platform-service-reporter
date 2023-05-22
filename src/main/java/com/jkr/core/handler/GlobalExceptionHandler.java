package com.jkr.core.handler;

import com.jkr.common.exception.BusinessException;
import com.jkr.common.exception.ValidException;
import com.jkr.common.model.ResponseData;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @Author：jikeruan
 * @Description: 全局的的异常拦截器（拦截所有的控制器）
 * @Date: 2019/9/6 13:25
 */
@RestControllerAdvice
@Order(-1)
@Slf4j
public class GlobalExceptionHandler {

    /**
     * 校验异常
     *
     * @param validException
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(ValidException.class)
    @ResponseBody
    public ResponseData<String> validException(ValidException validException) {
        return ResponseData.error(validException.getMessage());
    }

    /**
     * 业务异常
     *
     * @param businessException
     * @return
     */
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(BusinessException.class)
    @ResponseBody
    public ResponseData<String> businessException(BusinessException businessException) {
        return ResponseData.error(businessException.getMessage());
    }

    /**
     * @title: systemException
     * @author: 曾令文
     * @date: 2022/6/15 13:48
     * @description: 系统异常
     * @param: [exception]
     * @return: com.jkr.common.model.ResponseData<java.lang.String>
     */
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(Exception.class)
    @ResponseBody
    public ResponseData<String> systemException(Exception exception) {
        return ResponseData.error(exception.getMessage());
    }

}
