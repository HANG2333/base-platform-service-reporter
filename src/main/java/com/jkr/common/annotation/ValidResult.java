package com.jkr.common.annotation;

import java.lang.annotation.*;

/**
 * @author jikeruan
 * @Date: 2019/10/14 10:17
 * @Description: 校验对象 第一个参数必须是实体
 */
@Target({ElementType.METHOD, ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface ValidResult {
    String value() default "";
}
