package com.jkr.common.annotation;

import java.lang.annotation.*;

/**
 * @author zenglingwen
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface WebLog {

    /**
     * 描述
     */
    String description() default "";

}
